package se.sead.bugsimport.taxaseasonality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.locations.country.CountryImporter;
import se.sead.bugsimport.species.IndexImporter;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;

@Service
public class TaxaSeasonalityImporter extends Importer<SeasonActiveAdult, TaxaSeasonality> {

    @Autowired
    public TaxaSeasonalityImporter(
            TaxaSeasonalityBugsMapper bugsMapper,
            TaxaSeasonalityPersister persister,
            IndexImporter indexImporter,
            CountryImporter countryImporter
    ){
        super(bugsMapper, persister, indexImporter, countryImporter);
    }
}
