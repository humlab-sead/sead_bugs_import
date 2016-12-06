package se.sead.speciesbiology;

import se.sead.bugsimport.speciesbiology.bugsmodel.Biology;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<Biology> EXPECTED_DATA =
            Arrays.asList(
                    create(1d, "Ref 1", "Note exists"),
                    create(2d, "Ref 1", "Note to be inserted"),
                    create(3d, "Ref 3", "Ref does not exist"),
                    create(4d, "Ref 2", "Species does not exist"),
                    create(5d, "Ref 2", null),
                    create(null, "Ref 2", "No species defined"),
                    create(6d, null, "No reference")
            );

    private static final Biology create(Double code, String reference, String data){
        Biology biology = new Biology();
        biology.setCode(code);
        biology.setRef(reference);
        biology.setData(data);
        return biology;
    }

}
