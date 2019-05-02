package se.sead.ecocodes;

import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.model.TestEcocode;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.EcocodeDefinitionRepository;
import se.sead.repositories.EcocodeRepository;
import se.sead.repositories.SpeciesRepository;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<Ecocode> {

    private SpeciesRepository speciesRepository;
    private EcocodeDefinitionRepository definitionRepository;
    private EcocodeRepository ecocodeRepository;

    public DatabaseContentProvider(SpeciesRepository speciesRepository, EcocodeDefinitionRepository definitionRepository, EcocodeRepository ecocodeRepository) {
        this.speciesRepository = speciesRepository;
        this.definitionRepository = definitionRepository;
        this.ecocodeRepository = ecocodeRepository;
    }

    @Override
    public List<Ecocode> getExpectedData() {
        return Arrays.asList(
                TestEcocode.create(
                        1,
                        speciesRepository.findOne(1),
                        definitionRepository.findOne(1)
                ),
                TestEcocode.create(
                        2,
                        speciesRepository.findOne(3),
                        definitionRepository.findOne(1)
                ),
                TestEcocode.create(
                        null,
                        speciesRepository.findOne(2),
                        definitionRepository.findOne(2)
                ),
                TestEcocode.create(
                        null,
                        speciesRepository.findOne(3),
                        definitionRepository.findOne(2)
                ),
                TestEcocode.create(
                        null,
                        speciesRepository.findOne(2),
                        definitionRepository.findOne(3)
                )
        );
    }

    @Override
    public List<Ecocode> getActualData() {
        return ecocodeRepository.findAll();
    }

    @Override
    public Comparator<Ecocode> getSorter() {
        return new EcocodeComparator();
    }

    @Override
    public TestEqualityHelper<Ecocode> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class EcocodeComparator implements Comparator<Ecocode> {
        @Override
        public int compare(Ecocode o1, Ecocode o2) {
            if(Objects.equals(o1.getSpecies(), o2.getSpecies())){
                return o1.getEcocodeDefinition().getId().compareTo(o2.getEcocodeDefinition().getId());
            } else {
                return o1.getSpecies().getId().compareTo(o2.getSpecies().getId());
            }
        }
    }
}
