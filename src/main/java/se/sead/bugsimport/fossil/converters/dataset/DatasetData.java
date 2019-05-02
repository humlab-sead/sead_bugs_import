package se.sead.bugsimport.fossil.converters.dataset;

import se.sead.sead.data.DataType;

public class DatasetData {

    private String countSheetCode;
    private DataType dataType;

    DatasetData(String countSheetCode, DataType dataType){
        this.countSheetCode = countSheetCode;
        this.dataType = dataType;
    }

    public String getCountSheetCode() {
        return countSheetCode;
    }

    public DataType getDataType() {
        return dataType;
    }
}
