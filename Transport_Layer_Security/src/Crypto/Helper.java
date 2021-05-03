package Crypto;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class Helper {
    // print byte array
    public static void printByteArr(byte[] arr) {
        int count = 0;
        for (byte b : arr) {
            System.out.println(Integer.toHexString(b));
            count++;
        }
        System.out.println("Size of array: " + count);
    }

    public static void printByteArrHex(byte[] arr) {
        for (byte b : arr) {
            System.out.print(String.format("0x%08X", b) + ",");
        }
        System.out.println();
    }

    //  https://www.techiedelight.com/how-to-read-file-using-inputstream-java/
   static void printFileInputStream(FileInputStream in) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.lineSeparator());
            }
            System.out.println("this is the file");
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void printChars(byte [] arr){
        for(byte b : arr){
            System.out.print((char) b);
        }
        System.out.println();
    }
}
