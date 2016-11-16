package se.sead.specieskeys;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.specieskeys.seadmodel.TextIdentificationKeys;
import se.sead.model.TestBiblio;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestTextIdentificationKeys;
import se.sead.repositories.SpeciesRepository;
import se.sead.repositories.TaxaOrderRepository;
import se.sead.repositories.TextIdentificationKeysRepository;
import se.sead.sead.model.Biblio;
import se.sead.testutils.DatabaseContentVerification;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

class DatabaseContentVerifier implements DatabaseContentVerification.DatabaseContentTestDataProvider<TextIdentificationKeys> {

    private SpeciesBuilder speciesBuilder;
    private TextIdentificationKeysRepository identificationKeysRepository;
    private Biblio skidMoreReference =
            TestBiblio.create(
                    1,
                    "Skidmore (unpubl.)",
                    "Skidmore unpubl.",
                    "Keys to the British Beetle (Coleoptera) Fauna."
            );

    DatabaseContentVerifier(TaxaOrderRepository orderRepository, TextIdentificationKeysRepository identificationKeysRepository){
        speciesBuilder = new SpeciesBuilder(orderRepository.getImportOrder());
        this.identificationKeysRepository = identificationKeysRepository;
    }


    @Override
    public List<TextIdentificationKeys> getExpectedData() {
        List<TextIdentificationKeys> expectedKeys = new ArrayList<>();
        expectedKeys.add(
                TestTextIdentificationKeys.create(1, speciesBuilder.getSpecies("01.00100010"), skidMoreReference, "Key to Subfamilies of Carabidae"));
        expectedKeys.add(TestTextIdentificationKeys.create(null, speciesBuilder.getSpecies("01.00101220"), skidMoreReference, "Key to Cicindelinae"));
        expectedKeys.add(TestTextIdentificationKeys.create(null, speciesBuilder.getSpecies("01.00200820"), skidMoreReference, "Large to very large beetles often of brilliant colouration; hind angles of pronotum obtuse .L.16-30mm."));
        return expectedKeys;
    }

    @Override
    public List<TextIdentificationKeys> getActualData() {
        return identificationKeysRepository.findAll();
    }

    @Override
    public Comparator<TextIdentificationKeys> getSorter() {
        return new TextIdentificationKeysComparator();
    }

    @Override
    public TestEqualityHelper<TextIdentificationKeys> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class TextIdentificationKeysComparator  implements Comparator<TextIdentificationKeys> {
        @Override
        public int compare(TextIdentificationKeys o1, TextIdentificationKeys o2) {
            if(Objects.equals(o1.getKeys(), o2.getKeys())){
                if(Objects.equals(o1.getSpecies().getId(), o2.getSpecies().getId())){
                    return o1.getReference().getId().compareTo(o2.getReference().getId());
                } else if(o1.getSpecies().getId() == null && o2.getSpecies().getId() != null) {
                    return 1;
                } else if(o1.getSpecies().getId() != null && o2.getSpecies().getId() == null){
                    return -1;
                } else {
                    return 0;
                }
            } else {
                return o1.getKeys().compareTo(o2.getKeys());
            }
        }
    }
}
