cmake_minimum_required(VERSION 3.17)
project(RandomTestGenerator)

set(CMAKE_CXX_STANDARD 14)

add_executable(RandomTestGenerator run_exec.cpp exec.cpp exec.h)