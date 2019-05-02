package se.sead.bugsimport.mcrnames.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;

public class BugsMCRNames extends TraceableBugsData {

    @BugsColumn("MCRNameTrim")
    private String mcrNameTrim;
    @BugsColumn("CompareStatus")
    private String compareStatus;
    @BugsColumn("CODE")
    private Double code;
    @BugsColumn("tempCODE")
    private Double tempCode;
    @BugsColumn("MCRNumber")
    private Short mcrNumber;
    @BugsColumn("MCRName")
    private String mcrName;

    public String getMcrNameTrim() {
        return mcrNameTrim;
    }

    public void setMcrNameTrim(String mcrNameTrim) {
        this.mcrNameTrim = mcrNameTrim;
    }

    public String getCompareStatus() {
        return compareStatus;
    }

    public void setCompareStatus(String compareStatus) {
        this.compareStatus = compareStatus;
    }

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public Double getTempCode() {
        return tempCode;
    }

    public void setTempCode(Double tempCode) {
        this.tempCode = tempCode;
    }

    public Short getMcrNumber() {
        return mcrNumber;
    }

    public void setMcrNumber(Short mcrNumber) {
        this.mcrNumber = mcrNumber;
    }

    public String getMcrName() {
        return mcrName;
    }

    public void setMcrName(String mcrName) {
        this.mcrName = mcrName;
    }

    @Override
    public String compressToString() {
        return "{" +
                code + ',' +
                mcrNumber + ',' +
                mcrName + ',' +
                compareStatus +
                '}';
    }

    @Override
    public String getBugsIdentifier() {
        return code != null ? code.toString() : "";
    }

    @Override
    public String bugsTable() {
        return MCRNamesBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BugsMCRNames that = (BugsMCRNames) o;

        if (mcrNameTrim != null ? !mcrNameTrim.equals(that.mcrNameTrim) : that.mcrNameTrim != null) return false;
        if (compareStatus != null ? !compareStatus.equals(that.compareStatus) : that.compareStatus != null)
            return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (tempCode != null ? !tempCode.equals(that.tempCode) : that.tempCode != null) return false;
        if (mcrNumber != null ? !mcrNumber.equals(that.mcrNumber) : that.mcrNumber != null) return false;
        return mcrName != null ? mcrName.equals(that.mcrName) : that.mcrName == null;

    }

    @Override
    public int hashCode() {
        int result = mcrNameTrim != null ? mcrNameTrim.hashCode() : 0;
        result = 31 * result + (compareStatus != null ? compareStatus.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (tempCode != null ? tempCode.hashCode() : 0);
        result = 31 * result + (mcrNumber != null ? mcrNumber.hashCode() : 0);
        result = 31 * result + (mcrName != null ? mcrName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BugsMCRNames{" +
                "compareStatus='" + compareStatus + '\'' +
                ", code=" + code +
                ", mcrNumber=" + mcrNumber +
                ", mcrName='" + mcrName + '\'' +
                '}';
    }
}
