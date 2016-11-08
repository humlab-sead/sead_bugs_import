package se.sead.testutils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import se.sead.AccessReaderTest;
import se.sead.ApplicationConfiguration;
import se.sead.DataSourceFactory;
import se.sead.DefaultAccessDatabaseReader;
import se.sead.bugs.AccessReaderProvider;

import javax.sql.DataSource;

public abstract class DefaultConfig implements ApplicationConfiguration{

    private final String mdbFile;
    private final String dataFile;

    protected DefaultConfig(String allNamesSameAsDirectory){
        this(allNamesSameAsDirectory, allNamesSameAsDirectory + ".mdb", allNamesSameAsDirectory + ".sql");
    }

    protected DefaultConfig(String mdbFile, String dataFile){
        this("", mdbFile, dataFile);
    }

    protected DefaultConfig(String subDirectory, String mdbFile, String dataFile){
        this(AccessReaderTest.RESOURCE_FOLDER, subDirectory, mdbFile, dataFile);
    }

    protected DefaultConfig(String resourcePath, String subDirectory, String mdbFile, String dataFile){
        this.mdbFile = pathBuilder(resourcePath, subDirectory, mdbFile);
        this.dataFile = pathBuilder(resourcePath, subDirectory, dataFile);
    }

    private static String pathBuilder(String... pathItems){
        return String.join("/", pathItems)
                .replaceAll("//", "/");
    }

    protected String getMdbFile() {
        return mdbFile;
    }

    protected String getDataFile() {
        return dataFile;
    }

    @Override
    @Bean
    @Primary
    public AccessReaderProvider getDatabaseReader() {
        return new DefaultAccessDatabaseReader(getMdbFile());
    }

    @Override
    @Bean
    @Primary
    public DataSource createDataSource() {
        return DataSourceFactory.createDefault(getDataFile());
    }
}
