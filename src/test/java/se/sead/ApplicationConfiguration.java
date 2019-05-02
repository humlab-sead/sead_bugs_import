package se.sead;

import se.sead.bugs.AccessDatabaseProvider;

import javax.sql.DataSource;

public interface ApplicationConfiguration {
    AccessDatabaseProvider getDatabaseReader();
    DataSource createDataSource();
}
