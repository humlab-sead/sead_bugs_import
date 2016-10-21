package se.sead.model;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociationType;
import se.sead.sead.model.Biblio;

public class TestSpeciesAssociation extends SpeciesAssociation {

    private TestSpeciesAssociation(Integer id){
        setId(id);
    }

    public static SpeciesAssociation create(
            Integer id,
            TaxaSpecies source,
            TaxaSpecies target,
            SpeciesAssociationType type,
            Biblio reference
    ) {
        SpeciesAssociation association = new TestSpeciesAssociation(id);
        association.setSourceSpecies(source);
        association.setTargetSpecies(target);
        association.setType(type);
        association.setReference(reference);
        return association;
    }
}
