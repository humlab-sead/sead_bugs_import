package se.sead;

import se.sead.bugs.AccessReader;
import se.sead.bugs.AccessReaderProvider;

/**
 * Created by erer0001 on 2016-05-12.
 */
public class DefaultAccessDatabaseReader implements AccessReaderProvider {

    private AccessReader accessReader;

    public DefaultAccessDatabaseReader(AccessReader accessReader){
        this.accessReader = accessReader;
    }

    public DefaultAccessDatabaseReader(String mdbFileFullPath){
        this(new AccessReader(mdbFileFullPath));
    }

    @Override
    public AccessReader getReader() {
        return accessReader;
    }
}
