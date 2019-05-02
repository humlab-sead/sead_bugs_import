package se.sead.bugsimport.periods.converters.geography;

import se.sead.bugsimport.locations.seadmodel.Location;

class NoLocationLocation extends Location {
    public NoLocationLocation(){
        addError("No location found");
    }
}
