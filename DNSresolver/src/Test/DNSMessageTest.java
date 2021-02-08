package Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practice.company.DNSMessage;
import practice.company.DNSServer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DNSMessageTest {

    byte[] byteArray, everythingAfterHeader, responseArray, arrayQueryAndAnswer, responseArrayMinusHeader;
    DNSMessage testMessage;
    ByteArrayInputStream targetStream, fullsStreamQueryAnswer;
//    String testDigDataDump = "B9F801200001000000000001076578616D706C6503636F6D00000100010000291000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n";
//    byte [] testDigDataDumpArr = testDigDataDump.getBytes();


    //TODO:
    byte[]CompleteMessage;

    @BeforeEach
    void init() throws IOException {
        // https://www2.cs.duke.edu/courses/fall16/compsci356/DNS/DNS-primer.pdf
        // example on pg 6
        byteArray = new byte[]{
                //      |  header | |  Flags | QDCount  |
                (byte) 0xdb, 0x42, 0x01, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 //header
                , 0x03, 0x77, 0x77, 0x77, 0x0c , 0x6e, 0x6f, 0x72 , 0x74, 0x68, 0x65, 0x61, 0x73, 0x74, 0x65, 0x72, 0x6e,
                0x03, 0x65, 0x64, 0x75, 0x00, 0x00, 0x01, 0x00, 0x01
        };

        everythingAfterHeader = new byte[]{0x03, 0x77, 0x77, 0x77, 0x0c, 0x6e, 0x6f, 0x72, 0x74, 0x68, 0x65, 0x61, 0x73, 0x74, 0x65, 0x72, 0x6e,
                0x03, 0x65, 0x64, 0x75, 0x00, 0x00, 0x01, 0x00, 0x01};
        // test bitArrInput Stream

        // Page 7
        //https://www2.cs.duke.edu/courses/fall16/compsci356/DNS/DNS-primer.pdf
        responseArray = new byte[]{
                /*header*/ (byte) 0xdb, 0x42, (byte) 0x81, (byte) 0x80, 0x00, 0x01, 0x00, 0x01,0x00, 0x00, 0x00, 0x00, /*3*/ 0x03,/*www*/ 0x77, 0x77, 0x77,
                /*12*/ 0x0c, /*northeastern.com*/(byte) 0x6e, 0x6f, 0x72,0x74, 0x68, 0x65, 0x61, 0x73, 0x74, 0x65, 0x72 , 0x6e, /*3*/0x03,/*edu*/ 0x65, 0x64,
                0x75,/*end*/ 0x00,0x00, 0x01, 0x00, 0x01, (byte)/*Compression*/ 0xc0, 0x0c, 0x00, 0x01,  0x00, 0x01, 0x00, 0x00, 0x02, 0x58,
                0x00, 0x04, (byte) 0x9b, 0x21, 0x11, 0x44 };                  //offset should be 12

        responseArrayMinusHeader = new byte[]{
                (byte) /*3*/ 0x03,/*www*/ 0x77, 0x77, 0x77,
                /*12*/ 0x0c, /*northeastern.com*/(byte) 0x6e, 0x6f, 0x72,0x74, 0x68, 0x65, 0x61, 0x73, 0x74, 0x65, 0x72 , 0x6e, /*3*/0x03,/*edu*/ 0x65, 0x64,
                0x75,/*end*/ 0x00,0x00, 0x01, 0x00, 0x01, (byte)/*Compression*/ 0xc0, 0x0c, 0x00, 0x01,  0x00, 0x01, 0x00, 0x00, 0x02, 0x58,
                0x00, 0x04, (byte) 0x9b, 0x21, 0x11, 0x44 };

        arrayQueryAndAnswer = new byte[]{
                //      |  header | |  Flags | QDCount  |
                (byte) 0xdb, 0x42, 0x01, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 //header
                , 0x03, 0x77, 0x77, 0x77, 0x0c , 0x6e, 0x6f, 0x72 , 0x74, 0x68, 0x65, 0x61, 0x73, 0x74, 0x65, 0x72, 0x6e,
                0x03, 0x65, 0x64, 0x75, 0x00, 0x00, 0x01, 0x00, 0x01, (byte) 0xdb, 0x42, (byte) 0x81, (byte) 0x80, 0x00, 0x01, 0x00, 0x01,0x00, 0x00, 0x00, 0x00, 0x03, 0x77, 0x77, 0x77,
                0x0c, (byte) 0x6e, 0x6f, 0x72,0x74, 0x68, 0x65, 0x61, 0x73, 0x74, 0x65, 0x72 , 0x6e, 0x03, 0x65, 0x64,
                0x75, 0x00,0x00, 0x01, 0x00, 0x01, (byte) 0xc0, 0x0c, 0x00, 0x01,  0x00, 0x01, 0x00, 0x00, 0x02, 0x58,
                0x00, 0x04, (byte) 0x9b, 0x21, 0x11, 0x44,
        };


        targetStream = new ByteArrayInputStream(everythingAfterHeader);
        fullsStreamQueryAnswer = new ByteArrayInputStream(arrayQueryAndAnswer);
    }

    @AfterEach
    void reset() {
        byteArray = null;
        everythingAfterHeader = null;
    }

    @Test
    void decodeMessage() throws IOException { //TODO: Figure out why this is failing
        testMessage =  DNSMessage.decodeMessage(byteArray);

//        String[] QNameAnswer = new String[]{"www", "northeastern", "edu"};
//        assert(Arrays.equals(QNameAnswer, testMessage.getQuestion().getQName()));

    }


    @Test
    void setTestDigDataDump() {
//        System.out.println(testDigDataDumpArr);
       // Helper.printByteArrayHex("Test DataDump ", testDigDataDumpArr);

    }

    @Test
    void readDomainNameAnswer() throws IOException { //TODO: Octect is starting at wrong spot
//        DNSMessage testDomainNameMessage = DNSMessage.decodeMessage(responseArrayMinusHeader);
//        ByteArrayInputStream domainNameInputStream = new ByteArrayInputStream(responseArrayMinusHeader);
//        String[]testDomainName = testDomainNameMessage.readDomainName(domainNameInputStream);
//        System.out.println("This is without compression ");
//        for(String s: testDomainName){
//            System.out.print(s+ " ");
//        }
//
//        //Checking compression
//        DNSMessage testDomainNameMessageCompression = DNSMessage.decodeMessage(responseArray);
//        ByteArrayInputStream domainNameInputStreamCompression = new ByteArrayInputStream(responseArray);
//        String[] testDomainCompression = testDomainNameMessageCompression.readDomainName(12); // assuming 12 io is passed
//        System.out.println("This is passing 12 ");
//        for(String s: testDomainCompression){
//            System.out.print(s + " ");
//        }
//




    }

    @Test
    void testReadDomainName() {
    }

    @Test
    void buildResponse() {
    }

    @Test
    void toBytes() {
    }

    @Test
    void writeDomainName() {
    }

    @Test
    void octetsToString() throws IOException {
//        testMessage =  DNSMessage.decodeMessage(byteArray);
//        //TODO: TEST THIS
//        String testStringAnswer = "www.northeastern.edu"; //TODO: Do I need a period?
//        String testStringActual = testMessage.octetsToString(testMessage.getQuestion().getQName());
//        assertEquals(testStringActual, testStringAnswer);
    }

    @Test
    void testToString() {
    }
}