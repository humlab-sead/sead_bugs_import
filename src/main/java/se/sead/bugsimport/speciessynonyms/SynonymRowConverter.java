package se.sead.bugsimport.speciessynonyms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.bugsimport.speciessynonyms.bugsmodel.Synonym;
import se.sead.bugsimport.speciessynonyms.bugsmodel.SynonymBugsTable;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.SpeciesAssociationRepository;

@Component
public class SynonymRowConverter implements BugsTableRowConverter<Synonym, SpeciesAssociation> {

    @Autowired
    private SynonymTraceHelper traceHelper;
    @Autowired
    private SynonymCreator creator;

    @Override
    public SpeciesAssociation convertForDataRow(Synonym bugsData) {
        SpeciesAssociation fromLastTrace = traceHelper.getFromLastTrace(bugsData.compressToString());
        if(fromLastTrace == null){
            return create(bugsData);
        } else {
            return fromLastTrace;
        }
    }

    private SpeciesAssociation create(Synonym bugsData){
        return creator.create(bugsData);
    }

    @Component
    public static class SynonymTraceHelper extends SeadDataFromTraceHelper<Synonym, SpeciesAssociation> {

        public SynonymTraceHelper(SpeciesAssociationRepository repository){
            super(SynonymBugsTable.TABLE_NAME, "tbl_species_associations", true, repository);
        }
    }
}
