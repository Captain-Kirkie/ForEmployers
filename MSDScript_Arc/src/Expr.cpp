//
// Created by Kirk Hietpas on 1/21/21.
//

#include "Cont.h"
#include "Step.h"
#include "Env.h"
#include "Val.h"
#include "Expr.h"


/**
 * Expr methods, not overwritten
 */

std::string Expr::to_string() {
    std::stringstream ss;
    this->print(ss);
    return ss.str();
}

/**
 * Helper method to create string out of pretty printed expression
 */
std::string Expr::to_string_pretty() {
    std::stringstream ss;
    this->prettyPrint(ss);
    return ss.str();
}

/**
 * Helper method to print expressions to console
 */
void Expr::print_to_console() {
    std::ostream &consoleOutputStream = std::cout;
    this->print(consoleOutputStream);
    std::cout << std::endl;
}

/**
 *
 * @param ostream stream to write to
 * Calls helper method pretty_print_at and pass appropriate parameters. pretty_print_at recurs down and
 * writes appropriate chars to stream to print an expression
 */

void Expr::prettyPrint(std::ostream &ostream) {
    int spaceCount = 0;
    this->pretty_print_at(ostream, print_group_none, spaceCount, not_applicable);
}


/**
 * Helper method to test pretty print. Prints to console
 */
void Expr::pretty_print_to_console() {
    std::ostream &consoleOutputStream = std::cout;
    this->prettyPrint(consoleOutputStream);
    std::cout << std::endl;
}

/**
 * NumExpr Class represents a single number
 */

NumExpr::NumExpr(int rep) {
    this->rep = rep;
    this->val = NEW(NumVal)(rep);
}
/**
 *
 * @param other comparison environment
 * @return wheter both envronments are equal
 */
bool NumExpr::equals(PTR(Expr) other) {
    PTR(NumExpr) otherPtr = CAST(NumExpr)(other);
    if (otherPtr == nullptr) //this means that they are not of the smae type
        return false;
    else {
        return this->rep == otherPtr->rep;
    }
}

/**
 *
 * @param env Environment passed, Not necessary here
 * @return the value associate with the numVal
 */
PTR(Val)NumExpr::interp(PTR(Env) env) {
    return this->val;
}
/**
 *
 * @return false. NumExprs will not contain variables
 */
bool NumExpr::has_variable() {
    return false;
}
/**
 *
 * @param out_stream streamt to write to.
 * Write the rep to the stream
 */
void NumExpr::print(std::ostream &out_stream) {
    out_stream << this->rep;
}
/**
 *
 * @param ostream stream to write to
 * @param mode indicates need for paranthesis. Will not ever need parentheses
 * @param spaceCount used for spacing
 * @param cameFrom where did I  come from
 */
void NumExpr::pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) {
    ostream << this->rep;
}

/**
 * set mode to continue mode... no more work to be done
 * set return val to a NumVal containing this rep
 */
void NumExpr::step_interp() {
    Step::mode = Step::continue_mode;// change to continiue moed
    Step::val = NEW(NumVal)(rep); // put the value in the register
    Step::cont = Step::cont;
}
//--------------------------------------------------------------------------------------------------------------------
/**
 * AddExpr class- Addition of two expressions
 */

AddExpr::AddExpr(PTR(Expr) lhs, PTR(Expr) rhs) {
    this->lhs = lhs;
    this->rhs = rhs;
}

/**
 *
 * @param other comparison expression
 * @return Whether both expressions are of the same type and hold equilvant members
 */

bool AddExpr::equals(PTR(Expr) other) {
    PTR(AddExpr) otherPtr = CAST(AddExpr)(other); // checks if pointers or the same, if not returns nullptr
    if (otherPtr == nullptr) //this means that they are NOT of the same type
        return false;
    else { //return weather the value of
        return (this->lhs->equals(otherPtr->lhs) && this->rhs->equals(otherPtr->rhs));
    }
}

/**
 *
 * @param env envirnoment passed
 * @return a new expression with lhs and rhs added together
 */
PTR(Val)AddExpr::interp(PTR(Env) env) {
    //   this var       addd to           this var
    return this->lhs->interp(env)->add_to(this->rhs->interp(env));
}

/**
 *
 * @return whether left-hand-Side or right-hand-side contains a variable
 */
bool AddExpr::has_variable() {
    return lhs->has_variable() || rhs->has_variable();
}

/**
 *
 * @param out_stream  stream to write to
 * write paranthesis in proper places
 * write '+' between lhs and rhs
 */
void AddExpr::print(std::ostream &out_stream) {
    out_stream << "(";
    this->lhs->print(out_stream);
    out_stream << "+";
    this->rhs->print(out_stream);
    out_stream << ")";
}

// ADD(2*3 + 1)
// ADD(1 + 2 * 3)
//ADD(1 + 2*3)
//  ADD(1 + 2 + 3) RHS
//  ADD((1 + 2) + 3) LHS
//  ADD(1 * 2 + 3) LHS
// (2 + 3) + 1
// ADD((1+2)+ 1) // need para
// ADD((1 + 1 + 2)
// ADD(1 + 2*3)
/**
 *
 * @param ostream stream to write to
 * @param mode enum to pass
 * @param spaceCount used for printing proper spacing
 * @param cameFrom where did i come from. Accumulator
 */

void AddExpr::pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) {

    bool printPara = false;
    if (mode == print_group_add_or_mult || cameFrom == in_lhs_mult || cameFrom == in_rhs_of_mult ||
        cameFrom == in_lhs_add || cameFrom == rhs_lhs_nested) {
        printPara = true;
    }

    if (printPara)
        ostream << "(";

    // print_group_add
    this->lhs->pretty_print_at(ostream, print_group_add, spaceCount, in_lhs_add);
    ostream << " + ";
    // print group none
    this->rhs->pretty_print_at(ostream, print_group_none, spaceCount, in_rhs_add);

    if (printPara)
        ostream << ")";
}
/**
 * set mode register to interp mode
 * Remember lhs
 * Set Same environment
 * Change contiuation to a new RightThenAddCont
 */
void AddExpr::step_interp() {
    Step::mode = Step::interp_mode;
    Step::expr = lhs;
    Step::env = Step::env; /* no-op, so could ommit*/
    Step::cont = NEW(RightThenAddCont)(rhs, Step::env, Step::cont);
}

//-------------------------------------------------------------------------------------
/**
 * Multiplication class- multiplication of two expressions
 * Constructor
 */
MultExpr::MultExpr(PTR(Expr) lhs, PTR(Expr) rhs) {
    this->lhs = lhs;
    this->rhs = rhs;
}

/**
 *
 * @param env envrionment passed
 * @return lhs and rhs added together
 */
PTR(Val)MultExpr::interp(PTR(Env) env) {
    return lhs->interp(env)->mult_to(rhs->interp(env));
}

/**
 *
 * @return weather or not the lhs or rhs contains a variable
 */
bool MultExpr::has_variable() {
    return lhs->has_variable() || rhs->has_variable();
}
/**
 *
 * @param other exprssion for comparision
 * @return if this and other are of the same type, and member variables are equilivant
 */
bool MultExpr::equals(PTR(Expr) other) {
    PTR(MultExpr) otherPtr = CAST(MultExpr)(other); // checks if pointers or the same, if not returns nullptr
    if (otherPtr == nullptr) //this means that they are NOT of the same type
        return false;
    else { //return weather the value of these is the same
        return (this->lhs->equals(otherPtr->lhs) && this->rhs->equals(otherPtr->rhs));
    }
}
/**
 *
 * @param out_stream stream to write to.
 * print parentheses in the correct spot. add multiplication symbol in between
 */
void MultExpr::print(std::ostream &out_stream) {
    out_stream << "(";
    this->lhs->print(out_stream);
    out_stream << "*";
    this->rhs->print(out_stream);
    out_stream << ")";
}


// MultExpr((1*2)*3) lhs
//MultExpr((1+2)*3) lhs
// MultExpr(1 * 1*2) rhs
// MultExpr(1 * (1+2)) rhs
// MultExpr((1 * 2) * 1) lhs
// MultExpr((1 + 2) * 1) lhs
/**
 *
 * @param ostream stream to write to
 * @param mode mode passed- determines need for parenthesis (accumulator)
 * @param spaceCount used to print proper spacing
 * @param cameFrom accumulator, where did I come from
 */
void MultExpr::pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) {
    bool printPara = false;
    if (mode == print_group_add_or_mult || cameFrom == in_lhs_mult) { //print para if i come from add or mult
        printPara = true;
    }
    if (printPara)
        ostream << "(";
    // print group add or mult
    this->lhs->pretty_print_at(ostream, print_group_add_or_mult, spaceCount, rhs_lhs_nested);

    ostream << " * ";

    //Print group add
    if (cameFrom == in_lhs_add)
        this->rhs->pretty_print_at(ostream, print_group_add, spaceCount, rhs_lhs_nested);
    else
        this->rhs->pretty_print_at(ostream, print_group_add, spaceCount, in_rhs_of_mult);

    if (printPara)
        ostream << ")";
}

/**
 * Set mode to interp mode
 * Remember lhs
 * Same env
 * Change continuation to a new RightThenMultCont
 */
void MultExpr::step_interp() {
    Step::mode = Step::interp_mode;
    Step::expr = lhs;
    Step::env = Step::env;
    Step::cont = NEW(RightThenMultCont)(rhs, Step::env, Step::cont);
}



//-----------------------------------------------------------------------------
/**
 * Variable class represents a variable such as x, y
 */

VarExpr::VarExpr(std::string value) {
    this->rep = value;
}

/**
 *
 * @param other Expression to be compared
 * @return Whether the expression are of the same type and member variables are equivalent
 */
bool VarExpr::equals(PTR(Expr) other) {
    PTR(VarExpr) otherPtr = CAST(VarExpr)(other); // dynamic cast to rep pointer
    if (otherPtr == nullptr) { //then they are not of the same type
        return false;
    } else {
        return this->rep == otherPtr->rep;
    }
}

PTR(Val)VarExpr::interp(PTR(Env) env) {
    return env->lookup(this->rep);
}

bool VarExpr::has_variable() {
    return true;
}

void VarExpr::pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) {
    ostream << this->rep;
}

void VarExpr::print(std::ostream &out_stream) {
    out_stream << this->rep;
}

void VarExpr::step_interp() {
    Step::mode = Step::continue_mode;
    Step::val = Step::env->lookup(rep);
    Step::cont = Step::cont;
}

//------------------------ LET_EXPR ------------------------
bool LetExpr::equals(PTR(Expr) other) {
    // ... rep .. rhs .. in .
    PTR(LetExpr) otherLet = CAST(LetExpr)(other);
    if (otherLet == nullptr)
        return false;

    return this->var->equals(otherLet->var)
           && this->rhs->equals(otherLet->rhs)
           && this->_in->equals(otherLet->_in);
}

bool LetExpr::has_variable() {
    // ... rep .. rhs .. in ..
    return rhs->has_variable() || _in->has_variable(); // if rhs and body has variable
}

void LetExpr::print(std::ostream &out_stream) {
    out_stream << "(_let " << this->var->to_string() << "=" << this->rhs->to_string() << " _in "
               << this->_in->to_string() << ")";
}

void LetExpr::pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) {
    //The right-hand side or body of a _let expression will not need any more parentheses than if that right-hand side or body were the whole expression.
    // .... rep... rhs ... _in ...
    bool needsPara = false;

    if (mode == print_group_let || cameFrom == in_lhs_add || cameFrom == rhs_lhs_nested)
        needsPara = true;

    if (needsPara) {
        ostream << "(";
        letPrettyPrintHelper(spaceCount, ostream);
        ostream << ")";
    } else {
        letPrettyPrintHelper(spaceCount, ostream);
    }
}

PTR(Val)LetExpr::interp(PTR(Env) env) {
    // ... rep ... rhs... _in ...
    // ...equals, subs, hash variable
    PTR(Val) rhs_val = rhs->interp(env);
    PTR(Env) new_env = NEW(ExtendedEnv)(var->to_string(), rhs_val, env);
    return _in->interp(new_env);
}

LetExpr::LetExpr(PTR(VarExpr) var, PTR(Expr) rhs, PTR(Expr) _in) {
    this->var = var;
    this->_in = _in;
    this->rhs = rhs;
}

void LetExpr::letPrettyPrintHelper(int space_count, std::ostream &ostream) {
    int locationOfLet = ostream.tellp();
    ostream << "_let ";
    this->var->pretty_print_at(ostream, print_group_let, space_count, lhs_let);
    ostream << " = ";
    this->rhs->pretty_print_at(ostream, print_group_let, space_count,
                               rhs_let);
    ostream << "\n";
    int newLineLocation = ostream.tellp();

    for (int i = 0; i < (locationOfLet - space_count); ++i) { //write spaces
        ostream << " ";
    }
    ostream << "_in  ";
    this->_in->pretty_print_at(ostream, print_group_let, newLineLocation, body_let);
}

void LetExpr::step_interp() {
    Step::mode = Step::interp_mode;
    Step::expr = rhs;
    Step::env = Step::env;
    Step::cont = NEW(LetBodyCont)(this->var->to_string(), _in, Step::env, Step::cont);
}

//------------------------ BOOL_EXPR ------------------------
void BoolExpr::print(std::ostream &out_stream) {
    std::string trueFalse = (this->rep) ? "_true" : "_false";
    out_stream << trueFalse;
}

bool BoolExpr::equals(PTR(Expr) other) {
    PTR(BoolExpr) newBoolExpr = CAST(BoolExpr)(other);
    if (newBoolExpr == nullptr)
        return false;
    else
        return this->rep == newBoolExpr->rep;
}

PTR(Val)BoolExpr::interp(PTR(Env) env) {
    return this->val;
}

bool BoolExpr::has_variable() {
    return false;
}

void BoolExpr::pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) {
    std::string boolean = (this->rep) ? "_true" : "_false";
    ostream << boolean;
}

BoolExpr::BoolExpr(bool rep) {
    this->rep = rep;
    this->val = NEW(BoolVal)(rep);
}

void BoolExpr::step_interp() {
    Step::mode = Step::continue_mode;
    Step::val = NEW(BoolVal)(rep);
    Step::cont = Step::cont;
}


// == expression returns a bool val
EqualExpr::EqualExpr(PTR(Expr) lhs, PTR(Expr) rhs) {
    this->rhs = rhs;
    this->lhs = lhs;
}

void EqualExpr::print(std::ostream &out_stream) {
    this->lhs->print(out_stream);
    out_stream << "==";
    this->rhs->print(out_stream);
}

bool EqualExpr::equals(PTR(Expr) other) {
    PTR(EqualExpr) otherPtr = CAST(EqualExpr)(other); // checks if pointers or the same, if not returns nullptr
    if (otherPtr == nullptr) //this means that they are NOT of the same type
        return false;
    else { //return weather the value of these is the same
        return (this->lhs->equals(otherPtr->lhs) && this->rhs->equals(otherPtr->rhs));
    }
}

PTR(Val)EqualExpr::interp(PTR(Env) env) {
    return NEW(BoolVal)(lhs->interp(env)->equals(rhs->interp(env))); // return a new bool if rhs and lhs are equal
}

bool EqualExpr::has_variable() {
    return this->lhs->has_variable() || this->rhs->has_variable();
}


void EqualExpr::pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) {
    //TODO needs para on rhs, except with another equal expression,
    // Everthing on rhs of equals
    bool needsPara = true;

    if (cameFrom == rhs_eq || mode == print_group_none ||
        cameFrom == lhs_let || cameFrom == rhs_let ||
        cameFrom == body_let ||
        cameFrom == if_bool) //if equals anything but rhs of eq, or its top level
        needsPara = false;

    if (needsPara) {
        ostream << "(";
    }
    this->lhs->pretty_print_at(ostream, print_group_none, spaceCount, lhs_eq);

    ostream << " == ";

    //Print group add
    this->rhs->pretty_print_at(ostream, print_group_none, spaceCount, rhs_eq);

    if (needsPara) {
        ostream << ")";
    }
}

void EqualExpr::step_interp() {
    Step::mode = Step::interp_mode;
    Step::expr = lhs;
    Step::env = Step::env;
    Step::cont = NEW(RightThenCompCont)(rhs, Step::env, Step::cont);
}

// 1 == (1*2)    == (1)
// IF_ELSE EXPRESSION
// ....test/Bool part .....
// ....then_part .......
// .....else_part .....

IfExpr::IfExpr(PTR(Expr) boolean, PTR(Expr) _then, PTR(Expr) _else) {
    this->boolean = boolean;
    this->_then = _then;
    this->_else = _else;
}

void IfExpr::print(std::ostream &out_stream) {
    out_stream << "_if " << this->boolean->to_string() << " _then " << this->_then->to_string()
               << " _else " << this->_else->to_string();
}

bool IfExpr::equals(PTR(Expr) other) {
    //... bool ... _then ... _else
    PTR(IfExpr) otherPtr = CAST(IfExpr)(other); // checks if pointers or the same, if not returns nullptr
    if (otherPtr == nullptr) //this means that they are NOT of the same type
        return false;
    else { //return weather the value of these is the same
        return (this->boolean->equals(otherPtr->boolean) && this->_then->equals(otherPtr->_then) &&
                this->_else->equals(otherPtr->_else));
    }
}

PTR(Val)IfExpr::interp(PTR(Env) env) {
    if (boolean->interp(env)->is_true()) {
        return _then->interp(env);
    } else
        return _else->interp(env);
}

bool IfExpr::has_variable() {
    //... bool ... _then ... _else
    return this->boolean->has_variable() || this->_then->has_variable() || this->_else->has_variable();
}


void IfExpr::pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) {
    // should have lowest presedence, so if it comes from anything, needs para , add, mult,
    int locationOfLet = ostream.tellp();
    ostream << "_if ";
    this->boolean->pretty_print_at(ostream, print_group_let, spaceCount, if_bool);
    ostream << "\n";

    for (int i = 0; i < (locationOfLet - spaceCount); i++) { //write spaces
        ostream << " ";
    }
    ostream << "_then ";
    this->_then->pretty_print_at(ostream, print_group_let, spaceCount,
                                 if_then);
    ostream << "\n";

    int newLineLocation = ostream.tellp();

    for (int i = 0; i < (locationOfLet - spaceCount); i++) { //write spaces
        ostream << " ";
    }
    ostream << "_else ";
    this->_else->pretty_print_at(ostream, print_group_let, newLineLocation, if_else);
}

void IfExpr::step_interp() {
    Step::mode = Step::interp_mode; // set to interp mode
    Step::expr = this->boolean; // the part we are testing
    Step::env = Step::env;
    Step::cont = NEW(IFBranchCont)(_then, _else, Step::env, Step::cont);
}


// ----------------------Function Expressions -------------------------
PTR(Val)FunExpr::interp(PTR(Env) env) { // return new function value, with this body, and formal arg (x)
    return NEW(FunVal)(this->formal_arg, this->body, env);
}

void FunExpr::print(std::ostream &out_stream) {
    out_stream << "(_fun (" + this->formal_arg + ") ";
    this->body->print(out_stream);
    out_stream << ')';
}

void FunExpr::pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) {

    bool needsPara = false;

    if (mode == print_group_let || cameFrom == in_lhs_add || cameFrom == rhs_lhs_nested)
        needsPara = true;

    if (needsPara)
        ostream << "(";

    ostream << "_fun (" + this->formal_arg + ")\n" + "  ";

    this->body->pretty_print_at(ostream, print_group_none, spaceCount, fun_body);

    if (needsPara)
        ostream << ")";
}

bool FunExpr::equals(PTR(Expr) e) {
    PTR(FunExpr) otherPtr = CAST(FunExpr)(e); // checks if pointers or the same, if not returns nullptr
    if (otherPtr == nullptr) //this means that they are NOT of the same type
        return false;
    else { //return weather the value of these is the same
        return this->formal_arg == otherPtr->formal_arg && this->body->equals(otherPtr->body);
    }
}

bool FunExpr::has_variable() {
    return this->body->has_variable();
}

FunExpr::FunExpr(std::string formal_arg, PTR(Expr) body) {
    this->body = body;
    this->formal_arg = formal_arg;
}

void FunExpr::step_interp() {
    //.... string-formal_arg ..... expr-Body
    Step::mode = Step::continue_mode; // this is contiue mode
    Step::expr = Step::expr; // dont make new expressions at runtime
    Step::env = Step::env;
    Step::val = NEW(FunVal)(this->formal_arg, this->body, Step::env);
    Step::cont = Step::cont;
}


// Function call stuff ---------------------------------------------------
bool CallExpr::equals(PTR(Expr) e) {
    PTR(CallExpr) otherPtr = CAST(CallExpr)(e); // checks if pointers or the same, if not returns nullptr
    if (otherPtr == nullptr) //this means that they are NOT of the same type
        return false;
    else { //return weather the value of these is the same
        return this->to_be_called->equals(otherPtr->to_be_called) && this->actual_arg->equals(otherPtr->actual_arg);
    }
}

PTR(Val)CallExpr::interp(PTR(Env) env) {
    //     write call method
    // ....to_be_called .... Expression
    // .... actual_arg....Expression
    return to_be_called->interp(env)->call(actual_arg->interp(env));
}

bool CallExpr::has_variable() {
    return this->to_be_called->has_variable() || this->actual_arg->has_variable();
}

void CallExpr::print(std::ostream &out_stream) {
    this->to_be_called->print(out_stream);
    out_stream << " (";
    this->actual_arg->print(out_stream);
    out_stream << ") ";
}

void CallExpr::pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) {
    this->to_be_called->print(ostream);
    ostream << " (";
    this->actual_arg->print(ostream);
    ostream << ")";
}

// f(2) -- to be called is the functionExpression f and actual arg is 2
CallExpr::CallExpr(PTR(Expr) to_be_called, PTR(Expr) actual_arg) {
    this->to_be_called = to_be_called;
    this->actual_arg = actual_arg;
}

void CallExpr::step_interp() {
    Step::mode = Step::interp_mode;
    Step::expr = to_be_called; // whatever we gonna call
    Step::cont = NEW(ArgThenCallCont)(actual_arg, Step::env, Step::cont);
}
