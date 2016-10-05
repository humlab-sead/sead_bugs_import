package se.sead.ecocodedefinition;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.Application;
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
