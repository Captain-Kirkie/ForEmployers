#include <string>
#include <iostream>
#include <cassert>

#include <unistd.h>
#include <fcntl.h>
#include <poll.h>
#include <errno.h>

#include "exec.h"

static const int READ_END  = 0;
static const int WRITE_END = 1;

static const int STDIN_FD  = 0;
static const int STDOUT_FD = 1;
static const int STDERR_FD = 2;

static void nonblocking(int fd, bool enabled);
static int needs_retry(int rtn);
static void wait_until_one_ready(int wr_fd, bool wr_done,
                                 int rd1_fd, bool rd1_done,
                                 int rd2_fd, bool rd2_done);
static void pump_to(std::string &str, int fd, bool &done);
static void pump_from(int fd, std::string &str, bool &done);
static void wait_child(pid_t pid, int &exit_code);

// Run the program in command[0], where `command` must be a NULL-terminated
// array (like `execv` expects). Supply the given string as stdin to the
// program, wait until it complete, and report its exit status, stdout
// as a string, and stderr s a string. The exit status is set to a signal
// number if the program exits with a signal.
ExecResult exec_program(int argc, const char * const *argv, std::string input) {
  // Need a NULL-teriminated array for `execv`:
  const char * command[argc + 1];
  for (int i = 0; i < argc; i++) {
    if (argv[i] == NULL) // a little defensive
      throw std::runtime_error("bad argc length or argv string given to exec_program");
    command[i] = argv[i];
  }
  command[argc] = NULL;

  signal(SIGPIPE, SIG_IGN);
  
  int in[2];
  if (pipe(in) != 0)
    throw std::runtime_error("stdin pipe failed");
  
  int out[2];
  if (pipe(out) != 0)
    throw std::runtime_error("stdout pipe failed");
  
  int err[2];
  if (pipe(err) != 0)
    throw std::runtime_error("stdout pipe failed");

  pid_t pid = fork();
  if (pid == -1)
    throw std::runtime_error("fork failed");
  else if (pid ==  0) {
    // child
    dup2(in[READ_END], STDIN_FD);
    dup2(out[WRITE_END], STDOUT_FD);
    dup2(err[WRITE_END], STDERR_FD);
    
    close(in[READ_END]);
    close(in[WRITE_END]);
    close(out[READ_END]);
    close(out[WRITE_END]);
    close(err[READ_END]);
    close(err[WRITE_END]);
    
    execv(command[0], (char * const *)command);
    
    // Getting here means that the execve failed
    {
      const char *msg = "exec failed\n";
      write(STDERR_FD, msg, strlen(msg));
      exit(1);
    }
  } else {
    // parent
    bool in_done = false, out_done = false, err_done = false;
    ExecResult r;

    close(in[READ_END]);
    close(out[WRITE_END]);
    close(err[WRITE_END]);
    
    do {
      wait_until_one_ready(in[WRITE_END], in_done,
                           out[READ_END], out_done,
                           err[READ_END], err_done);
      pump_to(input, in[WRITE_END], in_done);
      pump_from(out[READ_END], r.out, out_done);
      pump_from(err[READ_END], r.err, err_done);
    } while (!in_done || !out_done || !err_done);
    
    wait_child(pid, r.exit_code);
    
    return r;
  }
}

// Enable/disable nonblocking mode for a file descriptor
static void nonblocking(int fd, bool enabled) {
  int old_flags = fcntl(fd, F_GETFL, 0);
  fcntl(fd, F_SETFL, (enabled
                      ? old_flags | O_NONBLOCK
                      : old_flags - (old_flags & O_NONBLOCK)));
}

// Check whether a system call result means "retry"
static int needs_retry(int rtn) {
  return (rtn == -1) && (errno == EINTR);
}

// Block until reading or writing is possible on one of the
// given file descriptors
static void wait_until_one_ready(int wr_fd, bool wr_done,
                                 int rd1_fd, bool rd1_done,
                                 int rd2_fd, bool rd2_done) {
  struct pollfd poll_info[3];
  int count = 0;
  
  if (!wr_done) {
    poll_info[count].fd = wr_fd;
    poll_info[count++].events = POLLOUT;
  }
  if (!rd1_done) {
    poll_info[count].fd = rd1_fd;
    poll_info[count++].events = POLLIN;
  }
  if (!rd2_done) {
    poll_info[count].fd = rd2_fd;
    poll_info[count++].events = POLLIN;
  }
  
  if (count == 0)
    return;

  int rtn;
  do {
    rtn = poll(poll_info, count, -1);
  } while (needs_retry(rtn));
  
  if (rtn == -1)
    throw std::runtime_error("poll failed");
}

// Move characters from the given string to the given file descriptor,
// closing the file descriptor if the string is empty. The `done` flag
// is consulted and possibly set to indicate whether the file descriptor
// is still open.
static void pump_to(std::string &str, int fd, bool &done) {
  if (!done) {
    ssize_t len;
    nonblocking(fd, true);
    do {
      len = write(fd, str.c_str(), str.length());
    } while (needs_retry((int)len));
    nonblocking(fd, false);
    if ((len < 0) && (errno == EAGAIN)) {
      // not ready to write
    } else {
      if (len < 0)
        str.erase(0, str.length()); // treat error like writing all
      else
        str.erase(0, len);
      if (str.length() == 0) {
        done = true;
        close(fd);
      }
    }
  }
}

// Move characters from the given file descriptor to the given string
// closing the file descriptor on EOF. The `done` flag is consulted
// and possibly set to indicate whether the file descriptor is still
// open.
static void pump_from(int fd, std::string &str, bool &done) {
  if (!done) {
    char buffer[256];
    ssize_t len;
    nonblocking(fd, true);
    do {
      len = read(fd, buffer, sizeof(buffer)-1);
    } while (needs_retry((int)len));
    nonblocking(fd, false);
    if ((len < 0) && (errno == EAGAIN)) {
      // nothing ready to read
    } else {
      if (len < 1) {
        // error or EOF
        done = true;
        close(fd);
      } else {
        ssize_t old_len = str.length();
        str.insert(old_len, buffer, len);
        assert(str.length() == old_len + len);
      }
    }
  }
}

// Wait until a process has terminated
static void wait_child(pid_t pid, int &exit_code) {
  int status;
  pid_t rtn;
  
  do {
    rtn = waitpid(pid, &status, 0);
  } while (needs_retry(rtn));
  if (rtn == -1)
    throw std::runtime_error("waitpid failed");
  if (WIFEXITED(status))
    exit_code = WEXITSTATUS(status);
  else if (WIFSIGNALED(status))
    exit_code = WTERMSIG(status);
  else
    throw std::runtime_error("unrecognized status from waitpid");
}
