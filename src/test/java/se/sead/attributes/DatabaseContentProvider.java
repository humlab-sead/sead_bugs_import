package se.sead.attributes;

import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestTaxaMeasuredAttributes;
import se.sead.repositories.MeasuredAttributesRepository;
import se.sead.repositories.SpeciesRepository;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.utils.BigDecimalDefinition;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<TaxaMeasuredAttributes> {

    private static final BigDecimal ONE = BigDecimalDefinition.convertToSeadCode(1d);
    private static final BigDecimal TWO = BigDecimalDefinition.convertToSeadCode(2d);

    private SpeciesRepository speciesRepository;
    private MeasuredAttributesRepository measuredAttributesRepository;

    DatabaseContentProvider(SpeciesRepository speciesRepository, MeasuredAttributesRepository measuredAttributesRepository) {
        this.speciesRepository = speciesRepository;
        this.measuredAttributesRepository = measuredAttributesRepository;
    }

    @Override
    public List<TaxaMeasuredAttributes> getExpectedData() {
        return Arrays.asList(
                TestTaxaMeasuredAttributes.create(1, "Stored", "Max", ONE, "mm", speciesRepository.findOne(1)),
                TestTaxaMeasuredAttributes.create(2, "Stored", "Min", ONE, "mm", speciesRepository.findOne(1)),
                TestTaxaMeasuredAttributes.create(3, "Stored", "Max", ONE, "mm", speciesRepository.findOne(2)),
                TestTaxaMeasuredAttributes.create(null, "New", "Min", ONE, "mm", speciesRepository.findOne(2)),
                TestTaxaMeasuredAttributes.create(null, "New", "Max", ONE, "mm", speciesRepository.findOne(3)),
                TestTaxaMeasuredAttributes.create(null, "New", "Min", ONE, "mm", speciesRepository.findOne(3)),
                TestTaxaMeasuredAttributes.create(4, "Stored w/o trace", "Max", ONE, "mm", speciesRepository.findOne(4)),
                TestTaxaMeasuredAttributes.create(5, "Stored w/o trace", "Min", ONE, "mm", speciesRepository.findOne(4)),
                TestTaxaMeasuredAttributes.create(6, "Changed", "Max", TWO, "mm", speciesRepository.findOne(5)),
                TestTaxaMeasuredAttributes.create(7, "Changed", "Min", TWO, "mm", speciesRepository.findOne(5)),
                TestTaxaMeasuredAttributes.create(8, "Stored", "Max", ONE, "mm", speciesRepository.findOne(6)),
                TestTaxaMeasuredAttributes.create(9, "Changed", "Min", TWO, "mm", speciesRepository.findOne(6)),
                TestTaxaMeasuredAttributes.create(10, "Changed after import", "Max", ONE, "mm", speciesRepository.findOne(7)),
                TestTaxaMeasuredAttributes.create(11, "Stored", "Min", ONE, "mm", speciesRepository.findOne(7))

        );
    }

    @Override
    public List<TaxaMeasuredAttributes> getActualData() {
        return measuredAttributesRepository.findAll();
    }

    @Override
    public Comparator<TaxaMeasuredAttributes> getSorter() {
        return new TaxaMeasuredAttributesComparator();
    }

    @Override
    public TestEqualityHelper<TaxaMeasuredAttributes> getEqualityHelper() {
        return new TestEqualityHelper<>(true);
    }

    private static class TaxaMeasuredAttributesComparator implements Comparator<TaxaMeasuredAttributes> {
        @Override
        public int compare(TaxaMeasuredAttributes o1, TaxaMeasuredAttributes o2) {
            int difference = o1.getSpecies().getId().compareTo(o2.getSpecies().getId());
            if(difference == 0){
                difference = o1.getType().compareTo(o2.getType());
                if(difference == 0){
                    return o1.getMeasure().compareTo(o2.getMeasure());
                }
            }
            return difference;
        }
    }
}
