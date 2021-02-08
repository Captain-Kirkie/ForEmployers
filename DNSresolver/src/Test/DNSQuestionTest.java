package Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practice.company.DNSMessage;
import practice.company.DNSQuestion;

import java.io.ByteArrayInputStream;
import java.io.IOException;

class DNSQuestionTest {

    byte[] byteArray, everythingAfterHeader;
    DNSMessage testMessage;
    ByteArrayInputStream targetStream;

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
        targetStream = new ByteArrayInputStream(everythingAfterHeader);

    }

    @AfterEach
    void reset() {
        byteArray = null;
        everythingAfterHeader = null;
    }


    @Test
    void decodeQuestion() throws IOException {
       // DNSQuestion testQuestion = DNSQuestion.decodeQuestion(targetStream, testMessage);


    }

    @Test
    void writeBytes() {
    }

    @Test
    void testToString() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }
}