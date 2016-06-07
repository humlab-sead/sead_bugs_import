package se.sead.species;

import se.sead.bugs.AccessReader;
import se.sead.AccessReaderTest;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.seadmodel.*;
import se.sead.model.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by erer0001 on 2016-04-28.
 */
public class INDEXAccessDatabaseTestDefinition {

    public static final String DATA_FILE = "INDEX.mdb";
    static final List<INDEX> EXPECTED_ROW_DATA =
            Arrays.asList(
                    createRowData(1.0010001, "CARABIDAE", "Carabidae", "indet.", null),
                    createRowData(1.0010020, "CARABIDAE", "Cicindela", "sylvatica", "L."),
                    createRowData(1.0010050, "CARABIDAE", "Cicindela", "hybrida", "L."),
                    createRowData(1.0010060, "CARABIDAE", "Cicindela", "maritima", "Dej."),
                    createRowData(1.0010070, "CARABIDAE", "Cicindela", "campestris", "L."), // new taxonomic order, species
                    createRowData(1.0010080, "CARABIDAE", "Cylindera", "germanica", "L."),  // new taxonomic order, species, genus
                    createRowData(1.0010122, "CARABIDAE", "Cicindela", "sp.", null),
                    createRowData(1.0010125, "CARABIDAE", "Cicindela", "spp.", null), // new taxonomic order, species
                    createRowData(1083.0350240, "MUSCIDAE", "Helina", "confinis", "(Fallén)"), // new taxonomic order, species, genus, family, author
                    createRowData(1083.0350250, "MUSCIDAE", "Helina", "cilipes", "(Schnabl)"), // new taxonomic order, species, genus, family, author
                    createRowData(9999.0000001, "None", "No data", "No data", null) // new taxonomic order, species, genus, family
            );

    private TaxaOrder defaultImportOrder;
    private TaxonomicOrderSystem defaultBugsOrderSystem;

    public INDEXAccessDatabaseTestDefinition(TaxaOrder defaultImportOrder, TaxonomicOrderSystem defaultBugsOrderSystem) {
        this.defaultImportOrder = defaultImportOrder;
        this.defaultBugsOrderSystem = defaultBugsOrderSystem;
    }

    private static INDEX createRowData(Double code, String family, String genus, String species, String author) {
        INDEX index = new INDEX();
        index.setCode(code);
        index.setSpecies(species);
        index.setGenus(genus);
        index.setFamily(family);
        index.setAuthority(author);
        return index;
    }

    public List<TaxonomicOrder> getExpectedResults() {
        ExpectedDataGenerator generator = new ExpectedDataGenerator(defaultImportOrder, defaultBugsOrderSystem);
        return generator.generateOrders();
    }

    private static class ExpectedDataGenerator {
        private TaxonomicOrderSystem defaultSystem;

        private TaxaFamily carabidaeFamily;
        private TaxaFamily muscidaeFamily;
        private TaxaFamily noDataFamily;
        private TaxaGenus carabidaeGenus;
        private TaxaGenus cynlinderaGenus;
        private TaxaGenus cicindelaGenus;
        private TaxaGenus helinaGenus;
        private TaxaGenus noDataGenus;

        private TaxaAuthor existingLAuthor;
        private TaxaAuthor existingDejAuthor;

        public ExpectedDataGenerator(TaxaOrder defaultOrder, TaxonomicOrderSystem defaultSystem) {
            this.defaultSystem = defaultSystem;
            createFamilies(defaultOrder);
            createGenera();
            createExistingAuthors();
        }

        private void createFamilies(TaxaOrder defaultOrder) {
            createExistingFamilies(defaultOrder);
            createNewFamilies(defaultOrder);
        }

        private void createExistingFamilies(TaxaOrder defaultOrder) {
            carabidaeFamily = TestTaxaFamily.create(1, "CARABIDAE", defaultOrder);
        }

        private void createNewFamilies(TaxaOrder defaultOrder) {
            noDataFamily = TestTaxaFamily.create(null, "None", defaultOrder);
            muscidaeFamily = TestTaxaFamily.create(null, "MUSCIDAE", defaultOrder);
        }

        private void createGenera() {
            createExistingGenera();
            createNewGenera();
        }

        private void createExistingGenera() {
            carabidaeGenus = TestTaxaGenus.create(1, "Carabidae", carabidaeFamily);
            cicindelaGenus = TestTaxaGenus.create(2, "Cicindela", carabidaeFamily);
        }

        private void createNewGenera() {
            cynlinderaGenus = TestTaxaGenus.create(null, "Cylindera", carabidaeFamily);
            helinaGenus = TestTaxaGenus.create(null, "Helina", muscidaeFamily);
            noDataGenus = TestTaxaGenus.create(null, "No data", noDataFamily);
        }

        private void createExistingAuthors(){
            existingLAuthor = TestTaxaAuthor.create(1, "L.");
            existingDejAuthor = TestTaxaAuthor.create(2, "Dej.");
        }

        private List<TaxonomicOrder> generateOrders(){
            List<TaxonomicOrder> orders = createExistingTaxonomicOrders();
            orders.addAll(createNewTaxonomicOrders());
            Collections.sort(orders);
            return orders;
        }

        private List<TaxonomicOrder> createExistingTaxonomicOrders(){
            List<TaxonomicOrder> orders = new ArrayList<>();
            TaxaSpecies carabidaeIndet = TestTaxaSpecies.create(1, "indet.", carabidaeGenus, null);
            TaxaSpecies cicindelaSylvatica = TestTaxaSpecies.create(2, "sylvatica", cicindelaGenus, existingLAuthor);
            TaxaSpecies cicindelaHybrida = TestTaxaSpecies.create(3, "hybrida", cicindelaGenus, existingLAuthor);
            TaxaSpecies cicindelaMaritima = TestTaxaSpecies.create(4, "maritima", cicindelaGenus, existingDejAuthor);
            TaxaSpecies cicindelaSp = TestTaxaSpecies.create(5, "sp.", cicindelaGenus, null);
            orders.add(TestTaxonomyOrder.create(1, carabidaeIndet, new BigDecimal("1.0010001"), defaultSystem));
            orders.add(TestTaxonomyOrder.create(2, cicindelaSylvatica, new BigDecimal("1.0010020"), defaultSystem));
            orders.add(TestTaxonomyOrder.create(3, cicindelaHybrida, new BigDecimal("1.0010050"), defaultSystem));
            orders.add(TestTaxonomyOrder.create(4, cicindelaMaritima, new BigDecimal("1.0010060"), defaultSystem));
            orders.add(TestTaxonomyOrder.create(5, cicindelaSp, new BigDecimal("1.0010122"), defaultSystem));
            return orders;
        }

        private List<TaxonomicOrder> createNewTaxonomicOrders(){
            TaxaAuthor fallenAuthor = TestTaxaAuthor.create(null, "(Fallén)");
            TaxaAuthor schnablAuthor = TestTaxaAuthor.create(null, "(Schnabl)");
            TaxaSpecies helinaConfinis = TestTaxaSpecies.create(null, "confinis", helinaGenus, fallenAuthor);
            TaxaSpecies helinaCilipes = TestTaxaSpecies.create(null, "cilipes", helinaGenus, schnablAuthor);
            TaxaSpecies cicindelaCampestris = TestTaxaSpecies.create(null, "campestris", cicindelaGenus, existingLAuthor);
            TaxaSpecies cylinderaGermanica = TestTaxaSpecies.create(null, "germanica", cynlinderaGenus, existingLAuthor);
            TaxaSpecies cicindelaSpp = TestTaxaSpecies.create(null, "spp.", cicindelaGenus, null);
            TaxaSpecies noData = TestTaxaSpecies.create(null, "No data", noDataGenus, null);

            List<TaxonomicOrder> orders = new ArrayList<>();
            orders.add(TestTaxonomyOrder.create(null, helinaConfinis, new BigDecimal("1083.0350240"), defaultSystem));
            orders.add(TestTaxonomyOrder.create(null, helinaCilipes, new BigDecimal("1083.0350250"), defaultSystem));
            orders.add(TestTaxonomyOrder.create(null, cicindelaCampestris, new BigDecimal("1.0010070"), defaultSystem));
            orders.add(TestTaxonomyOrder.create(null, cylinderaGermanica, new BigDecimal("1.0010080"), defaultSystem));
            orders.add(TestTaxonomyOrder.create(null, cicindelaSpp, new BigDecimal("1.0010125"), defaultSystem));
            orders.add(TestTaxonomyOrder.create(null, noData, new BigDecimal("9999.0000001"), defaultSystem));
            return orders;
        }
    }
}
