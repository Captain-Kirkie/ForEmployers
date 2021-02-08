package Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import practice.company.DNSHeader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DNSHeaderTest {
    byte[] byteArrayFull;
    byte[] everythingAfterHeader;
    DNSHeader testHeaderQuery1;


    @BeforeEach
    void init() throws IOException {
        // https://www2.cs.duke.edu/courses/fall16/compsci356/DNS/DNS-primer.pdf
        // example on pg 6
        byteArrayFull = new byte[]{
                //      |  header | |  Flags | QDCount  |
                (byte) 0xdb, 0x42, 0x01, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 //header
                , 0x03, 0x77, 0x77, 0x77, 0x0c, 0x6e, 0x6f, 0x74, 0x68, 0x65, 0x61, 0x73, 0x74, 0x65, 0x72, 0x6e,
                0x03, 0x65, 0x64, 0x75, 0x00, 0x00, 0x01, 0x00, 0x01
        };



        // test bitArrInput Stream
        ByteArrayInputStream targetStream = new ByteArrayInputStream(byteArrayFull);
        testHeaderQuery1 = DNSHeader.decodeHeader(targetStream); //This is currently null
    }

    @AfterEach
    void reset() {
        byteArrayFull = null;
        everythingAfterHeader = null;
    }

    @org.junit.jupiter.api.Test
    void decodeHeader() throws IOException {
        //ID
        byte[] IDAnswer = new byte[]{(byte) 0xdb, 0x42};
        assert (Arrays.equals(testHeaderQuery1.getID(), IDAnswer));

        // Flags
        byte[] FlagAnswer = new byte[]{(byte) 0x01, 0x00};
        assert (Arrays.equals(testHeaderQuery1.getFlags(), FlagAnswer));

        //QR
        byte QRAnswer = 0x00;
        assertEquals(testHeaderQuery1.getQR(), QRAnswer);

        //OPCode
        byte OPCODEAnswer = 0x00;
        assertEquals(testHeaderQuery1.getOPCODE(), OPCODEAnswer);

        //TC
        byte TCAnswer = 0x00;
        assertEquals(testHeaderQuery1.getTC(), TCAnswer);

        //RD
        byte RDAnswer = 0x01;
        assertEquals(testHeaderQuery1.getRD(), RDAnswer);

        //Z
        byte ZAnswer = 0x00;
        assertEquals(testHeaderQuery1.getZ(), ZAnswer);

        //RCode
        byte rCodeAnswer = 0x00;
        assertEquals(testHeaderQuery1.getRCODE(), rCodeAnswer);

        //QDCount
        byte[] QDCountAnswer = new byte[]{0x00, 0x01};
        assert (Arrays.equals(testHeaderQuery1.getQDCount(), QDCountAnswer));

        //ANCount
        byte[] ANCountAnswer = new byte[]{0x00, 0x00};
        assert (Arrays.equals(testHeaderQuery1.getANCount(), ANCountAnswer));

        //ANCount
        byte[] NSCountAnswer = new byte[]{0x00, 0x00};
        assert (Arrays.equals(testHeaderQuery1.getNSCount(), NSCountAnswer));

        //ARCount
        byte[] ARCountAnswer = new byte[]{0x00, 0x00};
        assert (Arrays.equals(testHeaderQuery1.getARCount(), ARCountAnswer));


    }


    @org.junit.jupiter.api.Test
    void buildResponseHeader() {
    }

    @org.junit.jupiter.api.Test
    void writeBytes() {
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        System.out.println(testHeaderQuery1.toString());
    }

    @org.junit.jupiter.api.Test
    void printByteArrayHex() {
    }

    @org.junit.jupiter.api.Test
    void printByteToHex() {
    }
}