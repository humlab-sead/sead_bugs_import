package se.sead.bugsimport.periods.converters.geography;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.country.CountryImportTraceHelper;
import se.sead.bugsimport.locations.seadmodel.Location;

@Component
@Order(1)
public class CountryFromTraceGeographicExtentSearch implements GeographicExtentSearch {
    @Autowired
    private CountryImportTraceHelper countryTraceHelper;

    @Override
    public Location findForName(String geographicName) {
        Location fromLastTrace = countryTraceHelper.getFromLastTrace(geographicName);
        if(fromLastTrace == null){
            return NO_LOCATION;
        }
        return fromLastTrace;
    }
}
