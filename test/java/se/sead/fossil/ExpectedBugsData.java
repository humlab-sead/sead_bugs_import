package se.sead.fossil;

import se.sead.bugsimport.fossil.bugsmodel.Fossil;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<Fossil> EXPECTED_DATA =
            Arrays.asList(
                    create("FOSI000001", null, 0, 1d),
                    create("FOSI000002", "SAMP000001", 0, null),
                    create("FOSI000003", "SAMP000001", 0, 99d),
                    create("FOSI000004", "SAMP999999", 0, 1d),
                    create("FOSI000005", "SAMP000001", 1, 1d), //already existing
                    create("FOSI000006", "SAMP000001", 3, 2d), // update (possibly fail)
                    create("FOSI000007", "SAMP000002", 1, 1d), // insert into existing dataset
                    create("FOSI000008", "SAMP000002", 5, 2d), // insert into existing dataset
                    create("FOSI000009", "SAMP000003", 1, 1d), // insert into different analysis entity than existing.
                    create("FOSI000010", "SAMP000004", 2, 1d), // try to insert for malformed countsheet (no data type name match)
                    create("FOSI000011", "SAMP000005", 1, 1d), // insert into new dataset
                    create("FOSI000012", "SAMP000005", 2, 2d) // insert into new dataset
            );

    private static Fossil create(
            String fossilCode,
            String sampleCode,
            Integer abundance,
            Double code
    ){
        Fossil fossil = new Fossil();
        fossil.setFossilBugsCODE(fossilCode);
        fossil.setSampleCODE(sampleCode);
        fossil.setAbundance(abundance);
        fossil.setCode(code);
        return fossil;
    }
}
