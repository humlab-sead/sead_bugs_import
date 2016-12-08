package se.sead.datescalendar;

import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestRelativeAge;
import se.sead.repositories.RelativeAgeRepository;
import se.sead.repositories.RelativeAgeTypeRepository;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.utils.BigDecimalDefinition;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class RelativeAgesDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<RelativeAge> {

    private RelativeAgeTypeRepository typeRepository;
    private RelativeAgeRepository ageRepository;

    RelativeAgesDatabaseContentProvider(RelativeAgeTypeRepository typeRepository, RelativeAgeRepository ageRepository) {
        this.typeRepository = typeRepository;
        this.ageRepository = ageRepository;
    }

    @Override
    public List<RelativeAge> getExpectedData() {
        return Arrays.asList(
                TestRelativeAge.create(
                        1,
                        "CAL_100_AD",
                        null,
                        null,
                        null,
                        RelativeDatesDatabaseContentProvider.createSeadValue(1850),
                        RelativeDatesDatabaseContentProvider.createSeadValue(1850),
                        "Autocreated from bugs import",
                        typeRepository.findOne(2),
                        null
                ),
                TestRelativeAge.create(
                        2,
                        "CAL_120_AD",
                        null,
                        null,
                        null,
                        RelativeDatesDatabaseContentProvider.createSeadValue(1830),
                        RelativeDatesDatabaseContentProvider.createSeadValue(1830),
                        "Autocreated from bugs import",
                        typeRepository.findOne(2),
                        null
                ),
                TestRelativeAge.create(
                        null,
                        "CAL_100_BC",
                        null,
                        null,
                        null,
                        RelativeDatesDatabaseContentProvider.createSeadValue(2050),
                        RelativeDatesDatabaseContentProvider.createSeadValue(2050),
                        "Autocreated from bugs import",
                        typeRepository.findOne(2),
                        null
                ),
                TestRelativeAge.create(
                        null,
                        "CAL_100_BP",
                        null,
                        RelativeDatesDatabaseContentProvider.createSeadValue(100),
                        RelativeDatesDatabaseContentProvider.createSeadValue(100),
                        null,
                        null,
                        "Autocreated from bugs import",
                        typeRepository.findOne(2),
                        null
                ),
                TestRelativeAge.create(
                        null,
                        "CAL_100-200_AD",
                        null,
                        null,
                        null,
                        RelativeDatesDatabaseContentProvider.createSeadValue(1850),
                        RelativeDatesDatabaseContentProvider.createSeadValue(1750),
                        "Autocreated from bugs import",
                        typeRepository.findOne(3),
                        null
                )
        );
    }

    @Override
    public List<RelativeAge> getActualData() {
        return ageRepository.findAll();
    }

    @Override
    public Comparator<RelativeAge> getSorter() {
        return new RelativeAgeComparator();
    }

    @Override
    public TestEqualityHelper<RelativeAge> getEqualityHelper() {
        return new TestEqualityHelper<>(true);
    }

    @Override
    public boolean useEqualityHelper(RelativeAge expected, RelativeAge actual) {
        return true;
    }

    private static class RelativeAgeComparator implements Comparator<RelativeAge> {
        @Override
        public int compare(RelativeAge o1, RelativeAge o2) {
            return o1.getAbbreviation().compareTo(o2.getAbbreviation());
        }
    }
}
