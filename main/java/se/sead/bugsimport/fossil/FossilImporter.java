package se.sead.bugsimport.fossil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.bugsimport.fossil.converters.AnalysisEntityManager;
import se.sead.bugsimport.fossil.seadmodel.Abundance;
import se.sead.bugsimport.sample.SampleImporter;
import se.sead.bugsimport.species.IndexImporter;

@Service
public class FossilImporter extends Importer<Fossil, Abundance> {

    @Autowired
    private AnalysisEntityManager analysisEntityManager;

    @Autowired
    public FossilImporter(
            FossilBugsSeadMapper dataMapper,
            FossilPersister persister,
            SampleImporter sampleImporter,
            IndexImporter speciesImporter) {
        super(dataMapper, persister, sampleImporter, speciesImporter);
    }

    @Override
    public void run() {
        analysisEntityManager.initCache();
        super.run();
        analysisEntityManager.initCache();
    }
}
