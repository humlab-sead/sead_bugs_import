package se.sead;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessDatabaseProvider;
import se.sead.bugs.AccessReader;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Component
public class AccessConfiguration implements AccessDatabaseProvider {

    private String accessDatabaseFile;

    @Autowired
    public AccessConfiguration(
            @Value("${file:}")
            String accessDatabaseFile,
            ApplicationArguments arguments){
        setDatabaseFile(arguments, accessDatabaseFile);
    }

    private void setDatabaseFile(ApplicationArguments arguments, String fileNameFromProperty){
        List<String> optionValues = Collections.EMPTY_LIST;
        if(arguments.containsOption("file")){
            optionValues = arguments.getOptionValues("file");
        } else {
            optionValues = arguments.getNonOptionArgs();
        }
        if(!optionValues.isEmpty()){
           accessDatabaseFile = optionValues.get(0);
        } else {
            accessDatabaseFile = fileNameFromProperty;
        }
    }

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
