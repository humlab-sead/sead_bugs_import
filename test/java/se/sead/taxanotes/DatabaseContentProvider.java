package se.sead.taxanotes;

import se.sead.bugsimport.taxanotes.seadmodel.TaxonomicNotes;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestTaxonomicNotes;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.SpeciesRepository;
import se.sead.repositories.TaxonomicNotesRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<TaxonomicNotes> {

    private TaxonomicNotesRepository repository;
    private BiblioDataRepository biblioDataRepository;
    private SpeciesRepository speciesRepository;

    public DatabaseContentProvider(TaxonomicNotesRepository repository, BiblioDataRepository biblioDataRepository, SpeciesRepository speciesRepository) {
        this.repository = repository;
        this.biblioDataRepository = biblioDataRepository;
        this.speciesRepository = speciesRepository;
    }

    @Override
    public List<TaxonomicNotes> getExpectedData() {
        return Arrays.asList(
                TestTaxonomicNotes.create(1, biblioDataRepository.findOne(1), speciesRepository.findOne(1), "Note exists"),
                TestTaxonomicNotes.create(null, biblioDataRepository.findOne(1), speciesRepository.findOne(2), "Note to be inserted")
        );
    }

    @Override
    public List<TaxonomicNotes> getActualData() {
        return repository.findAll();
    }

    @Override
    public Comparator<TaxonomicNotes> getSorter() {
        return new TaxonomicNotesComparator();
    }

    @Override
    public TestEqualityHelper<TaxonomicNotes> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class TaxonomicNotesComparator implements Comparator<TaxonomicNotes> {
        @Override
        public int compare(TaxonomicNotes o1, TaxonomicNotes o2) {
            return o1.getSpecies().getId().compareTo(o2.getSpecies().getId());
        }
    }
}
