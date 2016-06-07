package se.sead.taxanotes;

import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotes;
import se.sead.bugsimport.taxanotes.seadmodel.TaxonomicNotes;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.model.TestTaxonomicNotes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TaxaNotesImportTestDefinition {

    static final List<TaxoNotes> EXPECTED_READ_ITEMS =
            Arrays.asList(
                    // taxa notes exists in target db
                create(1.0010050d, "Hodge & Jones 1995", "See Cicindela maritima also."),
                    // taxa notes is to be created
                create(1.0010060d, "Hodge & Jones 1995", "regarded as a variety of Cicindela hybrida by Joy 1932, white central band on elytra bent strongly and produced towards apex. Coastal sand dunes."),
                    //species created, no biblio item exists
                create(1.0010080d, "Skidmore unpubl.", "See Cicindela sp for key to UK spp."),
                create(9999.0000001d, "Björk et al. (1994)", "testesdf"),
                    // no species exists
                create(1.0040150d, "Hurka 1996", "Very variable with numerous regional forms across its distribution."),
                create(1.00401600d, "Hodge & Jones 1995", "Omitted by Joy (1932) as very rare and doubtfully indigenous, 22-28mm, shining golden green, elytra each with 3 raised carinae. Legs red.")
            );

    private static TaxoNotes create(double code, String ref, String data){
        TaxoNotes notes = new TaxoNotes();
        notes.setCode(code);
        notes.setReference(ref);
        notes.setData(data);
        return notes;
    }

    private SpeciesBuilder speciesBuilder;
    private BiblioBuilder biblioBuilder;

    TaxaNotesImportTestDefinition(SpeciesBuilder speciesBuilder, BiblioBuilder biblioBuilder) {
        this.speciesBuilder = speciesBuilder;
        this.biblioBuilder = biblioBuilder;
    }

    List<TaxonomicNotes> getExpectedNotes(){
        List<TaxonomicNotes> createdNotes = new ArrayList<>();
        createdNotes.add(TestTaxonomicNotes.create(1, biblioBuilder.get("Hodge & Jones 1995"), speciesBuilder.getSpecies("10010050"), "See Cicindela maritima also."));
        createdNotes.add(TestTaxonomicNotes.create(null, biblioBuilder.get("Hodge & Jones 1995"), speciesBuilder.getSpecies("10010060"), "regarded as a variety of Cicindela hybrida by Joy 1932, white central band on elytra bent strongly and produced towards apex. Coastal sand dunes."));
        return createdNotes;
    }

    void assertTrace(List<BugsTrace> actualList, TaxoNotes readItem){
        if(readItem.getCode() == 1.0010060d){
            assertFalse(actualList.isEmpty());
            assertTrue(actualList.stream()
                    .map(trace -> trace.getSeadTable())
                    .allMatch(tableName -> "tbl_taxonomic_notes".equals(tableName))
            );
        } else {
            assertEquals(0, actualList.size());
        }
    }

    void assertErrors(List<BugsError> actualErrors, TaxoNotes readItem){
        if(readItem.getCode() == 1.0010080d ||
                readItem.getCode() == 9999.0000001d){
            assertEquals(1, actualErrors.size());
            BugsError error = actualErrors.get(0);
            assertEquals("No biblio item from reference: " + readItem.getReference(), error.getMessage());
        } else if(readItem.getCode() == 1.0040150d ||
                readItem.getCode() == 1.00401600d){
            assertEquals(1, actualErrors.size());
            BugsError error = actualErrors.get(0);
            assertEquals("No species found for taxanote: " + readItem.getCode(), error.getMessage());
        } else {
            assertEquals(0, actualErrors.size());
        }
    }
}
