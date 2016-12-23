package se.sead;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.ApplicationArguments;
import se.sead.bugs.AccessDatabase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AccessFileConfigurationTest {

    private static final String EXISTING_MDB = "./src/test/resources/accessfileconfiguration/accessfileconfiguration.mdb";

    @Test(expected = IllegalArgumentException.class)
    public void noFileDefined(){
        AccessConfiguration configuration = new AccessConfiguration("", new MockApplicationArguments("", false));
        configuration.getDatabase();
    }

    @Test
    public void fallbackProperty(){
        AccessConfiguration configuration = new AccessConfiguration(EXISTING_MDB, new MockApplicationArguments("", false));
        AccessDatabase reader = configuration.getDatabase();
        Assert.assertNotNull(reader);
    }

    @Test
    public void fromNamedCommandLineOption(){
        AccessConfiguration configuration = new AccessConfiguration("", new MockApplicationArguments(EXISTING_MDB, true));
        Assert.assertNotNull(configuration.getDatabase());
    }

    @Test
    public void fromNonNamedCommandLineOption(){
        AccessConfiguration configuration = new AccessConfiguration("", new MockApplicationArguments(EXISTING_MDB, false));
        Assert.assertNotNull(configuration.getDatabase());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fileNotFound(){
        AccessConfiguration configuration = new AccessConfiguration(EXISTING_MDB + ".sql", new MockApplicationArguments("", false));
        configuration.getDatabase();
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
