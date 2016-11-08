package se.sead;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessReader;
import se.sead.bugs.AccessReaderProvider;

import java.io.File;

@Component
public class AccessFileConfiguration implements AccessReaderProvider{

    @Value("${file:}")
    private String accessDatabaseFile;

    @Override
    public AccessReader getReader() {
        if( ! fileExists()){
            throw new IllegalArgumentException("No mdb file specified");
        }
        return new AccessReader(accessDatabaseFile);
    }

    private boolean fileExists() {
        File file = new File(accessDatabaseFile);
        return file.exists() && file.isFile();
    }
}
