//
// Created by Kirk Hietpas on 4/1/21.
//


#include "Step.h"
#include "Env.h"
#include "Cont.h"
#include "Expr.h"


Step::mode_t Step::mode;
PTR(Expr) Step::expr;
PTR(Env) Step::env;
PTR(Val) Step::val;
PTR(Cont) Step::cont;

PTR(Val) Step::interp_by_steps(PTR(Expr) e) {
    Step::mode = Step::interp_mode;
    Step::expr = e;
    Step::env = Env::empty; // starts with an empty env
    Step::val = nullptr;
    Step::cont = Cont::done;

    while(1){
        if(Step::mode == Step::interp_mode)
            Step::expr->step_interp();
        else{
            if(Step::cont == Cont::done){
                return Step::val;
            }else{
                Step::cont->step_continue();
            }
        }
    }
}
