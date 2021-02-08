jeffery_path = "testOutputFiles/jeffery.txt"
google_de_ping = "testOutputFiles/google_de_german.txt"
jeffery_repeat = "testOutputFiles/jeffery_repeat.txt"
test_path = "testOutputFiles/test.txt"


def parse_ping(file_path, out_put):
    print("Parsing ping")
    avg = 0.00
    count = 0
    min_time = 0.00
    write_file = open(out_put, "w")
    with open(file_path) as fp:
        for line in fp:
            split_line = line.split()
            if line.find("time=") != -1:
                # print("This data " + split_line[6][5:])
                avg += float(split_line[6][5:])
                count += 1
            if line.find("round-trip") != -1:
                # print(split_line)
                min_time = float(split_line[3][0:6])
                # print(min_time)

        avg = avg / count

        write_file.write("This is the Average " + str(avg) + " m/s\n")
        write_file.write("This is the Min " + str(min_time) + " m/s\n")
        write_file.write("This is the Average - min " + str(avg - min_time) + " m/s\n")
        # print(avg)

    write_file.close()


def __parse_trace_path__(file_path, out_put_path_name):
    print("Parsing trace_path")
    write_file = open(out_put_path_name, "w")
    with open(file_path) as fp:  # this opens the file and automatically closes when finished
        line = fp.readline()  # read the first line
        hop_count = None
        address_name = None
        ip_address = None

        # todo: figure out how handle stars
        while line:  # while line exists

            split_line = line.split()  # split the line up
            # print(split_line)
            hop_count = split_line[0]
            try:
                hop_count_int = int(hop_count)
            except:
                print("Cannot convert to Int")
                line = fp.readline()
                continue
            # This skips lines that are are hitting multiple routers
            if not in_range(hop_count_int):  # go to next iteration
                line = fp.readline()
                continue
            # resets to 0 everytime
            avg = 0.00
            num_count = 0
            list_length = len(split_line)

            for item in range(0, list_length):
                if len(split_line[item]) != 0 and split_line[item][0] == '(' and split_line[item][-1] == ')':
                    ip_address = split_line[item][1:-1]
                    #  print(ip_address)
                if (item + 1) != list_length and list_length > 0:
                    if split_line[item + 1] == "ms" or split_line[item + 1] == "ms\n":
                        num_count += 1
                        avg += float(split_line[item])

            if num_count != 0:
                avg = avg / num_count
                write_file.write(hop_count + " " + ip_address + " " + str(avg) + "\n")
            else:
                avg = 0
                write_file.write(hop_count + " " + ip_address + " " + "NoResponseData" + "\n")
                # skip items without a response
            line = fp.readline()
    write_file.close()


# determine if hop_count s withing a specific range
def in_range(hop_count):
    return 0 < hop_count < 100


# main definition
def main():
    __parse_trace_path__(test_path, "test_results.txt")
    __parse_trace_path__(jeffery_path, "results/firstTryTracePath.txt")
    __parse_trace_path__(jeffery_repeat, "results/secondTryTracePath.txt")
    parse_ping(google_de_ping, "results/ping_results.txt")


# run main
if __name__ == "__main__":
    main()
