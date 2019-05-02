package se.sead.speciesassociation;

import se.sead.bugsimport.speciesassociation.bugsmodel.BugsSpeciesAssociation;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<BugsSpeciesAssociation> EXPECTED_DATA =
            Arrays.asList(
                    create(1, 1d, 2d, "parasitizes", null), // exists
                    create(2, 1d, 2d, "is associated with", null), // insert
                    create(3, 1d, 2d, "parasitizes", "Ref 1"), // insert with ref
                    create(4, 1d, null, null, null), //fail: no target
                    create(5, null, 2d, null, null), //fail: no source
                    create(6, 1d, 99d, null, null), // fail: no target found for code
                    create(7, 99d, 2d, null, null), //fail: no source found for code
                    create(9, 1d, 2d, "in borings of", "Ref 2"), //fail: no reference found
                    create(10, 1d, 2d, "unknown", null), //fail: no type found
                    create(11, 1d, 2d, "parasitizes", "Ref 3"), // update
                    create(12, 1d, 2d, "is associated with", "Ref 3"), //fail: try to update updated sead data
                    create(13, 1d, 3d, null, null) // success store default association type
            );

    private static BugsSpeciesAssociation create(
            Integer id,
            Double source,
            Double target,
            String type,
            String ref
    ){
        BugsSpeciesAssociation association = new BugsSpeciesAssociation();
        association.setSpeciesAssociationID(id);
        association.setCode(source);
        association.setAssociatedSpeciesCODE(target);
        association.setAssociationType(type);
        association.setRef(ref);
        return association;
    }

}
