package se.sead.species;

import se.sead.bugsimport.species.seadmodel.TaxaAuthor;
import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.model.*;
import se.sead.repositories.*;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SpeciesDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<TaxaSpecies> {

    private TaxaGenusRepository genusRepository;
    private TaxaAuthorRepository authorRepository;
    private TaxaFamilyRepository familyRepository;
    private TaxaOrderRepository orderRepository;
    private SpeciesRepository speciesRepository;

    public SpeciesDatabaseContentProvider(SpeciesRepository speciesRepository, TaxaGenusRepository genusRepository, TaxaFamilyRepository familyRepository, TaxaOrderRepository orderRepository, TaxaAuthorRepository authorRepository) {
        this.speciesRepository = speciesRepository;
        this.genusRepository = genusRepository;
        this.familyRepository = familyRepository;
        this.orderRepository = orderRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public List<TaxaSpecies> getExpectedData() {
        return Arrays.asList(
                TestTaxaSpecies.create(
                        1,
                        "indet.",
                        genusRepository.findOne(1),
                        null
                ),
                TestTaxaSpecies.create(
                        2,
                        "createTaxonomicOrder",
                        genusRepository.findOne(1),
                        null
                ),
                TestTaxaSpecies.create(
                        3,
                        "createTaxonomicOrder",
                        genusRepository.findOne(1),
                        authorRepository.findOne(1)
                ),
                TestTaxaSpecies.create(
                        4,
                        "No data",
                        genusRepository.findOne(2),
                        null
                ),
                TestTaxaSpecies.create(
                        5,
                        "No data",
                        genusRepository.findOne(3),
                        null
                ),
                TestTaxaSpecies.create(
                        null,
                        "sp.",
                        TestTaxaGenus.create(
                                null,
                                "NewGenus",
                                familyRepository.findOne(1)
                        ),
                        null
                ),
                TestTaxaSpecies.create(
                        null,
                        "sp.",
                        TestTaxaGenus.create(
                                null,
                                "NewGenus",
                                familyRepository.findOne(1)
                        ),
                        TestTaxaAuthor.create(
                                null,
                                "NewAuthor"
                        )
                ),
                TestTaxaSpecies.create(
                        null,
                        "newSpecies",
                        genusRepository.findOne(1),
                        null
                ),
                TestTaxaSpecies.create(
                        null,
                        "someSpecies",
                        TestTaxaGenus.create(
                                null,
                                "SomeGenus",
                                TestTaxaFamily.create(
                                        null,
                                        "NewFamily",
                                        orderRepository.findOne(1)
                                )
                        ),
                        null
                ),
                TestTaxaSpecies.create(
                        null,
                        "someSpecies",
                        TestTaxaGenus.create(
                                null,
                                "SomeGenus",
                                TestTaxaFamily.create(
                                        null,
                                        "NewFamily",
                                        orderRepository.findOne(1)
                                )
                        ),
                        TestTaxaAuthor.create(
                                null,
                                "NewAuthor"
                        )
                )
        );
    }

    @Override
    public List<TaxaSpecies> getActualData() {
        return speciesRepository.findAll();
    }

    @Override
    public Comparator<TaxaSpecies> getSorter() {
        return new SpeciesComparator();
    }

    @Override
    public TestEqualityHelper<TaxaSpecies> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    @Override
    public boolean useEqualityHelper(TaxaSpecies expected, TaxaSpecies actual) {
        return true;
    }

    private static class SpeciesComparator implements Comparator<TaxaSpecies> {

        private static final String NAME_TEMPLATE = "%s %s %s(%s) %s";

        @Override
        public int compare(TaxaSpecies o1, TaxaSpecies o2) {
            String o1String = createTemplateValue(o1);
            String o2String = createTemplateValue(o2);
            return o1String.compareTo(o2String);
        }

        private String createTemplateValue(TaxaSpecies species) {
            return String.format(
                    NAME_TEMPLATE,
                    species.getSpeciesName(),
                    species.getGenus().getGenusName(),
                    species.getGenus().getFamily().getFamilyName(),
                    species.getGenus().getFamily().getOrder().getOrderName(),
                    (species.getAuthor() != null ? species.getAuthor().getAuthorName() : "")
                    );
        }
    }
}
