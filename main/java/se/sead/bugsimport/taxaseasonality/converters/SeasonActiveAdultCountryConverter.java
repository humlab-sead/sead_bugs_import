package se.sead.bugsimport.taxaseasonality.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.country.CountryImportTraceHelper;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;

@Component
public class SeasonActiveAdultCountryConverter {

    @Autowired
    private CountryImportTraceHelper countryTraceHelper;

    private Location noLocationSpecified;
    private Location noLocationFoundForName;

    public SeasonActiveAdultCountryConverter(){
        noLocationSpecified = new Location();
        noLocationSpecified.addError("Country cannot be empty");
        noLocationFoundForName = new Location();
        noLocationFoundForName.addError("Unknown country");
    }

    public Location getLocation(SeasonActiveAdult bugsData) {
        if(bugsData.getCountryCode() == null ||bugsData.getCountryCode().isEmpty()){
            return noLocationSpecified;
        }
        Location fromLastTrace = countryTraceHelper.getFromLastTrace(bugsData.getCountryCode());
        if(fromLastTrace == null){
            return noLocationFoundForName;
        }
        return fromLastTrace;
    }
}
