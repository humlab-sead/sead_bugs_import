package se.sead.bugsimport.locations.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.locations.LocationCreator;
import se.sead.bugsimport.locations.country.bugsmodel.Country;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;

@Component
public class CountryRowConverter implements BugsTableRowConverter<Country, Location> {

    @Autowired
    private LocationRepository locationRepository;
    private LocationCreator countryCreator;

    @Autowired
    public CountryRowConverter(LocationTypeRepository locationTypeRepository){
        countryCreator = new LocationCreator(locationTypeRepository.getCountryType());
    }

    @Override
    public Location convertForDataRow(Country bugsData) {
        Location countryByName = locationRepository.findCountryByName(bugsData.getCountry());
        if(countryByName == null){
            return createNewCountry(bugsData);
        }
        return countryByName;
    }

    private Location createNewCountry(Country bugsData) {
        return countryCreator.create(bugsData.getCountry());
    }

}
