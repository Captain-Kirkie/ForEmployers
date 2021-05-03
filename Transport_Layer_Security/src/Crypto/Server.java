package Crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class Server extends TLS {
    Socket socket; // used to communicate with client
    ServerSocket serverSocket; // used to listen for request


    public Server(int port) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, CertificateException {
        // create secret key
        super();
        RSAPrivKey = Shared.getPrivKeyFromFile("serverPrivateKey.der");
        RSAPubKey = Shared.getPubKeyFromFile("CASignedServerCertificate.der");
        serverSocket = new ServerSocket(port); // create a new server socket on specified port
        socket = null;
        System.out.println("Server Socket listening on port " + port);

    }

    public void runServer() throws IOException, ClassNotFoundException, CertificateException, InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        try {
            System.out.println("Server started");
            while (true) {
                System.out.println("Waiting for message from Client");
                socket = serverSocket.accept(); // wait for a connection, now have connection from client
                System.out.println("Client accepted\n");
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream()); // get outputstream
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                /* ****************** HandShake ******************************** */
                handShake();
                /* ****************** HandShake End ******************************** */
                System.out.println("HandShake completed. Time to communicate with client");
                communicateWithClient("src/Crypto/count.txt");

            }

        } catch (Exception e) {
            System.out.println("Exception thrown in server");
            e.printStackTrace();
        }
    }

    private void handShake() throws IOException, InvalidKeyException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException, SignatureException, ClassNotFoundException {
        // now i have the nonce
        this.nonce = (byte[]) objectInputStream.readObject();

        // send Server Certificate
        X509Certificate serverCert = Shared.getCertificate("CASignedServerCertificate.der");
        objectOutputStream.writeObject(serverCert); // send it to client
        // create Apub send to client
        this.DHPubKey = Shared.g.modPow(new BigInteger(this.RSAPrivKey.getEncoded()), Shared.N);

        objectOutputStream.writeObject(this.DHPubKey); // write APub
        // server signPubKey and Sends
        BigInteger ServerSignedPubKey = Shared.signDHPubKey(this.RSAPrivKey, this.DHPubKey);
        objectOutputStream.writeObject(ServerSignedPubKey);

        // catch things from client
        X509Certificate clientCert = (X509Certificate) objectInputStream.readObject();
        BigInteger clientPubDHKey = (BigInteger) objectInputStream.readObject();
        BigInteger SignedClientPubDHKey = (BigInteger) objectInputStream.readObject();

        // verify signature from cert
        if (!Shared.verifyDigitalSignature(clientCert.getPublicKey(), clientPubDHKey, SignedClientPubDHKey)) {
            System.err.println("Client digital signature NOT verified!");
        } else {
            System.out.println("Client digital signature verified!!!");
        }
        // garguedas@backcountry.com

        // calculate shared secret
        computeDHSharedSecret(this.DHPubKey, clientPubDHKey);

        // calculate session keys
        makeSecretKeys(this.nonce);
        // printKeysByteArrayHex();
        // final messagesMAC
        byte[] allHandShakeMessages = Shared.convertAllMessagesToByteArray(this.nonce, clientCert, serverCert, clientPubDHKey, this.DHPubKey, SignedClientPubDHKey, ServerSignedPubKey);
        byte[] macedAllHanShake = Shared.HMAC(this.MacSecretKeyServer, allHandShakeMessages);
        objectOutputStream.writeObject(macedAllHanShake); // servers macs with its mac key

        ByteArrayOutputStream combined = new ByteArrayOutputStream();
        combined.write(allHandShakeMessages);
        combined.write(macedAllHanShake);
        // mac it
        byte[] finalArr = Shared.HMAC(this.MacSecretKeyClient, combined.toByteArray());
        byte[] macedMessagesFromClient = (byte[]) objectInputStream.readObject(); // server catches clients handshake

        // compare that all messages are exactly what we think they are
        if (Arrays.equals(finalArr, macedMessagesFromClient)) {
            System.out.println("MAC'd messages are equal! After adding mac from server!");
        } else {
            System.err.println("MAC'd messages are NOT equal! After adding mac from server!");
        }
    }

    // https://stackoverflow.com/questions/17285846/large-file-transfer-over-java-socket
    void communicateWithClient(String filePath) throws InterruptedException, IOException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, ClassNotFoundException {

        while (true) {
            // send file to client
            // read in file
            Path path = Paths.get(filePath);
            byte[] fileByteArr = Files.readAllBytes(path);
            sendEncryptedMessage(fileByteArr);
            byte [] clientResponse = readMessageAndDecrypt();
            Helper.printChars(clientResponse);
            System.exit(0); // TODO: Remove this and make server run forever


        }

    }


}
