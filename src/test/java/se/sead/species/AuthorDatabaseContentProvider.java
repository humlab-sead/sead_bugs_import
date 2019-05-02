package se.sead.species;

import se.sead.bugsimport.species.seadmodel.TaxaAuthor;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestTaxaAuthor;
import se.sead.repositories.TaxaAuthorRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AuthorDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<TaxaAuthor> {

    private TaxaAuthorRepository authorRepository;

    public AuthorDatabaseContentProvider(TaxaAuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<TaxaAuthor> getExpectedData() {
        return Arrays.asList(
                TestTaxaAuthor.create(
                        1,
                        "Author"
                ),
                TestTaxaAuthor.create(
                        null,
                        "NewAuthor"
                )
        );
    }

    @Override
    public List<TaxaAuthor> getActualData() {
        return authorRepository.findAll();
    }

    @Override
    public Comparator<TaxaAuthor> getSorter() {
        return new TaxaAuthorComparator();
    }

    @Override
    public TestEqualityHelper<TaxaAuthor> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    @Override
    public boolean useEqualityHelper(TaxaAuthor expected, TaxaAuthor actual) {
        return true;
    }

    private static class TaxaAuthorComparator implements Comparator<TaxaAuthor> {
        @Override
        public int compare(TaxaAuthor o1, TaxaAuthor o2) {
            return o1.getAuthorName().compareTo(o2.getAuthorName());
        }
    }
}
