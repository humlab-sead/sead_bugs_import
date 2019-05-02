package se.sead.bugsimport.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.countsheets.SampleGroupImporter;
import se.sead.bugsimport.sample.bugsmodel.BugsSample;
import se.sead.bugsimport.sample.seadmodel.Sample;

@Service
public class SampleImporter extends Importer<BugsSample, Sample> {

    @Autowired
    public SampleImporter(
        SampleDataMapper dataMapper,
        SamplePersister persister,
        SampleGroupImporter countSheetImporter
    ){
        super(dataMapper, persister, countSheetImporter);
    }
}
