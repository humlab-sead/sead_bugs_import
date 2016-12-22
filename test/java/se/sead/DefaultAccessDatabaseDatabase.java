package se.sead;

import se.sead.bugs.AccessReader;
import se.sead.bugs.AccessDatabaseProvider;

/**
 * Created by erer0001 on 2016-05-12.
 */
public class DefaultAccessDatabaseDatabase implements AccessDatabaseProvider {

    private AccessReader accessReader;

    public DefaultAccessDatabaseDatabase(AccessReader accessReader){
        this.accessReader = accessReader;
    }

    public DefaultAccessDatabaseDatabase(String mdbFileFullPath){
        this(new AccessReader(mdbFileFullPath));
    }

    @Override
    public AccessReader getReader() {
        return accessReader;
    }
}
