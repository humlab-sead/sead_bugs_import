package se.sead.species;

import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

class LogVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<INDEX> {

    private AssertHelper taxonomicOrderAssertHelper;
    private AssertHelper speciesAssertHelper;
    private AssertHelper genusAssertHelper;
    private AssertHelper authorityAssertHelper;
    private AssertHelper familyAssertHelper;

    LogVerifier(){
        taxonomicOrderAssertHelper = new AssertHelper("tbl_taxonomic_order");
        speciesAssertHelper = new AssertHelper("tbl_taxa_tree_master");
        genusAssertHelper = new AssertHelper("tbl_taxa_tree_genera");
        authorityAssertHelper = new AssertHelper("tbl_taxa_tree_authors");
        familyAssertHelper = new AssertHelper("tbl_taxa_tree_families");
    }

    @Override
    public void verifyLogData(INDEX bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getCode().toString()){
            case "1.0":
                taxonomicOrderAssertHelper.assertEmpty(errors);
                break;
            case "2.0":
            case "3.0":
                taxonomicOrderAssertHelper.assertSize(traces,1);
                taxonomicOrderAssertHelper.assertInserts(traces, 1);
                speciesAssertHelper.assertInserts(traces, 0);
                taxonomicOrderAssertHelper.assertEmpty(errors);
                break;
            case "4.0":
                taxonomicOrderAssertHelper.assertInserts(traces, 1);
                speciesAssertHelper.assertInserts(traces, 1);
                genusAssertHelper.assertInserts(traces, 1);
                taxonomicOrderAssertHelper.assertSize(traces, 3);
                taxonomicOrderAssertHelper.assertEmpty(errors);
                break;
            case "5.0":
                taxonomicOrderAssertHelper.assertInserts(traces, 1);
                speciesAssertHelper.assertInserts(traces, 1);
                genusAssertHelper.assertInserts(traces, 1);
                authorityAssertHelper.assertInserts(traces, 1);
                taxonomicOrderAssertHelper.assertSize(traces, 4);
                taxonomicOrderAssertHelper.assertEmpty(errors);
                break;
            case "8.0":
                taxonomicOrderAssertHelper.assertInserts(traces, 1);
                speciesAssertHelper.assertInserts(traces, 1);
                taxonomicOrderAssertHelper.assertSize(traces, 2);
                taxonomicOrderAssertHelper.assertEmpty(errors);
                break;
            case "9.0":
                taxonomicOrderAssertHelper.assertInserts(traces, 1);
                speciesAssertHelper.assertInserts(traces, 1);
                genusAssertHelper.assertInserts(traces, 1);
                familyAssertHelper.assertInserts(traces, 1);
                taxonomicOrderAssertHelper.assertSize(traces, 4);
                taxonomicOrderAssertHelper.assertEmpty(errors);
                break;
            case "10.0":
                taxonomicOrderAssertHelper.assertEmpty(traces);
                taxonomicOrderAssertHelper.assertContainsError(errors, "No Species name provided");
                break;
        }
    }
}
