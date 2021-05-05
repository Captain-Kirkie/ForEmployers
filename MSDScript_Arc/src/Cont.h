//
// Created by Kirk Hietpas on 4/1/21.
//

#ifndef CAPTAIN_KIRKIE_MSDSCRIPT_CONT_H
#define CAPTAIN_KIRKIE_MSDSCRIPT_CONT_H

#include "pointer.h"
#include <iostream>


class Expr;

class Val;

class Env;

class Step;

class VarExpr;

class ExtendedEnv;


CLASS (Cont) {
public:
    static PTR(Cont) done;

    virtual void step_continue() = 0;

    virtual bool equals(PTR(Cont) other_cont) = 0;

};

class ContDone : public Cont {
public:
    ContDone();

    void step_continue() override;

    bool equals(PTR(Cont) other_cont) override;
};

// start evaluating lhs,
// remeber in the future need to evaluate rhs
// then add lhs + rhs
class RightThenAddCont : public Cont {
public:
    PTR(Expr) rhs;
    PTR(Env) env;
    PTR(Cont) rest;

    RightThenAddCont(PTR(Expr) rhs, PTR(Env) env, PTR(Cont) cont);

    void step_continue() override;

    bool equals(PTR(Cont) other) override;
};

class RightThenMultCont : public Cont {
public:
    PTR(Expr) rhs;
    PTR(Env) env;
    PTR(Cont) rest;

    RightThenMultCont(PTR(Expr) rhs, PTR(Env) env, PTR(Cont) cont);

    void step_continue() override;

    bool equals(PTR(Cont) other) override;
};

class AddCont : public Cont {
public:
    PTR(Val) lhs_val;
    PTR(Cont) rest;

    AddCont(PTR(Val) lhs_val, PTR(Cont) rest);

    void step_continue() override;

    bool equals(PTR(Cont) other) override;
};

class MultCont : public Cont {
public:
    PTR(Val) lhs_val;
    PTR(Cont) rest;

    MultCont(PTR(Val) lhs_val, PTR(Cont) rest);

    void step_continue() override;

    bool equals(PTR(Cont) other) override;
};

class IFBranchCont : public Cont {
public:
    PTR(Expr) then_part;
    PTR(Expr) else_part;
    PTR(Env) env;
    PTR(Cont) rest;

    IFBranchCont(PTR(Expr) then_part, PTR(Expr) else_part, PTR(Env) env, PTR(Cont) rest);

    void step_continue() override;

    bool equals(PTR(Cont) other) override;
};

class LetBodyCont : public Cont {
public:
    std::string var;
    PTR(Expr) body;
    PTR(Env) env;
    PTR(Cont) rest;

    LetBodyCont(std::string var, PTR(Expr) body, PTR(Env) env, PTR(Cont) rest);

    void step_continue() override;

    bool equals(PTR(Cont) other) override;
};


class ArgThenCallCont : public Cont {
public:
    PTR(Expr) actual_arg;
    PTR(Env) env;
    PTR(Cont) rest;

    ArgThenCallCont(PTR(Expr) actual_arg, PTR(Env) env, PTR(Cont) rest);

    void step_continue() override;

    bool equals(PTR(Cont) other) override;

};


class CallCont : public Cont {
public:
    PTR(Val) to_be_called_val;
    PTR(Cont) rest;

    CallCont(PTR(Val) to_be_called_val, PTR(Cont) rest);

    void step_continue() override;

    bool equals(PTR(Cont) other) override;
};

// comparision
class RightThenCompCont : public Cont {
public:
    PTR(Expr) rhs;
    PTR(Env) env;
    PTR(Cont) rest;

    RightThenCompCont(PTR(Expr) rhs, PTR(Env) env, PTR(Cont) cont);

    void step_continue() override;

    bool equals(PTR(Cont) other) override;
};

class CompCont : public Cont {
public:
    PTR(Val) lhs_val;
    PTR(Cont) rest;

    CompCont(PTR(Val) lhs_val, PTR(Cont) rest);

    void step_continue() override;

    bool equals(PTR(Cont) other) override;
};

#endif //CAPTAIN_KIRKIE_MSDSCRIPT_CONT_H
