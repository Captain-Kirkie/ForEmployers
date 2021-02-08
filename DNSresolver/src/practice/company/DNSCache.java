package practice.company;

import java.util.HashMap;
import java.util.Objects;

public class DNSCache {
    private HashMap<DNSQuestion, DNSRecord> cache = new HashMap<>();

    /**
     * This is to access our cache, just calls cache.contains key
     * checks for valid time stamp, if time stamp is not valid, removes from cache
     * @param question The questino used as the key in the chash,
     * @return  if cache contains key && timestamp on value is valid
     */
    public boolean checkCache(DNSQuestion question) {
        if (cache.containsKey(question) && cache.get(question).timestampValid()) {
            return true;
        } else {
            if (cache.containsKey(question)) { // the time stamp is not valid
                System.err.println("This object does not have a valid time stamp");
                cache.remove(question);
                return false;
            }
        }
        return false;
    }

    /**
     *
     * @param question key
     * @return value
     */
    public DNSRecord getRecord(DNSQuestion question) {
        return this.cache.get(question);
    }

    /**
     *
     * @param question key
     * @param record value
     */
    public void addToCache(DNSQuestion question, DNSRecord record) {
        this.cache.put(question, record);
    }

    public HashMap<DNSQuestion, DNSRecord> getCache() {
        return cache;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DNSCache cache1 = (DNSCache) o;
        return Objects.equals(cache, cache1.cache);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cache);
    }


}
