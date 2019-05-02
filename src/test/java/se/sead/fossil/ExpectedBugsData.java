package se.sead.fossil;

import se.sead.bugsimport.fossil.bugsmodel.Fossil;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<Fossil> EXPECTED_DATA =
            Arrays.asList(
                    create("FOSI000001", 1d, "SAMP000001", 1),
                    create("FOSI000002", 1d, "SAMP000002", 1),
                    create("FOSI000003", 2d, "SAMP000002", 1),
                    create("FOSI000004", 1d, "SAMP000003", 1),
                    create("FOSI000005", 2d, "SAMP000003", 1),
                    create("FOSI000006", 1d, "SAMP000004", 1),
                    create("FOSI000007", 2d, "SAMP000004", 1),
                    create("FOSI000008", 3d, "SAMP000004", 1),
                    create("ERRO000001", null, "SAMP000001", 0),
                    create("ERRO000002", 1d, null, 1),
                    create("ERRO000003", 99d, "SAMP000001", 1)
            );

    private static Fossil create(
            String fossilCode,
            Double code, String sampleCode,
            Integer abundance
    ){
        Fossil fossil = new Fossil();
        fossil.setFossilBugsCODE(fossilCode);
        fossil.setSampleCODE(sampleCode);
        fossil.setAbundance(abundance);
        fossil.setCode(code);
        return fossil;
    }
}
