package se.sead.bugsimport.site.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class Site implements TraceableBugsData {

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
        return '{' +
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
        return null;
    }
}
