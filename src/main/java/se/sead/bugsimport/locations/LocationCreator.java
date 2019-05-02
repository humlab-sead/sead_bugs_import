package se.sead.bugsimport.locations;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.locations.seadmodel.LocationType;

public class LocationCreator {

    private LocationType countryType;

    public LocationCreator(LocationType countryType) {
        this.countryType = countryType;
    }

   public Location create(String locationName){
        Location createdItem = new Location();
        setName(createdItem, locationName);
        setType(createdItem);
        return createdItem;
    }

    private void setName(Location createdItem, String locationName){
        createdItem.setName(locationName);
        if(locationName == null || locationName.isEmpty()){
            createdItem.addError("Location name cannot be empty");
        }
    }

    private void setType(Location createdItem){
        createdItem.setType(countryType);
    }
}
