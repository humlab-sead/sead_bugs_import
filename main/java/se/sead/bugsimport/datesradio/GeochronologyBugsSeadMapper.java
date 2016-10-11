package se.sead.bugsimport.datesradio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadioBugsTable;
import se.sead.bugsimport.datesradio.seadmodel.Geochronology;

@Component
public class GeochronologyBugsSeadMapper extends BugsSeadMapper<DatesRadio, Geochronology> {

    @Autowired
    public GeochronologyBugsSeadMapper(GeochronologyRowConverter singleBugsTableRowConverterForMapper) {
        super(new DatesRadioBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
