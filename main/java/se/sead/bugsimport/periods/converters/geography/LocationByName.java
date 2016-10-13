package se.sead.bugsimport.periods.converters.geography;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.repositories.LocationRepository;

import java.util.List;

@Component
@Order
public class LocationByName implements GeographicExtentSearch {

    @Autowired
    private LocationRepository repository;

    @Override
    public Location findForName(String geographicName) {
        List<Location> allFound = repository.findAllByName(geographicName);
        if(allFound.size() == 0){
            return NO_LOCATION;
        } else if(allFound.size() == 1){
            return allFound.get(0);
        } else {
            return new TooManyLocationsForNameFound();
        }
    }

    private static class TooManyLocationsForNameFound extends Location {
        public TooManyLocationsForNameFound(){
            addError("Too many locations for name found");
        }
    }
}
