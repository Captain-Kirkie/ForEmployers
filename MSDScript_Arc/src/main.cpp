
//Needs to be included for testing

 //  c++ -std=c++14 -O2 -o which_day which_day.cpp ../image.a
#include <iostream>
#include "CommandLine.h"
#include "pointer.h"


int main(int argc, char *argv[]) {
    try {
        use_arguments(argc, argv);
        return 0;
    } catch (std::runtime_error &exn) {
        std::cerr << exn.what() << std::endl;
        return 1;
    }
}





