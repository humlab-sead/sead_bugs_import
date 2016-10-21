package se.sead.bugsimport.speciesassociation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.speciesassociation.bugsmodel.BugsSpeciesAssociation;
import se.sead.bugsimport.speciesassociation.bugsmodel.SpeciesAssociationBugsTable;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;

@Component
public class SpeciesAssociationBugsSeadMapper extends BugsSeadMapper<BugsSpeciesAssociation, SpeciesAssociation> {

    @Autowired
    public SpeciesAssociationBugsSeadMapper(SpeciesAssociationRowConverter singleBugsTableRowConverterForMapper) {
        super(new SpeciesAssociationBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
