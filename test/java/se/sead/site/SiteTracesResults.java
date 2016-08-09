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
            SiteBugsErrorAssertions errorAssertions = new SiteBugsErrorAssertions(errors, dataFromAccess);
            switch (dataFromAccess.getCode()) {
                case "SITE000006":
                    if(canUpdateSite && canCreateCountry){
                        traceAssertions.assertUpdatedSite();
                        traceAssertions.assertInsertedSiteLocations(1);
                        traceAssertions.assertInsertedLocations(1);
                    } else if(!canUpdateSite && !canCreateCountry){
                        errorAssertions.assertCountryError();
                    } else if(!canUpdateSite){
                        errorAssertions.assertUpdateError();
                    } else if(!canCreateCountry){
                        errorAssertions.assertCountryError();
                    }
                    break;
                case "SITE000008":
                    traceAssertions.assertNoMoreTraces();
                    break;
                case "SITE000009":
                    if (canUpdateSite) {
                        traceAssertions.assertUpdatedSite();
                    } else {
                        errorAssertions.assertUpdateError();
                    }
                    break;
                case "SITE000010":
                    if (canCreateCountry) {
                        traceAssertions.assertNewSite();
                        traceAssertions.assertInsertedSiteLocations(2);
                        traceAssertions.assertInsertedLocations(1);
                    } else {
                        errorAssertions.assertCountryError();
                    }
                    break;
                case "SITE000011":
                case "SITE000012":
                case "SITE000013":
                case "SITE000020":
                case "SITE001274":
                    traceAssertions.assertNewSite();
                    traceAssertions.assertInsertedSiteLocations(2);
                    break;
                case "SITE000017":
                    if (canCreateCountry) {
                        traceAssertions.assertNewSite();
                        traceAssertions.assertInsertedSiteLocations(2);
                        traceAssertions.assertInsertedLocations(2);
                    } else {
                        errorAssertions.assertCountryError();
                    }
                    break;
                case "SITE000018":
                case "SITE000019":
                    traceAssertions.assertNewSite();
                    traceAssertions.assertInsertedSiteLocations(2);
                    traceAssertions.assertInsertedLocations(1);
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

        private Predicate<BugsTrace> insertedSiteFilter
                = trace -> trace.getSeadTable().equals("tbl_sites") && trace.getType() == BugsTraceType.INSERT;
        private Predicate<BugsTrace> updatedSiteFilter
                = trace -> trace.getSeadTable().equals("tbl_sites") && trace.getType() == BugsTraceType.UPDATE;
        private Predicate<BugsTrace> insertedSiteLocationFilter
                = trace -> trace.getSeadTable().equals("tbl_site_locations") && trace.getType() == BugsTraceType.INSERT;
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

    }

    private static class SiteBugsErrorAssertions {

        private static final String EXPECTED_COUNTRY_ERROR = "No country exists for site %s";
        private static final String EXPECTED_UPDATE_ERRROR = "Bugs data is updated but updates are disallowed.";

        private List<BugsError> errors;
        private BugsSite errorSite;

        SiteBugsErrorAssertions(List<BugsError> errors, BugsSite errorSite){
            this.errors = errors;
            this.errorSite = errorSite;
        }

        void assertCountryError(){
            assertAndRemoveContainingString(String.format(EXPECTED_COUNTRY_ERROR, errorSite.getCode()));
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
    }
}
