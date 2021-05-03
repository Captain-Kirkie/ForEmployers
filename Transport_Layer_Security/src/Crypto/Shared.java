package Crypto;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class Shared {
    public static final int DIFFE_HELLMAN_BIT_LENGTH = 2048;
    public static final int PORT_NUM = 6666;

    // real big prim num used for modolus
    static public BigInteger N = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1" +
            "29024E088A67CC74020BBEA63B139B22514A08798E3404DD" +
            "EF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245" +
            "E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED" +
            "EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3D" +
            "C2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F" +
            "83655D23DCA3AD961C62F356208552BB9ED529077096966D" +
            "670C354E4ABC9804F1746C08CA18217C32905E462E36CE3B" +
            "E39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9" +
            "DE2BCBF6955817183995497CEA956AE515D2261898FA0510" +
            "15728E5A8AACAA68FFFFFFFFFFFFFFFF", 16);

    //generator
    static public BigInteger g = new BigInteger("2");

    static void createDHSharedSecret(Client Alice, Server Bob) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // shout g
        // shout N
        // shout Apub
        // shout Bpub
        BigInteger APub = g.modPow(new BigInteger(Alice.RSAPrivKey.getEncoded()), N);
        BigInteger BPub = g.modPow(new BigInteger(Bob.RSAPrivKey.getEncoded()), N);

        Alice.DHPubKey = APub; // diffe hellman calculated pub key
        Bob.DHPubKey = BPub; // diffe hellman calculated pub key

        // calculate shared secret
        Alice.sharedSecretBigInt = BPub.modPow(new BigInteger(Alice.RSAPrivKey.getEncoded()), N); // Bpub ^ secretKey % N
        Bob.sharedSecretBigInt = APub.modPow(new BigInteger(Bob.RSAPrivKey.getEncoded()), N); // // Apub ^ secretKey % N
    }


    static BigInteger signDHPubKey(PrivateKey privateKey, BigInteger publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256WithRSA"); // algorithm to use
        SecureRandom secureRandom = new SecureRandom();
        signature.initSign(privateKey, secureRandom);
        signature.update(publicKey.toByteArray()); // sign it
        byte[] digitalSignature = signature.sign(); // sign it
        // Helper.printByteArr(digitalSignature);
        return new BigInteger(digitalSignature);
    }


    public static PrivateKey getPrivKeyFromFile(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Path path = Paths.get(filePath);
        byte[] privKeyByteArray = Files.readAllBytes(path);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privKeyByteArray);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
// https://gist.github.com/luismoramedina/735617e06fa1e59cdc3fb4b6830c0a2d
    //   https://stackoverflow.com/questions/6358555/obtaining-public-key-from-certificate

    public static PublicKey getPubKeyFromFile(String filePath) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, CertificateException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        CertificateFactory cf = CertificateFactory.getInstance("x.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(fileInputStream);
        return cert.getPublicKey();
    }


    public static X509Certificate getCertificate(String filePath) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, CertificateException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        CertificateFactory cf = CertificateFactory.getInstance("x.509");
        return (X509Certificate) cf.generateCertificate(fileInputStream);
    }

    static boolean verifyDigitalSignature(PublicKey RSAPublicKey, BigInteger publicKeyUnsigned, BigInteger publicKeysigned) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initVerify(RSAPublicKey);
        signature.update(publicKeyUnsigned.toByteArray());
        return signature.verify(publicKeysigned.toByteArray());
    }


    // write into a byte array
    static byte[] convertAllMessagesToByteArray(byte[] nonce, X509Certificate ClientCert, X509Certificate ServerCert, BigInteger ClientDHPubKey, BigInteger ServerDHPubKey, BigInteger SignedClientPubDHKey, BigInteger SignedServerPubDHKey) throws CertificateEncodingException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(nonce);
            //Certs
            outputStream.write(ClientCert.getEncoded());
            outputStream.write(ServerCert.getEncoded());
            // DHPubKeys unsigned
            outputStream.write(ClientDHPubKey.toByteArray());
            outputStream.write(ServerDHPubKey.toByteArray());
            // Signed DH keys
            outputStream.write(SignedClientPubDHKey.toByteArray());
            outputStream.write(SignedClientPubDHKey.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }


    static byte[] HMAC(SecretKeySpec key, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), "HmacSHA256");
        sha256_HMAC.init(secretKeySpec);
        return sha256_HMAC.doFinal(data); // do the mac encryption
    }

    public static byte[] concatMacAndMessage(byte[] message, SecretKeySpec secretKey) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        ByteArrayOutputStream encrypted = new ByteArrayOutputStream();
        encrypted.write(message);
        byte[] macMessage = Shared.HMAC(secretKey, message);
        encrypted.write(macMessage); // write the maced message
        return encrypted.toByteArray();
    }


    public static byte[] cipherMessageEncrypt(byte[] message, SecretKeySpec key, IvParameterSpec ivPara) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivPara);
        return cipher.doFinal(message); // or call update???
    }

    public static byte[] cipherMessageDecrypt(byte[] message, SecretKeySpec key, IvParameterSpec ivPara) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivPara);
        return cipher.doFinal(message);
    }

    public static byte[] getMessage(byte[] message){
        int end = message.length - 32;
        byte[] noMac = new byte[end];
        if (end >= 0) System.arraycopy(message, 0, noMac, 0, end);
        return noMac;
    }

    public static byte[] pull32BitsHMAC(byte[] message) {
        int end = message.length - 1; // account for indexing
        int startOfHMAC = end - 31; // start of the mac
        byte[] hmac = new byte[32];
        for (int i = 0; i < 32; i++) {
            hmac[i] = message[startOfHMAC];
            startOfHMAC++;
        }
        return hmac;
    }


    // https://stackoverflow.com/questions/39064479/public-private-key-to-encrypt-aes-session-key
    // create session keys
    // https://www.novixys.com/blog/hmac-sha256-message-authentication-mac-java/
    // https://stackoverflow.com/questions/46988540/java-hmacsha256-with-key

}
