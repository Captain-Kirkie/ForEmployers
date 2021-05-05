//
// Created by Kirk Hietpas on 3/28/21.
//

#ifndef CAPTAIN_KIRKIE_MSDSCRIPT_ENV_H
#define CAPTAIN_KIRKIE_MSDSCRIPT_ENV_H

//#include "pointer.h"
#include <string>
class Cont;
class Expr;
class Val;
class Step;


CLASS (Env) {
public:
    virtual PTR(Val) lookup(std::string find_name) = 0;
    static PTR(Env)empty; // static empty var
    virtual bool equals(PTR(Env)other) = 0;
};



/* ****************************************************** */
class ExtendedEnv : public Env {
public:
    std::string name;
    PTR(Val) val;
    PTR(Env) rest;

    PTR(Val) lookup(std::string find_name) override;
    ExtendedEnv(std::string name, PTR(Val) val1, PTR(Env)rest );
    bool equals(PTR(Env)other) override;
};


/* ****************************************************** */
class EmptyEnv : public Env {
public:
    PTR(ExtendedEnv) extendedEnv; // does this hold an extended env
    EmptyEnv();
    PTR(Val) lookup(std::string find_name) override;
    bool equals(PTR(Env)other) override;
};




#endif //CAPTAIN_KIRKIE_MSDSCRIPT_ENV_H
