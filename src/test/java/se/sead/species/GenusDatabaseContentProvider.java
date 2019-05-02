package se.sead.species;

import se.sead.bugsimport.species.seadmodel.TaxaGenus;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestTaxaFamily;
import se.sead.model.TestTaxaGenus;
import se.sead.repositories.TaxaFamilyRepository;
import se.sead.repositories.TaxaGenusRepository;
import se.sead.repositories.TaxaOrderRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class GenusDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<TaxaGenus> {

    private TaxaFamilyRepository familyRepository;
    private TaxaGenusRepository genusRepository;
    private TaxaOrderRepository orderRepository;

    public GenusDatabaseContentProvider(TaxaFamilyRepository familyRepository, TaxaGenusRepository genusRepository, TaxaOrderRepository orderRepository) {
        this.familyRepository = familyRepository;
        this.genusRepository = genusRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<TaxaGenus> getExpectedData() {
        return Arrays.asList(
                TestTaxaGenus.create(
                        1,
                        "Genus",
                        familyRepository.findOne(1)
                ),
                TestTaxaGenus.create(
                        2,
                        "No data",
                        familyRepository.findOne(2)
                ),
                TestTaxaGenus.create(
                        3,
                        "No data",
                        familyRepository.findOne(3)
                ),
                TestTaxaGenus.create(
                        null,
                        "NewGenus",
                        familyRepository.findOne(1)
                ),
                TestTaxaGenus.create(
                        null,
                        "SomeGenus",
                        TestTaxaFamily.create(
                                null,
                                "NewFamily",
                                orderRepository.findOne(1)
                        )
                )
        );
    }

    @Override
    public List<TaxaGenus> getActualData() {
        return genusRepository.findAll();
    }

    @Override
    public Comparator<TaxaGenus> getSorter() {
        return new GenusComparator();
    }

    @Override
    public TestEqualityHelper<TaxaGenus> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class GenusComparator implements Comparator<TaxaGenus> {
        @Override
        public int compare(TaxaGenus o1, TaxaGenus o2) {
            int difference = o1.getGenusName().compareTo(o2.getGenusName());
            if(difference == 0){
                difference = o1.getFamily().getFamilyName().compareTo(o2.getFamily().getFamilyName());
                if(difference == 0){
                    difference = o1.getFamily().getOrder().getOrderName().compareTo(
                            o2.getFamily().getOrder().getOrderName()
                    );
                }
            }
            return difference;
        }
    }
}
