package se.sead.bugsimport.datesradio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.datesradio.converters.GeochronologyTraceHelper;
import se.sead.bugsimport.datesradio.converters.GeochronologyUpdater;
import se.sead.bugsimport.datesradio.seadmodel.Geochronology;

@Component
public class GeochronologyRowConverter implements BugsTableRowConverter<DatesRadio, Geochronology> {

    @Autowired
    private GeochronologyTraceHelper traceHelper;
    @Autowired
    private GeochronologyUpdater updater;

    @Override
    public Geochronology convertForDataRow(DatesRadio bugsData) {
        Geochronology fromLastTrace = traceHelper.getFromLastTrace(bugsData.getBugsIdentifier());
        if(fromLastTrace == null){
            return create(bugsData);
        } else if(fromLastTrace.isErrorFree()){
            return update(fromLastTrace, bugsData);
        } else {
            return fromLastTrace;
        }
    }

    private Geochronology create(DatesRadio bugsData){
        return update(new Geochronology(), bugsData);
    }

    private Geochronology update(Geochronology original, DatesRadio bugsData){
        updater.update(original, bugsData);
        return original;
    }
}
