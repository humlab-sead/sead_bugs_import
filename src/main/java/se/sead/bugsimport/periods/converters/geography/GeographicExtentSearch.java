package se.sead.bugsimport.periods.converters.geography;

import se.sead.bugsimport.locations.seadmodel.Location;

interface GeographicExtentSearch {
    Location NO_LOCATION = new NoLocationLocation();
    Location findForName(String geographicName);
}
