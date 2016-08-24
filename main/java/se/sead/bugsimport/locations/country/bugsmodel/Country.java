package se.sead.bugsimport.locations.country.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class Country extends TraceableBugsData {

    private String countryCode;
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

        if (!countryCode.equals(country1.countryCode)) return false;
        return country.equals(country1.country);

    }

    @Override
    public int hashCode() {
        int result = countryCode.hashCode();
        result = 31 * result + country.hashCode();
        return result;
    }
}
