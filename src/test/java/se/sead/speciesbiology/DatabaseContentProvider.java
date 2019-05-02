package se.sead.speciesbiology;

import se.sead.bugsimport.speciesbiology.seadmodel.TextBiology;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestTextBiology;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.SpeciesRepository;
import se.sead.repositories.TextBiologyDataRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<TextBiology> {

    private TextBiologyDataRepository biologyRepository;
    private SpeciesRepository speciesRepository;
    private BiblioDataRepository biblioRepository;

    DatabaseContentProvider(TextBiologyDataRepository biologyRepository, SpeciesRepository speciesRepository, BiblioDataRepository biblioRepository) {
        this.biologyRepository = biologyRepository;
        this.speciesRepository = speciesRepository;
        this.biblioRepository = biblioRepository;
    }

    @Override
    public List<TextBiology> getExpectedData() {
        return Arrays.asList(
                TestTextBiology.create(1, speciesRepository.findOne(1), biblioRepository.findOne(1), "Note exists"),
                TestTextBiology.create(null, speciesRepository.findOne(2), biblioRepository.findOne(1), "Note to be inserted")
        );
    }

    @Override
    public List<TextBiology> getActualData() {
        return biologyRepository.findAll();
    }

    @Override
    public Comparator<TextBiology> getSorter() {
        return new TextBiologyComparator();
    }

    @Override
    public TestEqualityHelper<TextBiology> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class TextBiologyComparator implements Comparator<TextBiology> {
        @Override
        public int compare(TextBiology o1, TextBiology o2) {
            return o1.getSpecies().getId().compareTo(o2.getSpecies().getId());
        }
    }
}
