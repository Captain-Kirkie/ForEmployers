package practice.company;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Helper {

    public static byte[] arrayListToByteArray(ArrayList<Byte> arrayList) {
        byte[] bytes = new byte[arrayList.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = arrayList.get(i);
        }
        return bytes;
    }

    public static void printByteToHex(String string, byte b) {
        String hex = String.format("%02X", b);
        System.out.println(string + " " + hex);
    }

    // Helper function to print bytes in hex
    public static void printByteArrayHex(String string, byte[] arr) {
        System.out.print(string + " ");
        for (byte b : arr) {
            String hex = String.format("%02X", b);
            System.out.print(" " +hex);
        }
        System.out.println("\n");
    }

    // Uses a string Builder to build string domain name
    public static String buildString(ByteArrayInputStream inputStream, int octet) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < octet; i++) {
            stringBuilder.append((char) inputStream.read());
        }
        return stringBuilder.toString();
    }


    public static String[] stringArrayListToStringArray(ArrayList<String> arrayList) {
        String[] strings = new String[arrayList.size()];
        for (int i = 0; i < strings.length; i++) {
           strings[i] = arrayList.get(i);
        }
        return strings;
    }

    public static int convertSizeTwoByteArrayToInteger(byte[] arr){
        int combinedArr = (arr[0] << 8) | arr[1];
        return combinedArr;
    }

    public static int byteArrToInt(byte[] arr){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for(Byte b : arr){
            outputStream.write(b);
        }
        byte[] product = outputStream.toByteArray();
//        System.out.println("This is the length of the product " + product.length);

        return (int)product[0];
    }

   public static byte[] shortToTwoByteArray(short count){
        byte[] answerArray = new byte[2];
        answerArray[1] = (byte) (count & 0xff);
        answerArray[0] = (byte) ((count >> 8) & 0xff);
        return answerArray;
    }


}
