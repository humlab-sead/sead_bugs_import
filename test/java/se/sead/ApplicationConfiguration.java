package se.sead;

import se.sead.bugs.AccessReaderProvider;

import javax.sql.DataSource;

public interface ApplicationConfiguration {
    AccessReaderProvider getDatabaseReader();
    DataSource createDataSource();
}
