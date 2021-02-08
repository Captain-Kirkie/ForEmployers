package practice.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

//dig example.com @127.0.0.1 -p 8053
public class DNSServer {
    private final DNSCache cache = new DNSCache();
    private byte[] buffer = new byte[512]; //?? Why is this 256?
    private final String localHostIP = "127.0.0.1";

    public void runDNSServer() throws IOException {
        boolean running = true;
        int port = 8053;
        DatagramSocket datagramSocket = new DatagramSocket(port);
        System.out.println("Server is Running");
        while (running) {
            DatagramPacket data = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(data); // blocks until datagram is received
            InetAddress clientIP = data.getAddress();
            int clientPort = data.getPort();
            System.out.println("Message Receieved!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            byte[] dataBuffer = data.getData();
            DNSMessage query = DNSMessage.decodeMessage(dataBuffer);
            if (query.getHeader().getQR() == 0) { // it is a query
                ArrayList<DNSRecord> answers = new ArrayList<>();
                for (DNSQuestion dnsQuestion : query.getDnsQuestionArrayList()) { // this will never go past one
                    if (this.cache.checkCache(dnsQuestion)) {
                        System.out.println("This is contained in the cache");
                        answers.add(this.cache.getRecord(dnsQuestion));
                    } else {
                        //send it to google
                        DNSMessage googleResponseMessage = sendMessageToGoogle(dataBuffer);
                        if(googleResponseMessage.getHeader().getRCODE() == 3){
                            sendMessageToClient(googleResponseMessage, datagramSocket, clientIP, clientPort);
                            continue;
                        }
                        // Add to cache,
                        answers = googleResponseMessage.getAnswersArrayList();
                        this.cache.addToCache(dnsQuestion, googleResponseMessage.getAnswersArrayList().get(0)); // add answers to cache
                    }
                }
                DNSMessage responseToClient = DNSMessage.buildResponse(query, answers);
                sendMessageToClient(responseToClient, datagramSocket, clientIP, clientPort);
            }
            buffer = new byte[512]; // reset buffer
        }
    }


    /**
     *  creates a new Datagram socket, and datagram packet. Sends message to google
     *  awaits googles response, and returns a new message with googles response
     * @param dataBuffer byte array from data socket recieve
     * @return returns a new DNS message decoded from the packet google sent
     * @throws IOException
     *
     */
    DNSMessage sendMessageToGoogle(byte[] dataBuffer) throws IOException {
        // 8.8.8.8 is the primary DNS server for Google DNS
        String googleDNS = "8.8.8.8";
        InetAddress googleIP = InetAddress.getByName(googleDNS); // 8.8.8.8
        DatagramSocket googleSocket = new DatagramSocket(); // 53 might be reservd
        int googlePort = 53;
        DatagramPacket sendToGooglePacket = new DatagramPacket(dataBuffer, dataBuffer.length, googleIP, googlePort); //googles port is 53
        System.out.println("sending to google");
        googleSocket.send(sendToGooglePacket);
        byte[] bufferGoogle = new byte[512];
        DatagramPacket packetReceievedFromGoogle = new DatagramPacket(bufferGoogle, bufferGoogle.length);
        googleSocket.receive(packetReceievedFromGoogle); //wait for googles response
        byte[] googleResponseBuffer = packetReceievedFromGoogle.getData();
        googleSocket.close();
        return DNSMessage.decodeMessage(googleResponseBuffer);
    }

    /**
     *
     * Sends message back to client, either with answers we had in the cache or answers from google, that we added to cache
     *
     * @param clientMessage message to send to client
     * @param socket local datagram socket
     * @param clientIP IP of client - from original packet recieved
     * @param clientPort  -  port client use to send original packet
     * @throws IOException
     */
    void sendMessageToClient(DNSMessage clientMessage, DatagramSocket socket, InetAddress clientIP, int clientPort) throws IOException {
        System.out.println("Send message to client");
        byte[] responseToClientByteArray = clientMessage.toBytes();
        DatagramPacket responsePacketToClient = new DatagramPacket(responseToClientByteArray, responseToClientByteArray.length, clientIP, clientPort);
        socket.send(responsePacketToClient);
    }

}


