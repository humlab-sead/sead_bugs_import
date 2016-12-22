package se.sead.testutils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import se.sead.*;
import se.sead.bugs.AccessDatabaseProvider;

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
    public AccessDatabaseProvider getDatabaseReader() {
        String mdbFile = getMdbFile();
        if(mdbFile == null || mdbFile.isEmpty()){
            return new NoOpAccessDatabaseProvider();
        }
        return new DefaultAccessDatabaseDatabase(mdbFile);
    }

    @Override
    @Bean
    @Primary
    public DataSource createDataSource() {
        String extraDataFile = getDataFile();
        if(!extraDataFile.isEmpty()){
            return DataSourceFactory.createDefault(extraDataFile);
        } else {
            return DataSourceFactory.createDefault();
        }
    }

    @Bean
    @Primary
    public DefaultImportRunner getImportRunner(){
        return new DefaultImportRunner(){
            @Override
            public void run() throws Exception {
            }
        };
    }

    @Bean
    public ExitReporter getExitReporter(){
        return new ExitReporter(){
            @Override
            public void reportErrors() {

            }
        };
    }
}
