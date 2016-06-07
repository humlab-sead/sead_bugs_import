package se.sead.species;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.sead.Application;
import se.sead.ApplicationConfiguration;
import se.sead.DataSourceFactory;
import se.sead.DefaultAccessDatabaseReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.IndexImporter;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.TaxaOrderRepository;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.repositories.TaxonomicOrderSystemRepository;
import se.sead.model.TestEqualityHelper;
import se.sead.util.DefaultConfig;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({Application.class, SpeciesImportTests.Config.class})
@ActiveProfiles("test")
public class SpeciesImportTests {

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("species", "INDEX.mdb", "species.sql");
        }

        @Bean
        @Override
        public AccessReaderProvider getDatabaseReader() {
            return new DefaultAccessDatabaseReader(getMdbFile());
        }

        @Bean
        @Override
        public DataSource createDataSource() {
            return DataSourceFactory.createDefault(getDataFile());
        }
    }

    @Autowired
    private IndexImporter importer;

    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;
    @Autowired
    private TaxonomicOrderSystemRepository taxonomicOrderSystemRepository;
    @Autowired
    private TaxaOrderRepository taxaOrderRepository;
    @Autowired
    private BugsTraceRepository bugsTraceRepository;
    private INDEXAccessDatabaseTestDefinition testResultDefinition;

    @Before
    public void setup(){
        testResultDefinition = new INDEXAccessDatabaseTestDefinition(taxaOrderRepository.getImportOrder(), taxonomicOrderSystemRepository.getBugsSystem());
    }

    @Test
    public void INDEXimport(){
        importer.run();
        testTaxonomicOrderDbContent();
        testBugsTraceContent();
    }

    private void testTaxonomicOrderDbContent() {
        List<TaxonomicOrder> generatedExpectedResults = testResultDefinition.getExpectedResults();
        for (TaxonomicOrder taxonomicOrder :
                generatedExpectedResults) {
            BigDecimal taxaCode = taxonomicOrder.getCode();
            TaxonomicOrder dbTaxonomicOrder = taxonomicOrderRepository.findBugsCodeByCode(taxaCode);
            ensureEquals(taxonomicOrder, dbTaxonomicOrder);
        }
    }

    private void ensureEquals(TaxonomicOrder expectedOrder, TaxonomicOrder dbTaxonomicOrder) {
        if(expectedOrder == null){
            assertNull(dbTaxonomicOrder);
        } else {
            assertNotNull(dbTaxonomicOrder);
            assertNotNull(dbTaxonomicOrder.getId());
            assertTrue(TestEqualityHelper.equalsWithoutIdIfNeeded(expectedOrder, dbTaxonomicOrder));
        }
    }

    private void testBugsTraceContent(){
        List<INDEX> expectedIndex = INDEXAccessDatabaseTestDefinition.EXPECTED_ROW_DATA;
        for (INDEX index :
                expectedIndex) {
            List<BugsTrace> bugsTraceData = bugsTraceRepository.findByBugsTableAndCompressedBugsData("INDEX", index.compressToString());
            boolean result = false;
            if(index.getCode() == 1.0010070d ||index.getCode() == 1.0010125d) {
                result = containsTables(bugsTraceData, "tbl_taxonomic_order", "tbl_taxa_tree_master");
            } else if(index.getCode() == 1.0010080d) {
                result = containsTables(bugsTraceData, "tbl_taxonomic_order", "tbl_taxa_tree_master", "tbl_taxa_tree_genera");
            } else if(index.getCode() == 1083.0350240d ||index.getCode() == 1083.0350250d) {
                result = containsTables(bugsTraceData, "tbl_taxonomic_order", "tbl_taxa_tree_master", "tbl_taxa_tree_genera", "tbl_taxa_tree_authors", "tbl_taxa_tree_families");
            } else if(index.getCode() == 9999.0000001d){
                result = containsTables(bugsTraceData, "tbl_taxonomic_order", "tbl_taxa_tree_master", "tbl_taxa_tree_genera", "tbl_taxa_tree_families");
            } else {
                result = bugsTraceData.isEmpty();
            }
            assertTrue(result);
        }
    }

    private boolean containsTables(List<BugsTrace> bugsTraceData, String... tableNames) {
        List<String> traceDataSeadTableNames = bugsTraceData.stream()
                .map(bugsTrace -> bugsTrace.getSeadTable())
                .collect(Collectors.toList());
        for (String tableName :
                tableNames) {
            if(!traceDataSeadTableNames.contains(tableName)){
                return false;
            }
        }
        return true;
    }
}