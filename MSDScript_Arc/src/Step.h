//
// Created by Kirk Hietpas on 4/1/21.
//

#ifndef CAPTAIN_KIRKIE_MSDSCRIPT_STEP_H
#define CAPTAIN_KIRKIE_MSDSCRIPT_STEP_H

#include "pointer.h"

class Cont;

class Expr;

class Val;

class Env;

CLASS(Step) {
public:
    typedef enum { // to define mode we are in
        interp_mode,
        continue_mode
    } mode_t;
    // registers
    static mode_t mode; /* choses mode */
    static PTR(Expr)expr; /* for interp_mode */
    static PTR(Env)env; /* for interp_mode */
    static PTR(Val)val; /* for continue_mode */
    static PTR(Cont)cont; /* for all modes*/

    static PTR(Val)interp_by_steps(PTR(Expr)e);

};


#endif //CAPTAIN_KIRKIE_MSDSCRIPT_STEP_H
