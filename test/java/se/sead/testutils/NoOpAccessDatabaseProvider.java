package se.sead.testutils;

import se.sead.bugs.AccessDatabase;
import se.sead.bugs.AccessDatabaseProvider;

public class NoOpAccessDatabaseProvider implements AccessDatabaseProvider {
    @Override
    public AccessDatabase getDatabase() {
        return null;
    }
}
