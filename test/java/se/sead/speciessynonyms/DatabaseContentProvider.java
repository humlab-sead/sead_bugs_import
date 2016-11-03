package se.sead.speciessynonyms;

import se.sead.bugsimport.species.seadmodel.TaxaAuthor;
import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociationType;
import se.sead.model.*;
import se.sead.repositories.*;
import se.sead.sead.model.Biblio;
import se.sead.testutils.DatabaseContentVerification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<SpeciesAssociation> {

    private SpeciesAssociationRepository speciesAssociationRepository;

    private SpeciesAssociationType synonymOfType;

    private TaxaSpecies code01;
    private TaxaSpecies code02;
    private TaxaSpecies code03;
    private TaxaSpecies existingSynonym;

    private TaxaGenus officialGenus;
    private TaxaFamily baseFamily;

    private Biblio ref1;

    DatabaseContentProvider(
            SpeciesAssociationRepository speciesAssociationRepository,
            SpeciesAssociationTypeRepository speciesAssociationTypeRepository,
            SpeciesRepository speciesRepository,
            TaxaGenusRepository genusRepository,
            TaxaFamilyRepository familyRepository,
            BiblioDataRepository biblioDataRepository
    ){
        this.speciesAssociationRepository = speciesAssociationRepository;
        synonymOfType = speciesAssociationTypeRepository.findOne(1);
        code01 = speciesRepository.findOne(1);
        code02 = speciesRepository.findOne(2);
        code03 = speciesRepository.findOne(3);
        existingSynonym = speciesRepository.findOne(4);
        officialGenus = genusRepository.findOne(1);
        baseFamily = familyRepository.findOne(1);
        ref1 = biblioDataRepository.findOne(1);
    }

    @Override
    public List<SpeciesAssociation> getExpectedData() {
        TaxaAuthor synAuthor = TestTaxaAuthor.create(null, "syn authority");
        return Arrays.asList(
                TestSpeciesAssociation.create(
                        1,
                        existingSynonym,
                        code01,
                        synonymOfType,
                        ref1
                ),
                TestSpeciesAssociation.create(
                        null,
                        TestTaxaSpecies.create(
                                null,
                                "alt species name",
                                officialGenus,
                                synAuthor
                        ),
                        code02,
                        synonymOfType,
                        ref1
                ),
                TestSpeciesAssociation.create(
                        null,
                        TestTaxaSpecies.create(
                                null,
                                "alt 2 name",
                                officialGenus,
                                synAuthor
                        ),
                        code01,
                        synonymOfType,
                        null
                ),
                TestSpeciesAssociation.create(
                        null,
                        TestTaxaSpecies.create(
                                null,
                                "alt 3 name",
                                officialGenus,
                                null
                        ),
                        code03,
                        synonymOfType,
                        ref1
                ),
                TestSpeciesAssociation.create(
                        null,
                        TestTaxaSpecies.create(
                                null,
                                code03.getSpeciesName(),
                                TestTaxaGenus.create(
                                        null,
                                        "Syn genus 2",
                                        code03.getGenus().getTaxaFamily()
                                ),
                                synAuthor
                        ),
                        code03,
                        synonymOfType,
                        ref1
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
            if(o1.isErrorFree() && !o2.isErrorFree()){
                return -1;
            } else if(!o1.isErrorFree() && o2.isErrorFree()){
                return 1;
            } else if(o1.isErrorFree() && o2.isErrorFree()){
                return compareProperies(o1,o2);
            } else {
                return compare(o1.getErrors(), o2.getErrors());
            }
        }

        private int compare(List<String> o1Errors, List<String> o2Errors) {
            if(Objects.equals(o1Errors, o2Errors)){
                return 0;
            } else if(o1Errors.size() > o2Errors.size()){
                return -1;
            } else if(o1Errors.size() < o2Errors.size()){
                return 1;
            } else {
                for (int i = 0; i < o1Errors.size(); i++) {
                    String o1 = o1Errors.get(i);
                    String o2 = o2Errors.get(i);
                    int difference = o1.compareTo(o2);
                    if(difference != 0){
                        return difference;
                    }
                }
            }
            return 0;
        }

        private int compareProperies(SpeciesAssociation o1, SpeciesAssociation o2) {
            int difference = o1.getTargetSpecies().getId().compareTo(o2.getTargetSpecies().getId());
            if(difference != 0){
                return difference;
            }
            difference = o1.getSourceSpecies().getSpeciesName().compareTo(o2.getSourceSpecies().getSpeciesName());
            return difference;
        }
    }
}
