//
// Created by Kirk Hietpas on 2/23/21.
//




#ifndef CAPTAIN_KIRKIE_MSDSCRIPT_VAL_H
#define CAPTAIN_KIRKIE_MSDSCRIPT_VAL_H

#include <iostream>
#include "pointer.h"

class Cont;

class Expr;

class Val;

class Step;

CLASS(Val) {
public:

    virtual PTR(Val)add_to(PTR(Val)other_val) = 0;

    virtual PTR(Val)mult_to(PTR(Val)other_val) = 0;

    virtual bool equals(PTR(Val)other_val) = 0;

    virtual bool is_true() = 0;

    virtual std::string to_string() = 0;

    virtual PTR(Val)call(PTR(Val)actual_arg) = 0;

    virtual void call_step(PTR(Val)actual_arg_val, PTR(Cont)rest) = 0;


};


class NumVal : public Val { // extends val
public:
    int rep;

    NumVal(int rep);

    PTR(Val)add_to(PTR(Val)other_val) override;

    PTR(Val)mult_to(PTR(Val)other_val) override;

    bool equals(PTR(Val)other_val) override;

    bool is_true() override;

    std::string to_string() override;

    PTR(Val)call(PTR(Val)actual_arg) override;

    void call_step(PTR(Val)actual_arg_val, PTR(Cont)rest) override;


};


class BoolVal : public Val {
public:
    bool rep; // make private?
    explicit BoolVal(bool rep);

    PTR(Val)add_to(PTR(Val)other_val) override;


    PTR(Val)mult_to(PTR(Val)other_val) override;

    bool equals(PTR(Val)other_val) override;

    bool is_true() override;

    std::string to_string() override;

    PTR(Val)call(PTR(Val)actual_arg) override;

    void call_step(PTR(Val)actual_arg_val, PTR(Cont)rest) override;
};


class FunVal : public Val {
public:

    std::string formal_arg;
    PTR(Expr)body; // expression inside value
    PTR(Env)env;

    FunVal(std::string formal_arg, PTR(Expr)body, PTR(Env)env);

    PTR(Val)add_to(PTR(Val)other_val) override;


    PTR(Val)mult_to(PTR(Val)other_val) override;

    bool equals(PTR(Val)other_val) override;

    bool is_true() override;

    std::string to_string() override;

    PTR(Val)call(PTR(Val)actual_arg) override;

    void call_step(PTR(Val)actual_arg_val, PTR(Cont)rest) override; //TODO Does this need to be virtual? for everything?
};


#endif //CAPTAIN_KIRKIE_MSDSCRIPT_VAL_H
