package se.sead.rdb;

import se.sead.bugsimport.rdb.seadmodel.Rdb;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestRdb;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.RdbCodeRepository;
import se.sead.repositories.RdbRepository;
import se.sead.repositories.SpeciesRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RdbTestDataContent implements DatabaseContentVerification.DatabaseContentTestDataProvider<Rdb> {


    private LocationRepository locationRepository;
    private SpeciesRepository speciesRepository;
    private RdbCodeRepository rdbCodeRepository;
    private RdbRepository rdbRepository;

    RdbTestDataContent(LocationRepository locationRepository, SpeciesRepository speciesRepository, RdbCodeRepository rdbCodeRepository, RdbRepository rdbRepository) {
        this.locationRepository = locationRepository;
        this.speciesRepository = speciesRepository;
        this.rdbCodeRepository = rdbCodeRepository;
        this.rdbRepository = rdbRepository;
    }

    @Override
    public List<Rdb> getExpectedData() {
        return Arrays.asList(
                TestRdb.create(1, speciesRepository.findOne(1), locationRepository.findOne(1), rdbCodeRepository.findOne(1)),
                TestRdb.create(3, speciesRepository.findOne(3), locationRepository.findOne(1), rdbCodeRepository.findOne(1)),
                TestRdb.create(4, speciesRepository.findOne(2), locationRepository.findOne(2), rdbCodeRepository.findOne(2))
        );
    }

    @Override
    public List<Rdb> getActualData() {
        return rdbRepository.findAll();
    }

    @Override
    public Comparator<Rdb> getSorter() {
        return new RdbComparator();
    }

    @Override
    public TestEqualityHelper<Rdb> getEqualityHelper() {
        return new TestEqualityHelper<>(true);
    }

    private static class RdbComparator implements Comparator<Rdb> {
        @Override
        public int compare(Rdb o1, Rdb o2) {
            if(o1.getId() != null && o2.getId() != null){
                return o1.getId().compareTo(o2.getId());
            } else {
                return o1.getSpecies().getId().compareTo(o2.getSpecies().getId());
            }
        }
    }
}
