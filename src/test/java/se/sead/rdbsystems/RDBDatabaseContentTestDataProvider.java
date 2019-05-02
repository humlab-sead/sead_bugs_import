package se.sead.rdbsystems;

import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestRdbSystem;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.RDBSystemRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RDBDatabaseContentTestDataProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<RdbSystem> {

    private RDBSystemRepository systemRepository;
    private BiblioDataRepository bibliographyRepository;
    private LocationRepository locationRepository;

    RDBDatabaseContentTestDataProvider(RDBSystemRepository systemRepository, BiblioDataRepository bibliographyRepository, LocationRepository locationRepository){
        this.systemRepository = systemRepository;
        this.bibliographyRepository = bibliographyRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<RdbSystem> getExpectedData() {
        return new ExpectedDataBuilder(bibliographyRepository, locationRepository).getExpectedData();
    }

    @Override
    public List<RdbSystem> getActualData() {
        return systemRepository.findAll();
    }

    @Override
    public Comparator<RdbSystem> getSorter() {
        return new RdbSystemComparator();
    }

    @Override
    public TestEqualityHelper<RdbSystem> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class ExpectedDataBuilder {

        private BiblioDataRepository bibliographyRepository;
        private LocationRepository locationRepository;

        public ExpectedDataBuilder(BiblioDataRepository bibliographyRepository, LocationRepository locationRepository) {
            this.bibliographyRepository = bibliographyRepository;
            this.locationRepository = locationRepository;
        }

        List<RdbSystem> getExpectedData(){
            return Arrays.asList(
                    TestRdbSystem.create(1, "UKRDB", null, null, null, bibliographyRepository.findOne(1), locationRepository.findOne(1)),
                    TestRdbSystem.create(2, "TestUpdate", "3.1.1", 1999, Short.valueOf("1894"), bibliographyRepository.findOne(5), locationRepository.findOne(2)),
                    TestRdbSystem.create(null, "IUCN", "3.1", 2001, Short.valueOf("1994"), bibliographyRepository.findOne(2), locationRepository.findOne(2)),
                    TestRdbSystem.create(null, "IUCN", null, 2000, Short.valueOf("1994"), bibliographyRepository.findOne(3), locationRepository.findOne(3)),
                    TestRdbSystem.create(null, "TestInsert", "1", null, null, bibliographyRepository.findOne(4), locationRepository.findOne(3)),
                    TestRdbSystem.create(3, "OldUpdate", "2", 2000, Short.valueOf("2000"), bibliographyRepository.findOne(3), locationRepository.findOne(3))
            );
        }
    }

    private static class RdbSystemComparator implements Comparator<RdbSystem> {
        @Override
        public int compare(RdbSystem o1, RdbSystem o2) {
            int difference = o1.getSystemName().compareTo(o2.getSystemName());
            if(difference == 0){
                difference = o1.getSystemDate().compareTo(o2.getSystemDate());
            }
            return difference;
        }
    }
}
