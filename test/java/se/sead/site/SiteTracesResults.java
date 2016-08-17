package se.sead.site;

import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.bugsimport.tracing.seadmodel.BugsTraceType;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class SiteTracesResults {

    private boolean canCreateCountry;
    private boolean canUpdateSite;

    SiteTracesResults(boolean canCreateCountry, boolean canUpdateSite){
        this.canCreateCountry = canCreateCountry;
        this.canUpdateSite = canUpdateSite;
    }

    public void assertTracesForBugsData(BugsSite dataFromAccess, List<BugsTrace> traces, List<BugsError> errors){
        try {
            SiteBugsTraceAssertions traceAssertions = new SiteBugsTraceAssertions(traces);
            SiteBugsErrorAssertions errorAssertions = new SiteBugsErrorAssertions(errors);
            switch (dataFromAccess.getCode()) {
                case "SITE000006":
                    traceAssertions.assertPreinsertedSiteTrace();
                    break;
                case "SITE000008":
                case "SITE000009":
                case "SITE000020":
                    traceAssertions.assertNewSite();
                    break;
                case "SITE000010":
                    traceAssertions.assertPreinsertedSiteTrace();
                    if(canUpdateSite){
                        traceAssertions.assertUpdatedSite();
                    } else {
                        errorAssertions.assertUpdateError();
                    }
                    break;
                case "SITE000021":
                    errorAssertions.assertSiteExistsError();
                    break;
                case "SITE000022":
                    traceAssertions.assertPreinsertedSiteTrace();
                    errorAssertions.assertSiteChangedError();
                    break;
                case "SITE000023":
                    errorAssertions.assertCountryError();
                    break;
                case "SITE000024":
                    if(canCreateCountry){
                        traceAssertions.assertNewSite();
                    } else {
                        errorAssertions.assertCountryError();
                    }
                    break;
                case "SITE000025":
                    traceAssertions.assertPreinsertedSiteTrace();
                    if(canUpdateSite){
                        traceAssertions.assertUpdatedSite();
                    } else {
                        errorAssertions.assertUpdateError();
                    }
                    break;
                case "SITE000026":
                    errorAssertions.assertEmptyNameError();
                    break;
                case "SITE000027":
                    errorAssertions.assertNonUniqueSite();
                    break;
            }
            traceAssertions.assertNoMoreTraces();
            errorAssertions.assertNoMoreErrors();
        }catch (AssertionError ae){
            System.out.println(dataFromAccess.getCode());
            throw ae;
        }
    }

    private static class SiteBugsTraceAssertions {

        private Predicate<BugsTrace> preInsertedTestDataFilter
                = trace -> trace.getSeadTable().equals("tbl_sites") && trace.getType() == null;
        private Predicate<BugsTrace> insertedSiteFilter
                = trace -> trace.getSeadTable().equals("tbl_sites") && trace.getType() == BugsTraceType.INSERT;
        private Predicate<BugsTrace> updatedSiteFilter
                = trace -> trace.getSeadTable().equals("tbl_sites") && trace.getType() == BugsTraceType.UPDATE;
        private Predicate<BugsTrace> insertedSiteLocationFilter
                = trace -> trace.getSeadTable().equals("tbl_site_locations") && trace.getType() == BugsTraceType.INSERT;
        private Predicate<BugsTrace> deletedSiteLocationFilter
                = trace -> trace.getSeadTable().equals("tbl_site_locations") && trace.getType() == BugsTraceType.DELETE;
        private Predicate<BugsTrace> insertedLocationFilter
                = trace -> trace.getSeadTable().equals("tbl_locations") && trace.getType() == BugsTraceType.INSERT;

        private List<BugsTrace> traces;

        SiteBugsTraceAssertions(List<BugsTrace> traces){
            this.traces = traces;
        }

        void assertNoMoreTraces(){
            assertNumberOfResidualTraces(0);
        }

        private void assertNumberOfResidualTraces(int numberOfResidualTraces){
            assertEquals(numberOfResidualTraces, traces.size());
        }

        void assertNewSite(){
            assertAndRemoveNumberOfFound(insertedSiteFilter, 1);
        }

        private void assertAndRemoveNumberOfFound(Predicate<BugsTrace> filter, int expectedNumberOfItems){
            List<BugsTrace> foundFilteredTraces = getFiltered(filter);
            assertEquals(expectedNumberOfItems, foundFilteredTraces.size());
            traces.removeAll(foundFilteredTraces);
        }

        private List<BugsTrace> getFiltered(Predicate<BugsTrace> filterFunction){
            return traces.stream().filter(filterFunction).collect(Collectors.toList());
        }

        void assertInsertedSiteLocations(int expectedNumberOfInsertedSiteLocations){
            assertAndRemoveNumberOfFound(insertedSiteLocationFilter, expectedNumberOfInsertedSiteLocations);
        }

        void assertInsertedLocations(int expectedNumberOfInsertedSiteLocations){
            assertAndRemoveNumberOfFound(insertedLocationFilter, expectedNumberOfInsertedSiteLocations);
        }

        void assertUpdatedSite(){
            assertAndRemoveNumberOfFound(updatedSiteFilter, 1);
        }

        void assertDeleteSiteLocation(int expectedNumberOfDeletedSiteLocations){
            assertAndRemoveNumberOfFound(deletedSiteLocationFilter, expectedNumberOfDeletedSiteLocations);
        }

        void assertPreinsertedSiteTrace(){
            assertAndRemoveNumberOfFound(preInsertedTestDataFilter, 1);
        }

    }

    private static class SiteBugsErrorAssertions {

        private static final String EXPECTED_COUNTRY_ERROR = "No country exists for site";
        private static final String EXPECTED_UPDATE_ERRROR = "Bugs data is updated but updates are disallowed.";
        private static final String EXPECTED_SITE_EXISTS_IN_NON_IMPORTED_MATERIAL_ERROR = "Site name exists for non-imported site";
        private static final String EXPECTED_SITE_HAS_BEEN_CHANGED_IN_SEAD = "Sead data has been updated since last bugs import";
        private static final String EXPECTED_EMPTY_NAME_ERROR = "Site name cannot be empty";
        private static final String EXPECTED_MULTIPLE_SITES_EXISTS_FOR_NAME = "More than one site found by name and location";

        private List<BugsError> errors;

        SiteBugsErrorAssertions(List<BugsError> errors){
            this.errors = errors;
        }

        void assertCountryError(){
            assertAndRemoveContainingString(EXPECTED_COUNTRY_ERROR);
        }

        private void assertAndRemoveContainingString(String errorsText){
            List<BugsError> foundErrors = errors.stream().filter(error -> errorsText.equals(error.getMessage())).collect(Collectors.toList());
            assertFalse(foundErrors.isEmpty());
            errors.removeAll(foundErrors);
        }

        void assertUpdateError(){
            assertAndRemoveContainingString(EXPECTED_UPDATE_ERRROR);
        }

        void assertNoMoreErrors(){
            assertTrue(errors.isEmpty());
        }

        void assertSiteExistsError(){
            assertAndRemoveContainingString(EXPECTED_SITE_EXISTS_IN_NON_IMPORTED_MATERIAL_ERROR);
        }

        void assertSiteChangedError(){
            assertAndRemoveContainingString(EXPECTED_SITE_HAS_BEEN_CHANGED_IN_SEAD);
        }

        public void assertEmptyNameError() {
            assertAndRemoveContainingString(EXPECTED_EMPTY_NAME_ERROR);
        }

        public void assertNonUniqueSite() {
            assertAndRemoveContainingString(EXPECTED_MULTIPLE_SITES_EXISTS_FOR_NAME);
        }
    }
}
