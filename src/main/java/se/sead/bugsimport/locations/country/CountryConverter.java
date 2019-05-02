package se.sead.bugsimport.locations.country;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.locations.seadmodel.Location;

public abstract class CountryConverter<BugsType extends TraceableBugsData>{

    @Autowired
    private CountryImportTraceHelper countryTraceHelper;

    private Location noLocationSpecified;
    private Location noLocationFoundForName;

    public CountryConverter(String noLocationSpecifiedError, String noLocationFoundError){
        noLocationSpecified = new Location();
        noLocationSpecified.addError(noLocationSpecifiedError);
        noLocationFoundForName = new Location();
        noLocationFoundForName.addError(noLocationFoundError);
    }

    public CountryConverter(){
        this("Country cannot be empty", "Unknown country");
    }

    public Location getLocation(BugsType bugsData) {
        String countryCode = getCountryCode(bugsData);
        if(countryCode == null || countryCode.isEmpty()){
            return noLocationSpecified;
        }
        Location fromLastTrace = countryTraceHelper.getFromLastTrace(countryCode);
        if(fromLastTrace == null){
            return noLocationFoundForName;
        }
        return fromLastTrace;
    }

    protected abstract String getCountryCode(BugsType bugsData);
}
