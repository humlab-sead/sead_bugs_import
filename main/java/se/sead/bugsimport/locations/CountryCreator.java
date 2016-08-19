package se.sead.bugsimport.locations;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.repositories.LocationTypeRepository;

public class CountryCreator extends LocationCreator{

    private boolean canCreateCountries;

    @Autowired
    public CountryCreator(LocationTypeRepository typeRepository, boolean canCreateCountries){
        super(typeRepository.getCountryType());
        this.canCreateCountries = canCreateCountries;
    }

    @Override
    public Location create(String locationName) {
        Location location = super.create(locationName);
        if(!canCreateCountries && location.isErrorFree()){
            location.addError("Creation of new country locations not allowed");
        }
        return location;
    }
}
