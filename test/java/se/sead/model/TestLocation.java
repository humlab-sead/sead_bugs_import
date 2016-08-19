package se.sead.model;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.locations.seadmodel.LocationType;

public class TestLocation extends Location {

    private TestLocation(Integer id){
        super.setId(id);
    }

    public static Location create(Integer id, String name, LocationType type){
        Location loc = new TestLocation(id);
        loc.setName(name);
        loc.setType(type);
        return loc;
    }
}
