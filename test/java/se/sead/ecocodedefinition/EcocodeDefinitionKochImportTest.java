package se.sead.ecocodedefinition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.sead.DataSourceFactory;
import se.sead.DefaultAccessDatabaseReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {Application.class, EcocodeDefinitionKochImportTest.Config.class})
//@TestConfiguration
//@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EcocodeDefinitionKochImportTest {

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("ecocodedefinition", "ecocodedefinition.mdb", "ecocodedefinition.sql");
        }

        @Bean
        @Override
        public AccessReaderProvider getDatabaseReader() {
            return new DefaultAccessDatabaseReader(getMdbFile());
        }

        @Bean
        @Override
        public DataSource createDataSource() {
            return DataSourceFactory.createDefault(getDataFile());
        }
    }



}
