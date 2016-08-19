package se.sead.bugsimport.sitelocations.bugsmodel;

import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.bugsmodel.BugsSiteBugsTable;

public class BugsSiteLocation extends TraceableBugsData {

    private String siteCode;
    private String country;
    private String region;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String compressToString() {
        return "{" +
                siteCode +
                '}';
    }

    @Override
    public String bugsTable() {
        return BugsSiteLocationBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BugsSiteLocation that = (BugsSiteLocation) o;

        if (!siteCode.equals(that.siteCode)) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        return region != null ? region.equals(that.region) : that.region == null;

    }

    @Override
    public int hashCode() {
        int result = siteCode.hashCode();
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BugsSiteLocation{" +
                "siteCode='" + siteCode + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                '}' ;
    }
}
