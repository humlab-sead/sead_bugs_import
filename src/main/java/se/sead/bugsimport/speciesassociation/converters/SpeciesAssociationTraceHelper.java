package se.sead.bugsimport.speciesassociation.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.speciesassociation.bugsmodel.BugsSpeciesAssociation;
import se.sead.bugsimport.speciesassociation.bugsmodel.SpeciesAssociationBugsTable;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.SpeciesAssociationRepository;

@Component
public class SpeciesAssociationTraceHelper extends SeadDataFromTraceHelper<BugsSpeciesAssociation, SpeciesAssociation> {

    @Autowired
    public SpeciesAssociationTraceHelper(SpeciesAssociationRepository repository){
        super(SpeciesAssociationBugsTable.TABLE_NAME, "tbl_species_associations", false, repository);
    }
}
