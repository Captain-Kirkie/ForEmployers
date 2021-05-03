package Crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class TLS {
    // input and output stream
    ObjectInputStream objectInputStream;  // objectInputStream
    ObjectOutputStream objectOutputStream; // objectOutputStream

    BigInteger sharedSecretBigInt;
    PrivateKey RSAPrivKey;
    BigInteger DHPubKey;
    PublicKey RSAPubKey;

    // byte arrays
    // server keys
    byte[] ServerEncrypt;
    byte[] ServerMAC;
    byte[] ServerIV;
    //client keys
    byte[] ClientEncrypt;
    byte[] ClientMAC;
    byte[] ClientIV;

    byte[] nonce;


    // actual secret keys
    SecretKeySpec EncryptSecretKeyServer;
    SecretKeySpec EncryptSecretKeyClient;

    SecretKeySpec MacSecretKeyServer;
    SecretKeySpec MacSecretKeyClient;

    IvParameterSpec IVSecretKeyServer;
    IvParameterSpec IVSecretKeyClient;

    TLS() {
    }


    public void makeSecretKeys(byte[] clientNonce) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(clientNonce, "HmacSHA256"); // input is client nonce??
        sha256_HMAC.init(secretKeySpec); // initialize HMAC

        byte[] prk = sha256_HMAC.doFinal(this.sharedSecretBigInt.toByteArray());
        //encrypts
        this.ServerEncrypt = hdkfExpand(prk, "Server encrypt".getBytes());
        this.ClientEncrypt = hdkfExpand(this.ServerEncrypt, "Client encrypt".getBytes());
        //MACs
        this.ServerMAC = hdkfExpand(this.ClientEncrypt, "Server MAC".getBytes());
        this.ClientMAC = hdkfExpand(this.ServerMAC, "Client MAC".getBytes());
        // IV
        this.ServerIV = hdkfExpand(this.ClientMAC, "Server IV".getBytes());
        this.ClientIV = hdkfExpand(this.ServerIV, "Client IV".getBytes());

        // store byte arrays as secret keys
        this.EncryptSecretKeyServer = new SecretKeySpec(ServerEncrypt, "AES");
        this.EncryptSecretKeyClient = new SecretKeySpec(ClientEncrypt, "AES");

        // MAC
        this.MacSecretKeyServer = new SecretKeySpec(ServerMAC, "MAC");
        this.MacSecretKeyClient = new SecretKeySpec(ClientMAC, "MAC");

        //IV
        this.IVSecretKeyServer = new IvParameterSpec(ServerIV);
        this.IVSecretKeyClient = new IvParameterSpec(ClientIV);
    }


    public byte[] hdkfExpand(byte[] input, byte[] tag) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        //concat tag with byte+1
        byte[] concat = new byte[tag.length + 1];
        System.arraycopy(tag, 0, concat, 0, tag.length);
        concat[tag.length] = 0x1; // add one to the end of tag

        // get mac instance
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(input, "HmacSHA256");
        sha256_HMAC.init(secretKeySpec);
        byte[] maced = sha256_HMAC.doFinal(concat); // do the mac encryption
        byte[] ret = new byte[16];
        System.arraycopy(maced, 0, ret, 0, 16); //  copy first 16 bytes
        return ret;
    }


    public byte[] generateNonce() { // Generate random random bytes (number used once)
        SecureRandom randomObj = new SecureRandom();
        byte[] nonce = new byte[32];
        randomObj.nextBytes(nonce);
        return nonce;
    }

    void computeDHSharedSecret(BigInteger APub, BigInteger BPub) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // calculate shared secret
        this.sharedSecretBigInt = BPub.modPow(new BigInteger(this.RSAPrivKey.getEncoded()), Shared.N); // Bpub ^ secretKey % N
    }


    void printKeysByteArrayHex() {
        System.out.println("Server Encrypt");
        for (byte b : ServerEncrypt) {
            System.out.print(String.format("0x%08X", b) + ",");
        }
        System.out.println();
        System.out.println("Server MAC");
        for (byte b : ServerMAC) {
            System.out.print(String.format("0x%08X", b) + ",");
        }
        System.out.println();
        System.out.println("ServerIV");
        for (byte b : ServerIV) {
            System.out.print(String.format("0x%08X", b) + ",");
        }
        System.out.println();
        System.out.println("Client Encrypt");
        for (byte b : ClientEncrypt) {
            System.out.print(String.format("0x%08X", b) + ",");
        }
        System.out.println();
        System.out.println("Client MAC");
        for (byte b : ClientMAC) {
            System.out.print(String.format("0x%08X", b) + ",");
        }
        System.out.println();
        System.out.println("ClientIV");
        for (byte b : ClientIV) {
            System.out.print(String.format("0x%08X", b) + ",");
        }
        System.out.println();
    }

    byte[] readMessageAndDecrypt() throws IOException, ClassNotFoundException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] fromServer = (byte[]) objectInputStream.readObject();
        //decrypt
        byte[] decrypted = Shared.cipherMessageDecrypt(fromServer, this.EncryptSecretKeyServer, this.IVSecretKeyServer);
        byte[] hmac = Shared.pull32BitsHMAC(decrypted);
        byte[] messageNoMac = Shared.getMessage(decrypted);
        byte[] macedPlaintext = Shared.HMAC(this.MacSecretKeyServer, messageNoMac);

        if (Arrays.equals(hmac, macedPlaintext)) {
            System.out.println("MAC'd plain text equals HMAC sent");
        } else {
            System.err.println("MAC'd plain text equals HMAC sent");
        }
        return messageNoMac;
    }

    void sendEncryptedMessage(byte [] message) throws NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        byte[] messagePlusMac = Shared.concatMacAndMessage(message, this.MacSecretKeyServer);
        byte[] encryptedMessagePlusMac = Shared.cipherMessageEncrypt(messagePlusMac, this.EncryptSecretKeyServer, this.IVSecretKeyServer);
        objectOutputStream.writeObject(encryptedMessagePlusMac);
    }

}
