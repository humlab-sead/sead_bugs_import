package se.sead.bugsimport.speciessynonyms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.bugsimport.speciessynonyms.bugsmodel.Synonym;
import se.sead.bugsimport.speciessynonyms.bugsmodel.SynonymBugsTable;

@Component
public class SynonymBugsSeadMapper extends BugsSeadMapper<Synonym, SpeciesAssociation> {

    @Autowired
    public SynonymBugsSeadMapper(SynonymRowConverter singleBugsTableRowConverterForMapper) {
        super(new SynonymBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
