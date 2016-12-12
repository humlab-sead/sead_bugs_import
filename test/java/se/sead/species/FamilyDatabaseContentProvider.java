package se.sead.species;

import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaOrder;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestTaxaFamily;
import se.sead.repositories.TaxaFamilyRepository;
import se.sead.repositories.TaxaOrderRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FamilyDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<TaxaFamily> {

    private TaxaFamilyRepository repository;
    private TaxaOrder bugsOrder;
    private TaxaOrder otherOrder;

    FamilyDatabaseContentProvider(TaxaFamilyRepository repository, TaxaOrderRepository orderRepository){
        this.repository = repository;
        this.bugsOrder = orderRepository.getImportOrder();
        this.otherOrder = orderRepository.findOne(2);
    }

    @Override
    public List<TaxaFamily> getExpectedData() {
        return Arrays.asList(
                TestTaxaFamily.create(
                        1,
                        "Family",
                        bugsOrder
                ),
                TestTaxaFamily.create(
                        2,
                        "No data",
                        bugsOrder
                ),
                TestTaxaFamily.create(
                        3,
                        "No data",
                        otherOrder
                ),
                TestTaxaFamily.create(
                        null,
                        "NewFamily",
                        bugsOrder
                )
        );
    }

    @Override
    public List<TaxaFamily> getActualData() {
        return repository.findAll();
    }

    @Override
    public Comparator<TaxaFamily> getSorter() {
        return new TaxaFamilyComparator();
    }

    @Override
    public TestEqualityHelper<TaxaFamily> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class TaxaFamilyComparator implements Comparator<TaxaFamily> {
        @Override
        public int compare(TaxaFamily o1, TaxaFamily o2) {
            return o1.getFamilyName().compareTo(o2.getFamilyName());
        }
    }
}
