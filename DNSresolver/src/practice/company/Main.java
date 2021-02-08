package practice.company;
// references:
// https://www.baeldung.com/udp-in-java
// https://github.com/EmilHernvall/dnsguide
// https://www2.cs.duke.edu/courses/fall16/compsci356/DNS/DNS-primer.pdf
// https://stackoverflow.com/questions/15455965/decoding-dns-header
// https://routley.io/posts/hand-writing-dns-messages/
// https://www2.cs.duke.edu/courses/fall16/compsci356/DNS/DNS-primer.pdf
// https://serverfault.com/questions/173187/what-does-a-dns-request-look-like
// https://www.youtube.com/watch?v=6wm5Qk4gEv0&t=134s
// https://www.youtube.com/results?search_query=Make+your+Own+DNS+Server+in+Python+-+Part+1
// https://www.geeksforgeeks.org/working-udp-datagramsockets-java/
// https://www.codejava.net/java-se/networking/java-udp-client-server-program-example
// //4.1.3 in https://www.ietf.org/rfc/rfc1035.txt
//http://www.steves-internet-guide.com/dns-guide-beginners/

import java.io.IOException;

// 192.168.0.36 This is my IP address
// Testing server dig example.com @127.0.0.1 -p 8053
public class Main {

    public static void main(String[] args) throws IOException {
        DNSServer server = new DNSServer();
        server.runDNSServer();
    }
}
