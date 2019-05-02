package se.sead.bugsimport.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.bugsimport.locations.country.CountryImporter;

@Service
public class LabImporter extends Importer<BugsLab, DatingLab> {

    @Autowired
    public LabImporter(
            LabBugsSeadMapper dataMapper,
            LabPersister persister,
            CountryImporter countryImporter
    ){
        super(dataMapper, persister, countryImporter);
    }
}
