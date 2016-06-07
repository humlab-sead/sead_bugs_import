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
        LocationCreator creator = new LocationCreator(locationTypeRepository.getCountryType(), bugsData);
        return creator.create();
    }

    private static class LocationCreator {
        private LocationType countryType;
        private Location createdItem;
        private Country bugsData;

        public LocationCreator(LocationType countryType, Country bugsData) {
            this.countryType = countryType;
            this.bugsData = bugsData;
        }

        Location create(){
            createdItem = new Location();
            setName();
            setType();
            return createdItem;
        }

        private void setName(){
            String countryName = bugsData.getCountry();
            createdItem.setName(countryName);
            if(countryName == null || countryName.isEmpty()){
                createdItem.addError("Empty country not allowed: " + bugsData.compressToString());
            }
        }

        private void setType(){
            createdItem.setType(countryType);
        }
    }
}
