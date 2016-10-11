package se.sead.bugsimport.datesradio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.datesradio.seadmodel.Geochronology;
import se.sead.bugsimport.lab.LabImporter;
import se.sead.bugsimport.sample.SampleImporter;

@Service
public class GeochronologyImporter extends Importer<DatesRadio, Geochronology> {

    @Autowired
    public GeochronologyImporter(
            GeochronologyBugsSeadMapper dataMapper,
            GeochronologyPersister persister,
            SampleImporter sampleImporter,
            LabImporter labImporter) {
        super(dataMapper, persister, sampleImporter, labImporter);
    }
}
