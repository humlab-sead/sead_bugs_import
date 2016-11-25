package se.sead.bugsimport.locations.country.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;

public class Country extends TraceableBugsData {

    @BugsColumn("CountryCode")
    private String countryCode;
    @BugsColumn("Country")
    private String country;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String compressToString() {
        return "{" + countryCode + "," + country + "}";
    }

    @Override
    public String getBugsIdentifier() {
        return countryCode;
    }

    @Override
    public String bugsTable() {
        return CountryBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country1 = (Country) o;

        if (countryCode != null ? !countryCode.equals(country1.countryCode) : country1.countryCode != null)
            return false;
        return country != null ? country.equals(country1.country) : country1.country == null;
    }

    @Override
    public int hashCode() {
        int result = countryCode != null ? countryCode.hashCode() : 0;
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Country{" +
                "countryCode='" + countryCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
