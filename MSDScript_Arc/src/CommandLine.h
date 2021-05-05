//
// Created by Kirk Hietpas on 1/19/21.
//

#ifndef CAPTAIN_KIRKIE_MSDSCRIPT_COMMANDLINE_H
#define CAPTAIN_KIRKIE_MSDSCRIPT_COMMANDLINE_H

#include <iostream>
#include "pointer.h"
class Cont;
class Expr;
class Val;
class Env;
class Step;


extern void use_arguments(int argc, char* argv[]);
// wrapper function
extern PTR(Expr)parse(std::istream &in);
void printHelpOptions();





#endif //CAPTAIN_KIRKIE_MSDSCRIPT_COMMANDLINE_H