package assignment06;

//https://www.math10.com/en/algebra/horner.html#:~:text=Horner's%20rule%20for%20polynomial%20division,multiplication%20and%20one%20addition%20processes.
//http://cseweb.ucsd.edu/~kube/cls/100/Lectures/lec16/lec16-16.html
public class GoodHashFunctor implements HashFunctor {
    @Override //1char is 8 bits, 1 byte
    public int hash(String item) {
        long hashVal = 0;
        char[] chars = item.toCharArray(); //turn it into a char array
        for (int i = 0; i < chars.length; i++) {
            hashVal = (hashVal << 4) + chars[i]; //shift over 4, add char
            long g = hashVal & 0xF0000000L; //& with 11110000 00000000 00000000 00000000 (32 bits)
            if (g != 0) {
                hashVal ^= g >>> 24; // unsigned right shift, 24 spaces AndOr it with g
            }                                     //   00000000 00000000 00000000 11110000
            hashVal &= ~g;                        // x &=y   == x = x & y
        }
        return (int) hashVal;
    }
}

