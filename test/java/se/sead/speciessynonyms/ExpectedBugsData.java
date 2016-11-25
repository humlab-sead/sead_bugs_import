package se.sead.speciessynonyms;

import se.sead.bugsimport.speciessynonyms.bugsmodel.Synonym;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<Synonym> EXPECTED_DATA =
            Arrays.asList(
                create(1d, "Off genus", "syn species", "A stored authority", "Ref 1", "exists"),
                create(1d, "Off genus", "alt 2 name", "syn authority", null, "No reference"),
                create(2d, "Off genus", "alt species name", "syn authority", "Ref 1", "new item"),
                create(3d, "Syn genus 2", null, "syn authority", "Ref 1", "no alt species"),
                create(3d, "Off genus", "alt 3 name", null, "Ref 1", "no authority"),
                create(3d, null, "alt name", "A stored authority", "Ref 1", "no genus"),
                create(3d, "Off genus", "alt name", "A stored authority", "Ref 2", "no reference found")
            );

    private static Synonym create(
            Double code,
            String synGenus,
            String synSpecies,
            String synAuthority,
            String ref,
            String notes
    ){
        Synonym synonym = new Synonym();
        synonym.setCode(code);
        synonym.setSynGenus(synGenus);
        synonym.setSynSpecies(synSpecies);
        synonym.setSynAuthority(synAuthority);
        synonym.setReference(ref);
        synonym.setNotes(notes);
        return synonym;
    }
}
