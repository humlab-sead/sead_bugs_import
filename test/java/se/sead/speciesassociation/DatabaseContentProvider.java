package se.sead.speciesassociation;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociationType;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestSpeciesAssociation;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.SpeciesAssociationRepository;
import se.sead.repositories.SpeciesAssociationTypeRepository;
import se.sead.repositories.SpeciesRepository;
import se.sead.sead.model.Biblio;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<SpeciesAssociation> {

    private SpeciesAssociationRepository speciesAssociationRepository;

    private TaxaSpecies code01;
    private TaxaSpecies code02;
    private TaxaSpecies code03;

    private SpeciesAssociationType parasitizesType;
    private SpeciesAssociationType isAssociatedWithType;

    private Biblio ref1;
    private Biblio ref3;

    DatabaseContentProvider(
        SpeciesAssociationRepository speciesAssociationRepository,
        SpeciesAssociationTypeRepository speciesAssociationTypeRepository,
        BiblioDataRepository referenceRepository,
        SpeciesRepository speciesRepository
    ){
        this.speciesAssociationRepository = speciesAssociationRepository;
        code01 = speciesRepository.findOne(1);
        code02 = speciesRepository.findOne(2);
        code03 = speciesRepository.findOne(3);
        parasitizesType = speciesAssociationTypeRepository.findOne(1);
        isAssociatedWithType = speciesAssociationTypeRepository.findOne(2);
        ref1 = referenceRepository.findOne(1);
        ref3 = referenceRepository.findOne(2);
    }

    @Override
    public List<SpeciesAssociation> getExpectedData() {
        return Arrays.asList(
                TestSpeciesAssociation.create(
                        1,
                        code01,
                        code02,
                        parasitizesType,
                        null
                ),
                TestSpeciesAssociation.create(
                        2,
                        code01,
                        code02,
                        parasitizesType,
                        ref3
                ),
                TestSpeciesAssociation.create(
                        3,
                        code01,
                        code02,
                        parasitizesType,
                        ref3
                ),
                TestSpeciesAssociation.create(
                        null,
                        code01,
                        code02,
                        isAssociatedWithType,
                        null
                ),
                TestSpeciesAssociation.create(
                        null,
                        code01,
                        code02,
                        parasitizesType,
                        ref1
                ),
                TestSpeciesAssociation.create(
                        null,
                        code01,
                        code03,
                        isAssociatedWithType,
                        null
                )
        );
    }

    @Override
    public List<SpeciesAssociation> getActualData() {
        return speciesAssociationRepository.findAll();
    }

    @Override
    public Comparator<SpeciesAssociation> getSorter() {
        return new SpeciesAssociationComparator();
    }

    @Override
    public TestEqualityHelper<SpeciesAssociation> getEqualityHelper() {
        return new TestEqualityHelper<>();
    }

    private static class SpeciesAssociationComparator implements Comparator<SpeciesAssociation> {
        @Override
        public int compare(SpeciesAssociation o1, SpeciesAssociation o2) {
//            if(o1.getId() != null && o2.getId() != null){
//                return o1.getId().compareTo(o2.getId());
//            } else {
                int typeDifference = o1.getType().getId().compareTo(o2.getType().getId());
                if(typeDifference != 0){
                    return typeDifference;
                } else if(o1.getReference() != null && o2.getReference() != null){
                    return o1.getReference().getId().compareTo(o2.getReference().getId());
                } else if(o1.getReference() != null && o2.getReference() == null){
                    return -1;
                } else if(o1.getReference() == null && o2.getReference() != null){
                    return 1;
                } else {
                    return 0;
                }
//            }
        }
    }
}
