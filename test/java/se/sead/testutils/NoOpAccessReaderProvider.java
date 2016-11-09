package se.sead.testutils;

import se.sead.bugs.AccessReader;
import se.sead.bugs.AccessReaderProvider;

public class NoOpAccessReaderProvider implements AccessReaderProvider {
    @Override
    public AccessReader getReader() {
        return null;
    }
}
