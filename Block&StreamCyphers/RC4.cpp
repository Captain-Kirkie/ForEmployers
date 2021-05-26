
//
// Created by Kirk Hietpas on 3/3/21.
//

#include "RC4.h"
#include "blockCipher.hpp"



RC4::RC4() {
   // std::cout << "Constructed RC4" << std::endl;
}

std::string RC4::encryptMessageRC4(const std::string &message, std::array<uint8_t, 256> RC4State, std::string key) {
    int j = 0; // initial shuffle
    for (int i = 0; i < 256; i++) {
        j = (j + RC4State[i] + key[i % key.length()]) % 256;
        std::swap(RC4State[i], RC4State[j]);
    }

    std::string encryptedString;
    int i = 0;
    j = 0;
    for (char c : message) {
        i = (i+1) % 255;
        j += RC4State[i];
        j = j %255;
        std::swap(RC4State[i], RC4State[j]);
        uint8_t output = RC4State[(RC4State[i]+RC4State[j]) % 256];
        uint8_t encrypted = output ^c;
        encryptedString += encrypted;
    }
    return encryptedString;
}

