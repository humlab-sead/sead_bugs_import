package se.sead.bugsimport.taxaseasonality.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;

import java.util.Locale;

public class SeasonActiveAdult extends TraceableBugsData{

    @BugsColumn("CODE")
    private Double code;
    @BugsColumn("HSeason")
    private String season;
    @BugsColumn("CountryCode")
    private String countryCode;

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String compressToString() {
        return String.format(Locale.ROOT, "{%.7f%s%s}", code, season, countryCode);
    }

    @Override
    public String getBugsIdentifier() {
        return code.toString();
    }

    @Override
    public String bugsTable() {
        return SeasonActiveAdultBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeasonActiveAdult that = (SeasonActiveAdult) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (season != null ? !season.equals(that.season) : that.season != null) return false;
        return countryCode != null ? countryCode.equals(that.countryCode) : that.countryCode == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (season != null ? season.hashCode() : 0);
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SeasonActiveAdult{" +
                "code=" + code +
                ", season='" + season + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
