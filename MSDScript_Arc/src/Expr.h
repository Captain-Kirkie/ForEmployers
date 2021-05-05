//
// Created by Kirk Hietpas on 1/21/21.
//



#ifndef CAPTAIN_KIRKIE_MSDSCRIPT_EXPR_H
#define CAPTAIN_KIRKIE_MSDSCRIPT_EXPR_H

#include <iostream>
#include <stdexcept>
#include "pointer.h"
//#include "Val.h"
#include <sstream>

class Cont;

class Val;

class Env;

class Step; // declaration
class NumVal;

class BoolVal;

/**
 * Super Class
 */

typedef enum {
    print_group_none, // 0
    print_group_add, // 1
    print_group_add_or_mult,  // 2
    print_group_let
} print_mode_t;

typedef enum {
    lhs_of_add_or_mult,// i need paratheses
    in_rhs_of_mult,
    in_lhs_mult,
    in_rhs_add,
    in_lhs_add,
    lhs_eq,
    rhs_eq,
    body_let,
    lhs_let,
    rhs_let,
    rhs_lhs_nested,
    not_applicable,
    if_bool,
    if_then,
    if_else,
    fun_body,

    // 1
} came_from;

CLASS(Expr) {
public: //  = 0 must be overwritten
    /**
     *
     * @param e expression to compare against
     * @return weather expressions for of same type and their member variables are equivalent
     */
    virtual bool equals(PTR(Expr) e) = 0;

    /**
     *
     * @return a value representation of the object,
     * if mult Val == (rep->lhs * rer->rhs)
     * if Add Val == (rep->lhs * rer->rhs) etc.
     */
    virtual PTR(Val) interp(PTR(Env) env) = 0;

    /**
     *
     * @return does any part of the expression contain a variable
     */
    virtual bool has_variable() = 0;

    /**
     *
     * @param out_stream stream to write to
     * Prints the expression with parentheses surround each individual expression
     * (1 + 2) + 3) == NEW(AddExpr)(NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)) , NEW(NumExpr)(3));
     */
    virtual void print(std::ostream &out_stream) = 0; //THIS NEEDS TO BE OVERWRITTEN

    /**
     *
     * @return string representation of the the Expression
     */
    std::string to_string();

    /**
     * Writes expression to stream in formatted correctly with spaces and parenthesis
     * @param ostream stream to write to
     */
    void prettyPrint(std::ostream &ostream);

    /**
     *
     * @param ostream stream to write to
     * @param mode accumulator passed down through nested expressions
     * @param spaceCount used to determine how many spaces to print for let and ifthenStatments
     * @param cameFrom another accumulator used to determine if a given expression needs parentheses
     */
    virtual void pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) = 0;

    /**
     * helper method used to print expressions to console
     */
    void print_to_console();

    /**
     *
     * @return returns a string representation of pretty-printed expression
     */
    std::string to_string_pretty();

    /**
     * helper method used to test pretty printing
     */
    void pretty_print_to_console();

    /**
     * Interprets expressions by updating global registers. Prevents stack overflow for large recursive calls.
     */
    virtual void step_interp() = 0;

};


/**
 * NumExpr Class represents a single number
 */

class NumExpr : public Expr {

public:
    int rep; // representation of the number
    PTR(NumVal) val;

    NumExpr(int rep);

    bool equals(PTR(Expr) other) override;

    // NumExpr interp will return itself
    //The value of a number is the number
    PTR(Val) interp(PTR(Env) env) override;

    //returns always returns false
    bool has_variable() override;

    void print(std::ostream &out_stream) override;

    void pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) override;

    void step_interp() override;


};

/**
 * AddExpr class- Addition of two expressions
 */
class AddExpr : public Expr {
private:
    PTR(Expr) lhs;
    PTR(Expr) rhs;
public:
    AddExpr(PTR(Expr) lhs, PTR(Expr) rhs);

    bool equals(PTR(Expr) other) override;

    // adds two Expressions, returns result
    // The value of an addition expression is the sum of the subexpression values
    PTR(Val) interp(PTR(Env) env) override;

    //returns true if either of the Expressions contains a variable
    bool has_variable() override;

    void print(std::ostream &out_stream) override;

    void pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) override;

    void step_interp() override;
};

/**
 * Multiplication class- multiplication of two expressions
 */
class MultExpr : public Expr {
private:
    PTR(Expr) lhs;
    PTR(Expr) rhs;
public:
    MultExpr(PTR(Expr) lhs, PTR(Expr) rhs);

    bool equals(PTR(Expr) other) override;

    //multiply two expressions, return result
    // The value of a multiplication expression is the product of the subexpression values
    PTR(Val) interp(PTR(Env) env) override;

    //returns true if either of the expressions has a variable
    bool has_variable() override;

    void print(std::ostream &out_stream) override;

    void pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) override;

    void step_interp() override;

};

/**
 * Variable class represents a variable such as x, y
 */

class VarExpr : public Expr {
public:
    std::string rep;

    VarExpr(std::string value);

    bool equals(PTR(Expr) other) override;
    //  A variable has no value, so interp for a variable should throw a std::runtime_error exception

    PTR(Val) interp(PTR(Env) env) override;

    // Always returns true
    bool has_variable() override;

    void print(std::ostream &out_stream) override;

    void pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) override;

    std::string getVar() { return this->rep; }

    void step_interp() override;

};


class LetExpr : public Expr {
private:
    PTR(VarExpr) var; // rep that is assigned
    PTR(Expr) rhs; // assignment to rep
    PTR(Expr) _in; // this is the expression that rep is used in
public:
    LetExpr(PTR(VarExpr) var, PTR(Expr) rhs, PTR(Expr) _in);

    void print(std::ostream &out_stream) override;

    bool equals(PTR(Expr) other) override;

    PTR(Val) interp(PTR(Env) env) override;

    bool has_variable() override;

    void pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) override;

    void letPrettyPrintHelper(int space_count, std::ostream &ostream);

    void step_interp() override;
};

// BOOL_EXPR------------------
class BoolExpr : public Expr {
private:
    bool rep;
    PTR(BoolVal) val;
public:
    explicit BoolExpr(bool rep);

    void print(std::ostream &out_stream) override;

    bool equals(PTR(Expr) other) override;

    PTR(Val) interp(PTR(Env) env) override;

    bool has_variable() override;

    void pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) override;

    void step_interp() override;

};


// EQUAL_EXPR ---------------------------------------------
class EqualExpr : public Expr {
public:
    PTR(Expr) rhs;
    PTR(Expr) lhs;

    EqualExpr(PTR(Expr) lhs, PTR(Expr) rhs);

    void print(std::ostream &out_stream) override;

    bool equals(PTR(Expr) other) override;

    PTR(Val) interp(PTR(Env) env) override;

    bool has_variable() override;

    void pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) override;

    void step_interp() override;

};

/**
 * Corresponds to If _then _else statements. Evaluate an expression and determine output accordingly
 */

class IfExpr : public Expr {
public:
    PTR(Expr) boolean;
    PTR(Expr) _then;
    PTR(Expr) _else;

    IfExpr(PTR(Expr) boolean, PTR(Expr) _then, PTR(Expr) _else);

    void print(std::ostream &out_stream) override;

    bool equals(PTR(Expr) other) override;

    PTR(Val) interp(PTR(Env) env) override;

    bool has_variable() override;

    void pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) override;

    void step_interp() override;

};


/**
 * Produces functions
 */
class FunExpr : public Expr {
public:
    std::string formal_arg; // argument f(x)
    PTR(Expr) body; // body of the function

    FunExpr(std::string formal_arg, PTR(Expr) body);

    bool equals(PTR(Expr) e) override;

    PTR(Val) interp(PTR(Env) env) override;

    bool has_variable() override;

    void print(std::ostream &out_stream) override;

    void pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) override;

    void step_interp() override;


};

/**
 * Function calls
 */
class CallExpr : public Expr {
public:
    PTR(Expr) to_be_called;
    PTR(Expr) actual_arg; // f(10)

    CallExpr(PTR(Expr) to_be_called, PTR(Expr) actual_arg);

    bool equals(PTR(Expr) e) override;

    PTR(Val) interp(PTR(Env) env) override;

    bool has_variable() override;


    void print(std::ostream &out_stream) override;

    void pretty_print_at(std::ostream &ostream, print_mode_t mode, int spaceCount, came_from cameFrom) override;

    void step_interp() override;
};

#endif //CAPTAIN_KIRKIE_MSDSCRIPT_EXPR_H