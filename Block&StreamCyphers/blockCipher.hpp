//
// Created by Kirk Hietpas on 3/3/21.
//

#ifndef CRYPTOHW1_BLOCKCIPHER_HPP
#define CRYPTOHW1_BLOCKCIPHER_HPP

#include <iostream>
#include <list>
#include <vector>
#include <iomanip>
#include <sstream>
#include <array>
#include <random>
#include <fstream>
#include "blockCipher.hpp"

std::array<uint8_t, 256> fisherYatesArrayShuffled(std::array<uint8_t, 256> arr);

void swap(uint8_t &a, uint8_t &b);

std::vector<std::array<uint8_t, 256> > buildEightShuffledTables(std::array<uint8_t, 256> firstTable);

std::array<uint8_t, 8> createKey(const std::string &password, std::array<uint8_t, 8> &key);

void printSize256Array(std::array<uint8_t, 256> &arr);

void printSize8Array(std::array<uint8_t, 8> &arr);

void CHARprintSize8Array(std::array<uint8_t, 8> &arr);

std::string encryptMessage(std::string message, std::array<uint8_t, 8> &key,
                           const std::vector<std::array<uint8_t, 256> > &vectorOfTables);

std::string decryptMessage(std::string message, std::array<uint8_t, 8> &key,
                           const std::vector<std::array<uint8_t, 256> > &vectorOfTables);

void populateBlock(std::array<uint8_t, 8> &block, std::string &message);

void XORBlock(std::array<uint8_t, 8> &block, std::array<uint8_t, 8> &key);

void printSize8ArrayHEX(std::array<uint8_t, 8> &arr);

int findIndex(std::array<uint8_t, 256> table, uint8_t c);

std::string convertBlockToString(std::array<uint8_t, 8> &block);

void swapWithCharsEncrypt(std::array<uint8_t, 8> &block, const std::vector<std::array<uint8_t, 256> > &vectorOfTables);

void swapWithCharsDecrypt(std::array<uint8_t, 8> &block, const std::vector<std::array<uint8_t, 256> > &vectorOfTables);

void shiftBlockLeft(std::array<uint8_t, 8> &block);

void shiftBlockRight(std::array<uint8_t, 8> &block);

void shiftArrayWrapAroundDecryptRight(std::array<uint8_t, 8> &block);

uint64_t convertBlockToUint64(std::array<uint8_t, 8> &block);

void uint64BackToBlock(std::array<uint8_t, 8> &block, uint64_t shifted);

std::vector<std::array<uint8_t, 256> > buildReverseTables(std::vector<std::array<uint8_t, 256> > &vectorOfTables);


#endif //CRYPTOHW1_BLOCKCIPHER_HPP
