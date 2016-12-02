package se.sead.species;

import se.sead.bugsimport.species.seadmodel.*;
import se.sead.model.*;
import se.sead.repositories.*;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.utils.BigDecimalDefinition;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<TaxonomicOrder> {

    private SpeciesRepository speciesRepository;
    private TaxaGenusRepository genusRepository;
    private TaxaFamilyRepository familyRepository;
    private TaxaOrderRepository orderRepository;
    private TaxonomicOrderRepository taxonomicOrderRepository;

    private TaxonomicOrderSystem bugsSystem;

    public DatabaseContentProvider(
            SpeciesRepository speciesRepository,
            TaxaGenusRepository genusRepository,
            TaxaFamilyRepository familyRepository,
            TaxaOrderRepository orderRepository,
            TaxonomicOrderRepository taxonomicOrderRepository,
            TaxonomicOrderSystemRepository taxonomicOrderSystemRepository
    ) {
        this.speciesRepository = speciesRepository;
        this.genusRepository = genusRepository;
        this.familyRepository = familyRepository;
        this.orderRepository = orderRepository;
        this.taxonomicOrderRepository = taxonomicOrderRepository;
        bugsSystem = taxonomicOrderSystemRepository.getBugsSystem();
    }

    @Override
    public List<TaxonomicOrder> getExpectedData() {
        return Arrays.asList(
                TestTaxonomyOrder.create(
                        1,
                        speciesRepository.findOne(1),
                        BigDecimalDefinition.convertToSeadCode(1d),
                        bugsSystem
                ),
                TestTaxonomyOrder.create(
                        null,
                        speciesRepository.findOne(2),
                        BigDecimalDefinition.convertToSeadCode(2d),
                        bugsSystem
                ),
                TestTaxonomyOrder.create(
                        null,
                        speciesRepository.findOne(3),
                        BigDecimalDefinition.convertToSeadCode(3d),
                        bugsSystem
                ),
                TestTaxonomyOrder.create(
                        null,
                        TestTaxaSpecies.create(
                                null,
                                "newSpecies",
                                genusRepository.findOne(1),
                                null),
                        BigDecimalDefinition.convertToSeadCode(8d),
                        bugsSystem
                ),
                TestTaxonomyOrder.create(
                        null,
                        TestTaxaSpecies.create(
                                null,
                                "sp.",
                                TestTaxaGenus.create(null, "NewGenus", familyRepository.findOne(1)),
                                null
                        ),
                        BigDecimalDefinition.convertToSeadCode(4d),
                        bugsSystem
                ),
                TestTaxonomyOrder.create(
                        null,
                        TestTaxaSpecies.create(
                                null,
                                "sp.",
                                TestTaxaGenus.create(
                                        null,
                                        "NewGenus",
                                        familyRepository.findOne(1)
                                ),
                                TestTaxaAuthor.create(null, "NewAuthor")
                        ),
                        BigDecimalDefinition.convertToSeadCode(5d),
                        bugsSystem
                ),
                TestTaxonomyOrder.create(
                        null,
                        TestTaxaSpecies.create(
                                null,
                                "someSpecies",
                                TestTaxaGenus.create(
                                        null,
                                        "SomeGenus",
                                        TestTaxaFamily.create(
                                                null,
                                                "NewFamily",
                                                orderRepository.getImportOrder()
                                        )
                                ),
                                null
                        ),
                        BigDecimalDefinition.convertToSeadCode(9d),
                        bugsSystem
                )
        );
    }

    @Override
    public List<TaxonomicOrder> getActualData() {
        return taxonomicOrderRepository.findAll();
    }

    @Override
    public Comparator<TaxonomicOrder> getSorter() {
        return new TaxonomicOrderComparator();
    }

    @Override
    public TestEqualityHelper<TaxonomicOrder> getEqualityHelper() {
        return new TestEqualityHelper<>(true);
    }

    private static class TaxonomicOrderComparator implements Comparator<TaxonomicOrder>{
        @Override
        public int compare(TaxonomicOrder o1, TaxonomicOrder o2) {
            return o1.getCode().compareTo(o2.getCode());
        }
    }
}
