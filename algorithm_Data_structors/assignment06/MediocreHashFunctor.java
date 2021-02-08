package assignment06;

public class MediocreHashFunctor implements HashFunctor{
    @Override
    public int hash(String item) {
       int hashValue = 0, length = item.length();
       char[] itemArray = item.toCharArray();
       for(int i = 0; i < length; i++){
           hashValue += itemArray[i];
       }
        return hashValue;
    }
}
