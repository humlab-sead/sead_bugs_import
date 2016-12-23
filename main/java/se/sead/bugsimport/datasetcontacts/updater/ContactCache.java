package se.sead.bugsimport.datasetcontacts.updater;

import se.sead.sead.contact.Contact;

import java.util.HashMap;
import java.util.Map;

public class ContactCache {

    private Map<CacheKey, Contact> cache;

    public ContactCache(){
        cache = new HashMap<>();
    }

    public Contact getCached(String name, boolean identifiedBy){
        CacheKey key = new CacheKey(name, identifiedBy);
        return cache.get(key);
    }


    private static class CacheKey {
        private String name;
        private boolean identifiedBy;

        public CacheKey(String name, boolean identifiedBy) {
            this.name = name;
            this.identifiedBy = identifiedBy;
        }

        public String getName() {
            return name;
        }

        public boolean isIdentifiedBy() {
            return identifiedBy;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey cacheKey = (CacheKey) o;

            if (identifiedBy != cacheKey.identifiedBy) return false;
            return name != null ? name.equals(cacheKey.name) : cacheKey.name == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (identifiedBy ? 1 : 0);
            return result;
        }
    }
}
