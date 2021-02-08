package practice.company;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DNSMessage {

    private DNSHeader header;
    private ArrayList<DNSQuestion> dnsQuestionArrayList = new ArrayList<>();
    private ArrayList<DNSRecord> answersArrayList = new ArrayList<>();
    private ArrayList<DNSRecord> authorityRecords = new ArrayList<>();
    private ArrayList<DNSRecord> additionalRecords = new ArrayList<>();
    private byte[] fullMessage;

    /**
     *
     * @param bytes full message hex dump
     * @return returns a new decoded message with appropriate parameters stored
     * @throws IOException
     */
    public static DNSMessage decodeMessage(byte[] bytes) throws IOException {

        DNSMessage message = new DNSMessage();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        message.fullMessage = bytes; // store full message
        message.header = DNSHeader.decodeHeader(byteArrayInputStream); // now I have an input stream to do with what I wish

        //Question count
        // get the question count
        int questionCount = Helper.convertSizeTwoByteArrayToInteger(message.header.getQDCount());
        for (int i = 0; i < questionCount; i++) {
            DNSQuestion question = DNSQuestion.decodeQuestion(byteArrayInputStream, message);
            message.dnsQuestionArrayList.add(question);
        }

        int answerCount = Helper.convertSizeTwoByteArrayToInteger(message.header.getANCount());
        for (int i = 0; i < answerCount; i++) {
            DNSRecord answer = DNSRecord.decodeRecord(byteArrayInputStream, message);
            message.answersArrayList.add(answer);
        }

        // for loop for authority records
        int NSCount = Helper.convertSizeTwoByteArrayToInteger(message.header.getNSCount());
        for (int i = 0; i < NSCount; i++) {
            DNSRecord authorityRecord = DNSRecord.decodeRecord(byteArrayInputStream, message);
            message.authorityRecords.add(authorityRecord);
        }

        // for loop for additional records
        int ARCount = Helper.convertSizeTwoByteArrayToInteger(message.header.getARCount());
        for (int i = 0; i < ARCount; i++) {
            DNSRecord additionalRecord = DNSRecord.decodeRecord(byteArrayInputStream, message);
            message.additionalRecords.add(additionalRecord);
        }
        return message;
    }

    /**
     *
     * Reads the domain name from hex dump
     * Handles compression, if name is compressed
     * @param inputStream Input stream to read from
     * @return Returns string array containing each section of the domain name
     *      e.g. [www], [google], [com]
     * @throws IOException
     */

    public String[] readDomainName(ByteArrayInputStream inputStream) throws IOException {
        ArrayList<String> domainNameList = new ArrayList<>();
        String[] domainNameArr;
        int octet = inputStream.read(); //read the first
        int checkForCompression = octet >> 6; // shift bits 6 to the right
        if (checkForCompression != 0) { // or could check if == 3
            byte removeFirstTwoBits = (byte) (0b00111111 & octet); // 0 out the first two bits....
            int offset = (removeFirstTwoBits << 8 | inputStream.read()); // add the first and second bytes together, the front to bits are zeroed
            domainNameArr = readDomainName(offset); //<---- read domain name starting from the offset.....
        } else {
            if (octet > 20 || octet < 0) {
                System.err.println("At Wrong spot in input stream");
            }
            while (octet != 0x00) { //This does not add the octets to the list
                domainNameList.add(Helper.buildString(inputStream, octet));
                if (inputStream.available() > 0)
                    octet = inputStream.read();
                else {
                    System.err.println("Reading past the end of the input stream");
                    System.exit(1);
                }
            }
            domainNameArr = Helper.stringArrayListToStringArray(domainNameList);
        }
        return domainNameArr;
    }

    /**
     * Reads compressed domain name
     * @param firstByte indicates the byte where the domain name was first seen
     * @return Returns string array containing each section of the domain name
     *         e.g. [www], [google], [com]
     * @throws IOException
     */
    public String[] readDomainName(int firstByte) throws IOException {
        ByteArrayInputStream shiftedStream = new ByteArrayInputStream(fullMessage);
        shiftedStream.skipNBytes(firstByte); // could also use readNBytes?
        return readDomainName(shiftedStream); // call read domain name on what is now a shifted stream
    }

    /**
     * @param request original query
     * @param answers list of answers
     * @return return new DNS message to send back to the client
     */
    static DNSMessage buildResponse(DNSMessage request, ArrayList<DNSRecord> answers) {
        DNSMessage responseMessage = new DNSMessage();
        responseMessage.dnsQuestionArrayList = request.dnsQuestionArrayList; // first fill the array list
        responseMessage.answersArrayList.addAll(answers);
        responseMessage.authorityRecords = request.authorityRecords;
        responseMessage.additionalRecords = request.additionalRecords;
        responseMessage.header = DNSHeader.buildResponseHeader(request, responseMessage);
        return responseMessage;
    }

    /**
     * Creates a byte array from a DNSmessage which can then be put into a data gram packet and sent
     * @return returns byte array corresponding to hex dump.
     * @throws IOException
     */

    byte[] toBytes() throws IOException {
        HashMap<String, Integer> domainNameLocations = new HashMap<>();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.header.writeBytes(outputStream);

        for (DNSQuestion q : dnsQuestionArrayList) {
            q.writeBytes(outputStream, domainNameLocations);
        }
        for (DNSRecord record : answersArrayList) {
            record.writeBytes(outputStream, domainNameLocations);
        }

        for (DNSRecord record : authorityRecords) {
            record.writeBytes(outputStream, domainNameLocations);
        }

        for (DNSRecord record : additionalRecords) {
            record.writeBytes(outputStream, domainNameLocations);
        }
        return outputStream.toByteArray();
    }

    /**
     *
     * @param byteArrayOutputStream output stream to write too
     * @param domainLocations map of domain locations and the corresponding byte position at which they were first seen
     *                        ued for writing compressed domain names
     * @param domainPieces Separate pieces of the domain name
     */
    static void writeDomainName(ByteArrayOutputStream byteArrayOutputStream, HashMap<String, Integer> domainLocations, String[] domainPieces) {
        String fullDomainName = octetsToString(domainPieces);
        if (domainLocations.containsKey(fullDomainName)) { // if we already have this, write compression
            int offset = domainLocations.get(fullDomainName);
            offset = offset & 0x0000FFFF; // 00000000 00000000 11111111 11111111  == 00000000 00000000 00000000 00001100
            offset = offset | 0x0000C000; // 00000000 00000000 11000000 00000000 == 11000000 00000000 00000000 00001100
            int shiftedOffset = offset >> 8; // write first byte
            byteArrayOutputStream.write(shiftedOffset);
            byteArrayOutputStream.write(offset); // write second byte
        } else {
            domainLocations.put(fullDomainName, byteArrayOutputStream.size()); // add the name and location to the hashmap
            for (String s : domainPieces) {
                DNSMessage.writeStringToOutputStream(byteArrayOutputStream, s);
            }
            byteArrayOutputStream.write(0x00);
        }
    }

    /**
     * Writes the length of the string, then each char of the string
     * called in write domain name
     * @param outputStream output stream to write to
     * @param s domain anmes section [www]
     */

    public static void writeStringToOutputStream(ByteArrayOutputStream outputStream, String s) {
        outputStream.write(s.length()); // right the length
        char[] stringToCharArray = s.toCharArray();
        for (char c : stringToCharArray) {
            outputStream.write(c);
        }
    }

    /**
     * join the pieces of a domain name with dots ([ "utah", "edu"] -> "utah.edu" )
     * called in write domain name to store the entire domain name
     * @param octets domain name separated by periods
     * @return return combined domain name
     */
    private static String octetsToString(String[] octets) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : octets) {
            if (!s.equals(octets[octets.length - 1]))
                s += ".";

            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "DNSMessage{" +
                "header=" + header +
                ", dnsQuestionArrayList=" + dnsQuestionArrayList +
                ", answersArrayList=" + answersArrayList +
                ", authorityRecords=" + authorityRecords +
                ", additionalRecords=" + additionalRecords +
                ", fullMessage=" + Arrays.toString(fullMessage) +
                '}';
    }

    //getters
    public DNSHeader getHeader() {
        return header;
    }

    public ArrayList<DNSQuestion> getDnsQuestionArrayList() {
        return dnsQuestionArrayList;
    }

    public ArrayList<DNSRecord> getAdditionalRecords() {
        return additionalRecords;
    }

    public ArrayList<DNSRecord> getAnswersArrayList() {
        return answersArrayList;
    }

    public ArrayList<DNSRecord> getAuthorityRecords() {
        return authorityRecords;
    }
}


