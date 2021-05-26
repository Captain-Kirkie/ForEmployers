//
// Created by Kirk Hietpas on 3/3/21.
//
#include <iostream>
#include <list>
#include <vector>
#include <iomanip>
#include <sstream>
#include <array>
#include <random>
#include <fstream>

#ifndef CRYPTOHW1_RC4_H
#define CRYPTOHW1_RC4_H
// takes secret key of any length

class RC4 {
public:
    RC4();
    std::string encryptMessageRC4(const std::string& message, std::array<uint8_t, 256> RC4State, std::string key);

};


#endif //CRYPTOHW1_RC4_H
