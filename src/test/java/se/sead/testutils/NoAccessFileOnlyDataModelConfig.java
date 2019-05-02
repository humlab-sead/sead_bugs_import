package se.sead.testutils;

public class NoAccessFileOnlyDataModelConfig extends DefaultConfig {

    public NoAccessFileOnlyDataModelConfig(){
        super("");
    }

    @Override
    protected String getMdbFile() {
        return "";
    }

    @Override
    protected String getDataFile() {
        return "";
    }
}
