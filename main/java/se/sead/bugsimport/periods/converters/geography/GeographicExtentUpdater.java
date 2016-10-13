package se.sead.bugsimport.periods.converters.geography;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.periods.bugsmodel.Period;

import java.util.List;

@Component
public class GeographicExtentUpdater {

    @Autowired
    private List<GeographicExtentSearch> searches;

    public Location getLocation(Period period){
        Location found = GeographicExtentSearch.NO_LOCATION;
        for (GeographicExtentSearch search :
                searches) {
            found = search.findForName(period.getGeography());
            if(found != GeographicExtentSearch.NO_LOCATION){
                break;
            }
        }
        return found;
    }

}
