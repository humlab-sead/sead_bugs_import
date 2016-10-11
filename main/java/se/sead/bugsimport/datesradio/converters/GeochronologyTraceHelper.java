package se.sead.bugsimport.datesradio.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadioBugsTable;
import se.sead.bugsimport.datesradio.seadmodel.Geochronology;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.GeochronologyRepository;

@Component
public class GeochronologyTraceHelper extends SeadDataFromTraceHelper<DatesRadio, Geochronology> {

    @Autowired
    public GeochronologyTraceHelper(GeochronologyRepository geochronologyRepository){
        super(DatesRadioBugsTable.TABLE_NAME, "tbl_geochronology", false, geochronologyRepository);
    }
}
