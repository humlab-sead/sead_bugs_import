package se.sead.bugsimport.rdbsystems.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class BugsRDBSystem extends TraceableBugsData {

    private Integer rdbSystemCode;
    private String rdbSystem;
    private String rdbVersion;
    private Integer rdbSystemDate;
    private Short rdbFirstPublished;
    private String ref;
    private String countryCode;

    public Integer getRdbSystemCode() {
        return rdbSystemCode;
    }

    public void setRdbSystemCode(Integer rdbSystemCode) {
        this.rdbSystemCode = rdbSystemCode;
    }

    public String getRdbSystem() {
        return rdbSystem;
    }

    public void setRdbSystem(String rdbSystem) {
        this.rdbSystem = rdbSystem;
    }

    public String getRdbVersion() {
        return rdbVersion;
    }

    public void setRdbVersion(String rdbVersion) {
        this.rdbVersion = rdbVersion;
    }

    public Integer getRdbSystemDate() {
        return rdbSystemDate;
    }

    public void setRdbSystemDate(Integer rdbSystemDate) {
        this.rdbSystemDate = rdbSystemDate;
    }

    public Short getRdbFirstPublished() {
        return rdbFirstPublished;
    }

    public void setRdbFirstPublished(Short rdbFirstPublished) {
        this.rdbFirstPublished = rdbFirstPublished;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String compressToString() {
        return "{" +
                rdbSystemCode + ',' +
                rdbSystem + ',' +
                rdbVersion + ',' +
                rdbSystemDate + ',' +
                rdbFirstPublished + ',' +
                ref + ',' +
                countryCode + '}';
    }

    @Override
    public String bugsTable() {
        return RDBSystemBugsTable.TABLE_NAME;
    }

    @Override
    public String getBugsIdentifier() {
        return String.valueOf(rdbSystemCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BugsRDBSystem rdbSystem1 = (BugsRDBSystem) o;

        if (rdbSystemCode != null ? !rdbSystemCode.equals(rdbSystem1.rdbSystemCode) : rdbSystem1.rdbSystemCode != null)
            return false;
        if (rdbSystem != null ? !rdbSystem.equals(rdbSystem1.rdbSystem) : rdbSystem1.rdbSystem != null) return false;
        if (rdbVersion != null ? !rdbVersion.equals(rdbSystem1.rdbVersion) : rdbSystem1.rdbVersion != null)
            return false;
        if (rdbSystemDate != null ? !rdbSystemDate.equals(rdbSystem1.rdbSystemDate) : rdbSystem1.rdbSystemDate != null)
            return false;
        if (rdbFirstPublished != null ? !rdbFirstPublished.equals(rdbSystem1.rdbFirstPublished) : rdbSystem1.rdbFirstPublished != null)
            return false;
        if (ref != null ? !ref.equals(rdbSystem1.ref) : rdbSystem1.ref != null) return false;
        return countryCode != null ? countryCode.equals(rdbSystem1.countryCode) : rdbSystem1.countryCode == null;

    }

    @Override
    public int hashCode() {
        int result = rdbSystemCode != null ? rdbSystemCode.hashCode() : 0;
        result = 31 * result + (rdbSystem != null ? rdbSystem.hashCode() : 0);
        result = 31 * result + (rdbVersion != null ? rdbVersion.hashCode() : 0);
        result = 31 * result + (rdbSystemDate != null ? rdbSystemDate.hashCode() : 0);
        result = 31 * result + (rdbFirstPublished != null ? rdbFirstPublished.hashCode() : 0);
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BugsRDBSystem{" +
                "rdbSystemCode=" + rdbSystemCode +
                ", rdbSystem='" + rdbSystem + '\'' +
                ", rdbVersion='" + rdbVersion + '\'' +
                ", rdbSystemDate=" + rdbSystemDate +
                ", rdbFirstPublished=" + rdbFirstPublished +
                ", ref='" + ref + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
