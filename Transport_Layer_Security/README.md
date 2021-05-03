Generate 3 public/private key pairs:
    [ ] certificate authority
    [ ] Server
    [ ] Client
Generate a RSA public/private key pair for certificate Authority
    openssl req -x509 -newkey rsa:4096 -keyout CAprivateKey.pem -out CAcertificate.pem -days 30 -nodes
    -req -- make a new certificate
    -x509 -- in x509 format (the standard format)
    -newkey -- generate a new RSA keypair for this certificate
    rsa:4096 -- make it a 4096 bit RSA key
    -keyout -- filename for the private key
    -out -- filename for the certificate
    -days 30 -- this certificate expires 30 days from now
    -nodes -- don't encrypt the certificate/private key
    
Generate client/server keys + "certificate signature requests"


index.txt is the "database" which lists all the certificates that have been issued by our CA (again, overkill for us, but it makes OpenSSL happy)

HANDSHAKE:
    - Client: Nonce1 (32 bytes from a SecureRandomObject)
    - Server: Server certificate, DiffeHellman public key, Signed DiffeHellman public key (Sign[g^ks % N], Spriv)
    - Client: Client Certificate, DiffeHellman public key, Signed DiffeHellman public key (Sign[g^kc % N], Cpriv)
    - client and server compute shared secret using DH
    - client and server derive 6 session keys, from shared secret. 2 each on bulk encryption keys, MAC keys, IV for CBC using HKDF
    - Server: MAC (all handshake messages so far, Server's MAC key)
    - Client: MAC (all handshake messages so far, Client's MAC key)
    
At this point, the client and server have authenticated.
We'll create a "Certificate Authority" which will (before we run the program) 
sign certificates for the client and server. 
For a real application, the certificate verification 
would makes sure that the chain of trust goes up to a trusted certificate authority. 
For this application, when we receive a certificate, we'll make sure that it is signed by our CA.


Testing:
    Write 2 programs (I'd suggest a single Java project with multiple classes including main() methods), a client a server.
    
    The server should open a socket, listen for connections. Once it receives one, it should handshake with the client then send a large file (in the range of MBs, so that it must be split across multiple messages)
    
    The client should receive the file, then send a message back to the server saying to indicate that it got the file. This is to verify that we can actually send messages bidirectionally.
    
    The client and server should both read in the CA certificate as well as their own Certificates + private keys, but should not read the other host's certificate/keys. The parties need to send the certificates to each other as part of the handshake protocol.
    


Useful Classes:
    BigInteger
    java.security.cert.CertificateFactory
    java.security.cert.Certificate
    java.security.SecureRandom;
    javax.crypto.Cypher
    javax.crypto.Mac;
    javax.crypto.SecretKey;
    javax.crypto.spec.IvParameterSpec;
    javax.crypto.spec.SecretKeySpec;
    java.security.spec.PKCS8EncodedKeySpec; //for loading secret keys
    ByteArrayOutputStream
    ObjectOutputStream and ObjectInputStream
    Many other classes...

    
    