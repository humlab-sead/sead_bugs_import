package se.sead.bugsimport.site.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class BugsSite extends TraceableBugsData {

    private String code;
    private String name;
    private String region;
    private String country;
    private String ngr;
    private Float latDD;
    private Float longDD;
    private Float alt;
    private String iDBy;
    private String interp;
    private String specimens;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNgr() {
        return ngr;
    }

    public void setNgr(String ngr) {
        this.ngr = ngr;
    }

    public Float getLatDD() {
        return latDD;
    }

    public void setLatDD(Float latDD) {
        this.latDD = latDD;
    }

    public Float getLongDD() {
        return longDD;
    }

    public void setLongDD(Float longDD) {
        this.longDD = longDD;
    }

    public Float getAlt() {
        return alt;
    }

    public void setAlt(Float alt) {
        this.alt = alt;
    }

    public String getiDBy() {
        return iDBy;
    }

    public void setiDBy(String iDBy) {
        this.iDBy = iDBy;
    }

    public String getInterp() {
        return interp;
    }

    public void setInterp(String interp) {
        this.interp = interp;
    }

    public String getSpecimens() {
        return specimens;
    }

    public void setSpecimens(String specimens) {
        this.specimens = specimens;
    }

    @Override
    public String compressToString() {
        return "{" +
                code + ',' +
                name + ',' +
                region + ',' +
                country + ',' +
                ngr + ',' +
                latDD + ',' +
                longDD + ',' +
                alt + ',' +
                iDBy + ',' +
                interp + ',' +
                specimens + '}';
    }

    @Override
    public String bugsTable() {
        return BugsSiteBugsTable.TABLE_NAME;
    }

    @Override
    public String getBugsIdentifier() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BugsSite site = (BugsSite) o;

        if (code != null ? !code.equals(site.code) : site.code != null) return false;
        if (name != null ? !name.equals(site.name) : site.name != null) return false;
        if (region != null ? !region.equals(site.region) : site.region != null) return false;
        if (country != null ? !country.equals(site.country) : site.country != null) return false;
        if (ngr != null ? !ngr.equals(site.ngr) : site.ngr != null) return false;
        if (latDD != null ? !latDD.equals(site.latDD) : site.latDD != null) return false;
        if (longDD != null ? !longDD.equals(site.longDD) : site.longDD != null) return false;
        if (alt != null ? !alt.equals(site.alt) : site.alt != null) return false;
        if (iDBy != null ? !iDBy.equals(site.iDBy) : site.iDBy != null) return false;
        if (interp != null ? !interp.equals(site.interp) : site.interp != null) return false;
        return specimens != null ? specimens.equals(site.specimens) : site.specimens == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (ngr != null ? ngr.hashCode() : 0);
        result = 31 * result + (latDD != null ? latDD.hashCode() : 0);
        result = 31 * result + (longDD != null ? longDD.hashCode() : 0);
        result = 31 * result + (alt != null ? alt.hashCode() : 0);
        result = 31 * result + (iDBy != null ? iDBy.hashCode() : 0);
        result = 31 * result + (interp != null ? interp.hashCode() : 0);
        result = 31 * result + (specimens != null ? specimens.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BugsSite{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                ", ngr='" + ngr + '\'' +
                ", latDD=" + latDD +
                ", longDD=" + longDD +
                ", alt=" + alt +
                ", iDBy='" + iDBy + '\'' +
                ", interp='" + interp + '\'' +
                ", specimens='" + specimens + '\'' +
                '}';
    }
}
