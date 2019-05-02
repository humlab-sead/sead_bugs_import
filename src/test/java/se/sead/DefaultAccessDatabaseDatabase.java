package se.sead;

import se.sead.bugs.AccessDatabase;
import se.sead.bugs.AccessDatabaseProvider;

/**
 * Created by erer0001 on 2016-05-12.
 */
public class DefaultAccessDatabaseDatabase implements AccessDatabaseProvider {

    private AccessDatabase accessDatabase;

    public DefaultAccessDatabaseDatabase(AccessDatabase accessDatabase){
        this.accessDatabase = accessDatabase;
    }

    public DefaultAccessDatabaseDatabase(String mdbFileFullPath){
        this(new AccessDatabase(mdbFileFullPath));
    }

    @Override
    public AccessDatabase getDatabase() {
        return accessDatabase;
    }
}
