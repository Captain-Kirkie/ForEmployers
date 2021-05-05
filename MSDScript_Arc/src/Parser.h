//
// Created by Kirk Hietpas on 2/9/21.
//


#ifndef CAPTAIN_KIRKIE_MSDSCRIPT_PARSER_H
#define CAPTAIN_KIRKIE_MSDSCRIPT_PARSER_H

#include <iostream>
#include <sstream>
#include "pointer.h"

class Cont;
class Expr;
class Val;
class Env;
class Step;


CLASS(Parser) {

public:
    static PTR(Expr)parse_expr(std::istream &in);

    static PTR(Expr)parse_num(std::istream &in);

    static void consume(std::istream &in, int expect);

    static void skip_whitespace(std::istream &in);

    static PTR(Expr)parse_addend(std::istream &in);

    static PTR(Expr)parse_multicand(std::istream &in);

    static PTR(Expr)parse_inner(std::istream &in);

    static PTR(Expr)parse_let(std::istream &in);

    static PTR(Expr)parse_keyword(std::istream &in);

    static PTR(FunExpr)parse_fun(std::istream &in);

    static PTR(VarExpr)parse_var(std::istream &in);

    static PTR(Expr)parse_string(const std::string &string);

    static PTR(Expr)parse_comparg(std::istream &in);

    static PTR(Expr)parse_if_statement(std::istream &in);

    static PTR(Expr)createExpressionFromKeyword(std::string &keyword, std::istream &in);


};


#endif //CAPTAIN_KIRKIE_MSDSCRIPT_PARSER_H
