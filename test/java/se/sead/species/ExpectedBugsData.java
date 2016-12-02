package se.sead.species;

import se.sead.bugsimport.species.bugsmodel.INDEX;

import java.util.Arrays;
import java.util.List;

public class ExpectedBugsData {

    static final List<INDEX> EXPECTED_ROW_DATA =
            Arrays.asList(
                    createRowData(1d, "Family", "Genus", "indet.", null),
                    createRowData(2d, "Family", "Genus", "createTaxonomicOrder", null),
                    createRowData(3d, "Family", "Genus", "createTaxonomicOrder", "Author"),
                    createRowData(4d, "Family", "NewGenus", "sp.", null),
                    createRowData(5d, "Family", "NewGenus", "sp.", "NewAuthor"),
                    createRowData(8d, "Family", "Genus", "newSpecies", null),
                    createRowData(9d, "NewFamily", "SomeGenus", "someSpecies", null),
                    createRowData(10d, "Family", "Genus", null, null)
            );

    private static INDEX createRowData(Double code, String family, String genus, String species, String author) {
        INDEX index = new INDEX();
        index.setCode(code);
        index.setSpecies(species);
        index.setGenus(genus);
        index.setFamily(family);
        index.setAuthority(author);
        return index;
    }
}
