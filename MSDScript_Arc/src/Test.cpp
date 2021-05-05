//
// Created by Kirk Hietpas on 1/26/21.
//
#include "Cont.h"
#include "Step.h"
#include "Env.h"
#include "Val.h"
#include "Expr.h"
#include "catch.h"
#include "CommandLine.h"
#include "Parser.h"
#include "pointer.h"

static bool step_ok(PTR(Expr) expr, PTR(Env) env, PTR(Cont) cont,
                    Step::mode_t expected_mode,
                    PTR(Val) expected_val, /* NULL if we expect interp_mode */
                    PTR(Expr) expected_expr, /* NULL if we expect continue_mode */
                    PTR(Cont) expected_cont);
// http://www.cplusplus.com/forum/beginner/120947/
//TEST
/*slow
 _let fib = _fun (fib)
             _fun (x)
               _if x == 0
               _then 1
               _else _if x == 1
                     _then 1
                     _else fib(fib)(x + -2) + fib(fib)(x + -1)
_in  fib(fib)(30)

*/
/*
 * Fast
  _let pair = _fun (a) _fun (b)
              _fun(sel)
              _if sel _then a _else b
_in _let fst = _fun (p) p(_true)
_in _let snd = _fun (p) p(_false)
_in _let fib = _fun (fib)
                _fun (x)
                  _if x == 0
                  _then pair(1)(1)
                  _else _if x == 1
                        _then pair(1)(1)
                        _else _let p = fib(fib)(x + -1)
                              _in pair(fst(p) + snd(p))(fst(p))
_in  fst(fib(fib)(30))
 */



TEST_CASE("equals") {

    PTR(NumExpr) a = NEW(NumExpr)(1);
    PTR(NumExpr) b = NEW(NumExpr)(1);
    PTR(NumExpr) c = NEW(NumExpr)(2);
    PTR(NumExpr) d = NEW(NumExpr)(1);
    PTR(NumExpr) e = NEW(NumExpr)(4);

    // std::cout << "This is the type of the class should be numExpr \t" << typeid(a).name() << std::endl;

    PTR(AddExpr) add1 = NEW(AddExpr)(a, b); // 1, 1
    PTR(AddExpr) add2 = NEW(AddExpr)(a, b); // 1, 1
    PTR(AddExpr) add3 = NEW(AddExpr)(c, d); // 2, 1
    PTR(AddExpr) add4 = NEW(AddExpr)(a, b); // 1, 1
    PTR(AddExpr) add5 = NEW(AddExpr)(a, b); // 1, 1

    PTR(AddExpr) addAdd1 = NEW(AddExpr)(add1, add2); // these should be the same
    PTR(AddExpr) addAdd2 = NEW(AddExpr)(add1, add2); // these should be the same

    PTR(MultExpr) mult1 = NEW(MultExpr)(a, b);
    PTR(MultExpr) mult2 = NEW(MultExpr)(a, b);
    PTR(MultExpr) mult3 = NEW(MultExpr)(e, a);
    PTR(MultExpr) mult4 = NEW(MultExpr)(c, d);

    PTR(MultExpr) multMult1 = NEW(MultExpr)(mult1, mult2);
    PTR(MultExpr) multMult2 = NEW(MultExpr)(mult1, mult2);
    PTR(VarExpr) var1 = NEW(VarExpr)("x");
    PTR(VarExpr) var2 = NEW(VarExpr)("x");
    PTR(VarExpr) var3 = NEW(VarExpr)("y");
    PTR(VarExpr) var4 = NEW(VarExpr)("X");

    // a numexprssion == 1


    CHECK(a->interp(Env::empty)->equals(NEW(NumVal)(1)));

    CHECK(var1->equals(mult2) == false);

    CHECK((NEW(NumExpr)(1))->equals(NEW(NumExpr)(1)) == true);
    CHECK((NEW(NumExpr)(1))->equals(NEW(NumExpr)(2)) == false);

    CHECK(add1->equals(add2) == true);
    CHECK(add1->equals(add1) == true);

    CHECK(addAdd1->equals(addAdd2));

    CHECK(mult1->equals(mult2) == true);
    CHECK(mult1->equals(mult3) == false); //this failed

    CHECK(multMult1->equals(multMult2) == true);

    CHECK(var1->equals(var2) == true);
    CHECK(var2->equals(var3) == false);

    CHECK(var1->equals(var1) == true);

}

TEST_CASE("interp") {
    PTR(NumExpr) a = NEW(NumExpr)(1);
    PTR(NumExpr) b = NEW(NumExpr)(1);
    PTR(NumExpr) c = NEW(NumExpr)(2);
    PTR(NumExpr) d = NEW(NumExpr)(1);
    PTR(NumExpr) e = NEW(NumExpr)(4);
    PTR(NumExpr) f = NEW(NumExpr)(-4);
    PTR(NumExpr) g = NEW(NumExpr)(7);
    PTR(NumExpr) h = NEW(NumExpr)(8);


    PTR(AddExpr) add1 = NEW(AddExpr)(a, b); // 1, 1
    PTR(AddExpr) add2 = NEW(AddExpr)(a, c); // 1, 2
    PTR(AddExpr) add3 = NEW(AddExpr)(a, e); // 1, 4
    PTR(AddExpr) addNeg = NEW(AddExpr)(a, f); // 1 + (-4) == -3

    PTR(MultExpr) mult1 = NEW(MultExpr)(a, b); // 1*1 == 1
    PTR(MultExpr) mult2 = NEW(MultExpr)(c, f); //  2 * -4 == -8
    PTR(MultExpr) mult3 = NEW(MultExpr)(e, a);//  4 * 1 == 4
    PTR(MultExpr) mult4 = NEW(MultExpr)(g, h); // 7 *  8 == 56

    PTR(MultExpr) multMult1 = NEW(MultExpr)(mult1,
                                            mult2);// (1) * (-8auto auto *multMult2 = NEW(MultExpr)(mult1, mult2);

    PTR(VarExpr) var1 = NEW(VarExpr)("x");
    PTR(VarExpr) var2 = NEW(VarExpr)("x");
    PTR(VarExpr) var3 = NEW(VarExpr)("y");
    PTR(VarExpr) var4 = NEW(VarExpr)("X");

    PTR(AddExpr) addMult = NEW(AddExpr)(multMult1, mult4); //-8 +  56 == 48

// num interp
    CHECK(a->interp(Env::empty)->equals(NEW(NumVal)(1)));
    CHECK(e->interp(Env::empty)->equals(NEW(NumVal)(4)));
    CHECK(!a->interp(Env::empty)->equals(NEW(NumVal)(7)));
    CHECK(!a->interp(Env::empty)->equals(NEW(NumVal)(15)));
    CHECK(f->interp(Env::empty)->equals(NEW(NumVal)(-4)));

//AddExpr interp
    CHECK(add1->interp(Env::empty)->equals(NEW(NumVal)(2)));
    CHECK(add2->interp(Env::empty)->equals(NEW(NumVal)(3)));
    CHECK(add3->interp(Env::empty)->equals(NEW(NumVal)(5))); // 5
    CHECK(!add3->interp(Env::empty)->equals(NEW(NumVal)(100))); // !100
    CHECK(addNeg->interp(Env::empty)->equals(NEW(NumVal)(-3))); // -3
    CHECK(addMult->interp(Env::empty)->equals(NEW(NumVal)(48))); // 48
    CHECK(!addMult->interp(Env::empty)->equals(NEW(NumVal)(4))); // !=4

//mult interp
    CHECK(mult4->interp(Env::empty)->equals(NEW(NumVal)(56))); // 56
    CHECK(mult3->interp(Env::empty)->equals(NEW(NumVal)(4))); // 4
    CHECK(mult2->interp(Env::empty)->equals(NEW(NumVal)(-8))); // -8
    CHECK(!mult2->interp(Env::empty)->equals(NEW(NumVal)(-20))); // != -20
    CHECK(multMult1->interp(Env::empty)->equals(NEW(NumVal)(-8))); // -8
    CHECK(!multMult1->interp(Env::empty)->equals(NEW(NumVal)(-15)));  // != -15

// rep interp
    CHECK_THROWS(var1->interp(Env::empty), "Trying to interpret a variable");
}

TEST_CASE("has_variable") {

    PTR(NumExpr) one = NEW(NumExpr)(1);

    PTR(NumExpr) two = NEW(NumExpr)(2);
    PTR(NumExpr) seven = NEW(NumExpr)(7);
    PTR(NumExpr) negSeven = NEW(NumExpr)(-7);
    PTR(NumExpr) twenty = NEW(NumExpr)(20);

    PTR(VarExpr) var1 = NEW(VarExpr)("x");
    PTR(VarExpr) var2 = NEW(VarExpr)("x");
    PTR(VarExpr) var3 = NEW(VarExpr)("y");
    PTR(VarExpr) var4 = NEW(VarExpr)("X");

    PTR(AddExpr) add1 = NEW(AddExpr)(one, var1); // == true
    PTR(AddExpr) add2 = NEW(AddExpr)(two, seven);  // == false
    PTR(AddExpr) add3 = NEW(AddExpr)(var4, negSeven); //  == false
    PTR(AddExpr) add4 = NEW(AddExpr)(twenty, negSeven); //    == false

    PTR(AddExpr) addAdd1 = NEW(AddExpr)(add1, add2); // == true
    PTR(AddExpr) addAdd2 = NEW(AddExpr)(add3, add4); //  == false

    PTR(MultExpr) mult1 = NEW(MultExpr)(one, var1); // == true
    PTR(MultExpr) mult2 = NEW(MultExpr)(negSeven, twenty); // == false
    PTR(MultExpr) mult3 = NEW(MultExpr)(var4, var2); // == true
    PTR(MultExpr) multMultVar = NEW(MultExpr)(mult1, mult2); // true
    PTR(MultExpr) multAddVar = NEW(MultExpr)(mult1, add2); // true
    PTR(MultExpr) multAddNoVar = NEW(MultExpr)(add4, add2); // false


//num
    CHECK(one->has_variable() == false);

//rep
    CHECK(var1->has_variable() == true);

//add
    CHECK(add1->has_variable() == true);
    CHECK(add2->has_variable() == false);

//mult
    CHECK(mult1->has_variable() == true);
    CHECK(mult2->has_variable() == false);
    CHECK(mult3->has_variable() == true);
    CHECK(multAddNoVar->has_variable() == false);
    CHECK(multAddVar->has_variable() == true);
}
//
//TEST_CASE("subst") {
//
//    //Variables
//    PTR(NumExpr) one = NEW(NumExpr)(1);
//    PTR(NumExpr) two = NEW(NumExpr)(2);
//    PTR(NumExpr) seven = NEW(NumExpr)(7);
//    PTR(NumExpr) negSeven = NEW(NumExpr)(-7);
//    PTR(NumExpr) eight = NEW(NumExpr)(8);
//
//    PTR(VarExpr) x = NEW(VarExpr)("x");
//    PTR(VarExpr) y = NEW(VarExpr)("y");
//
//    PTR(AddExpr) add_One_X = NEW(AddExpr)(one, x); //
//    PTR(AddExpr) add_Seven_Y = NEW(AddExpr)(two, seven);  //
//    PTR(AddExpr) add_NegSeven_Eight = NEW(AddExpr)(negSeven, eight); //
//    PTR(AddExpr) add4 = NEW(AddExpr)(eight, y); //
//    PTR(AddExpr) one_one = NEW(AddExpr)(one, one);
//    PTR(AddExpr) x_x = NEW(AddExpr)(NEW(VarExpr)("x"), NEW(VarExpr)("x"));
//
//    PTR(MultExpr) mult_7_x = NEW(MultExpr)(seven, x); // ==
//    PTR(MultExpr) mult_x_x = NEW(MultExpr)(x, x); // ==
//    PTR(MultExpr) mult_y_y = NEW(MultExpr)(y, y);
//    PTR(MultExpr) mult_1_2 = NEW(MultExpr)(one, two); // ==
//    PTR(MultExpr) mult_Mult_x_7_8_1 = NEW(MultExpr)(NEW(MultExpr)(x, seven), NEW(MultExpr)(eight,
//                                                                                           one)); //auto *mult_7_x_1_2 = NEW(MultExpr)(mult_7_x, mult_1_2); //
//    CHECK(!mult_1_2->equals(NEW(NumExpr)(1)));
//    CHECK(!add_One_X->equals(mult_1_2));
//
////this is matts test case
//// (x + 7).subst(x, (y+7)).equals(y + 7)
//    CHECK((NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(7)))->subst("x", NEW(VarExpr)("y"))->equals(
//            NEW(AddExpr)(NEW(VarExpr)("y"), NEW(NumExpr)(7))));
//    CHECK((NEW(AddExpr)(NEW(VarExpr)("x"), NEW(VarExpr)("x")))->subst("x",
//                                                                      NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(1)))->
//            equals(NEW(AddExpr)(one_one, one_one)) == true);
//
//    CHECK((NEW(AddExpr)(NEW(VarExpr)("x"), NEW(VarExpr)("x")))->subst("y",
//                                                                      NEW(AddExpr)(NEW(NumExpr)(1),
//                                                                                   NEW(NumExpr)(1)))->equals(
//            x_x) == true);
////    CHECK(mult_7_x->subst("x", NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(1)))->equals(NEW(MultExpr)(seven, one_one)) ==
////          true);
////
////    CHECK(mult_x_x->subst("x", y)->equals(mult_y_y));
////    CHECK(mult_x_x->subst("y", add_One_X)->equals(mult_x_x) == true);
////
////    PTR(NumExpr) ten = NEW(NumExpr)(10);
////    PTR(AddExpr) testAdd1 = NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(-1));
////    CHECK(ten->subst("x", testAdd1)->equals(NEW(NumExpr)(10)));
////
////    PTR(AddExpr) add_x_add = NEW(AddExpr)(NEW(VarExpr)("x"), NEW(AddExpr)(NEW(NumExpr)(2), NEW(NumExpr)(1)));
////    //(x + (2 + 1))
////    // ((1 * 7) + (2 + 1))
////    PTR(AddExpr) add_Solution1 = NEW(AddExpr)(NEW(MultExpr)(NEW(NumExpr)(1), NEW(NumExpr)(7)),
////                                              NEW(AddExpr)(NEW(NumExpr)(2), NEW(NumExpr)(1)));
////    CHECK(add_x_add->subst("x", NEW(MultExpr)(NEW(NumExpr)(1), NEW(NumExpr)(7)))->equals(add_Solution1));
//
//}


TEST_CASE("Print") {

    std::string addTestString1 = "(1+2)";
    PTR(AddExpr) printAdd1 = NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2));

    std::string addAddTestString1 = "(1+(1+2))";
    PTR(AddExpr) printAddAdd1 = NEW(AddExpr)(NEW(NumExpr)(1), NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)));

    std::string multAdd = "((1+3)*(1+2))";
    PTR(MultExpr) multAdd1 = NEW(MultExpr)(NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(3)),
                                           NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)));

    PTR(VarExpr) varPrint = NEW(VarExpr)("x");
    PTR(NumExpr) numPrint = NEW(NumExpr)(8);

    CHECK(printAdd1->to_string() == addTestString1);
    CHECK(printAddAdd1->to_string() == addAddTestString1);
    CHECK(multAdd1->to_string() == multAdd);
    CHECK(varPrint->to_string() == "x");
    CHECK(numPrint->to_string() == "8");

    std::string prettyAddString = "1 + 1 + 2";
    PTR(AddExpr) prettyAdd = NEW(AddExpr)(NEW(NumExpr)(1), NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)));
    std::ostream &consolePretty = std::cout;
    // prettyAdd->prettyPrint(consolePretty);
    CHECK(prettyAdd->to_string_pretty() == prettyAddString);

//    1 + 2 * 3     1
//    1 * (2 + 3)    2
//    (2 * 3) * 4    3
//    2 * 3 * 4      4

    // 1 + 2 * 3
    std::string example1Answer = "1 + 2 * 3";
    PTR(AddExpr) example1 = NEW(AddExpr)(NEW(NumExpr)(1), NEW(MultExpr)(NEW(NumExpr)(2), NEW(NumExpr)(3)));
    CHECK(example1->to_string_pretty() == example1Answer);


    // 1 * (2 + 3)
    std::string example2Answer = "1 * (2 + 3)";
    PTR(MultExpr) example2 = NEW(MultExpr)(NEW(NumExpr)(1), NEW(AddExpr)(NEW(NumExpr)(2), NEW(NumExpr)(3)));
    CHECK(example2->to_string_pretty() == example2Answer);


    // (2 * 3) * 4
    std::string example3Answer = "(2 * 3) * 4";
    PTR(MultExpr) example3 = NEW(MultExpr)(NEW(MultExpr)(NEW(NumExpr)(2), NEW(NumExpr)(3)), NEW(NumExpr)(4));
    CHECK(example3->to_string_pretty() == example3Answer);

    // 2 * 3 * 4
    std::string example4Answer = "2 * 3 * 4";
    PTR(MultExpr) example4 = NEW(MultExpr)(NEW(NumExpr)(2), NEW(MultExpr)(NEW(NumExpr)(3), NEW(NumExpr)(4)));
    // std::cout << std::endl;
    CHECK(example4->to_string_pretty() == example4Answer);

    // (2 * 3) * 2 * 3
    PTR(MultExpr) doubleMult = NEW(MultExpr)(NEW(MultExpr)(NEW(NumExpr)(2), NEW(NumExpr)(3)),
                                             NEW(MultExpr)(NEW(NumExpr)(2), NEW(NumExpr)(3)));
    std::string doubleMultAnwer = "(2 * 3) * 2 * 3";
    std::string doubleMultAnwerWrong = "2 * 3 * 2 * 3";
    CHECK(doubleMult->to_string_pretty() == doubleMultAnwer);
    CHECK(doubleMult->to_string_pretty() != doubleMultAnwerWrong);


    std::string FunCall1 = "(_fun (x) (x+1))";
    PTR(FunExpr) FunCallPrintTest1 = NEW(FunExpr)("x", NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)));
    CHECK(FunCallPrintTest1->to_string() == FunCall1);
    auto call1 = Parser::parse_string("f(10)");
    CHECK(call1->to_string() == "f (10) ");
}

TEST_CASE("_let") {

// has variable --------------------------------
    PTR(LetExpr) hasVarFalse = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1),
                                            NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(1)));
    PTR(LetExpr) hasVarTrue = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1),
                                           NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)));

    CHECK(hasVarFalse->has_variable() == false);
    CHECK(hasVarTrue->has_variable() == true);

// interp ______________________________________________________________________________



    // let x = 1 _in x
    PTR(LetExpr) let2 = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1),
                                     NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)));
    PTR(LetExpr) let3 = NEW(LetExpr)(NEW(VarExpr)("y"), NEW(NumExpr)(1),
                                     NEW(MultExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1))); // _let y = 1 in x * 1

    PTR(LetExpr) let5 = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(4),
                                     NEW(MultExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(2)));
    PTR(LetExpr) let6 = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(2),
                                     NEW(MultExpr)(NEW(NumExpr)(3), NEW(VarExpr)("x")));  // let x = 2 in 3*x === 6
    PTR(LetExpr) test = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(8),
                                     NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)));
    PTR(LetExpr) nestedLet = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(5), test); // ``let x = 5
    PTR(LetExpr) let8 = NEW(LetExpr)(NEW(VarExpr)("y"), NEW(NumExpr)(9),
                                     NEW(MultExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(2))); // let y = 9 in x * 2  ==


    CHECK(!let8->equals(NEW(NumExpr)(1)));
    CHECK_THROWS(let8->interp(Env::empty), "Trying to interpret a variable");
    CHECK(nestedLet->interp(Env::empty)->equals(NEW(NumVal)(9))); // equals 9
    CHECK((LetExpr(NEW(VarExpr)("x"), NEW(NumExpr)(5),
                   NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)))).interp(Env::empty)->equals(
            NEW(NumVal)(6))); // == 6
    CHECK(let2->interp(Env::empty)->equals(NEW(NumVal)(2))); // 2
    CHECK_THROWS(let3->interp(Env::empty), "Trying to interpret a variable");
    CHECK(let5->interp(Env::empty)->equals(NEW(NumVal)(8))); // == 8
    CHECK(let6->interp(Env::empty)->equals(NEW(NumVal)(6))); // ==6
    std::ostream &testStream1 = std::cout;




    //Subst-------------------------------------------------------------


    // _let x = 1
    // _in  x + 2  -> subst("x", y+3)
    // =
    // _let x = 1
    // _in  x + 2

    PTR(Expr) let1Matt = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1),
                                      NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(2)));

    PTR(Expr) let1MattAnswer = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1),
                                            NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(2)));



//    PTR(Expr)let1Matt = (new _let("x",
//                              NEW(NumExpr)(1),
//                              NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(2))));

//    CHECK(let1Matt->subst("x", NEW(AddExpr)(NEW(VarExpr)("y"), NEW(NumExpr)(3)))
//                  ->equals(let1MattAnswer));

    // _let x = x
    // _in  x + 2  -> subst("x", y+3)
    // =
    // _let x = y+3
    // _in  x + 2
    PTR(Expr) let2Matt = (NEW(LetExpr)(NEW(VarExpr)("x"),
                                       NEW(VarExpr)("x"),
                                       NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(2))));
//    CHECK(let2Matt->subst("x", NEW(AddExpr)(NEW(VarExpr)("y"), NEW(NumExpr)(3)))
//                  ->equals(NEW(LetExpr)(NEW(VarExpr)("x"),
//                                        NEW(AddExpr)(NEW(VarExpr)("y"), NEW(NumExpr)(3)),
//                                        NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(2)))));

    // _let z = x
    // _in  z + 32 -> subst("z", 0)
    // =
    // _let z = x
    // _in  z + 32

    PTR(LetExpr) sameVarSub = NEW(LetExpr)(NEW(VarExpr)("z"), NEW(VarExpr)("x"),
                                           NEW(AddExpr)(NEW(VarExpr)("z"), NEW(NumExpr)(32)));
    PTR(LetExpr) sameVarSubAnswer = NEW(LetExpr)(NEW(VarExpr)("z"), NEW(VarExpr)("x"),
                                                 NEW(AddExpr)(NEW(VarExpr)("z"), NEW(NumExpr)(32)));

//    CHECK(sameVarSub->subst("z", NEW(NumExpr)(7))->equals(sameVarSubAnswer));

    // _let x = 8
    // _in  x + 2 + y -> subst("y", 9)
    // =
    // _let x = 8
    // _in  x + 2 + 9

    PTR(AddExpr) two_Plus_Nine = NEW(AddExpr)(NEW(NumExpr)(2), NEW(NumExpr)(9));
    PTR(AddExpr) addXToTwoPlus_9 = NEW(AddExpr)(NEW(VarExpr)("x"), two_Plus_Nine);
    PTR(LetExpr) subWorksAnswer = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(8), addXToTwoPlus_9);

    PTR(AddExpr) two_Plus_Y = NEW(AddExpr)(NEW(NumExpr)(2), NEW(VarExpr)("y"));
    PTR(AddExpr) addXToTwoPlus_Y = NEW(AddExpr)(NEW(VarExpr)("x"), two_Plus_Y);
    PTR(LetExpr) subWorksQuestion = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(8), addXToTwoPlus_Y);
//    CHECK(subWorksQuestion->subst("y", NEW(NumExpr)(9))->equals(subWorksAnswer));


    // _let x = y
    // _in  x + 2 -> subst("y", 8)
    // =
    // _let x = 8
    // _in  x + 2

    // _let x = 8
    // _in  x + 2 + y -> subst("y", 9)
    // =
    // _let x = 8
    // _in  x + 2 + 9

    // y + y -> subst("y", 8)
    // =
    // 8 + 8

    // _let x = y
    // _in  x + y -> subst("y", 8)
    // =
    // _let x = 8
    // _in  x + 8

    // _let z = x
    // _in  z + 32 -> subst("z", 0)
    // =
    // _let z = x
    // _in  z + 32

    // _let z = z
    // _in  z + 32 -> subst("z", 0)
    // =
    // _let z = 0
    // _in  z + 32

    // _let z = z + 2
    // _in  z + 32 -> subst("z", 0)
    // =
    // _let z = 0 + 2
    // _in  z + 32

    //print ----------------------------------------------------------------
    // (_let x=5 _in ((_let y=3 _in (y+2))+x))
    std::string mattsTestString = "(_let x=5 _in ((_let y=3 _in (y+2))+x))";
    PTR(LetExpr) testNestedPrint = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(5),
                                                NEW(AddExpr)(NEW(LetExpr)(NEW(VarExpr)("y"), NEW(NumExpr)(3),
                                                                          NEW(AddExpr)(NEW(VarExpr)("y"),
                                                                                       NEW(NumExpr)(2))),
                                                             NEW(VarExpr)("x")));
    CHECK(testNestedPrint->to_string() == mattsTestString);

    //equals ------------------------------------------------

    PTR(LetExpr) let1Equ = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1),
                                        NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1))); // let x = 1 _in x
    PTR(LetExpr) let2Equ = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1),
                                        NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1))); // same
    PTR(LetExpr) let3Equ = NEW(LetExpr)(NEW(VarExpr)("y"), NEW(NumExpr)(1),
                                        NEW(MultExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1))); // _let y = 1 in x * 1
    PTR(LetExpr) let5Equ = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(4),
                                        NEW(MultExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(2)));
    PTR(LetExpr) let6Equ = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(2),
                                        NEW(MultExpr)(NEW(NumExpr)(3), NEW(VarExpr)("x")));

    CHECK(let1Equ->equals(let2Equ));



// pretty print------------------------------------------------------------------------------------
//    _let x = 5
//    _in  (_let y = 3
//    _in  y + 2) + x


    std::string letTestPretty3Answer = "_let x = 5\n"
                                       "_in  (_let y = 3\n"
                                       "      _in  y + 2) + x";

    PTR(LetExpr) letTestPretty3Nested = NEW(LetExpr)(NEW(VarExpr)("y"), NEW(NumExpr)(3),
                                                     NEW(AddExpr)(NEW(VarExpr)("y"), NEW(NumExpr)(2)));
    PTR(AddExpr) letTestPretty3Add = NEW(AddExpr)(letTestPretty3Nested, NEW(VarExpr)("x"));
    PTR(LetExpr) letTestPretty3NestedFinal = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(5), letTestPretty3Add);

    CHECK(letTestPretty3NestedFinal->to_string_pretty() == letTestPretty3Answer);

//
//    5 * (_let x = 5
//    _in  x) + 1
//
// interp == 26

    std::string testPretty1StringAnswer = "5 * (_let x = 5\n"
                                          "     _in  x) + 1";


    //ADD(Mult(let))
    PTR(LetExpr) letTest1Pretty = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(5), NEW(VarExpr)("x"));
    PTR(MultExpr) testMultPretty1 = NEW(MultExpr)(NEW(NumExpr)(5), letTest1Pretty); // with let on Right
    PTR(AddExpr) addTestPretty1 = NEW(AddExpr)(testMultPretty1, NEW(NumExpr)(1)); // ADD

    CHECK(addTestPretty1->to_string_pretty() == testPretty1StringAnswer);
    CHECK(addTestPretty1->interp(Env::empty)->equals(NEW(NumVal)(26))); // == 26

// -----------------------------------------------------------------
//    5 * _let x = 5
//    _in  x + 1

//interp  == 30
    std::string testPretty2AnswerString = "5 * _let x = 5\n"
                                          "    _in  x + 1";

    // add on rhs of let, in rhs of mult
    PTR(LetExpr) anotherLetAdd = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(5),
                                              NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)));
    PTR(MultExpr) anotherMultPrettyTest = NEW(MultExpr)(NEW(NumExpr)(5), anotherLetAdd);
    CHECK(anotherMultPrettyTest->interp(Env::empty)->equals(NEW(NumVal)(30))); // 30
    CHECK(anotherMultPrettyTest->to_string_pretty() == testPretty2AnswerString);


    PTR(AddExpr) addTestPretty3 = NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1));
    PTR(LetExpr) testPretty2 = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(5), addTestPretty3);
    PTR(MultExpr) testMultPretty2 = NEW(MultExpr)(NEW(NumExpr)(5), testPretty2);
    // CHECK(testMultPretty2->to_string_pretty() == testPretty2AnswerString);
    CHECK(testMultPretty2->interp(Env::empty)->equals(NEW(NumVal)(30))); // == 30



//-----------------------------------------------------------------


    std::string easy1 = "_let x = 5\n"
                        "_in  x + 1";
    PTR(LetExpr) easyLet = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(5),
                                        NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)));
    CHECK(easyLet->to_string_pretty() == easy1);


    std::string secondHarderAnswer = "5 * (_let x = 5\n"
                                     "     _in  x) + 1";

    //let on lhs of add in rhs of mult
    PTR(LetExpr) secondHarderLet = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(5), NEW(VarExpr)("x"));
    PTR(MultExpr) multHarder = NEW(MultExpr)(NEW(NumExpr)(5), secondHarderLet);
    PTR(AddExpr) addHarder = NEW(AddExpr)(multHarder, NEW(NumExpr)(1));
    CHECK(addHarder->interp(Env::empty)->equals(NEW(NumVal)(26))); // == 26
    CHECK(addHarder->to_string_pretty() == secondHarderAnswer);


    std::string leftSideOfAddAnswer = "(_let x = 5\n"
                                      " _in  x) + 1";

    PTR(LetExpr) leftSideOfAddLet = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(5), NEW(VarExpr)("x"));
    PTR(AddExpr) letOnLeftAdd = NEW(AddExpr)(leftSideOfAddLet, NEW(NumExpr)(1));
    CHECK(letOnLeftAdd->to_string_pretty() == leftSideOfAddAnswer);

}


TEST_CASE("parse") {
    std::string x_1 = "x+1";
    std::string failure1 = "x+dsfa1";
    PTR(AddExpr) x_1_Add = NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1));
    CHECK(!x_1_Add->equals(Parser::parse_string(failure1)));

    std::string let1 = "_let x = 1 _in x";
    PTR(LetExpr) let1Test = NEW(LetExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1), NEW(VarExpr)("x"));
    CHECK(let1Test->equals(Parser::parse_string(let1)));

    std::string negOne = "-1";
    PTR(NumExpr) negOneParse = NEW(NumExpr)(-1);
    CHECK(negOneParse->equals(Parser::parse_string(negOne)));

    std::string missingPara = "(x+5";
    CHECK_THROWS_WITH(Parser::parse_string(missingPara), "missing close parentheses");

    std::string nonSense = ".....dfadls";
    CHECK_THROWS_WITH(Parser::parse_string(nonSense), "Invalid input");

    std::string invalidKeyword = "_kirk";
    CHECK_THROWS(Parser::parse_string(invalidKeyword));

    std::string paraString = " (x+1)";
    CHECK(Parser::parse_string(paraString)->equals(x_1_Add));


    PTR(MultExpr) x_1_Mult = NEW(MultExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1));
    std::string multParaString = " (x*1)";
    CHECK(Parser::parse_string(multParaString)->equals(x_1_Mult));


    std::string consumeError = "x";
    std::stringstream ss(consumeError);
    CHECK_THROWS_WITH(Parser::consume(ss, 'P'), "Consume mismatch");

    std::string parseFunction = "_fun (x) x + 1";
    CHECK(Parser::parse_string(parseFunction)->equals(
            NEW(FunExpr)("x", NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)))));
    CHECK(!Parser::parse_string(parseFunction)->equals(
            NEW(FunExpr)("x", NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(2)))));

    std::string neg = "-a + 2";
    PTR(EqualExpr) parseNeg = NEW(EqualExpr)(NEW(NumExpr)(-1), NEW(NumExpr)(2));
    CHECK_THROWS_WITH(Parser::parse_string(neg)->equals(parseNeg), "double negative in front of num");
}


TEST_CASE("Val Extra") {
    PTR(NumVal) numVal1 = NEW(NumVal)(1);
    CHECK_THROWS_WITH(numVal1->add_to(nullptr), "add of non-number");
    CHECK_THROWS_WITH(numVal1->mult_to(nullptr), "Multiplying of non-num");
    CHECK(numVal1->equals(nullptr) == false);
    CHECK_THROWS_WITH(numVal1->is_true(), "Trying to is_true on NumVal");

}

TEST_CASE("Bool  Stuff") {
    PTR(NumVal) numVal1 = NEW(NumVal)(1); // numValTester
    PTR(NumExpr) numExpr1 = NEW(NumExpr)(1);
    PTR(BoolVal) testBoolValTrue1 = NEW(BoolVal)(true);
    PTR(BoolExpr) testBoolExprTrue1 = NEW(BoolExpr)(true);

    PTR(BoolVal) testBoolValFalse1 = NEW(BoolVal)(false);
    PTR(BoolExpr) testBoolExprFalse1 = NEW(BoolExpr)(false);

    PTR(BoolVal) testBoolValTrue2 = NEW(BoolVal)(true);
    PTR(BoolExpr) testBoolExprTrue2 = NEW(BoolExpr)(true);

    PTR(BoolVal) testBoolValFalse2 = NEW(BoolVal)(false);
    PTR(BoolExpr) testBoolExprFalse2 = NEW(BoolExpr)(false);

    // ---------------------------------------------numVal
    CHECK(numVal1->to_string() == "1");
    CHECK_THROWS_WITH(numVal1->call(NEW(NumVal)(1)), "Trying to call a NumVal!");

    // ---------------------------------------------- BoolVAL
    CHECK_THROWS_WITH(testBoolValTrue1->add_to(testBoolValTrue2), "Can't ADD boolean vars");
    CHECK_THROWS_WITH(testBoolValTrue1->mult_to(testBoolValTrue2), "Can't MULT boolean vars");
    CHECK(testBoolExprTrue1->to_string() == "_true");
    CHECK(testBoolExprFalse1->to_string() == "_false");

    //--------------------------------------------------equals
    CHECK(testBoolValTrue1->equals(testBoolValTrue1));
    CHECK(!testBoolValTrue1->equals(testBoolValFalse1)); // !=
//    CHECK(testBoolValTrue1->to_expr()->equals(testBoolExprTrue1)); // true
//    CHECK(!testBoolValTrue1->to_expr()->equals(testBoolExprFalse1)); // !=
    CHECK(!testBoolValTrue1->equals(numVal1));

    CHECK(testBoolValFalse1->to_string() == "_false");
    CHECK(testBoolValTrue1->to_string() == "_true");

    // ---------------------------------------------- BoolEXPR
    std::string trueString = "_true";
    CHECK(testBoolExprTrue1->to_string() == trueString);
    CHECK(testBoolExprTrue1->interp(Env::empty)->equals(testBoolValTrue1)); // Why isnt all coverd?
    CHECK(testBoolExprFalse1->interp(Env::empty)->equals(testBoolValFalse1));
    CHECK(testBoolExprFalse1->interp(Env::empty)); // Kinda weird that this passes???????
    CHECK(!testBoolExprTrue1->equals(numExpr1));
    CHECK(!testBoolExprTrue1->has_variable());
//    CHECK(testBoolExprTrue1->subst("x", NEW(NumExpr)(1))->equals(testBoolExprTrue2));
    CHECK_THROWS_WITH(testBoolValTrue1->call(NEW(NumVal)(1)), "Trying to call a BoolVal");
    PTR(BoolExpr) boolInterp = NEW(BoolExpr)(true);
    CHECK(boolInterp->interp(Env::empty)->equals(NEW(BoolVal)(true)));
}


TEST_CASE("EqualExpr") {// num expression
    PTR(NumExpr) testNum_1 = NEW(NumExpr)(1);
    PTR(NumExpr) testNum_2 = NEW(NumExpr)(2);

    //add expression
    PTR(AddExpr) addTest_1_plus_2 = NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)); // 1+2
    PTR(AddExpr) addTest_1_plus_2_duplicate = NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)); // 1+2

    // mult expressions
    PTR(MultExpr) multTest_2_plus_3 = NEW(MultExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)); // 2*3
    PTR(MultExpr) multTest_2_plus_3_duplicate = NEW(MultExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)); // 2*3
    CHECK(!multTest_2_plus_3->equals(addTest_1_plus_2));

    PTR(EqualExpr) equalExpressionTest1 = NEW(EqualExpr)(addTest_1_plus_2, addTest_1_plus_2_duplicate);
    PTR(EqualExpr) equalExpressionTest1_duplicate = NEW(EqualExpr)(addTest_1_plus_2, addTest_1_plus_2_duplicate);
    CHECK(!equalExpressionTest1->equals(addTest_1_plus_2));
    // -------------------------------------------------- interp
    CHECK(equalExpressionTest1->interp(Env::empty)->equals(NEW(BoolVal)(true)));

    // ------------------------------------------------------- print
    std::string answerString1 = "(1+2)==(1+2)";
    CHECK(equalExpressionTest1->to_string() == answerString1);
    CHECK(equalExpressionTest1->equals(equalExpressionTest1_duplicate));
    CHECK(!equalExpressionTest1->equals(testNum_1));
    CHECK(!equalExpressionTest1->has_variable());

    // ----------------------------------------------------hasVar

    PTR(AddExpr) addWithVar = NEW(AddExpr)(NEW(NumExpr)(1), NEW(VarExpr)("x"));
    PTR(EqualExpr) equalsWithVarLeft = NEW(EqualExpr)(addWithVar,
                                                      addTest_1_plus_2); // (1+x) == (1+2)   ===  (1+x) == (1+2)
    PTR(EqualExpr) equalsWithVarRight = NEW(EqualExpr)(addTest_1_plus_2, addWithVar); // (1+2) == (1+x)
    CHECK(equalsWithVarLeft->has_variable());

    // ------------------------------------------------------------------------ subst

    PTR(EqualExpr) equalsSubstAnswer = NEW(EqualExpr)(addTest_1_plus_2, addTest_1_plus_2);   // 1+2 == 1+2
//    CHECK(equalsWithVarLeft->subst("x", NEW(NumExpr)(2))->equals(equalsSubstAnswer));
//    CHECK(equalsWithVarRight->subst("x", NEW(NumExpr)(2))->equals(equalsSubstAnswer));

    PTR(AddExpr) addABool = NEW(AddExpr)(NEW(BoolExpr)(true), NEW(NumExpr)(1));
    CHECK_THROWS_WITH(addABool->interp(Env::empty), "Can't ADD boolean vars");
    // (1+x) == (1+2)
    CHECK(!equalsWithVarLeft->equals(NEW(NumExpr)(1)));





    //--------------------------------------------------PrettyPrint
    // 1+2 == 1+2
    std::string answer1 = "1 + 2 == 1 + 2";
    PTR(EqualExpr) equals1_2 = NEW(EqualExpr)(NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)),
                                              NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)));
    CHECK(equals1_2->to_string_pretty() == answer1);

    // 8 * (1 + 2) == 24
    std::string answer2 = "8 * (1 + 2) == 24";
    auto equalPretty2 = NEW(EqualExpr)(NEW(MultExpr)(NEW(NumExpr)(8), NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2))),
                                       NEW(NumExpr)(24));
    CHECK(equalPretty2->to_string_pretty() == answer2);


    // 8 * (1==1) == 8 // needs para
    std::string answer3 = "8 * (1 == 1) == 8";
    PTR(MultExpr) multWithEQOnRight = NEW(MultExpr)(NEW(NumExpr)(8), NEW(EqualExpr)(NEW(NumExpr)(1), NEW(NumExpr)(1)));
    PTR(EqualExpr) multOnLeftNumOnRight = NEW(EqualExpr)(multWithEQOnRight, NEW(NumExpr)(8));
    CHECK(multOnLeftNumOnRight->to_string_pretty() == answer3);


    // num  mult  eq num
    // 8 == 8 * 1 == 1 // does not need para
    std::string answer4 = "8 == 8 * 1 == 1";
    PTR(MultExpr) eightTimeOne = NEW(MultExpr)(NEW(NumExpr)(8), NEW(NumExpr)(1));
    PTR(EqualExpr) equalsWithMultOnLeftAndNumRight = NEW(EqualExpr)(eightTimeOne, NEW(NumExpr)(1));
    PTR(EqualExpr) anotherEQ = NEW(EqualExpr)(NEW(NumExpr)(8), equalsWithMultOnLeftAndNumRight);
    CHECK(anotherEQ->to_string_pretty() == answer4);


    //     mult
    //  eq         es
    // (8 == 8) * 1 == 1 // does not need para
    std::string answer5 = "(8 == 8) * (1 == 1)";
    PTR(EqualExpr) lhsEQ = NEW(EqualExpr)(NEW(NumExpr)(8), NEW(NumExpr)(8));
    PTR(EqualExpr) rhsEQ = NEW(EqualExpr)(NEW(NumExpr)(1), NEW(NumExpr)(1));
    PTR(MultExpr) multEQ = NEW(MultExpr)(lhsEQ, rhsEQ);
    CHECK(multEQ->to_string_pretty() == answer5);

}

TEST_CASE("if then statment") {

    //if(true)
    // then 8
    // else 1 ---->> 8

    PTR(NumExpr) testNum_1 = NEW(NumExpr)(1);

    // interp
    PTR(IfExpr) trueThenIfExpression = NEW(IfExpr)(NEW(BoolExpr)(true), NEW(NumExpr)(8), NEW(NumExpr)(1));
    PTR(NumExpr) eight = NEW(NumExpr)(8);
    CHECK(trueThenIfExpression->interp(Env::empty)->equals(eight->interp(Env::empty)));

    PTR(IfExpr) falseThenIfExpression = NEW(IfExpr)(NEW(BoolExpr)(false), NEW(NumExpr)(8), NEW(NumExpr)(1));
    PTR(NumExpr) one = NEW(NumExpr)(1);
    CHECK(falseThenIfExpression->interp(Env::empty)->equals(one->interp(Env::empty)));

    //-----------------------------------------print
    std::string ifTrueThen8Else1 = "_if _true _then 8 _else 1";
    CHECK(trueThenIfExpression->to_string() == ifTrueThen8Else1);

    //... bool ... _then ... _else
    PTR(IfExpr) trueWithVarInBool = NEW(IfExpr)(NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)), NEW(NumExpr)(1),
                                                NEW(NumExpr)(2));
    CHECK(trueWithVarInBool->has_variable());
    PTR(IfExpr) trueWithVarInThen = NEW(IfExpr)(NEW(BoolExpr)(true), NEW(VarExpr)("x"), NEW(NumExpr)(1));
    CHECK(trueWithVarInThen->has_variable());
    PTR(IfExpr) trueWithVarInElse = NEW(IfExpr)(NEW(BoolExpr)(true), NEW(NumExpr)(1), NEW(VarExpr)("x"));
    CHECK(trueWithVarInElse->has_variable());


    //-------------------------------------------- Equals
    PTR(IfExpr) substTest1 = NEW(IfExpr)(NEW(BoolExpr)(true), NEW(VarExpr)("x"),
                                         NEW(NumExpr)(5)); // if true, then 1, else 5
    //look for x and replace with new ADD (1+2)
    PTR(AddExpr) add1Plus2 = NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2));
    PTR(IfExpr) substTest1AnswerInThen = NEW(IfExpr)(NEW(BoolExpr)(true),
                                                     NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)),
                                                     NEW(NumExpr)(5)); // if true 1+2 else 5
//    CHECK(substTest1->subst("x", add1Plus2)->equals(substTest1AnswerInThen));
    PTR(IfExpr) substTest1AnswerInElse = NEW(IfExpr)(NEW(BoolExpr)(true), NEW(NumExpr)(5),
                                                     NEW(AddExpr)(NEW(NumExpr)(1),
                                                                  NEW(NumExpr)(2))); // if true 5 else 1+2
    PTR(IfExpr) substTestVarInElse = NEW(IfExpr)(NEW(BoolExpr)(true), NEW(NumExpr)(5),
                                                 NEW(VarExpr)("x")); // if true, then 1, else 5
//    CHECK(substTest1AnswerInElse->subst("x", add1Plus2)->equals(substTest1AnswerInElse));
    CHECK(!substTest1AnswerInElse->equals(testNum_1));




//---------------------------------------prettyPrint
//_if 4 + 1
//_then 2
//_else 3

    std::string ifThenFirstTest = "_if 4 + 1\n"
                                  "_then 2\n"
                                  "_else 3";
    auto ifThenTestPrettyPrint1 = NEW(IfExpr)(NEW(AddExpr)(NEW(NumExpr)(4), NEW(NumExpr)(1)), NEW(NumExpr)(2),
                                              NEW(NumExpr)(3));
    CHECK(ifThenTestPrettyPrint1->to_string_pretty() == ifThenFirstTest);


    std::string ifTrueThen1Else2PrettyAnswer = "_if _true\n"
                                               "_then 1\n"
                                               "_else 2";

    PTR(IfExpr) ifTrueThen1Else2 = NEW(IfExpr)(NEW(BoolExpr)(true), NEW(NumExpr)(1), NEW(NumExpr)(2));
    CHECK(Parser::parse_string(ifTrueThen1Else2PrettyAnswer)->equals(ifTrueThen1Else2));

    std::string mattsComplicatedPrettyAnser = "_let same = 1 == 2\n"
                                              "_in  _if 1 == 2\n"
                                              "     _then _false + 5\n"
                                              "     _else 88";

    PTR(EqualExpr) complicatedEQ = NEW(EqualExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2));
    PTR(AddExpr) addBoolFalsePlusFive = NEW(AddExpr)(NEW(BoolExpr)(false), NEW(NumExpr)(5));
    PTR(IfExpr) ifExpressionComplicated = NEW(IfExpr)(complicatedEQ, addBoolFalsePlusFive, NEW(NumExpr)(88));
    PTR(LetExpr) complicatedLet = NEW(LetExpr)(NEW(VarExpr)("same"), complicatedEQ, ifExpressionComplicated);
    CHECK(complicatedLet->to_string_pretty() == mattsComplicatedPrettyAnser);
    CHECK(complicatedLet->interp(Env::empty)->equals(NEW(NumVal)(88)));
}

TEST_CASE("Parse Equals") {
    std::string equalsParse1 = "1 == 2";
    PTR(EqualExpr) equalsParse1Answer = NEW(EqualExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2));
    CHECK(Parser::parse_string(equalsParse1)->equals(equalsParse1Answer));
}

TEST_CASE("Parse IfThen") {
    std::string ifThenTest1 = "_if _true _then 1 _else 2";
    PTR(IfExpr) ifThenTest1Answer = NEW(IfExpr)(NEW(BoolExpr)(true), NEW(NumExpr)(1), NEW(NumExpr)(2));
    CHECK(Parser::parse_string(ifThenTest1)->equals(ifThenTest1Answer));

    std::string ifThenTestFalse1 = "_if _false _then 1 _else 2";
    PTR(IfExpr) ifThenTestAnswerFalse = NEW(IfExpr)(NEW(BoolExpr)(false), NEW(NumExpr)(1), NEW(NumExpr)(2));
    CHECK(Parser::parse_string(ifThenTestFalse1)->equals(ifThenTestAnswerFalse));
    CHECK(Parser::parse_string(ifThenTestFalse1)->equals(ifThenTestAnswerFalse));

    std::string ifThenTest2 = "_if 1 == 1 _then 1 _else 2";
    auto ifThenTest2Answer = NEW(IfExpr)(NEW(EqualExpr)(NEW(NumExpr)(1), NEW(NumExpr)(1)), NEW(NumExpr)(1),
                                         NEW(NumExpr)(2));
    CHECK(Parser::parse_string(ifThenTest2)->equals(ifThenTest2Answer));
    CHECK(!Parser::parse_string(ifThenTest2)->equals(ifThenTest1Answer));

    std::string badEquals = "_if 1=1";
    CHECK_THROWS_WITH(Parser::parse_string(badEquals), "Expecting == but got =");
}

TEST_CASE("Functions Expression") {
    // Interp
    PTR(AddExpr) add1 = NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1));
    PTR(FunExpr) funExpr1 = NEW(FunExpr)("x", add1); // _fun (x) x + 1
    CHECK(funExpr1->interp(Env::empty)->call(NEW(NumVal)(1))->equals(NEW(NumVal)(2)));
    CHECK(!funExpr1->interp(Env::empty)->call(NEW(NumVal)(1))->equals(NEW(NumVal)(3)));
    CHECK(funExpr1->interp(Env::empty)->call(NEW(NumVal)(2))->equals(NEW(NumVal)(3)));

    // matts example
    CHECK(funExpr1->interp(Env::empty)->equals(
            NEW(FunVal)("x", NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)), Env::empty)));

    PTR(MultExpr) mult1 = NEW(MultExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(5)); // x * 5
    PTR(FunExpr) funExpr2 = NEW(FunExpr)("x", mult1); // _fun(x) x * 5
    CHECK(funExpr2->interp(Env::empty)->call(NEW(NumVal)(6))->equals(NEW(NumVal)(30))); // _fun(6) == 30
    CHECK(!funExpr2->interp(Env::empty)->call(NEW(NumVal)(6))->equals(NEW(NumVal)(25)));

    //Equals
    CHECK(funExpr1->equals(funExpr1)); // function equals itself
    CHECK(!funExpr1->equals(funExpr2)); // fun1 != fun2

    PTR(FunVal) FunVal1 = NEW(FunVal)("x", NEW(NumExpr)(1), Env::empty);
    PTR(FunVal) FunVal2 = NEW(FunVal)("x", NEW(NumExpr)(1), Env::empty);
    CHECK(FunVal1->equals(FunVal2));

    PTR(FunExpr) test2 = NEW(FunExpr)("x", NEW(MultExpr)(NEW(VarExpr)("x"), NEW(VarExpr)("x")));
    CHECK(test2->interp(Env::empty)->call(NEW(NumVal)(2))->equals(NEW(NumVal)(4)));

//  Subbing a function expression
    PTR(FunExpr) test3 = NEW(FunExpr)("x", NEW(MultExpr)(NEW(VarExpr)("x"), NEW(VarExpr)("y")));
//    CHECK(test3->subst("y", NEW(NumExpr)(1))->equals(
//            NEW(FunExpr)("x", NEW(MultExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)))));

    PTR(FunExpr) Matts = NEW(FunExpr)("x", NEW(MultExpr)(NEW(VarExpr)("x"), NEW(VarExpr)("y")));
    CHECK(Matts->interp(Env::empty)->equals(
            NEW(FunVal)("x", NEW(MultExpr)(NEW(VarExpr)("x"), NEW(VarExpr)("y")), Env::empty)));
}

TEST_CASE("Function Values") {

    PTR(AddExpr) add1 = NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1));
    PTR(FunVal) test1 = NEW(FunVal)("x", add1, Env::empty); //f(x) x + 1
    PTR(FunVal) test2 = NEW(FunVal)("x", add1, Env::empty);
    PTR(FunVal) test3 = NEW(FunVal)("y", add1, Env::empty);
    CHECK(test1->equals(test2));
    CHECK(!test1->equals(test3));

    std::string test1Answer = "_fun (x) x + 1";
    CHECK(test1->to_string() == test1Answer);
    CHECK(test1->call(NEW(NumVal)(15))->equals(NEW(NumVal)(16)));

    CHECK_THROWS_WITH(test1->is_true(), "Calling is_true on a FunVal");
    // CHECK(test1->to_expr()->equals(NEW(FunExpr)("x", add1)));
    CHECK_THROWS_WITH(test1->add_to(test2), "Trying to add a fun Val");
    CHECK_THROWS_WITH(test1->mult_to(test2), "Trying to mult a funVal");
    CHECK(!test1->equals(NEW(NumVal)(1)));
}

TEST_CASE("Call Expression") {
    //CallExpr(PTR(Expr)to_be_called, PTR(Expr)actual_arg)
    PTR(FunExpr) testFunExpr1 = NEW(FunExpr)("x", NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)));
    PTR(FunExpr) testFunExpr2 = NEW(FunExpr)("x", NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)));

    PTR(CallExpr) call1 = NEW(CallExpr)(testFunExpr1, NEW(NumExpr)(2)); // f(2) x + 1
    PTR(CallExpr) call2 = NEW(CallExpr)(testFunExpr2, NEW(NumExpr)(2)); // f(2) x + 1
    CHECK(call1->interp(Env::empty)->equals(NEW(NumVal)(3)));
    CHECK(!call1->interp(Env::empty)->equals(NEW(NumVal)(4)));
    CHECK(call1->equals(call2));
    CHECK(!call1->equals(NEW(CallExpr)(NEW(AddExpr)(NEW(NumExpr)(1), NEW(VarExpr)("x")), NEW(NumExpr)(1))));
    CHECK(!call1->equals(NEW(NumExpr)(1)));
    PTR(FunVal) callOnly = NEW(FunVal)("x", NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)), Env::empty);
    CHECK(callOnly->call(NEW(NumVal)(2))->equals(NEW(NumVal)(3))); // only calling a call expression

    // f(x) x + 1 == f(x) y + 1
//    Question _fun (x) x + 1
//    Answer _fun (y) y + 1
//    subbed _fun (y) y + 1
// or this
//  subbed _fun (x) y + 1
    PTR(CallExpr) subCall1Question = NEW(CallExpr)(NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)), NEW(VarExpr)("x"));
    PTR(CallExpr) subCall1Answer = NEW(CallExpr)(NEW(AddExpr)(NEW(VarExpr)("y"), NEW(NumExpr)(1)), NEW(VarExpr)("y"));
//    CHECK(subCall1Question->subst("x", NEW(VarExpr)("y"))->equals(subCall1Answer));

    //printing a call expression
    // (_fun (x) x + 1) (10)

}

TEST_CASE("Parse functions") {
    std::string test1String = "_fun (x) x + 1";
    PTR(FunExpr) test1Expression = NEW(FunExpr)("x", NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)));
    CHECK(Parser::parse_string(test1String)->equals(test1Expression));
    std::string callExpressionsString = "f(2)";

//    _let f = _fun (x) x*x
//    _in  f(2)
    PTR(FunExpr) test2Fun = NEW(FunExpr)("x", NEW(MultExpr)(NEW(VarExpr)("x"), NEW(VarExpr)("x")));
    PTR(LetExpr) test2LetExpression = NEW(LetExpr)(NEW(VarExpr)("f"),
                                                   test2Fun,
                                                   NEW(CallExpr)(test2Fun, NEW(NumExpr)(2)));
    CHECK(test2LetExpression->interp(Env::empty)->equals(NEW(NumVal)(4)));

    // _let f = (_fun (x) x + 1) _in f(10)
    std::string mattsLet2 = "_let f = (_fun (x) x + 1) _in f(10)";
    PTR(LetExpr) nestedLetFunCall = NEW(LetExpr)(NEW(VarExpr)("f"),
                                                 NEW(FunExpr)("x", NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1))),
                                                 NEW(CallExpr)(NEW(VarExpr)("f"), NEW(NumExpr)(10)));

    CHECK(nestedLetFunCall->interp(Env::empty)->equals(NEW(NumVal)(11)));
    CHECK(Parser::parse_string(mattsLet2)->equals(nestedLetFunCall));

    // Matt's big test
    auto bigTest = Parser::parse_string("_let factrl = _fun (factrl)\n"
                                        "                _fun (x)\n"
                                        "                  _if x == 1\n"
                                        "                  _then 1\n"
                                        "                  _else x * factrl(factrl)(x + -1)\n"
                                        "_in  factrl(factrl)(10)");

    //  std::cout << bigTest->to_string() << std::endl;
    CHECK(bigTest->interp(Env::empty)->equals(NEW(NumVal)(3628800)));

}

TEST_CASE("Parse Functions CAlls") {

    std::string funCall1 = "f(10)";
    CHECK(Parser::parse_string(funCall1)->equals(NEW(CallExpr)(NEW(VarExpr)("f"), NEW(NumExpr)(10))));
}


TEST_CASE("Pretty print Fun and Calls") {
    std::string test1String = "_fun (x)"
                              "\n  x + 1";
    PTR(FunExpr) test1Expression = NEW(FunExpr)("x", NEW(AddExpr)(NEW(VarExpr)("x"), NEW(NumExpr)(1)));
    CHECK(test1Expression->to_string_pretty() == test1String);

    std::string prettyCall1 = "f (10)";
    PTR(CallExpr) test1Call = NEW(CallExpr)(NEW(VarExpr)("f"), NEW(NumExpr)(10));
    CHECK(test1Call->to_string_pretty() == prettyCall1);

    std::string mattsBigTestString = "_let factrl = _fun (factrl)\n"
                                     "                _fun (x)\n"
                                     "                  _if x == 1\n"
                                     "                  _then 1\n"
                                     "                  _else x * factrl(factrl)(x + -1)\n"
                                     "_in  factrl(factrl)(10)";
    auto mattsBigTestExpression = Parser::parse_string(mattsBigTestString);

}

TEST_CASE("lookup") {
    PTR(EmptyEnv) Env1 = NEW(EmptyEnv)();
    CHECK_THROWS(Env1->lookup("X"));
}

TEST_CASE("ExtendedEnv Equals") {
    PTR(ExtendedEnv) ext1 = NEW(ExtendedEnv)("test", NEW(NumVal)(1), Env::empty);
    PTR(ExtendedEnv) ext2 = NEW(ExtendedEnv)("test", NEW(NumVal)(1), Env::empty);
    CHECK(ext1->equals(ext2));
}


TEST_CASE ("step_interp") {
    // matt's step_ok test
    CHECK(step_ok(NEW(NumExpr)(0), Env::empty, Cont::done,
                  Step::continue_mode, NEW(NumVal)(0), NULL, Cont::done));

    CHECK(step_ok(NEW(BoolExpr)(true), Env::empty, Cont::done,
                  Step::continue_mode, NEW(BoolVal)(true), NULL, Cont::done));


    CHECK(step_ok(NEW(EqualExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)), Env::empty, Cont::done,
                  Step::interp_mode, NULL, NEW(NumExpr)(1),
                  NEW(RightThenCompCont)(NEW(NumExpr)(2), Env::empty, Cont::done)));

    auto testMult = Parser::parse_string("3*4");
    CHECK(Step::interp_by_steps(testMult)->equals(NEW(NumVal)(12)));

    // let
    CHECK(Step::interp_by_steps(Parser::parse_string("1"))->equals(NEW(NumVal)(1)));
    auto test1 = Parser::parse_string("_fun (x) x");
    auto letTest1 = Parser::parse_string("_let x = 6 _in x + 5"); // 11
    CHECK(Step::interp_by_steps(Parser::parse_string("_let x = 6 _in x + 5"))->equals(letTest1->interp(Env::empty)));

    // ==
    CHECK(Step::interp_by_steps(Parser::parse_string("1==1"))->equals(NEW(BoolVal)(true)));


    // fun
    CHECK(Step::interp_by_steps(Parser::parse_string("_fun (x) x"))->equals(test1->interp(Env::empty)));

    CHECK(Step::interp_by_steps(Parser::parse_string("_fun (x) x"))->equals(
            NEW(FunVal)("x", NEW(VarExpr)("x"), Env::empty)));

    // call
    auto callTest1 = Parser::parse_string("_let f = _fun(x) x+ 1 _in f(3)"); // 4
    CHECK(Step::interp_by_steps(Parser::parse_string("_let f = _fun(x) x+ 1 _in f(3)"))->equals(
            callTest1->interp(Env::empty)));


    // if stuff TODO
    auto ifTest1True = Parser::parse_string("_if true _then 5 _else 10 ");
    auto ifTest1False = Parser::parse_string("_if false _then 5 _else 10 ");

    PTR(NumVal) numValThrows = NEW(NumVal)(1);
    CHECK_THROWS_WITH(numValThrows->call_step(NEW(NumVal)(1), Cont::done), "Calling call_step on NumVal");

    PTR(BoolVal) boolValThrows = NEW(BoolVal)(true);
    CHECK_THROWS_WITH(boolValThrows->call_step(NEW(NumVal)(1), Cont::done), "Calling call_step on BoolVal");


    // Matts's massive recursion
    std::cout << "About to start massive recursion, might take a second" << std::endl;
    // commented out becuase they take a long time to run
//    CHECK(Step::interp_by_steps(Parser::parse_string("_let countdown = _fun(countdown)\n"
//                                                     "                   _fun(n)\n"
//                                                     "                     _if n == 0\n"
//                                                     "                     _then 0\n"
//                                                     "                     _else countdown(countdown)(n + -1)\n"
//                                                     "_in countdown(countdown)(1000000)") )->equals(NEW(NumVal) (0)) );

    CHECK(Step::interp_by_steps(Parser::parse_string("_let countdown = _fun(countdown)\n"
                                                     "                   _fun(n)\n"
                                                     "                     _if n == 5\n"
                                                     "                     _then 5\n"
                                                     "                     _else countdown(countdown)(n + -1)\n"
                                                     "_in countdown(countdown)(1000000)"))->equals(NEW(NumVal)(5)));


}

TEST_CASE("Cont") { // check if these test are correct
//    static bool step_ok(PTR(Expr) expr, PTR(Env) env, PTR(Cont) cont,
//                        Step::mode_t expected_mode,
//                        PTR(Val) expected_val, /* NULL if we expect interp_mode */
//                        PTR(Expr) expected_expr, /* NULL if we expect continue_mode */
//                        PTR(Cont) expected_cont)


    PTR(ContDone) contDone = NEW(ContDone)();
    CHECK_THROWS_WITH(contDone->step_continue(), "calling step cont on Cont::done");

    // 1 + 2                            expr                           env
    CHECK(step_ok(NEW(AddExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)), Env::empty, Cont::done,
                  Step::interp_mode, NULL, NEW(NumExpr)(1),
                  NEW(RightThenAddCont)(NEW(NumExpr)(2), Env::empty, Cont::done)));



    // 1 * 2
    CHECK(step_ok(NEW(MultExpr)(NEW(NumExpr)(1), NEW(NumExpr)(2)), Env::empty, Cont::done,
                  Step::interp_mode, NULL, NEW(NumExpr)(1),
                  NEW(RightThenMultCont)(NEW(NumExpr)(2), Env::empty, Cont::done)));

}

TEST_CASE("Cont Equals") { // I realize this is unnecessary
    PTR(CallCont) callCont1 = NEW(CallCont)(NEW(NumVal)(1), Cont::done);
    PTR(CallCont) callCont2 = NEW(CallCont)(NEW(NumVal)(1), Cont::done);
    CHECK(callCont1->equals(callCont2));

    PTR(ArgThenCallCont) ArgCallCont1 = NEW(ArgThenCallCont)(NEW(NumExpr)(1), Env::empty, Cont::done);
    PTR(ArgThenCallCont) ArgCallCont2 = NEW(ArgThenCallCont)(NEW(NumExpr)(1), Env::empty, Cont::done);
    CHECK(ArgCallCont1->equals(ArgCallCont2));

    PTR(CompCont) CompCont1 = NEW(CompCont)(NEW(NumVal)(1), Cont::done);
    PTR(CompCont) CompCont2 = NEW(CompCont)(NEW(NumVal)(1), Cont::done);
    CHECK(CompCont1->equals(CompCont2));


    PTR(IFBranchCont) IfCont1 = NEW(IFBranchCont)(NEW(NumExpr)(1), NEW(NumExpr)(2), Env::empty, Cont::done);
    PTR(IFBranchCont) IfCont2 = NEW(IFBranchCont)(NEW(NumExpr)(1), NEW(NumExpr)(2), Env::empty, Cont::done);
    CHECK(IfCont1->equals(IfCont2));

    PTR(AddCont) addCont1 = NEW(AddCont)(NEW(NumVal)(1), Cont::done);
    PTR(AddCont) addCont2 = NEW(AddCont)(NEW(NumVal)(1), Cont::done);
    CHECK(addCont1->equals(addCont2));

    PTR(LetBodyCont) letCont1 = NEW(LetBodyCont)("x", NEW(NumExpr)(1), Env::empty, Cont::done);
    PTR(LetBodyCont) letCont2 = NEW(LetBodyCont)("x", NEW(NumExpr)(1), Env::empty, Cont::done);
    CHECK(letCont1->equals(letCont2));
    // !
    CHECK(!letCont1->equals(IfCont1));
    CHECK(!IfCont1->equals(letCont1));
    CHECK(!addCont1->equals(CompCont1));
    CHECK(!CompCont1->equals(addCont1));
    CHECK(!ArgCallCont1->equals(callCont1));
    CHECK(!callCont1->equals(ArgCallCont1));
}


TEST_CASE("Integer OverFlow") {
//    CHECK_THROWS(NEW(NumVal)(1000000000000000));
//    CHECK_THROWS(NEW(NumVal)(2147483648));
}

// Static function that Matt gave us for testing
static bool step_ok(PTR(Expr) expr, PTR(Env) env, PTR(Cont) cont,
                    Step::mode_t expected_mode,
                    PTR(Val) expected_val, /* NULL if we expect interp_mode */
                    PTR(Expr) expected_expr, /* NULL if we expect continue_mode */
                    PTR(Cont) expected_cont) {
    Step::expr = expr;
    Step::env = env;
    Step::cont = cont;
    Step::val = NULL;

    expr->step_interp();

    bool ok;
    if (expected_mode == Step::continue_mode)
        ok = (Step::val != NULL
              && Step::val->equals(expected_val)
              && Step::cont->equals(expected_cont));
    else
        ok = (Step::expr->equals(expected_expr)
              && Step::env->equals(env)
              && Step::cont->equals(expected_cont));

    Step::val = NULL;
    Step::env = NULL;
    Step::cont = NULL;

    return ok;
}