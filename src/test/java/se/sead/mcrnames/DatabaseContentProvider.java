package se.sead.mcrnames;

import se.sead.bugsimport.mcrnames.seadmodel.MCRName;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestMCRName;
import se.sead.repositories.McrNamesRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<MCRName> {

    private McrNamesRepository mcrNamesRepository;

    DatabaseContentProvider(McrNamesRepository mcrNamesRepository){
        this.mcrNamesRepository = mcrNamesRepository;
    }

    @Override
    public List<MCRName> getExpectedData() {
        return Arrays.asList(
                TestMCRName.create(1, "Found similarity", "CicindelasylvaticaL.",(short)273, "Cicindela sylvatica L."),
                TestMCRName.create(2, "=Carabus clatratus L. 1.004014", "CarabusclathratusL.",  (short)288, "Carabus clathratus L."),
                TestMCRName.create(3, "do not import", "Matchwithouttraces", (short)1, "Match without traces"),
                TestMCRName.create(4, "import with update", "Updatethis", (short)2, "Update this"),
                TestMCRName.create(5, "import?", "Seaddatachanged", (short)3, "Sead data changed")

        );
    }

    @Override
    public List<MCRName> getActualData() {
        return mcrNamesRepository.findAll();
    }

    @Override
    public Comparator<MCRName> getSorter() {
        return new MCRNameComparator();
    }

    @Override
    public TestEqualityHelper<MCRName> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class MCRNameComparator implements Comparator<MCRName> {
        @Override
        public int compare(MCRName o1, MCRName o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }
}
