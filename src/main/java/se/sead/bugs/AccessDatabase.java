package se.sead.bugs;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AccessDatabase {

    public static final List<Database.FileFormat> SUPPORTED_FORMATS =
            Arrays.asList(
                    Database.FileFormat.V2000,
                    Database.FileFormat.V2003,
                    Database.FileFormat.V2007,
                    Database.FileFormat.V2010
            );

    private Database accessDatabase;

    public AccessDatabase(String fileName){
        try {
            accessDatabase = DatabaseBuilder.open(new File(fileName));
            checkFileFormat(accessDatabase);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void checkFileFormat(Database accessDatabase) throws IOException {
        if (!SUPPORTED_FORMATS.contains(accessDatabase.getFileFormat())) {
            throw new IllegalArgumentException("Unsupported file format on access file: " + accessDatabase.getFileFormat());
        }
    }

    public Database getAccessDatabase(){
        return accessDatabase;
    }
}
