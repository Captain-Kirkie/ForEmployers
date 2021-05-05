//
// Created by Kirk Hietpas on 1/19/21.
//

#define CATCH_CONFIG_RUNNER

#include "Cont.h"
#include "Step.h"
#include "Env.h"
#include "Val.h"
#include "Expr.h"
#include "catch.h"
#include "CommandLine.h"
#include "Parser.h"
#include "pointer.h"

/**
 *
 * @param argc Argument Count
 * @param argv Array of strings (Arguments)
 *
 * Function used to check user arguments and run specific MSDScript modes
 */

extern void use_arguments(int argc, char *argv[]) {
    bool testSeen = false;
    char *fileName = NULL;
    PTR(Expr) e;
    bool file = false;

    if (argc == 1) { // if only one argument
        std::cerr << "No arguments found" << std::endl;
        printHelpOptions();
        exit(1);
    }

    if (argc == 3) { // this means we have a fileName
        fileName = argv[2];
        if (fileName != NULL) {
            std::ifstream f_in(fileName);
            e = parse(f_in);
            file = true;
        }
    }

    //argv[0] == ./msdscript
    for (int i = 1; i < argc; i++) {
        std::string argument = ((std::string) argv[i]);

        if (argument == "--esc") {
            exit(0);
        }
        if (argument == "--help") {
            printHelpOptions();
            exit(0);
        }
        if (argument == "--test" && !testSeen) {
            int ret = Catch::Session().run(1, argv);
            if (ret != 0) {
                std::cerr << "Catch sessions one return invalid value. Value: " << ret << std::endl;
                exit(1);
            }
            testSeen = true;
        } else if (argument == "--test" && testSeen) {
            std::cerr << "Duplicate  --test argument" << std::endl;
            exit(1);
        } else if (argument == "--interp") {
            if (!file) {
                std::cout << "Enter an expression to interpret:" << std::endl;
                e = parse(std::cin);
            }
            PTR(Val) v = e->interp(Env::empty); // ??
            std::cout << v->to_string() << std::endl;
            exit(0);

        } else if (argument == "--print") {
            if (!file) {
                std::cout << "Enter an expression to print" << std::endl;
                e = parse(std::cin);
            }
            e->print(std::cout);
            std::cout << std::endl;
            exit(0);
        } else if (argument == "--pretty-print") {
            std::stringstream ss;
            if (!file) {
                std::cout << "Enter an expression to pretty-print:" << std::endl;
                e = parse(std::cin);
            }
            e->prettyPrint(ss);
            std::cout << ss.str() << std::endl;
            exit(0);
        } else if (argument == "--step") {
            if (!file) {
                std::cout << "Enter an expression to interpret by steps:" << std::endl;
                e = parse(std::cin);
            }

            PTR(Val) v = Step::interp_by_steps(e);
            std::cout << v->to_string() << std::endl;
            exit(0);
        } else {
            std::cerr << "Invalid arguments" << std::endl;
            exit(1);
        }
    }
}

/**
 * Helper method to print all possible modes MSDScript takes
 */
void printHelpOptions() {
    std::cout << "These are the command options: " << std::endl;
    std::cout << "--help \n\t-lists all commands\n" << std::endl << "--test \n\t-runs all tests\n" << std::endl
              << "--esc \n\t-exit program\n" <<
              std::endl << "--interp \n\t-interpret results from std::cin\n" << std::endl
              << "--print \n\t-print expression\n"
              << std::endl << "--pretty-print \n\t-pretty-print expression\n" << std::endl;
}

/**
 *
 * @param in input stream
 * @return returns appropriate expression
 */
PTR(Expr) parse(std::istream &in) {
    return Parser::parse_expr(in);
}