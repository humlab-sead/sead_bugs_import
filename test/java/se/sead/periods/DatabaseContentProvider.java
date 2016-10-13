package se.sead.periods;

import junit.framework.Test;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.periods.seadmodel.RelativeAgeType;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestRelativeAge;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.RelativeAgeRepository;
import se.sead.repositories.RelativeAgeTypeRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<RelativeAge> {

    private static final MathContext SEAD_RELATIVE_AGE_VALUE_CONTEXT = new MathContext(20, RoundingMode.HALF_DOWN);
    private static final int RELATIVE_AGE_SCALE = 5;

    private RelativeAgeType geologicalType;
    private RelativeAgeType archaeologicalType;
    private Location ukLocation;
    private Location easterMediterraneanLocation;

    private RelativeAgeRepository relativeAgeRepository;

    public DatabaseContentProvider(
            RelativeAgeRepository relativeAgeRepository,
            RelativeAgeTypeRepository relativeAgeTypeRepository,
            LocationRepository locationRepository){
        ukLocation = locationRepository.findOne(1);
        easterMediterraneanLocation = locationRepository.findOne(2);
        geologicalType = relativeAgeTypeRepository.findOne(1);
        archaeologicalType = relativeAgeTypeRepository.findOne(2);
        this.relativeAgeRepository = relativeAgeRepository;
    }

    @Override
    public List<RelativeAge> getExpectedData() {
        return Arrays.asList(
                TestRelativeAge.create(
                        1,
                        "EXIST",
                        "Existing",
                        createRelativeAgeBigDecimal(478000),
                        createRelativeAgeBigDecimal(423000),
                        null,
                        null,
                        "Previously inserted",
                        geologicalType,
                        ukLocation
                        ),
                TestRelativeAge.create(
                        2,
                        "U_NEWER_SEAD",
                        "Sead data updated",
                        createRelativeAgeBigDecimal(0),
                        createRelativeAgeBigDecimal(1222),
                        null,
                        null,
                        "This data has changed since import",
                        geologicalType,
                        null
                ),
                TestRelativeAge.create(
                        null,
                        "INSERT",
                        "New",
                        createRelativeAgeBigDecimal(800),
                        createRelativeAgeBigDecimal(1100),
                        null,
                        null,
                        "BP insert",
                        archaeologicalType,
                        ukLocation
                ),
                TestRelativeAge.create(
                        null,
                        "INSERTCAL",
                        "New calendar",
                        null,
                        null,
                        createRelativeAgeBigDecimal(-550),
                        createRelativeAgeBigDecimal(1350),
                        "Calendar insert",
                        archaeologicalType,
                        ukLocation
                ),
                TestRelativeAge.create(
                        null,
                        "NO_BP_ETC",
                        "No age type",
                        createRelativeAgeBigDecimal(0),
                        createRelativeAgeBigDecimal(0),
                        null,
                        null,
                        "No BP etc specified",
                        geologicalType,
                        ukLocation
                ),
                TestRelativeAge.create(
                        3,
                        "UPDATE",
                        "Updated",
                        createRelativeAgeBigDecimal(666),
                        createRelativeAgeBigDecimal(1918),
                        null,
                        null,
                        "Update",
                        archaeologicalType,
                        easterMediterraneanLocation
                )
        );
    }

    private BigDecimal createRelativeAgeBigDecimal(Integer value){
        BigDecimal bdValue = new BigDecimal(value, SEAD_RELATIVE_AGE_VALUE_CONTEXT);
        bdValue.setScale(RELATIVE_AGE_SCALE);
        return bdValue;
    }

    @Override
    public List<RelativeAge> getActualData() {
        return relativeAgeRepository.findAll();
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
