package Crypto;

import java.io.IOException;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
// enable assertions
// https://intellij-support.jetbrains.com/hc/en-us/community/posts/207014815-How-to-enable-assert

// https://stackoverflow.com/questions/21081713/diffie-hellman-key-exchange-in-java
// http://tutorials.jenkov.com/java-cryptography/index.html#initializing-the-cipher
// https://github.com/GlenTiki/Client-Server-Encryption/blob/master/src/DoComms.java
public class ClientMain {

    // hashes produced:
    // openssl rsa -in clientPrivate.key -noout -modulus | openssl sha256
    // openssl x509 -noout -modulus -in CASignedClientCertificate.pem | openssl sha256
    String privKeyHash = "da6c791b9c5e366d0278d061d00424392be5dec8d099e1fa16bab5716618344b";
    String certKeyHash = "da6c791b9c5e366d0278d061d00424392be5dec8d099e1fa16bab5716618344b";
    String serverPrivKey = "4a919b5c6a75ad9dbc6129951911fae38511ec587c9c134ea8d012fd52438b37";
    String severCertKey = "4a919b5c6a75ad9dbc6129951911fae38511ec587c9c134ea8d012fd52438b37";

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException, CertificateException, ClassNotFoundException {
        // "127.0.0.1" Local host?
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            Client client = new Client(localHost, Shared.PORT_NUM); // create a client
            client.handShakeWithServer();
            client.readFileFromServerAndSendResponse();
        } catch (Exception e) {
            System.err.println("Exception in Client Main");
            e.printStackTrace();
        }
    }
}
