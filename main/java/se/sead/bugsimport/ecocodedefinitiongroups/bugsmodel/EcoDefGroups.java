package se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class EcoDefGroups extends TraceableBugsData {

    private String ecoGroupCode;
    private String ecoName;

    public String getEcoGroupCode() {
        return ecoGroupCode;
    }

    public void setEcoGroupCode(String ecoGroupCode) {
        this.ecoGroupCode = ecoGroupCode;
    }

    public String getEcoName() {
        return ecoName;
    }

    public void setEcoName(String ecoName) {
        this.ecoName = ecoName;
    }

    @Override
    public String compressToString() {
        return "{" +
                ecoGroupCode + ',' +
                ecoName + "}";
    }

    @Override
    public String getBugsIdentifier() {
        return ecoGroupCode;
    }

    @Override
    public String bugsTable() {
        return EcoDefGroupsBugsTable.TABLE_NAME;
    }
}
