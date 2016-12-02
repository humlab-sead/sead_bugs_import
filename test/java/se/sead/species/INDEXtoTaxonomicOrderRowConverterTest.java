package se.sead.species;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.species.INDEXtoTaxonomicOrderRowConverter;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.repositories.SpeciesRepository;
import se.sead.sead.model.LoggableEntity;
import se.sead.testutils.DefaultConfig;
import se.sead.utils.BigDecimalDefinition;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = INDEXtoTaxonomicOrderRowConverterTest.Config.class)
@ActiveProfiles("test")
@Transactional
public class INDEXtoTaxonomicOrderRowConverterTest {
    @TestConfiguration
    static class Config extends DefaultConfig{
        Config(){
            super("");
        }

        @Override
        protected String getMdbFile() {
            return "";
        }

        @Override
        protected String getDataFile() {
            return "species/species.sql";
        }
    }

    @Autowired
    private INDEXtoTaxonomicOrderRowConverter rowConverter;
    @Autowired
    private SpeciesRepository speciesRepository;


    @Test
    public void taxonomicCodeExists(){
        INDEX rowData = createRowData(1d, "Family", "Genus", "indet.");
        TaxonomicOrder taxonomicOrder = rowConverter.convertForDataRow(rowData);
        assertEquals(BigDecimalDefinition.convertToSeadCode(1d), taxonomicOrder.getCode());
        assertEquals(Integer.valueOf(1), taxonomicOrder.getId());
        assertExistingObject(taxonomicOrder.getSpecies(), 1);
    }

    private void assertExistingObject(LoggableEntity entity, Integer expectedId) {
        assertEquals(expectedId, entity.getId());
    }

    private INDEX createRowData(Double code, String family, String genus, String species){
        return createRowData(code, family, genus, species, null);
    }

    private INDEX createRowData(Double code, String family, String genus, String species, String author){
        INDEX rowData = new INDEX();
        rowData.setCode(code);
        rowData.setFamily(family);
        rowData.setGenus(genus);
        rowData.setSpecies(species);
        rowData.setAuthority(author);
        return rowData;
    }

    @Test
    public void createTaxonomicOrderFromExistingSpecies(){
        INDEX rowData = createRowData(2d, "Family", "Genus", "createTaxonomicOrder");
        TaxonomicOrder taxonomicOrder = rowConverter.convertForDataRow(rowData);
        assertEquals(BigDecimalDefinition.convertToSeadCode(2d), taxonomicOrder.getCode());
        assertNewObject(taxonomicOrder);
        assertExistingObject(taxonomicOrder.getSpecies(), 2);
    }

    private void assertNewObject(LoggableEntity entity){
        assertNull(entity.getId());
        assertTrue(entity.isNewItem());
    }

    @Test
    public void createTaxonomicOrderFromExistingSpeciesWithAuthor(){
        INDEX rowData = createRowData(3d, "Family", "Genus", "createTaxonomicOrder", "Author");
        TaxonomicOrder taxonomicOrder = rowConverter.convertForDataRow(rowData);
        assertEquals(BigDecimalDefinition.convertToSeadCode(3d), taxonomicOrder.getCode());
        assertNewObject(taxonomicOrder);
        assertExistingObject(taxonomicOrder.getSpecies(), 3);
    }

    @Test
    public void createTaxonomicOrderFromNewSpeciesAndExistingGenus(){
        INDEX rowData = createRowData(4d, "Family", "Genus", "newSpecies");
        TaxonomicOrder taxonomicOrder = rowConverter.convertForDataRow(rowData);
        assertEquals(BigDecimalDefinition.convertToSeadCode(4d), taxonomicOrder.getCode());
        assertNewObject(taxonomicOrder);
        assertNewObject(taxonomicOrder.getSpecies());
        assertExistingObject(taxonomicOrder.getSpecies().getGenus(), 1);
    }

    @Test
    public void createTaxonomicOrderFromNewGenus(){
        INDEX rowData = createRowData(5d, "Family", "NewGenus", "sp.");
        TaxonomicOrder taxonomicOrder = rowConverter.convertForDataRow(rowData);
        assertEquals(BigDecimalDefinition.convertToSeadCode(5d), taxonomicOrder.getCode());
        assertNewObject(taxonomicOrder);
        assertNewObject(taxonomicOrder.getSpecies());
        assertNewObject(taxonomicOrder.getSpecies().getGenus());
        assertExistingObject(taxonomicOrder.getSpecies().getGenus().getTaxaFamily(), 1);
    }

    @Test
    public void createTaxonomicOrderFromNewFamily(){
        INDEX rowData = createRowData(5d, "NewFamily", "NewGenus", "sp.");
        TaxonomicOrder taxonomicOrder = rowConverter.convertForDataRow(rowData);
        assertEquals(BigDecimalDefinition.convertToSeadCode(5d), taxonomicOrder.getCode());
        assertNewObject(taxonomicOrder);
        assertNewObject(taxonomicOrder.getSpecies());
        assertNewObject(taxonomicOrder.getSpecies().getGenus());
        assertNewObject(taxonomicOrder.getSpecies().getGenus().getTaxaFamily());
    }
}
