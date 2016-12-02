package se.sead.bugs;

/**
 * Created by erer0001 on 2016-05-11.
 */
public abstract class TraceableBugsData {

    private String compressedStringBeforeTranslation;

    public abstract String compressToString();
    public abstract String bugsTable();

    public String getBugsIdentifier(){
        return null;
    }

    public void setCompressedStringBeforeTranslation(String compressedStringBeforeTranslation){
        this.compressedStringBeforeTranslation = compressedStringBeforeTranslation;
    }

    public String getCompressedStringBeforeTranslation(){
        if(compressedStringBeforeTranslation == null){
            setCompressedStringBeforeTranslation(compressToString());
        }
        return compressedStringBeforeTranslation;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                compressToString();
    }
}
