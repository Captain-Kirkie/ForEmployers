cmake_minimum_required(VERSION 3.17)
project(CryptoHW1)

set(CMAKE_CXX_STANDARD 14 )
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fsanitize=address")
set(CMAKE_LINKER_FLAGS "${CMAKE_LINKER_FLAGS} -fsanitize=address")

add_executable(CryptoHW1 blockCypher.cpp RC4.cpp RC4.h blockCipher.hpp main.cpp)