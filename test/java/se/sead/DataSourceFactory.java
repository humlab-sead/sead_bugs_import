package se.sead;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;


public class DataSourceFactory {

    private static final String FILE_PREFIX = "file:";
    private static final String RESOURCE_PATH = "./src/test/resources/";
    private static final String DATA_MODEL_FILE = FILE_PREFIX + RESOURCE_PATH + "databasemodel.sql";

    public static DataSource createDefault(){
        return createRoot().build();
    }

    private static EmbeddedDatabaseBuilder createRoot(){
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript(DATA_MODEL_FILE);
    }

    public static DataSource createDefault(String... initScripts){
        EmbeddedDatabaseBuilder builder = createRoot();
        for (String initScript :
                initScripts) {
            if(!initScript.startsWith(RESOURCE_PATH)){
                initScript = RESOURCE_PATH + initScript;
            }
            if(!initScript.startsWith(FILE_PREFIX)){
                initScript = FILE_PREFIX + initScript;
            }
            builder.addScript(initScript);
        }
        return builder.build();
    }
}
