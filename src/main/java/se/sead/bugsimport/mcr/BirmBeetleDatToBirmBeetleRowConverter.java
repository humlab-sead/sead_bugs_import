package se.sead.bugsimport.mcr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.mcr.bugsmodel.BirmBeetleDat;
import se.sead.bugsimport.mcr.seadmodel.BirmBeetleData;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.BirmBeetleDataRepository;
import se.sead.repositories.SpeciesRepository;
import se.sead.repositories.TaxonomicOrderRepository;

@Component
public class BirmBeetleDatToBirmBeetleRowConverter implements BugsTableRowConverter<BirmBeetleDat, BirmBeetleData> {

    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;
    @Autowired
    private BirmBeetleDataRepository mcrDataRepository;

    @Override
    public BirmBeetleData convertForDataRow(BirmBeetleDat bugsData) {
        TaxaSpecies species = getSpecies(bugsData);
        return getOrCreateData(bugsData, species);
    }

    private TaxaSpecies getSpecies(BirmBeetleDat bugsData) {
        return taxonomicOrderRepository.findBugsSpeciesByCode(bugsData.getBugsCode());
    }

    private BirmBeetleData getOrCreateData(BirmBeetleDat bugsData, TaxaSpecies species) {
        BirmBeetleData storedData = mcrDataRepository.findBySpeciesAndRowNumber(species, bugsData.getRow());
        if(storedData == null){
            storedData = createData(bugsData, species);
        }
        return storedData;
    }

    private BirmBeetleData createData(BirmBeetleDat bugsData, TaxaSpecies species) {
        BirmBeetleDataCreator creator = new BirmBeetleDataCreator(bugsData, species);
        return creator.create();
    }

    private static class BirmBeetleDataCreator {

        private BirmBeetleData createdData;
        private BirmBeetleDat bugsData;
        private TaxaSpecies species;

        public BirmBeetleDataCreator(BirmBeetleDat bugsData, TaxaSpecies species) {
            this.bugsData = bugsData;
            this.species = species;
        }

        BirmBeetleData create(){
            createdData = new BirmBeetleData();
            setRow();
            setSpecies();
            setMCRData();
            return createdData;
        }

        private void setRow() {
            createdData.setRowNumber(bugsData.getRow());
        }

        private void setSpecies() {
            createdData.setSpecies(species);
            if(species == null){
                createdData.addError(
                        String.format(
                                "Species not found in SEAD, not imported? bugs_code = %2.7f",
                                bugsData.getBugsCode())
                        );
            }
        }

        private void setMCRData() {
            createdData.setMcrData(bugsData.compressFieldValues());
        }
    }
}
