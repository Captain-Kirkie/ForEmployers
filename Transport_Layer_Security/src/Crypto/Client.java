package Crypto;
// https://stackoverflow.com/questions/20119874/how-to-load-the-private-key-from-a-der-file-into-java-private-key-object
// https://stackoverflow.com/questions/61888443/storing-a-diffie-hellman-key-pair-for-reuse-in-a-keystore-in-java

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

// https://www.geeksforgeeks.org/socket-programming-in-java/
// https://stackoverflow.com/questions/13539535/extract-public-key-from-certificate-in-der-format
public class Client extends TLS {
    Socket socket;
    InetAddress host;
    int port;

    // https://www.journaldev.com/741/java-socket-programming-server-client
    public Client(InetAddress host, int port) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, CertificateException {
        // create secret key
        super();
        RSAPrivKey = Shared.getPrivKeyFromFile("clientPrivateKey.der"); // get the RSAPriv key
        RSAPubKey = Shared.getPubKeyFromFile("CASignedClientCertificate.der"); // get RSA Pub key

        // create a socket
        socket = null;
        objectInputStream = null;
        objectOutputStream = null;
        this.host = host;
        this.port = port;
    }

    public void handShakeWithServer() throws IOException, ClassNotFoundException, CertificateException, InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        // establish connection with server

        socket = new Socket(host.getHostName(), port); // create a new socket
        //create input and output streams
        objectInputStream = new ObjectInputStream(socket.getInputStream()); // create a new objInputStream
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream()); // create a new objOutputStream

        //generate nonce
        this.nonce = generateNonce();

        // send Send nonce to client
        objectOutputStream.writeObject(this.nonce); // writes to sockets output stream

        // catch things from server
        X509Certificate serverCert = (X509Certificate) objectInputStream.readObject();
        BigInteger serverDHPubKey = (BigInteger) objectInputStream.readObject();
        BigInteger SignedServerPubDHKey = (BigInteger) objectInputStream.readObject();

        //verify signed server
        // send Server Certificate
        X509Certificate clientCert = Shared.getCertificate("CASignedClientCertificate.der");
        objectOutputStream.writeObject(clientCert); // send it to Server
        // create Apub send to client
        this.DHPubKey = Shared.g.modPow(new BigInteger(this.RSAPrivKey.getEncoded()), Shared.N);

        objectOutputStream.writeObject(this.DHPubKey); // write DHpub key to server
        // server signPubKey and Sends
        BigInteger signedDHPubKeyClient = Shared.signDHPubKey(this.RSAPrivKey, this.DHPubKey);
        objectOutputStream.writeObject(signedDHPubKeyClient);

        // Verify signed pub DH key
        boolean certifedSig = Shared.verifyDigitalSignature(serverCert.getPublicKey(), serverDHPubKey, SignedServerPubDHKey);
        if (!certifedSig) {
            System.err.println("Server digital signature NOT verified!");
        } else {
            System.out.println("Server digital signature verified");
        }

        // compute shared DH Secret
        computeDHSharedSecret(this.DHPubKey, serverDHPubKey);

        // compute session keys
        makeSecretKeys(this.nonce);
        // printKeysByteArrayHex();

        // MAC(all handshake messages so far including the previous step, Client's MAC key).
        byte[] allHandShakeMessages = Shared.convertAllMessagesToByteArray(this.nonce, clientCert, serverCert, this.DHPubKey, serverDHPubKey, signedDHPubKeyClient, SignedServerPubDHKey);

        // catch servers mac'd messages and authenticates
        byte[] macedMessagesFromServer = (byte[]) objectInputStream.readObject();
        if (Arrays.equals(macedMessagesFromServer, Shared.HMAC(this.MacSecretKeyServer, allHandShakeMessages))) {
            System.out.println("MAC'd messages are equal! Before adding mac from server!");
        } else {
            System.err.println("MAC'd messages are NOT equal!");
        }

        // final mac messages
        ByteArrayOutputStream addedMacedMessages = new ByteArrayOutputStream();
        addedMacedMessages.write(allHandShakeMessages); // write all handshake messages so far
        addedMacedMessages.write(macedMessagesFromServer); // add to the end the servers stuff
        objectOutputStream.writeObject(Shared.HMAC(this.MacSecretKeyClient, addedMacedMessages.toByteArray()));// client macs with client key

    }

    void readFileFromServerAndSendResponse() throws IOException, ClassNotFoundException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        readMessageAndDecrypt();
        String Message = "The client has received the servers message";
        sendEncryptedMessage(Message.getBytes());
    }

}
