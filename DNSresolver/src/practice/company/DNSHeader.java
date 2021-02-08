package practice.company;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class DNSHeader {
    private byte[] ID, Flags, QDCount, ANCount, NSCount, ARCount;
    private byte QR, OPCODE, AA, TC, RD, RA, Z, RCODE;
    //qd count == question count
    // NS count == Authority records
    // AR count == resourse records


    /**
     * decodes the header of the DNSMessage setting appropriate member variables
     * @param input byte array input stream (hex dump)
     * @return  a new DNS header with appropriate members set
     * @throws IOException
     */
    public static DNSHeader decodeHeader(ByteArrayInputStream input) throws IOException {
        DNSHeader header = new DNSHeader();
        header.ID = input.readNBytes(2);
        header.Flags = input.readNBytes(2);
        header.parseFlags(); // parses flags and sets to appropriate member var
        header.QDCount = input.readNBytes(2);
        header.ANCount = input.readNBytes(2);
        header.NSCount = input.readNBytes(2);
        header.ARCount = input.readNBytes(2);
        return header;
    }


    /**
     *
     * @param request original query
     * @param response  response being created to send back to client
     * @return returns a new header to send back to the client.
     * With appropriate, Flags, QDcount, ANCount, record, and additional record count
     */

    public static DNSHeader buildResponseHeader(DNSMessage request, DNSMessage response) {
        DNSHeader responseHeader = new DNSHeader();
        responseHeader.ID = request.getHeader().getID();
        responseHeader.QR = (byte) 0b10000000; // Set QD to one
        responseHeader.OPCODE = request.getHeader().getOPCODE();
        responseHeader.AA = request.getHeader().getAA();
        responseHeader.TC = request.getHeader().getTC();
        responseHeader.RD = request.getHeader().getRD();
        responseHeader.RA = (byte) 0b10000000; //set RA to one
        responseHeader.Z = (byte) 0b00000000;
        responseHeader.RCODE = request.getHeader().getRCODE();

        short flagShort = 0;
        flagShort = (short) (flagShort | responseHeader.QR << 8);
        flagShort = (short) (flagShort | responseHeader.OPCODE << 8);
        flagShort = (short) (flagShort | responseHeader.AA << 8);
        flagShort = (short) (flagShort | responseHeader.TC << 8);
        flagShort = (short) (flagShort | responseHeader.RD << 8);
        //------------------------------------------------------------
        flagShort = (short) (flagShort | responseHeader.RA);
        flagShort = (short) (flagShort | responseHeader.Z);
        flagShort = (short) (flagShort | responseHeader.RCODE);

        byte[] responseFlags = new byte[2];

        responseFlags[0] = (byte) (flagShort & 0xff);
        responseFlags[1] = (byte) ((flagShort >> 8) & 0xff);

        responseHeader.Flags = responseFlags;

        //qdcount
        responseHeader.QDCount = request.getHeader().getQDCount();

        //Answer count
        short answerCount = (short) response.getAnswersArrayList().size();
        System.out.println("This is the size of the array list " + answerCount);

        responseHeader.ANCount = Helper.shortToTwoByteArray(answerCount);


        //record
        short NSCountNum = (short) response.getAuthorityRecords().size();
        responseHeader.NSCount = Helper.shortToTwoByteArray(NSCountNum);

        //Additional Records
        short ARCountNum = (short) response.getAdditionalRecords().size();
        responseHeader.ARCount = Helper.shortToTwoByteArray(ARCountNum);

        return responseHeader;

    }

    /**
     * writes appropriate header sections to the output stream
     * @param outputStream outputstream to write to
     * @throws IOException
     */
    public void writeBytes(ByteArrayOutputStream outputStream) throws IOException {
        outputStream.write(ID);
        outputStream.write(Flags);
        outputStream.write(QDCount);
        outputStream.write(ANCount);
        outputStream.write(NSCount);
        outputStream.write(ARCount);
    }

    @Override
    public String toString() {
        return "DNSHeader{" +
                "ID=" + Arrays.toString(ID) +
                ", Flags=" + Arrays.toString(Flags) +
                ", QDCount=" + Arrays.toString(QDCount) +
                ", ANCount=" + Arrays.toString(ANCount) +
                ", NSCount=" + Arrays.toString(NSCount) +
                ", ARCount=" + Arrays.toString(ARCount) +
                ", QR=" + QR +
                ", OPCODE=" + OPCODE +
                ", AA=" + AA +
                ", TC=" + TC +
                ", RD=" + RD +
                ", RA=" + RA +
                ", Z=" + Z +
                ", RCODE=" + RCODE +
                '}';
    }

    /**
     * parses flags and sets appropriate values when decoding the header
     * flags are set in their original positions, to be used later
     */

    private void parseFlags() {
        this.QR = (byte) (this.Flags[0] & 0b10000000);     //0x80 == 10000000
        this.OPCODE = (byte) (this.Flags[0] & 0b01111000); //0x78 == 01111000
        this.AA = (byte) (this.Flags[0] & 0b00000100);      // 0x4 == 00000100
        this.TC = (byte) (this.Flags[0] & 0b00000010);       //0x2 == 00000010
        this.RD = (byte) (this.Flags[0] & 0b00000001);     //0x01 == 00000001
//     ----------------------------------------------------------------------
        this.RA = (byte) (this.Flags[1] & 0x80);     // 0x80 == 10000000
        this.Z = (byte) (this.Flags[1] & 0x70);     //  0x70 == 01110000
        this.RCODE = (byte) (this.Flags[1] & 0xF);    // 0xF == 00001111

    }


    /**
     * Getter Methods
     */

    public byte[] getID() {
        return ID;
    }

    public byte[] getFlags() {
        return Flags;
    }

    public byte[] getQDCount() {
        return QDCount;
    }

    public byte[] getANCount() {
        return ANCount;
    }

    public byte[] getNSCount() {
        return NSCount;
    }

    public byte[] getARCount() {
        return ARCount;
    }

    public byte getQR() {
        return QR;
    }

    public byte getOPCODE() {
        return OPCODE;
    }

    public byte getAA() {
        return AA;
    }

    public byte getTC() {
        return TC;
    }

    public byte getRD() {
        return RD;
    }

    public byte getRA() {
        return RA;
    }

    public byte getZ() {
        return Z;
    }

    public byte getRCODE() {
        return RCODE;
    }
}
