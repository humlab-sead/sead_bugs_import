package se.sead.bugsimport.rdbsystems.converters;

import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.country.CountryConverter;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;

@Component
public class RdbSystemCountryConverter extends CountryConverter<BugsRDBSystem> {

    @Override
    protected String getCountryCode(BugsRDBSystem bugsData) {
        return bugsData.getCountryCode();
    }
}
