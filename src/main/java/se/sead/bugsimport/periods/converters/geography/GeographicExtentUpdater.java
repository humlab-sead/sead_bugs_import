package se.sead.bugsimport.periods.converters.geography;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.periods.bugsmodel.Period;

import java.util.List;

@Component
public class GeographicExtentUpdater {

    public static final Location NO_OP_LOCATION = new Location();

    private List<GeographicExtentSearch> searches;

    @Autowired
    public GeographicExtentUpdater(List<GeographicExtentSearch> searches) {
        this.searches = searches;
    }

    public Location getLocation(Period period){
        if(isEmpty(period.getGeography())){
            return NO_OP_LOCATION;
        }
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

    private boolean isEmpty(String geographyName){
        return geographyName == null ||
                geographyName.trim().isEmpty();
    }

}
