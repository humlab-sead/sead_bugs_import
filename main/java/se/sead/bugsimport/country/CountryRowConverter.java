package se.sead.bugsimport.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.country.bugsmodel.Country;
import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.country.seadmodel.LocationType;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;

@Component
public class CountryRowConverter implements BugsTableRowConverter<Country, Location> {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationTypeRepository locationTypeRepository;

    @Override
    public Location convertForDataRow(Country bugsData) {
        Location countryByName = locationRepository.findCountryByName(bugsData.getCountry());
        if(countryByName == null){
            return createNewCountry(bugsData);
        }
        return countryByName;
    }

    private Location createNewCountry(Country bugsData) {
        LocationCreator creator = new CountryCreator(locationTypeRepository.getCountryType(), bugsData);
        return creator.create();
    }

    private static class CountryCreator extends LocationCreator {

        private Country bugsData;

        CountryCreator(LocationType type, Country bugsData){
            super(type);
            this.bugsData = bugsData;
        }

        @Override
        protected String getBugsLocationName() {
            return bugsData.getCountry();
        }

        @Override
        protected String getErrorForEmptyName() {
            return "Empty country not allowed: " + bugsData.getCompressedStringBeforeTranslation();
        }
    }
}
