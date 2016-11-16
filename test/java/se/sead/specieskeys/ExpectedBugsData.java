package se.sead.specieskeys;

import se.sead.bugsimport.specieskeys.bugsmodel.Keys;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {
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
                    ),
                    create(
                            01.00101220d,
                            null,
                            "Error, no reference"
                    ),
                    create(
                            01.00101220d,
                            "Skidmore unpubl.",
                            null
                    )
            );

    private static Keys create(double code, String ref, String data){
        Keys key = new Keys();
        key.setCode(code);
        key.setRef(ref);
        key.setData(data);
        return key;
    }
}
