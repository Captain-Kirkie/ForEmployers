package practice.company;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class DNSQuestion {

    private String[] QName;
    private byte[] QType, QClass;


    /**
     *
     * @param inputStream stream to read from
     * @param message query to read the question
     * @return returns a new question to be stored in the question list within the message
     * @throws IOException
     */
    public static DNSQuestion decodeQuestion(ByteArrayInputStream inputStream, DNSMessage message) throws IOException {
        DNSQuestion question = new DNSQuestion();
        question.QName = message.readDomainName(inputStream);
        question.QType = inputStream.readNBytes(2);
        question.QClass = inputStream.readNBytes(2);
        return question;
    }

    void writeBytes(ByteArrayOutputStream byteArrayOutputStream, HashMap<String, Integer> domainNameLocations) throws IOException {
        DNSMessage.writeDomainName(byteArrayOutputStream, domainNameLocations, QName);
        byteArrayOutputStream.write(QType);
        byteArrayOutputStream.write(QClass);
    }

    @Override
    public String toString() {
        return "DNSQuestion{" +
                "QName=" + Arrays.toString(QName) +
                ", QType=" + Arrays.toString(QType) +
                ", QClass=" + Arrays.toString(QClass) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DNSQuestion question = (DNSQuestion) o;
        return Arrays.equals(QName, question.QName) &&
                Arrays.equals(QType, question.QType) &&
                Arrays.equals(QClass, question.QClass);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(QName);
        result = 31 * result + Arrays.hashCode(QType);
        result = 31 * result + Arrays.hashCode(QClass);
        return result;
    }

    public String[] getQName() {
        return QName;
    }

    public byte[] getQType() {
        return QType;
    }

    public byte[] getQClass() {
        return QClass;
    }
}
