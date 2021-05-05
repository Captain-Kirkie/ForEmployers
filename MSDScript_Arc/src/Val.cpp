//
// Created by Kirk Hietpas on 2/23/21.
//

#include "Cont.h"
#include "Step.h"
#include "Env.h"
#include "Val.h"
#include "Expr.h"
#include "pointer.h"


NumVal::NumVal(int rep) {
    this->rep = rep;
}

PTR(Val)NumVal::add_to(PTR(Val) other_val) {
    PTR(NumVal) other_num = CAST(NumVal)(other_val);
    if (other_num == nullptr)
        throw std::runtime_error("add of non-number");

    double result = (double) rep + (double) other_num->rep;
    if (result >= INT_MAX  || result <= INT_MIN)
        throw std::runtime_error("input value too large ADD");
    return NEW(NumVal)(result); // cast to unsigned
}


PTR(Val)NumVal::mult_to(PTR(Val) other_val) {
    PTR(NumVal) other_num = CAST(NumVal)(other_val);
    if (other_num == nullptr)
        throw std::runtime_error("Multiplying of non-num");

    double result = (double)rep * (double)other_num->rep;
    if (result >= INT_MAX  || result <= INT_MIN)
        throw std::runtime_error("input value too large Mult");
    else
        return NEW(NumVal)(result);
}

bool NumVal::equals(PTR(Val) other_val) {
    PTR(NumVal) other_num = CAST(NumVal)(other_val);
    if (other_num == nullptr)
        return false;
    return this->rep == other_num->rep;
}


bool NumVal::is_true() {
    throw std::runtime_error("Trying to is_true on NumVal");
}

std::string NumVal::to_string() {
    return std::to_string(this->rep);
}

PTR(Val)NumVal::call(PTR(Val) actual_arg) {
    throw std::runtime_error("Trying to call a NumVal!");
}

void NumVal::call_step(PTR(Val) actual_arg_val, PTR(Cont) rest) {
    throw std::runtime_error("Calling call_step on NumVal");
}


//BoolVal------------------------------------------------------
BoolVal::BoolVal(bool rep) {
    this->rep = rep;
}

PTR(Val)BoolVal::add_to(PTR(Val) other_val) {
    throw std::runtime_error("Can't ADD boolean vars");
}

PTR(Val)BoolVal::mult_to(PTR(Val) other_val) {
    throw std::runtime_error("Can't MULT boolean vars");
}

bool BoolVal::equals(PTR(Val) other_val) {
    PTR(BoolVal) newBoolVal = CAST(BoolVal)(other_val);
    if (newBoolVal == nullptr)
        return false;
    return newBoolVal->rep == this->rep;
}

bool BoolVal::is_true() {
    return this->rep; // Just return the bool rep of the class
}

std::string BoolVal::to_string() {
    std::string represent = (this->rep) ? "_true" : "_false";
    return represent;
}

PTR(Val)BoolVal::call(PTR(Val) actual_arg) {
    throw std::runtime_error("Trying to call a BoolVal");
}

void BoolVal::call_step(PTR(Val) actual_arg_val, PTR(Cont) rest) {
    throw std::runtime_error("Calling call_step on BoolVal");
}


// Function Value --------------------------------------------
PTR(Val)FunVal::mult_to(PTR(Val) other_val) {
    // .... Expr body ......
    //..... string formal_arg .....
    throw std::runtime_error("Trying to mult a funVal");
}

PTR(Val)FunVal::add_to(PTR(Val) other_val) {
    throw std::runtime_error("Trying to add a fun Val");
}

bool FunVal::equals(PTR(Val) other_val) {
    PTR(FunVal) otherPtr = CAST(FunVal)(other_val);
    if (otherPtr == nullptr)
        return false;
    else
        return this->formal_arg == otherPtr->formal_arg && this->body->equals(otherPtr->body);
}

bool FunVal::is_true() {
    throw std::runtime_error("Calling is_true on a FunVal");
}

std::string FunVal::to_string() {
    std::string ret = "_fun (" + this->formal_arg + ") " + this->body->to_string_pretty();
    return ret;
}


FunVal::FunVal(std::string formal_arg, PTR(Expr) body, PTR(Env) env) {
    this->body = body;
    this->formal_arg = formal_arg; // string f(x) ==x
    this->env = env;
}


PTR(Val)FunVal::call(PTR(Val) actual_arg) {
    // ... string formal_arg ... f(x) == x
    //  .. actual_arg ... val to sub in
    // in the body
    return body->interp(NEW(ExtendedEnv)(formal_arg, actual_arg, env));
}

void FunVal::call_step(PTR(Val) actual_arg_val, PTR(Cont) rest) {
    //std::cout << "in FunCal call Step" << std::endl;
    Step::mode = Step::interp_mode; //interp Mode???
    Step::expr = body;
    Step::env = NEW(ExtendedEnv)(formal_arg, actual_arg_val, env);
    Step::cont = rest;
}


