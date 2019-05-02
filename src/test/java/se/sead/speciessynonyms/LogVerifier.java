package se.sead.speciessynonyms;

import se.sead.bugsimport.speciessynonyms.bugsmodel.Synonym;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

import static org.junit.Assert.assertEquals;

class LogVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<Synonym> {

    private AssertHelper assertAssociationHelper;
    private AssertHelper assertSpeciesHelper;
    private AssertHelper assertAuthorityHelper;
    private AssertHelper assertGenusHelper;

    LogVerifier(){
        assertAssociationHelper = new AssertHelper("tbl_species_associations");
        assertSpeciesHelper = new AssertHelper("tbl_taxa_tree_master");
        assertAuthorityHelper = new AssertHelper("tbl_taxa_tree_authors");
        assertGenusHelper = new AssertHelper("tbl_taxa_tree_genera");
    }

    private int synAuthorCreated = 0;

    @Override
    public void verifyLogData(Synonym bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch (bugsData.compressToString()){
            case "{1.0,Off genus,syn species,A stored authority,Ref 1,exists}":
                assertAssociationHelper.assertSize(traces, 1);
                assertAssociationHelper.assertPrestoredTrace(traces, 1);
                assertAssociationHelper.assertEmpty(errors);
                break;
            case "{1.0,Off genus,alt 2 name,syn authority,null,No reference}":
                assertAuthorInserted(traces);
                assertAssociationHelper.assertInserts(traces, 1);
                assertSpeciesHelper.assertInserts(traces, 1);
                assertAssociationHelper.assertEmpty(errors);
                break;
            case "{3.0,Syn genus 2,null,syn authority,Ref 1,no alt species}":
                assertAuthorInserted(traces);
                assertAssociationHelper.assertInserts(traces, 1);
                assertSpeciesHelper.assertInserts(traces, 1);
                assertGenusHelper.assertInserts(traces, 1);
                assertAssociationHelper.assertEmpty(errors);
                break;
            case "{2.0,Off genus,alt species name,syn authority,Ref 1,new item}":
                assertAuthorInserted(traces);
                assertAssociationHelper.assertInserts(traces, 1);
                assertSpeciesHelper.assertInserts(traces, 1);
                assertAssociationHelper.assertEmpty(errors);
                break;
            case "{3.0,Off genus,alt 3 name,null,Ref 1,no authority}":
                assertAssociationHelper.assertSize(traces, 2);
                assertAssociationHelper.assertInserts(traces, 1);
                assertSpeciesHelper.assertInserts(traces, 1);
                assertAssociationHelper.assertEmpty(errors);
                break;
            case "{3.0,null,alt name,A stored authority,Ref 1,no genus}":
                assertAssociationHelper.assertEmpty(traces);
                assertAssociationHelper.assertContainsError(errors, "No synonym genus specified");
                break;
            case "{3.0,Off genus,alt name,A stored authority,Ref 2,no reference found}":
                assertAssociationHelper.assertEmpty(traces);
                assertAssociationHelper.assertContainsError(errors, "No reference found");
                break;
        }
    }

    private void assertAuthorInserted(List<BugsTrace> traces) {
        if(traces.stream().anyMatch(trace -> trace.getSeadTable().equals("tbl_taxa_tree_authors"))){
            synAuthorCreated++;
        }
    }

    void assertOneAuthorCreated(){
        assertEquals(1, synAuthorCreated);
    }
}
