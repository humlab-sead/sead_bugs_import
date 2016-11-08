package se.sead;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.ApplicationArguments;
import se.sead.bugs.AccessReader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AccessFileConfigurationTest {

    private static final String EXISTING_MDB = "./src/test/resources/accessfileconfiguration/accessfileconfiguration.mdb";

    @Test(expected = IllegalArgumentException.class)
    public void noFileDefined(){
        AccessFileConfiguration configuration = new AccessFileConfiguration("", new MockApplicationArguments("", false));
        configuration.getReader();
    }

    @Test
    public void fallbackProperty(){
        AccessFileConfiguration configuration = new AccessFileConfiguration(EXISTING_MDB, new MockApplicationArguments("", false));
        AccessReader reader = configuration.getReader();
        Assert.assertNotNull(reader);
    }

    @Test
    public void fromNamedCommandLineOption(){
        AccessFileConfiguration configuration = new AccessFileConfiguration("", new MockApplicationArguments(EXISTING_MDB, true));
        Assert.assertNotNull(configuration.getReader());
    }

    @Test
    public void fromNonNamedCommandLineOption(){
        AccessFileConfiguration configuration = new AccessFileConfiguration("", new MockApplicationArguments(EXISTING_MDB, false));
        Assert.assertNotNull(configuration.getReader());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fileNotFound(){
        AccessFileConfiguration configuration = new AccessFileConfiguration(EXISTING_MDB + ".sql", new MockApplicationArguments("", false));
        configuration.getReader();
    }

    private static class MockApplicationArguments implements ApplicationArguments {

        private String file;
        private boolean namedArgument;

        public MockApplicationArguments(String file, boolean namedArgument) {
            this.file = file;
            this.namedArgument = namedArgument;
        }

        @Override
        public String[] getSourceArgs() {
            return null;
        }

        @Override
        public Set<String> getOptionNames() {
            return null;
        }

        @Override
        public boolean containsOption(String s) {
            return namedArgument && s.equals("file");
        }

        @Override
        public List<String> getOptionValues(String s) {
            if(namedArgument) {
                return Arrays.asList(file);
            } else {
                return Collections.EMPTY_LIST;
            }
        }

        @Override
        public List<String> getNonOptionArgs() {
            if(namedArgument || file.isEmpty()){
                return Collections.EMPTY_LIST;
            } else {
                return Arrays.asList(file);
            }
        }
    }
}
