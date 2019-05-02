package se.sead.bibliography;

import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.model.TestBiblio;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.BiblioDataRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BibliographyDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<Biblio> {

    private BiblioDataRepository repository;

    public BibliographyDatabaseContentProvider(BiblioDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Biblio> getExpectedData() {
        return Arrays.asList(
                TestBiblio.create(1,"Exists, A.B (2000)", "Exists 2000", "An existing bibliography non-edited"),
                TestBiblio.create(2, "Exists, A.B (2001)", "Exists 2001", "A changed entry"),
                TestBiblio.create(null, "Exists, Not (2000)", "NonExists 2000", "A non-existing bibliography entry."),
                TestBiblio.create(3, "Exists, A.B (2012)", "Exists 2012", "A preinstalled bugs reference")
        );
    }

    @Override
    public List<Biblio> getActualData() {
        return repository.findAll();
    }

    @Override
    public Comparator<Biblio> getSorter() {
        return new BiblioComparator();
    }

    @Override
    public TestEqualityHelper<Biblio> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class BiblioComparator implements Comparator<Biblio> {
        @Override
        public int compare(Biblio o1, Biblio o2) {
            return o1.getBugsReference().compareTo(o2.getBugsReference());
        }
    }
}
