#include <iostream>

#include <time.h>
#include <stdlib.h>
#include "exec.h"
#include <string>
#include <vector>
#include <fstream>


/*
 * 0 breaks on print, his does not print anything
 * 1 breaks on interp, his does not return something
 * 2 breaks after a few tries, mine interps a neg number (Either mine or matts is haveing wrap around issues)
 * 3 Matts is printing a mult when it should be printing a +
 * 4 isnt breaking?????
 * 5 isnt breaking????
 * 6 isnt breaking???
 * 7 isnt breaking???
 *
 */

// file path to msdscript
// /Users/kirkhietpas/Desktop/GitHub/MSDSpring2021/CS6015/Captain-Kirkie-MSDscript/msdScriptMaster/src/msdscript
//declarations
static std::string random_bytes();

static std::string random_expr_string();

void test_scripts(int argc, char **argv);

static std::string random_var();

void checkFilePaths(int argc, char **argv);

void printErrors(const ExecResult &result1, const ExecResult &result2);


int main(int argc, char **argv) {
    std::srand(clock()); // seed the random generator
    test_scripts(argc, argv);
    return 0;
}


void test_scripts(int argc, char **argv) {
    assert(argc > 1);

    int count = 1;
    checkFilePaths(argc, argv);

    const char *interp_argv_1[] = {argv[1], "--interp"};
    const char *print_argv_1[] = {argv[1], "--print"};
    const char *pretty_print_argv_1[] = {argv[1], "--pretty-print"};
    const char *interp_argv_2[2];
    const char *print_argv_2[2];
    const char *pretty_print_argv_2[2];
    // that means there is multiple to compare
    if (argc == 3) { // if there are multiple arguments
        interp_argv_2[0] = argv[2];
        interp_argv_2[1] = "--interp";
        print_argv_2[0] = argv[2];
        print_argv_2[1] = "--print";
        pretty_print_argv_2[0] = argv[2];
        pretty_print_argv_2[1] = "--pretty-print";
    }
    if (argc == 2) { // if only one argument compare against itself
        interp_argv_2[0] = argv[1];
        interp_argv_2[1] = "--interp";
        print_argv_2[0] = argv[1];
        print_argv_2[1] = "--print";
        pretty_print_argv_2[0] = argv[1];
        pretty_print_argv_2[1] = "--pretty-print";
    }
    // testing that what I have inputted is an actual filepath, breaks if not
    // if argc == 2 then I want to test my program
    for (int i = 1; i <= 100; i++) {
        std::string randomInputString = random_expr_string(); // this should generate random input
        std::cout << "Count:     " << count << "  ";
        std::cout << "trying " << randomInputString << std::endl;
        ExecResult interp_result_1 = exec_program(2, interp_argv_1, randomInputString); // should return an exec result
        ExecResult print_result_1 = exec_program(2, print_argv_1, randomInputString);
        ExecResult interp_result_2 = exec_program(2, interp_argv_2, randomInputString); // should return an exec result
        ExecResult print_result_2 = exec_program(2, print_argv_2, randomInputString);
        ExecResult pretty_print_result_1 = exec_program(2, pretty_print_argv_1, randomInputString);
        ExecResult pretty_print_result_2 = exec_program(2, pretty_print_argv_2, randomInputString);


        // interp again
        ExecResult interp_again_result = exec_program(2, interp_argv_1, print_result_1.out);
        if(interp_again_result.out != interp_result_1.out ){
            printErrors(interp_again_result, interp_result_1);
            throw std::runtime_error("different results for interp again");
        }

        // interp results for two different sf
        if (interp_result_1.out != interp_result_2.out) {
            std::cout << std::endl;
            std::cerr << "interp results bad" << std::endl << std::endl;
            printErrors(interp_result_1, interp_result_2);
            throw std::runtime_error("DIFFERENT RESULT FOR INTERP");
        }

        //  print results
        if (print_result_1.out != print_result_2.out) {
            std::cout << std::endl;
            std::cerr << "Print results bad" << std::endl << std::endl;
            printErrors(pretty_print_result_1, pretty_print_result_2);
            throw std::runtime_error("DIFFERENT RESULT FOR PRINT");
        }

        if (pretty_print_result_1.out != pretty_print_result_2.out) { //TODO: Fix my pretty print
            std::cout << std::endl;
            std::cout << "-----------------------------------------------------------------------" << std::endl;
            std::cerr << "Pretty_print results bad" << std::endl << std::endl;
            printErrors(pretty_print_result_1, pretty_print_result_2);
            std::cout << "-----------------------------------------------------------------------" << std::endl;
            // throw std::runtime_error("DIFFERENT RESULT FOR PrettyPrint");
        }


        count++;
    }

}


// -----------------------------------------------------------

void printErrors(const ExecResult &result1, const ExecResult &result2) {
    std::cout << "arg1 (mine): " << std::endl;
    std::cout << result1.out << std::endl;
    std::cout << "arg2 (matts): " << std::endl;
    std::cout << result2.out << std::endl;
    std::cout << "These are error messages " << "1: " << result1.err << std::endl << "2: " << result2.err << std::endl;
    std::cout << "These are the exit codes: " << "1: " << result1.exit_code << std::endl << " 2: " << std::endl;
}

void checkFilePaths(int argc, char **argv) {
    for (int i = 1; i < argc; i++) {
        std::ifstream filePath(argv[i]);
        if (argv[i] != NULL)
            assert(filePath);
    }
}


static std::string random_bytes() {
    std::string word;
    for (int i = rand() % 32; i-- > 0;) { //% 32 limiting how big it can be
        word += rand() % 256;
    }
    return word;
}

static std::string random_var() {
    std::string var;
    var = 'a' + rand() % 26;
    return var;
}


static std::string random_expr_string() { // todo: make this more intense

    int randomInt = rand() % 10;
    if (randomInt < 6) { // 60 % of the time return a number or a var
        int whatKindOfStop = rand() % 10;

        if (whatKindOfStop < 7) // 70 % of the time return a num
            return std::to_string(rand());
        else // 20% return a var //TODO: I wonder if we are going to want this
            return random_var();

    } else {
        int randIntTwo = rand() % 10;
        if (randIntTwo < 8) { // 80% of the time its going to return +/*
            int multOrAdd = rand() % 10;
            if (multOrAdd < 5) {
                return random_expr_string() + "+" + random_expr_string();
            } else {
                return random_expr_string() + "*" + random_expr_string();
            }
        } else //20%  return let
            return "_let " + random_var() + " = " + random_expr_string() + " _in " + random_expr_string();
    }
}

// -----------------------------------------------------------

