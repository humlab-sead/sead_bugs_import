package se.sead.rdbcodes;

import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestRdbCode;
import se.sead.repositories.RDBSystemRepository;
import se.sead.repositories.RdbCodeRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RDBCodesContentTestDataProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<RdbCode> {

    private RdbCodeRepository rdbCodeRepository;
    private RDBSystemRepository rdbSystemRepository;

    public RDBCodesContentTestDataProvider(RdbCodeRepository rdbCodeRepository, RDBSystemRepository rdbSystemRepository) {
        this.rdbCodeRepository = rdbCodeRepository;
        this.rdbSystemRepository = rdbSystemRepository;
    }

    @Override
    public List<RdbCode> getExpectedData() {
        return Arrays.asList(
                TestRdbCode.create(1, "AB", "already stored w. trace", rdbSystemRepository.findOne(1)),
                TestRdbCode.create(2, "AC", "already added w/o import", rdbSystemRepository.findOne(1)),
                TestRdbCode.create(3, "AD", "update", rdbSystemRepository.findOne(1)),
                TestRdbCode.create(4, "E2", "sead changes made", rdbSystemRepository.findOne(1)),
                TestRdbCode.create(null, "A", "normal insert", rdbSystemRepository.findOne(1))
        );
    }

    @Override
    public List<RdbCode> getActualData() {
        return rdbCodeRepository.findAll();
    }

    @Override
    public Comparator<RdbCode> getSorter() {
        return new RdbCodeComparator();
    }

    @Override
    public TestEqualityHelper<RdbCode> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class RdbCodeComparator implements Comparator<RdbCode>{
        @Override
        public int compare(RdbCode o1, RdbCode o2) {
            return o1.getCategory().compareTo(o2.getCategory());
        }
    }
}
