package se.sead.specieskeys;

import se.sead.bugsimport.specieskeys.bugsmodel.Keys;
import se.sead.bugsimport.specieskeys.seadmodel.TextIdentificationKeys;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.sead.model.Biblio;
import se.sead.model.TestBiblio;
import se.sead.model.TestTextIdentificationKeys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class KeysTestDefinition {

    static final List<Keys> EXPECTED_READ_ITEMS =
            Arrays.asList(
                    create(
                            01.00100010d,
                            "Skidmore unpubl.",
                            "Key to Subfamilies of Carabidae"
                    ),
                    create(
                            01.00101220d,
                            "Skidmore unpubl.",
                            "Key to Cicindelinae"
                    ),
                    create(
                            01.00200820d,
                            "Skidmore unpubl.",
                            "Large to very large beetles often of brilliant colouration; hind angles of pronotum obtuse .L.16-30mm."
                    ),
                    create(
                            01.00403420d,
                            "Björk 2004",
                            "Error, reference does not exist. Not added."
                    ),
                    create(
                            9999.00000100d,
                            "Skidmore unpubl.",
                            "Error, species does not exist. Not added."
                    )
            );

    private static Keys create(double code, String ref, String data){
        Keys key = new Keys();
        key.setCode(code);
        key.setRef(ref);
        key.setData(data);
        return key;
    }

    private SpeciesBuilder speciesBuilder;
    private Biblio skidMoreReference =
            TestBiblio.create(
                1,
                "Skidmore (unpubl.)",
                "Skidmore unpubl.",
                "Keys to the British Beetle (Coleoptera) Fauna."
            );

    KeysTestDefinition(SpeciesBuilder speciesBuilder){
        this.speciesBuilder = speciesBuilder;
    }

    List<TextIdentificationKeys> getExpectedResults(){
        List<TextIdentificationKeys> expectedKeys = new ArrayList<>();
        expectedKeys.add(TestTextIdentificationKeys.create(null, speciesBuilder.getSpecies("01.00101220"), skidMoreReference, "Key to Cicindelinae"));
        expectedKeys.add(TestTextIdentificationKeys.create(null, speciesBuilder.getSpecies("01.00200820"), skidMoreReference, "Large to very large beetles often of brilliant colouration; hind angles of pronotum obtuse .L.16-30mm."));
        return expectedKeys;
    }

    void assertTracces(List<BugsTrace> traces, Keys bugsData) {
        if(bugsData.getCode() == 01.00101220d ||
                bugsData.getCode() == 01.00200820d){
            assertEquals(1, traces.size());
            BugsTrace bugsTrace = traces.get(0);
            assertEquals("tbl_text_identification_keys", bugsTrace.getSeadTable());
        } else {
            assertEquals(0, traces.size());
        }
    }

    public void assertErrors(List<BugsError> errors, Keys bugsData) {
        if(bugsData.getCode() == 01.00403420d){
            assertEquals(1, errors.size());
            BugsError bugsError = errors.get(0);
            assertEquals("No reference found for: " + bugsData.getRef(), bugsError.getMessage());
        } else if(bugsData.getCode() == 9999.00000100d){
            assertEquals(1, errors.size());
            BugsError bugsError = errors.get(0);
            assertEquals("No species found for code: " + bugsData.getCode(), bugsError.getMessage());
        } else {
            assertEquals(0, errors.size());
        }
    }
}
