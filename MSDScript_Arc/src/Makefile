
INCS = CommandLine.h catch.h Expr.h Parser.h Val.h Env.h Cont.h Step.h
SRCS = main.cpp CommandLine.cpp Expr.cpp Test.cpp Parser.cpp Val.cpp Env.cpp Cont.cpp Step.cpp
OBJS = main.o CommandLine.o Expr.o Test.o Parser.o Val.o Env.o Cont.o Step.o


CXX = c++
CXXFLAGS = --std=c++14 -O2

target: $(OBJS)
	$(CXX) $(CXXFLAGS) -o msdscript $(OBJS)

.PHONY: test
test: msdscript
	./msdscript --test


main.o: main.cpp $(INCS)

CommandLine.o: CommandLine.cpp $(INCS)

Expr.o: Expr.cpp $(INCS)

Test.o: Test.cpp $(INCS)

Parser.o : Parser.cpp $(INCS)

Val.o : Val.cpp $(INCS)

Env.o : Env.cpp $(INCS)

Step.o : Step.cpp $(INCS)

Cont.o : Cont.cpp $(INCS)






OBJS2 = ../../randomTesting/RandomTestGenerator/exec.o  ../../randomTesting/RandomTestGenerator/run_exec.o
INCS2 = ../../randomTesting/RandomTestGenerator/exec.h ../../randomTesting/RandomTestGenerator/Expr.h

msdscript_test: $(OBJS2)
	$(CXX) $(CXXFLAGS) -o msdscript_test $(OBJS2)

run_exec.o: run_exec.cpp $(INCS2)

exec.o: exec.cpp $(INCS2)





