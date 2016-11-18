package se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;

public class EcoDefGroups extends TraceableBugsData {

    @BugsColumn("EcoGroupCode")
    private String ecoGroupCode;
    @BugsColumn("EcoName")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EcoDefGroups that = (EcoDefGroups) o;

        if (ecoGroupCode != null ? !ecoGroupCode.equals(that.ecoGroupCode) : that.ecoGroupCode != null) return false;
        return ecoName != null ? ecoName.equals(that.ecoName) : that.ecoName == null;

    }

    @Override
    public int hashCode() {
        int result = ecoGroupCode != null ? ecoGroupCode.hashCode() : 0;
        result = 31 * result + (ecoName != null ? ecoName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EcoDefGroups{" +
                "ecoGroupCode='" + ecoGroupCode + '\'' +
                ", ecoName='" + ecoName + '\'' +
                '}';
    }
}
