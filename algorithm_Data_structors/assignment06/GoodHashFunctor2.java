package assignment06;

public class GoodHashFunctor2 implements HashFunctor{


    @Override //TODO: There is somthign wrong with this
    public int hash(String item) {
        int hash = 7;
        char[] chars = item.toCharArray();
        for(int i = 0; i < chars.length; i++){
            hash = (hash * 31) + chars[i];
        }
        return Math.abs(hash);
    }
}
