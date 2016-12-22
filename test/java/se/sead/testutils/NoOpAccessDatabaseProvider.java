package se.sead.testutils;

import se.sead.bugs.AccessReader;
import se.sead.bugs.AccessDatabaseProvider;

public class NoOpAccessDatabaseProvider implements AccessDatabaseProvider {
    @Override
    public AccessReader getReader() {
        return null;
    }
}
