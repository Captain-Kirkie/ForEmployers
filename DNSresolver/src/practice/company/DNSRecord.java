package practice.company;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class DNSRecord {

    private String[] Name;
    private byte[] Type;
    private byte[] Class;
    private byte[] TTL;
    private byte[] RDLENGTH;
    private byte[] RDATA;
    private Calendar creationDate;

    /**
     * Used to decode the records of a DNS Query and or response.
     * Records include, Answers, Resource records and additional Records
     * @param inputStream input stream to read from
     * @param dnsMessage DNS Query or response from google
     * @return  new DNS record with appropriate fields filled in
     * @throws IOException
     */
    public static DNSRecord decodeRecord(ByteArrayInputStream inputStream, DNSMessage dnsMessage) throws IOException {
        DNSRecord record = new DNSRecord();
        record.creationDate = Calendar.getInstance(); // Create a creation date
        record.Name = dnsMessage.readDomainName(inputStream);
        record.Type = inputStream.readNBytes(2); // read two bytes
        record.Class = inputStream.readNBytes(2);
        record.TTL = inputStream.readNBytes(4);
        record.RDLENGTH = inputStream.readNBytes(2);
        int lengthRD = Helper.convertSizeTwoByteArrayToInteger(record.RDLENGTH);
        record.RDATA = inputStream.readNBytes(lengthRD);
        return record;
    }

    /**
     * Writes bytes to output stream, used for creating a return message
     * @param byteArrayOutputStream Output stream to write to
     * @param domainNameLocations stores domain names and the locations at which they were first seen
     * @throws IOException
     */

    public void writeBytes(ByteArrayOutputStream byteArrayOutputStream, HashMap<String, Integer> domainNameLocations) throws IOException {
        DNSMessage.writeDomainName(byteArrayOutputStream, domainNameLocations, Name);
        byteArrayOutputStream.write(Type);
        byteArrayOutputStream.write(Class);
        byteArrayOutputStream.write(TTL);
        byteArrayOutputStream.write(RDLENGTH);
        byteArrayOutputStream.write(RDATA);
    }

    /**
     * Verifies weather a record has expired
     * @return true if timestamp as not expired, false otherwise
     */

    public boolean timestampValid() {
        Calendar currentTime = Calendar.getInstance();
        int timeToLive = Helper.byteArrToInt(this.TTL) * 1000;
        long creationPlusTimeToLive = this.creationDate.getTimeInMillis() + timeToLive;
        return currentTime.getTimeInMillis() > creationPlusTimeToLive;
    }


    @Override
    public String toString() {
        return "DNSRecord{" +
                "Name=" + Arrays.toString(Name) +
                ", Type=" + Arrays.toString(Type) +
                ", Class=" + Arrays.toString(Class) +
                ", TTL=" + Arrays.toString(TTL) +
                ", RDLENGTH=" + Arrays.toString(RDLENGTH) +
                ", RDATA=" + Arrays.toString(RDATA) +
                ", creationDate=" + creationDate +
                '}';
    }
}
