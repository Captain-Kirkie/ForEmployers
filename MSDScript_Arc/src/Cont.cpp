//
// Created by Kirk Hietpas on 4/1/21.
//

#include "Cont.h"
#include "Step.h"
#include "Env.h"
#include "Val.h"
#include "Expr.h"


class ExtendedEnv;

ContDone::ContDone() {} // empty Constructor

PTR(Cont)Cont::done = NEW(ContDone)();

//  ****************************************** Cont done ********************************
void ContDone::step_continue() {
   throw std::runtime_error("calling step cont on Cont::done");
}

bool ContDone::equals(PTR(Cont)other_cont) {
    PTR(ContDone)casted_cont = CAST(ContDone)(other_cont);
    if (casted_cont == nullptr)
        return false;

    return true;
}

//  ************************************* Add Conts  *************************************
RightThenAddCont::RightThenAddCont(PTR(Expr)rhs, PTR(Env)env, PTR(Cont)cont) {
    this->rhs = rhs;
    this->env = env;
    this->rest = cont;
}

void RightThenAddCont::step_continue() {
    PTR(Val)lhs_val = Step::val;
    Step::mode = Step::interp_mode;
    Step::expr = rhs;
    Step::env = env;
    Step::cont = NEW(AddCont)(lhs_val, rest);
}

bool RightThenAddCont::equals(PTR(Cont)other) {
    PTR(RightThenAddCont)casted_cont = CAST(RightThenAddCont)(other);
    if (casted_cont == nullptr)
        return false;
    return this->rhs->equals(casted_cont->rhs) && this->rest->equals(casted_cont->rest)
           && this->env->equals(casted_cont->env);
}

AddCont::AddCont(PTR(Val)lhs_val, PTR(Cont)rest) {
    this->lhs_val = lhs_val;
    this->rest = rest;
}

void AddCont::step_continue() {
    PTR(Val)rhs_val = Step::val; // grab the remebered val
    Step::mode = Step::continue_mode; // this is wrong in slides?
    Step::val = lhs_val->add_to(rhs_val); // store added value in register
    Step::cont = rest;
}

bool AddCont::equals(PTR(Cont)other) {
    PTR(AddCont)casted = CAST(AddCont)(other);
    if (casted == nullptr)
        return false;

    return this->lhs_val->equals(casted->lhs_val)
           && this->rest->equals(casted->rest);
}

// If Conts  **************************************************************************
IFBranchCont::IFBranchCont(PTR(Expr)then_part, PTR(Expr)else_part, PTR(Env)env, PTR(Cont)rest) {
    this->then_part = then_part;
    this->else_part = else_part;
    this->env = env;
    this->rest = rest;
}

void IFBranchCont::step_continue() {
    PTR(Val)test_val = Step::val;
    Step::mode = Step::interp_mode;
    if (test_val->is_true()) {
        Step::expr = then_part;
    } else {
        Step::expr = else_part;
    }
    Step::env = env;
    Step::cont = rest;
}

bool IFBranchCont::equals(PTR(Cont)other) {
    PTR(IFBranchCont)casted = CAST(IFBranchCont)(other);
    if (casted == nullptr)
        return false;

    return this->then_part->equals(casted->then_part)
           && this->else_part->equals(casted->else_part)
           && this->env->equals(casted->env)
           && this->rest->equals(casted->rest);
}

// Let Cont  **************************************************************************
// letCont //TODO: CHECK ENV IS CORRECT!!!
void LetBodyCont::step_continue() {
    Step::mode = Step::interp_mode;
    Step::expr = body;
    Step::env = NEW(ExtendedEnv)(var, Step::val, env); // todo: is this the right env?
    Step::cont = rest;
}


bool LetBodyCont::equals(PTR(Cont)other) {
    PTR(LetBodyCont)casted = CAST(LetBodyCont)(other);
    if (casted == nullptr)
        return false;
    return this->var == casted->var
           && this->body->equals(casted->body)
           && this->env->equals(casted->env)
           && this->rest->equals(casted->rest);

}

LetBodyCont::LetBodyCont(std::string var, PTR(Expr)body, PTR(Env)env, PTR(Cont)rest) {
    this->var = var;
    this->body = body;
    this->env = env;
    this->rest = rest;
}



// Mult Conts  **************************************************************************
RightThenMultCont::RightThenMultCont(PTR(Expr)rhs, PTR(Env)env, PTR(Cont)cont) {
    this->rhs = rhs;
    this->env = env;
    this->rest = cont;
}

void RightThenMultCont::step_continue() {
    PTR(Val)lhs_val = Step::val;
    Step::mode = Step::interp_mode; // interp
    Step::expr = rhs;
    Step::env = env;
    Step::cont = NEW(MultCont)(lhs_val, rest);

}

bool RightThenMultCont::equals(PTR(Cont)other) {
    PTR(RightThenMultCont)casted = CAST(RightThenMultCont)(other);
    if (casted == nullptr)
        return false;
    return this->rhs->equals(casted->rhs)
           && this->env->equals(casted->env)
           && this->rest->equals(casted->rest);
}

MultCont::MultCont(PTR(Val)lhs_val, PTR(Cont)rest) {
    this->lhs_val = lhs_val;
    this->rest = rest;
}

void MultCont::step_continue() {
    PTR(Val)rhs_val = Step::val;
    Step::mode = Step::continue_mode; // this needs to be continue mode
    Step::val = lhs_val->mult_to(rhs_val);
    Step::cont = rest;
}

bool MultCont::equals(PTR(Cont)other) {
    PTR(MultCont) casted = CAST(MultCont)(other);
    if (casted == nullptr)
        return false;

    return this->lhs_val->equals(casted->lhs_val)
           && this->rest->equals(casted->rest);
}


RightThenCompCont::RightThenCompCont(PTR(Expr)rhs, PTR(Env)env, PTR(Cont)cont) {
    this->rhs = rhs;
    this->env = env;
    this->rest = cont;
}


void RightThenCompCont::step_continue() {
    PTR(Val)lhs_val = Step::val;
    Step::mode = Step::interp_mode;
    Step::expr = rhs;
    Step::env = env;
    Step::cont = NEW(CompCont)(lhs_val, rest);
}

bool RightThenCompCont::equals(PTR(Cont)other) {
    PTR(RightThenCompCont)casted = CAST(RightThenCompCont)(other);
    if (casted == nullptr)
        return false;

    return this->rhs->equals(casted->rhs)
           && this->env->equals(casted->env)
           && this->rest->equals(casted->rest);
}

CompCont::CompCont(PTR(Val)lhs_val, PTR(Cont)rest) {
    this->lhs_val = lhs_val;
    this->rest = rest;
}


void CompCont::step_continue() {
    PTR(Val)rhs_val = Step::val;
    Step::mode = Step::continue_mode; // this needs to be continue mode
    PTR(BoolVal)boolVal = NEW(BoolVal)(lhs_val->equals(rhs_val));
    Step::val = boolVal;
    Step::cont = rest;
}

bool CompCont::equals(PTR(Cont)other) {
    PTR(CompCont)casted = CAST(CompCont)(other);
    if (casted == nullptr)
        return false;
    return this->lhs_val->equals(casted->lhs_val) && this->rest->equals(casted->rest);
}


// Function Conts  **************************************************************************
 // Call stuff  **************************************************************************
ArgThenCallCont::ArgThenCallCont(PTR(Expr)actual_arg, PTR(Env)env, PTR(Cont)rest) {
    this->actual_arg = actual_arg;
    this->env = env;
    this->rest = rest;
}

void ArgThenCallCont::step_continue() {
    Step::mode = Step::interp_mode; // this should be interp
    Step::expr = actual_arg;
    Step::env = env;
    Step::cont = NEW(CallCont)(Step::val, rest);
}

bool ArgThenCallCont::equals(PTR(Cont)other) {
    PTR(ArgThenCallCont)casted = CAST(ArgThenCallCont)(other);
    if (casted == nullptr)
        return false;
    return this->actual_arg->equals(casted->actual_arg)
           && this->env->equals(casted->env)
           && this->rest->equals(casted->rest);
}

CallCont::CallCont(PTR(Val)to_be_called_val, PTR(Cont)rest) {
    this->to_be_called_val = to_be_called_val;
    this->rest = rest;
}

void CallCont::step_continue() {
    to_be_called_val->call_step(Step::val, rest);
}

bool CallCont::equals(PTR(Cont)other) {
    PTR(CallCont)casted = CAST(CallCont)(other);
    if (casted == nullptr)
        return false;
    return this->to_be_called_val->equals(casted->to_be_called_val)
           && this->rest->equals(casted->rest);

}