package se.sead.ecocodedefinitiongroups;

import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeSystem;
import se.sead.model.TestEcocodeGroup;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.EcocodeGroupRepository;
import se.sead.repositories.EcocodeSystemRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<EcocodeGroup> {

    private EcocodeGroupRepository ecocodeGroupRepository;
    private EcocodeSystem kochSystem;

    public DatabaseContentProvider(EcocodeGroupRepository ecocodeGroupRepository,
                                   EcocodeSystemRepository ecocodeSystemRepository) {
        this.ecocodeGroupRepository = ecocodeGroupRepository;
        kochSystem = ecocodeSystemRepository.findKochSystem();
    }

    @Override
    public List<EcocodeGroup> getExpectedData() {
        return Arrays.asList(
                TestEcocodeGroup.create(1, "Ext", "Existing group", kochSystem),
                TestEcocodeGroup.create(2, "Upd", "Updated group", kochSystem),
                TestEcocodeGroup.create(null, "New", "New group", kochSystem)
        );
    }

    @Override
    public List<EcocodeGroup> getActualData() {
        return ecocodeGroupRepository.findBySystem(kochSystem);
    }

    @Override
    public Comparator<EcocodeGroup> getSorter() {
        return null;
    }

    @Override
    public TestEqualityHelper<EcocodeGroup> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class EcocodeGroupComparator implements Comparator<EcocodeGroup> {
        @Override
        public int compare(EcocodeGroup o1, EcocodeGroup o2) {
            return o1.getAbbreviation().compareTo(o2.getAbbreviation());
        }
    }
}
