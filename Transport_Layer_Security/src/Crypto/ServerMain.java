package Crypto;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

public class ServerMain {


    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException, CertificateException, ClassNotFoundException {

        try {
            Server server = new Server(Shared.PORT_NUM);
            server.runServer();
        } catch (Exception e) {
            System.err.println("Exception thrown in Server Main");
            e.printStackTrace();
        }

    }
}
