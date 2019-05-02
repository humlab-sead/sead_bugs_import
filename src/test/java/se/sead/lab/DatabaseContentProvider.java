package se.sead.lab;

import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.model.TestDatingLab;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.DatingLabRepository;
import se.sead.repositories.LocationRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<DatingLab>{

    private LocationRepository locationRepository;
    private DatingLabRepository datingLabRepository;

    DatabaseContentProvider(LocationRepository locationRepository, DatingLabRepository datingLabRepository) {
        this.locationRepository = locationRepository;
        this.datingLabRepository = datingLabRepository;
    }

    @Override
    public List<DatingLab> getExpectedData() {
        return Arrays.asList(
                TestDatingLab.create(1, "A", "Arizona", locationRepository.findOne(1)),
                TestDatingLab.create(2, "B", "Bern", locationRepository.findOne(3)),
                TestDatingLab.create(null, "BC*", "Brooklyn College", locationRepository.findOne(1)),
                TestDatingLab.create(3, "Test 3", "Update", locationRepository.findOne(1)),
                TestDatingLab.create(4, "Test 4", "Updated country", locationRepository.findOne(2))
        );
    }

    @Override
    public List<DatingLab> getActualData() {
        return datingLabRepository.findAll();
    }

    @Override
    public Comparator<DatingLab> getSorter() {
        return new DatingLabComparator();
    }

    @Override
    public TestEqualityHelper<DatingLab> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class DatingLabComparator implements Comparator<DatingLab> {
        @Override
        public int compare(DatingLab o1, DatingLab o2) {
            return o1.getLabId().compareTo(o2.getLabId());
        }
    }
}
