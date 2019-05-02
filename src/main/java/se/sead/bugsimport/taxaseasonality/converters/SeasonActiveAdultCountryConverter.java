package se.sead.bugsimport.taxaseasonality.converters;

import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.country.CountryConverter;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;

@Component
public class SeasonActiveAdultCountryConverter extends CountryConverter<SeasonActiveAdult>{

    @Override
    protected String getCountryCode(SeasonActiveAdult bugsData) {
        return bugsData.getCountryCode();
    }
}
