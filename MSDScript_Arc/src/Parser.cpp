//
// Created by Kirk Hietpas on 2/9/21.
//
// and alexander wept. for there were no more worlds to conquer

#include "Cont.h"
#include "Step.h"
#include "Env.h"
#include "Val.h"
#include "Expr.h"
#include "catch.h"
#include "CommandLine.h"
#include "Parser.h"
#include "pointer.h"

// parse expression always parsing the thing with the lowest presedece
PTR(Expr)Parser::parse_expr(std::istream &in) {
    PTR(Expr) e;

    e = parse_comparg(in);

    skip_whitespace(in);

    int c = in.peek();
    if (c == '=') {
        consume(in, '=');
        int x = in.peek();
        if (x != '=') {
            throw std::runtime_error("Expecting == but got =");
        } else
            consume(in, '=');
        PTR(Expr) rhs = parse_expr(in);
        return NEW(EqualExpr)(e, rhs);
    } else {
        return e;
    }
}


PTR(Expr)Parser::parse_comparg(std::istream &in) {
    PTR(Expr) e;

    e = parse_addend(in);

    skip_whitespace(in);

    int c = in.peek();
    if (c == '+') {
        consume(in, '+');
        PTR(Expr) rhs = parse_comparg(in);
        return NEW(AddExpr)(e, rhs);
    } else {
        return e;
    }
}


PTR(Expr)Parser::parse_addend(std::istream &in) {
    PTR(Expr) e;

    e = parse_multicand(in);

    skip_whitespace(in);

    int c = in.peek();
    if (c == '*') {
        consume(in, '*');
        PTR(Expr) rhs = parse_addend(in);
        return NEW(MultExpr)(e, rhs);
    } else {
        return e;
    }
}

PTR(Expr)Parser::parse_multicand(std::istream &in) {
    PTR(Expr) expr = parse_inner(in);
    skip_whitespace(in);
    while (in.peek() == '(') {
        consume(in, '(');
        PTR(Expr) actual_arg = parse_expr(in);
        consume(in, ')');
        expr = NEW(CallExpr)(expr, actual_arg);
    }
    return expr;
}


PTR(Expr)Parser::parse_inner(std::istream &in) {

    skip_whitespace(in);

    int c = in.peek();
    if (c == '-' || isdigit(c)) {
        return parse_num(in);
    } else if (isalpha(c)) { // if its an alpha, parse rep
        return parse_var(in);
    } else if (c == '_') { // if it starts with  '_' its a keyword
        return parse_keyword(in);
    } else if (c == '(') {
        consume(in, '(');
        PTR(Expr) e = parse_expr(in); // This should still call parse expression
        skip_whitespace(in);
        c = in.get();
        if (c != ')') {
            throw std::runtime_error("missing close parentheses");
        }
        return e;
    } else {
        consume(in, c);
        throw std::runtime_error("Invalid input");
    }
}

PTR(Expr)Parser::parse_num(std::istream &in) {
    int n = 0;
    bool negative = false; // is there a negative before the num

    if (in.peek() == '-') { // peek ahead, if its negative consume
        negative = true;
        consume(in, '-');
    }

    if (negative) {
        int peek = in.peek();
        if (!isdigit(peek)) // what if this is -x?
            throw std::runtime_error("double negative in front of num");
    }

    while (true) {
        int c = in.peek();
        if (isdigit(c)) {
            consume(in, c);
            n = (unsigned) n * 10 +
                (c - '0'); // subtract the zero string NOLINT(cppcoreguidelines-narrowing-conversions)
            if (n < 0) // wrap around failed
                throw std::runtime_error("Integer wrap around errors");
        } else {
            break;
        }
    }
    if (negative) {
        n = -n;
    }
    return NEW(NumExpr)(n);
}

void Parser::consume(std::istream &in, int expect) {
    int c = in.get();
    if (c != expect) {
        throw std::runtime_error("Consume mismatch");
    }
}

void Parser::skip_whitespace(std::istream &in) {
    while (true) {
        int c = in.peek();
        if (!isspace(c))
            break;
        consume(in, c);
    }
}


PTR(Expr)Parser::parse_let(std::istream &in) {
    PTR(Expr) rhs;
    PTR(Expr) body;
    PTR(VarExpr) lhs;

    skip_whitespace(in);
    lhs = parse_var(in);
    skip_whitespace(in);

    // _let x = 5 _in x + 1
    int c = in.peek();
    if (c == '=') {
        consume(in, c);
        skip_whitespace(in);
    }
    rhs = parse_expr(in);

    skip_whitespace(in);

    body = parse_expr(in);

    PTR(LetExpr) retLet = NEW(LetExpr)(lhs, rhs, body);

    return retLet;
}


PTR(Expr)Parser::parse_keyword(std::istream &in) {
    std::string keyword;
    while (true) {
        char c = in.peek();
        if (!isspace(c) && keyword.length() < 100 && c != '(' && c != ')') {
            consume(in, c);
            keyword.push_back(c);
        } else {
            break;
        }
    }
    return createExpressionFromKeyword(keyword, in);
}

PTR(Expr)Parser::createExpressionFromKeyword(std::string &keyword, std::istream &in) {
    skip_whitespace(in);
    if (keyword == "_let") {
        return parse_let(in); // if let
    } else if (keyword == "_if") {
        return parse_if_statement(in);
    } else if (keyword == "_in" || keyword == "_then" || keyword == "_else") {
        return parse_expr(in);
    } else if (keyword == "_true") {
        return NEW(BoolExpr)(true);
    } else if (keyword == "_false") {
        return NEW(BoolExpr)(false);
    } else if (keyword == "_fun") {
        return parse_fun(in);
    } else {
        throw std::runtime_error("Keyword does not exist:" + keyword);
    }
}

//TODO TEST THIS!!!!!!
PTR(FunExpr)Parser::parse_fun(std::istream &in) {
    // (_fun (x) x + 1)(10)
    // std::cout <<"I am getting inside parse fun" << std::endl;
    std::string formalArg;
    PTR(Expr) body;
    skip_whitespace(in);
    char c = in.peek();
    if (c == '(') {
        consume(in, c);
    }
    formalArg = parse_var(in)->to_string();
    c = in.peek();
    if (c == ')') {
        consume(in, c);
    }
    body = parse_expr(in);
    return NEW(FunExpr)(formalArg, body);
}

PTR(VarExpr)Parser::parse_var(std::istream &in) {
    std::string varString;
    skip_whitespace(in);
    while (true) {
        char c = in.peek();
        if (isalpha(c)) {
            consume(in, c); // move forward
            varString.push_back(c); // add it to the string
        } else {
            break;
        }
    }
    return NEW(VarExpr)(varString);
}

PTR(Expr)Parser::parse_string(const std::string &string) {
    std::istringstream iss(string);
    PTR(Expr) e = parse(iss);
    return e;
}

PTR(Expr)Parser::parse_if_statement(std::istream &in) {
    //  boolean ..... _then ...... _else
    PTR(Expr) boolean;
    PTR(Expr) _then;
    PTR(Expr) _else;

    boolean = parse_expr(in);
    _then = parse_expr(in);
    _else = parse_expr(in);
    return NEW(IfExpr)(boolean, _then, _else);
}






