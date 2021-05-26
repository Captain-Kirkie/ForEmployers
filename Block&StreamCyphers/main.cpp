//
// Created by Kirk Hietpas on 3/3/21.
//
#include "blockCipher.hpp"
#include "RC4.h"

// -fsanitize=address
int main() {
    // std::string test = "es about the teams heading up the mountain. “Dead Weight,” published more than a decade ago, is one of my favorites. In many ways, this story—in which Eric Hansen spends five days as a porter in Nepal—follows the classic theme of the confident white westerner testing his physical limits by performing manual labor in another country, only to return home run-down and demoralized. But Hansen writes in a humorous, awe-inspired way that gives the reader a real insight into the lives of Sherpas and a true appreciation for the work they do. If you want a detailed account of the life of a Sherpa during Everest season, this is a good place to start.\n"
    "Plaintext Size: 808";
    // std::string test = "es about";
//std::string test = "es ab";
    // std::string test = "hello123hello123";
    std::string test = "hello123";
    // std::string test = "Outside editors love Everest. Every spring, as the climbing season begins, our office buzzes with excitement and our homepage is plastered with dispatches about the teams heading up the mountain. “Dead Weight,” published more than a decade ago, is one of my favorites. In many ways, this story—in which Eric Hansen spends five days as a porter in Nepal—follows the classic theme of the confident white westerner testing his physical limits by performing manual labor in another country, only to return home run-down and demoralized. But Hansen writes in a humorous, awe-inspired way that gives the reader a real insight into the lives of Sherpas and a true appreciation for the work they do. If you want a detailed account of the life of a Sherpa during Everest season, this is a good place to start.";
    // std::string test = "Outside editors love Everest.Every spring, as the climbing season begins, our office buzzes with excitement and our homepage is plastered with dispatches about the teams heading up the mountain. “Dead Weight,” published more than a decade ago, is one ";
    //  std::string test = "\n"
    "LOREM IPSUM\n"
    "IMAGES\n"
    "PLUGINS\n"
    "GENERATORS\n"
    "ENGLISH\n"
    "Lorem Ipsum Generator\n"
    "Generate Lorem Ipsum placeholder text. Select the number of characters, words, sentences or paragraphs, and hit generate!\n"
    "\n"
    "\n"
    "GENERATED LOREM IPSUM\n"
    "5\n"
    " \n"
    "PARAGRAPHS\n"
    " \n"
    "COPY \n"
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Egestas diam in arcu cursus euismod quis viverra. Id aliquet risus feugiat in ante. Blandit turpis cursus in hac habitasse platea. Porttitor lacus luctus accumsan tortor posuere ac ut consequat semper. At urna condimentum mattis pellentesque id nibh. Egestas fringilla phasellus faucibus scelerisque eleifend donec pretium vulputate. In iaculis nunc sed augue lacus viverra vitae congue. Sed euismod nisi porta lorem mollis aliquam ut porttitor leo. Enim lobortis scelerisque fermentum dui faucibus in ornare quam viverra. Fringilla urna porttitor rhoncus dolor purus non enim. Orci dapibus ultrices in iaculis nunc sed. Aenean pharetra magna ac placerat. Fermentum iaculis eu non diam phasellus vestibulum lorem sed risus. Sed cras ornare arcu dui vivamus arcu felis. Tellus orci ac auctor augue mauris. Pulvinar pellentesque habitant morbi tristique.\n"
    "\n"
    "Interdum velit euismod in pellentesque massa placerat duis ultricies lacus. Lorem ipsum dolor sit amet consectetur adipiscing elit duis. Malesuada nunc vel risus commodo viverra maecenas accumsan lacus. Nunc mi ipsum faucibus vitae aliquet. Bibendum est ultricies integer quis auctor elit sed. Nisl condimentum id venenatis a condimentum vitae. Consectetur purus ut faucibus pulvinar elementum integer enim neque. Elementum curabitur vitae nunc sed velit dignissim. Semper risus in hendrerit gravida rutrum quisque non. Pulvinar etiam non quam lacus. Sed lectus vestibulum mattis ullamcorper velit sed ullamcorper. Suspendisse faucibus interdum posuere lorem ipsum. Aliquam malesuada bibendum arcu vitae elementum curabitur.\n"
    "\n"
    "Pharetra sit amet aliquam id diam. Commodo viverra maecenas accumsan lacus vel facilisis volutpat est. Varius vel pharetra vel turpis nunc eget. Quam viverra orci sagittis eu volutpat. Sed cras ornare arcu dui. Leo a diam sollicitudin tempor id. Ut faucibus pulvinar elementum integer enim neque volutpat ac. Ipsum dolor sit amet consectetur adipiscing elit. Interdum varius sit amet mattis vulputate enim nulla aliquet porttitor. Nunc sed augue lacus viverra vitae congue eu consequat ac. Iaculis urna id volutpat lacus laoreet. Vel facilisis volutpat est velit egestas dui id ornare. Nulla facilisi nullam vehicula ipsum a arcu cursus vitae. Quam viverra orci sagittis eu. Pulvinar etiam non quam lacus.\n"
    "\n"
    "Fringilla urna porttitor rhoncus dolor purus. Non nisi est sit amet facilisis magna etiam. Ornare massa eget egestas purus. Mattis nunc sed blandit libero volutpat sed cras ornare. Leo duis ut diam quam nulla porttitor. Viverra ipsum nunc aliquet bibendum enim facilisis gravida neque. Et netus et malesuada fames ac turpis. Nibh tortor id aliquet lectus proin nibh nisl condimentum id. Nisl tincidunt eget nullam non nisi est. Placerat in egestas erat imperdiet sed euismod. Enim lobortis scelerisque fermentum dui faucibus in ornare quam. Tincidunt tortor aliquam nulla facilisi cras. Sed enim ut sem viverra aliquet eget sit amet tellus. Nibh sit amet commodo nulla. Cursus metus aliquam eleifend mi in. Commodo viverra maecenas accumsan lacus vel facilisis volutpat est. Duis at tellus at urna condimentum mattis. Donec adipiscing tristique risus nec feugiat. Velit ut tortor pretium viverra suspendisse potenti nullam ac tortor. Quis varius quam quisque id diam vel.\n"
    "\n"
    "Mauris pharetra et ultrices neque ornare. Pulvinar neque laoreet suspendisse interdum. Egestas erat imperdiet sed euismod nisi porta lorem mollis aliquam. Sagittis aliquam malesuada bibendum arcu vitae elementum curabitur vitae. Quis enim lobortis scelerisque fermentum dui faucibus in. Ultricies mi eget mauris pharetra et ultrices neque ornare. Justo donec enim diam vulputate ut pharetra sit. Etiam dignissim diam quis enim lobortis scelerisque fermentum dui faucibus. Nisi porta lorem mollis aliquam ut porttitor. Urna molestie at elementum eu facilisis sed. Sagittis vitae et leo duis ut diam quam nulla. Odio tempor orci dapibus ultrices. Ultrices gravida dictum fusce ut placerat orci nulla pellentesque. In metus vulputate eu scelerisque felis imperdiet proin fermentum. Tortor at risus viverra adipiscing at in tellus integer. Lobortis feugiat vivamus at augue eget arcu dictum varius duis. Amet consectetur adipiscing elit duis tristique. In fermentum posuere urna nec tincidunt. Praesent elementum facilisis leo vel fringilla. Nam libero justo laoreet sit amet.\n"
    "\n"
    "\n"
    "\n"
    "\n"
    "© 2015 — 2020\n"
    "PRIVACY POLICY\n"
    "SITEMAP\n"
    "IMAGES\n"
    "PLUGINS\n"
    "GENERATORS\n"
    "SHARE THE LOREM\n"
    "WA\n"
    "SAI\n"
    "Close X";

    int errorCount = 0;
    std::string password = "supercalifragilisticexpialidocious";
    //  std::string plainText = "HelloThisIsKirkWhyAmISoHandsome?supercalifragilisticexpiazcvzvxlidocious";
    std::string encryptedMessage;
    std::string decryptedMessage;


    //  create a key array
    std::array<uint8_t, 8> key{};
    createKey(password, key);


    // build first table v
    std::array<uint8_t, 256> firstTable{}; // create a table of uint8 values
    for (int i = 0; i < 256; i++) { // creat a first table
        firstTable[i] = i;
    }


    //  fill vector with shuffled tables
//
//    buildEightShuffledTables(firstTable, vectorOfTables); // fill vector with sub tables
    std::vector<std::array<uint8_t, 256> > vectorOfTables = buildEightShuffledTables(
            firstTable); // create a vector of tables

    //build reverse tables
    std::vector<std::array<uint8_t, 256> > reverseTables = buildReverseTables(
            vectorOfTables); // create a vector of tables


    //TEST 100 times
    for (int i = 0; i < 100; i++) {

        //encyrpt/decrypt
        encryptedMessage = encryptMessage(test, key, vectorOfTables);
        // std::cout << "This is the encrypted message size " << encryptedMessage.size() << std::endl;
       // encryptedMessage[2] = encryptedMessage[2] >> 1; // testing changing one byte
        decryptedMessage = decryptMessage(encryptedMessage, key, vectorOfTables);


        if (decryptedMessage != test)
            errorCount++;
        //assert(decryptedMessage == plainText);
    }
    std::cerr << "Error count: " << errorCount << std::endl;
    if (decryptedMessage != test) {
    std::cout << "Plaintext: " << test << std::endl;
    std::cout << "Decrypted: " << decryptedMessage << std::endl;
    std::cout << "Plaintext Size: " << test.size() << std::endl;
    std::cout << "Decrypted Size " << decryptedMessage.size() << std::endl;
    }




    //-----------------------------------------------------------------------------RC4
    //create and populate RC4State
    std::array<uint8_t, 256> RC4State{}; // create initial state
    for (int i = 0; i < 255; ++i) {
        RC4State[i] = i;
    }

    std::string RC4key = "Password";
    std::string rc4Message = "Your Salary is $1000";
    std::string rc4MessageMalloryAttack = "youWillGetAJob -M";
    // std::string rc4Message = "\n"
    "LOREM IPSUM\n"
    "IMAGES\n"
    "PLUGINS\n"
    "GENERATORS\n"
    "ENGLISH\n"
    "Lorem Ipsum Generator\n"
    "Generate Lorem Ipsum placeholder text. Select the number of characters, words, sentences or paragraphs, and hit generate!\n"
    "\n"
    "\n"
    "GENERATED LOREM IPSUM\n"
    "5\n"
    " \n"
    "PARAGRAPHS\n"
    " \n"
    "COPY \n"
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Egestas diam in arcu cursus euismod quis viverra. Id aliquet risus feugiat in ante. Blandit turpis cursus in hac habitasse platea. Porttitor lacus luctus accumsan tortor posuere ac ut consequat semper. At urna condimentum mattis pellentesque id nibh. Egestas fringilla phasellus faucibus scelerisque eleifend donec pretium vulputate. In iaculis nunc sed augue lacus viverra vitae congue. Sed euismod nisi porta lorem mollis aliquam ut porttitor leo. Enim lobortis scelerisque fermentum dui faucibus in ornare quam viverra. Fringilla urna porttitor rhoncus dolor purus non enim. Orci dapibus ultrices in iaculis nunc sed. Aenean pharetra magna ac placerat. Fermentum iaculis eu non diam phasellus vestibulum lorem sed risus. Sed cras ornare arcu dui vivamus arcu felis. Tellus orci ac auctor augue mauris. Pulvinar pellentesque habitant morbi tristique.\n"
    "\n"
    "Interdum velit euismod in pellentesque massa placerat duis ultricies lacus. Lorem ipsum dolor sit amet consectetur adipiscing elit duis. Malesuada nunc vel risus commodo viverra maecenas accumsan lacus. Nunc mi ipsum faucibus vitae aliquet. Bibendum est ultricies integer quis auctor elit sed. Nisl condimentum id venenatis a condimentum vitae. Consectetur purus ut faucibus pulvinar elementum integer enim neque. Elementum curabitur vitae nunc sed velit dignissim. Semper risus in hendrerit gravida rutrum quisque non. Pulvinar etiam non quam lacus. Sed lectus vestibulum mattis ullamcorper velit sed ullamcorper. Suspendisse faucibus interdum posuere lorem ipsum. Aliquam malesuada bibendum arcu vitae elementum curabitur.\n"
    "\n"
    "Pharetra sit amet aliquam id diam. Commodo viverra maecenas accumsan lacus vel facilisis volutpat est. Varius vel pharetra vel turpis nunc eget. Quam viverra orci sagittis eu volutpat. Sed cras ornare arcu dui. Leo a diam sollicitudin tempor id. Ut faucibus pulvinar elementum integer enim neque volutpat ac. Ipsum dolor sit amet consectetur adipiscing elit. Interdum varius sit amet mattis vulputate enim nulla aliquet porttitor. Nunc sed augue lacus viverra vitae congue eu consequat ac. Iaculis urna id volutpat lacus laoreet. Vel facilisis volutpat est velit egestas dui id ornare. Nulla facilisi nullam vehicula ipsum a arcu cursus vitae. Quam viverra orci sagittis eu. Pulvinar etiam non quam lacus.\n"
    "\n"
    "Fringilla urna porttitor rhoncus dolor purus. Non nisi est sit amet facilisis magna etiam. Ornare massa eget egestas purus. Mattis nunc sed blandit libero volutpat sed cras ornare. Leo duis ut diam quam nulla porttitor. Viverra ipsum nunc aliquet bibendum enim facilisis gravida neque. Et netus et malesuada fames ac turpis. Nibh tortor id aliquet lectus proin nibh nisl condimentum id. Nisl tincidunt eget nullam non nisi est. Placerat in egestas erat imperdiet sed euismod. Enim lobortis scelerisque fermentum dui faucibus in ornare quam. Tincidunt tortor aliquam nulla facilisi cras. Sed enim ut sem viverra aliquet eget sit amet tellus. Nibh sit amet commodo nulla. Cursus metus aliquam eleifend mi in. Commodo viverra maecenas accumsan lacus vel facilisis volutpat est. Duis at tellus at urna condimentum mattis. Donec adipiscing tristique risus nec feugiat. Velit ut tortor pretium viverra suspendisse potenti nullam ac tortor. Quis varius quam quisque id diam vel.\n"
    "\n"
    "Mauris pharetra et ultrices neque ornare. Pulvinar neque laoreet suspendisse interdum. Egestas erat imperdiet sed euismod nisi porta lorem mollis aliquam. Sagittis aliquam malesuada bibendum arcu vitae elementum curabitur vitae. Quis enim lobortis scelerisque fermentum dui faucibus in. Ultricies mi eget mauris pharetra et ultrices neque ornare. Justo donec enim diam vulputate ut pharetra sit. Etiam dignissim diam quis enim lobortis scelerisque fermentum dui faucibus. Nisi porta lorem mollis aliquam ut porttitor. Urna molestie at elementum eu facilisis sed. Sagittis vitae et leo duis ut diam quam nulla. Odio tempor orci dapibus ultrices. Ultrices gravida dictum fusce ut placerat orci nulla pellentesque. In metus vulputate eu scelerisque felis imperdiet proin fermentum. Tortor at risus viverra adipiscing at in tellus integer. Lobortis feugiat vivamus at augue eget arcu dictum varius duis. Amet consectetur adipiscing elit duis tristique. In fermentum posuere urna nec tincidunt. Praesent elementum facilisis leo vel fringilla. Nam libero justo laoreet sit amet.\n"
    "\n"
    "\n"
    "\n"
    "\n"
    "© 2015 — 2020\n"
    "PRIVACY POLICY\n"
    "SITEMAP\n"
    "IMAGES\n"
    "PLUGINS\n"
    "GENERATORS\n"
    "SHARE THE LOREM\n"
    "WA\n"
    "SAI\n"
    "Close X";
    RC4 rc4First;
    std::string Test2 = "Modify part of a message using a bitflipping attack. For example, try sending the message Your salary is 1000 encrypted with RC4. ";
    std::string encyrptedMessageRC4 = rc4First.encryptMessageRC4(rc4Message, RC4State, RC4key);

    // Mallory bit flip
    for (int i = 0; i < encyrptedMessageRC4.size(); i++) {
        encyrptedMessageRC4[i] = encyrptedMessageRC4[i] ^ rc4Message[i]; // cancels out original messag, leave key
        encyrptedMessageRC4[i] = encyrptedMessageRC4[i] ^ rc4MessageMalloryAttack[i]; // put new message in
    }


    std::string decryptedMessageRC4 = rc4First.encryptMessageRC4(encyrptedMessageRC4, RC4State, RC4key);
    std::cout << std::endl;
    std::cout << "This is plaintext: " << rc4Message << std::endl;
    std::cout << "This is decrypted message: " << decryptedMessageRC4 << std::endl;


    return 0;
}
