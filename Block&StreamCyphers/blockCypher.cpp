


#include "blockCipher.hpp"
#include "vector"


// create key
std::array<uint8_t, 8> createKey(const std::string &password, std::array<uint8_t, 8> &key) {
    for (int i = 0; i < password.size(); i++) {
        key[i % 8] = key[i % 8] ^ password[i];
    }
    return key;
}

void swap(uint8_t &a, uint8_t &b) {
    int tmp = a; // hold it
    a = b;
    b = tmp;
}

void printArray(uint8_t arr[], int size) {
    for (int i = 0; i < size; i++) {
        std::cout << arr[i] << " ";
    }
    std::cout << std::endl;
}

std::array<uint8_t, 256> fisherYatesArrayShuffled(std::array<uint8_t, 256> arr) { // pass by value
    // std::array<uint8_t, 256> returnTable{}; // create new table on the heap
    srand(clock()); // This may not be seeding it
    for (int i = 255; i > 0; i--) {
        int j = random() % (i + 1); // pick a random num between the size
        std::swap(arr[i], arr[j]); // pass reference and swap
    }
    return arr;
}


std::vector<std::array<uint8_t, 256> > buildReverseTables(std::vector<std::array<uint8_t, 256> > &vectorOfTables) {
    std::vector<std::array<uint8_t, 256>> vectorOfTablesReverse;
    for (auto & vectorOfTable : vectorOfTables) {
        std::array<uint8_t, 256> newTable{};
        for (int j = 0; j < newTable.size(); j++) {
            newTable[j] = findIndex(vectorOfTable, j);
        }
        vectorOfTablesReverse.push_back(newTable);
    }
    return vectorOfTablesReverse;
}

int findIndex(std::array<uint8_t, 256> table, uint8_t c) {
    for (int i = 0; i < 256; i++) {
        if (table[i] == c) {
            return i;
        }
    }
    throw std::runtime_error("not found in table");
}

std::vector<std::array<uint8_t, 256> > buildEightShuffledTables(std::array<uint8_t, 256> firstTable) {
    std::vector<std::array<uint8_t, 256>> vectorOfTables;
    //std::array<uint8_t, 256> newShuffle = fisherYatesArrayShuffled(firstTable); // shuffle first table
    for (int i = 0; i < 8; i++) {
        std::array<uint8_t, 256> arr = fisherYatesArrayShuffled(firstTable);
        vectorOfTables.push_back(arr);

        for (int j = 0; j < 256; j++) { // CHECKing to see that tables contain values from 0 - 255
            uint8_t *foo = std::find(std::begin(arr), std::end(arr), j);
            assert(foo != std::end(arr));
        }
        firstTable = arr;


    }
    return vectorOfTables;
}

/**
 * Encryption
 * @param message
 * @param key
 * @param vectorOfTables
 * @return
 */
std::string encryptMessage(std::string message, std::array<uint8_t, 8> &key,
                           const std::vector<std::array<uint8_t, 256>> &vectorOfTables) {
    // grab next 8 block of the message
    // https://stackoverflow.com/questions/15648116/bit-shifting-a-character-with-wrap-c

    std::string ret;
    while(!message.empty()){
        std::array<uint8_t, 8> block{0, 0, 0, 0, 0, 0, 0, 0};

        for (unsigned char &i : block) { // populate teh block
            // if its not the end of the message
            if(!message.empty()){
                i = message[0];
                message = message.erase(0, 1);
            }else
                break;
        }

        for (int i = 0; i < 16; i++) { // mess with block
            XORBlock(block, key); // // XOR the key with block 16 times
            swapWithCharsEncrypt(block, vectorOfTables);
            shiftBlockLeft(block);
        }

        for (uint8_t c : block) {
            ret += c;
        }
    }
    return ret;
}




/**
 * Decryption
 * @param bytes
 * @param message
 */


std::string decryptMessage(std::string message, std::array<uint8_t, 8> &key,
                           const std::vector<std::array<uint8_t, 256>> &vectorOfTables) {

    std::string ret;
    while(!message.empty()){
        std::array<uint8_t, 8> block{0, 0, 0, 0, 0, 0, 0, 0};

        for (unsigned char &i : block) { // populate teh block
            // if its not the end of the message
            if(!message.empty()){
                i = message[0];
                message = message.erase(0, 1);
            }else
                break;
        }

        for (int i = 0; i < 16; i++) { // mess with block
            shiftBlockRight(block);
            swapWithCharsDecrypt(block, vectorOfTables);
            XORBlock(block, key); // // XOR the key with block 16 times
        }

        // only can check for zero only in decrypt
        for (uint8_t c : block) { //This is only for decrypt
            if(c != 0)
                ret += c;
            else
                break;
        }
    }

    return ret;
}



void XORBlock(std::array<uint8_t, 8> &block, std::array<uint8_t, 8> &key) {
    for (int i = 0; i < block.size() && i < key.size(); i++) {
        block[i] = block[i] ^ key[i]; // xor the indices at each
    }
}

void swapWithCharsEncrypt(std::array<uint8_t, 8> &block, const std::vector<std::array<uint8_t, 256>> &vectorOfTables) {
    // for each byte in the state, substitute that byte using the appropriate substitution table
    // (byte 0 should use table 0, byte 1 should use table 1, etc)
    for (int i = 0; i < block.size(); i++) {
        int value = block[i]; // get the value of the block
        std::array<uint8_t, 256> table = vectorOfTables[i];
        // uint8_t newVal = vectorOfTables[i][value]; // get pointer to the array
        uint8_t newVal = table[value];
        block[i] = newVal; // set the value at block to the value at new val
    }
}

void swapWithCharsDecrypt(std::array<uint8_t, 8> &block, const std::vector<std::array<uint8_t, 256>> &vectorOfTables) {
    // for each byte in the state, substitute that byte using the appropriate substitution table
    // (byte 0 should use table 0, byte 1 should use table 1, etc)
    for (int table = 0; table < block.size(); table++) { // to get the tables
        // block table uses table table
        int value = block[table];
        for (int j = 0; j < vectorOfTables[table].size(); j++) { // search the whole table
            if (value == vectorOfTables.at(table).at(j)) {  // search for the value
                block[table] = (uint8_t) j;
            }
        }
    }
}


void shiftBlockLeft(std::array<uint8_t, 8> &block) {
    uint64_t converted = convertBlockToUint64(block); // convert block to uint 64
    uint64_t lastBitOnLeft = (converted >> 63); // grab left most tib
    uint64_t ShiftedOneLeft = (converted << 1);  // shift left by 1
    uint64_t combined = ShiftedOneLeft | lastBitOnLeft; // combine....
    uint64BackToBlock(block, combined);

    // https://stackoverflow.com/questions/15648116/bit-shifting-a-character-with-wrap-c
    //http://www.herongyang.com/Java/Bit-String-Left-Rotation-All-Bits-in-Byte-Array.html
}


void shiftBlockRight(std::array<uint8_t, 8> &block) {
    uint64_t converted = convertBlockToUint64(block);  // convert block to uint64
    uint64_t lastBitOnRight = (converted << 63); // grab the right most bit
    uint64_t shiftedOneRight = (converted >> 1);
    uint64_t combined = lastBitOnRight | shiftedOneRight;
    uint64BackToBlock(block, combined);
}


void printSize8Array(std::array<uint8_t, 8> &arr) {
    for (unsigned char i : arr) {
        std::cout << i << " "; // prints deref pointer
    }
    std::cout << std::endl;
}

void printSize8ArrayHEX(std::array<uint8_t, 8> &arr) {
    for (unsigned char i : arr) {
        std::cout << std::hex << i << " "; // prints deref pointer
    }
    std::cout << std::endl;
}

void CHARprintSize8Array(std::array<uint8_t, 8> &arr) {
    for (unsigned char i : arr) {
        std::cout << (int) i << " "; // prints deref pointer
    }
    std::cout << std::endl;
}

void printSize256Array(std::array<uint8_t, 256> &arr) {
    for (unsigned char i : arr) {
        std::cout << i << " "; // prints deref pointer
    }
    std::cout << std::endl;
}


void printVectorOfTables(std::vector<std::array<uint8_t, 256>> &vectorOfTables) {
    for (std::array<uint8_t, 256> array : vectorOfTables) {
        for (uint8_t integer : array) {
            std::cout << (int) integer << " ";
        }
        std::cout << std::endl;
        std::cout << std::endl;
    }
}

uint64_t convertBlockToUint64(std::array<uint8_t, 8> &block) {
    uint64_t convertedBlock = 0;
    for (uint8_t byte : block) {
        convertedBlock = (convertedBlock << 8) | byte;
    }
    return convertedBlock;
}

void uint64BackToBlock(std::array<uint8_t, 8> &block, uint64_t shifted) {
    for (int i = 0; i < 8; i++) {
        block[i] = uint8_t((shifted >> 8 * (7 - i) & 0xFF));
    }
}
