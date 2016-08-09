package se.sead.bugsimport.country;

import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.country.seadmodel.LocationType;

public abstract class LocationCreator {
    private LocationType countryType;
    private Location createdItem;

    public LocationCreator(LocationType countryType) {
        this.countryType = countryType;
    }

   public Location create(){
        createdItem = new Location();
        setName();
        setType();
        return createdItem;
    }

    private void setName(){
        String countryName = getBugsLocationName();
        createdItem.setName(countryName);
        if(countryName == null || countryName.isEmpty()){
            createdItem.addError(getErrorForEmptyName());
        }
    }

    protected abstract String getBugsLocationName();

    protected abstract String getErrorForEmptyName();

    private void setType(){
        createdItem.setType(countryType);
    }
}
