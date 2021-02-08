package assignment06;

public class ReallyBadHashFunctor implements HashFunctor{
    @Override
    public int hash(String item) {
        char[] chars = item.toCharArray();
        return (int) chars[0];
    }
}
