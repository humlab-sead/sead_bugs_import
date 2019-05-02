package se.sead.sitelocations;

import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.SiteLocationRepository;
import se.sead.repositories.SiteRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class SiteLocationContentVerificationDataProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<SiteLocation> {

    private boolean canCreateCountry;
    private ImportTestDefinition testDefinition;
    private SiteRepository siteRepository;
    private SiteLocationRepository siteLocationRepository;

    public SiteLocationContentVerificationDataProvider(boolean canCreateCountry, ImportTestDefinition testDefinition, SiteRepository siteRepository, SiteLocationRepository siteLocationRepository) {
        this.canCreateCountry = canCreateCountry;
        this.testDefinition = testDefinition;
        this.siteRepository = siteRepository;
        this.siteLocationRepository = siteLocationRepository;
    }

    @Override
    public List<SiteLocation> getExpectedData() {
        return testDefinition.getExpectedData(canCreateCountry);
    }

    @Override
    public List<SiteLocation> getActualData() {
        List<SiteLocation> allStored = new ArrayList<>();
        for (SeadSite site :
                siteRepository.findAllByNameStartingWithIgnoreCase("")) {
            allStored.addAll(siteLocationRepository.findBySite(site));
        }
        return allStored;
    }

    @Override
    public Comparator<SiteLocation> getSorter() {
        return new SiteLocationComparator();
    }

    @Override
    public TestEqualityHelper<SiteLocation> getEqualityHelper() {
        TestEqualityHelper.ClassMethodInformation siteMethodInformation =
                new TestEqualityHelper.ClassMethodInformation(SeadSite.class, "getSiteLocations");
        TestEqualityHelper<SiteLocation> equalityHelper = new TestEqualityHelper<>(true);
        equalityHelper.addMethodInformation(siteMethodInformation);
        return equalityHelper;
    }

    private static class SiteLocationComparator implements Comparator<SiteLocation> {
        @Override
        public int compare(SiteLocation o1, SiteLocation o2) {
            if(o1.getSite().equals(o2.getSite())){
                return o1.getLocation().compareTo(o2.getLocation());
            } else {
                return o1.getSite().compareTo(o2.getSite());
            }
        }
    }
}
