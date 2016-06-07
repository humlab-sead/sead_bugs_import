package se.sead.bugsimport.mcr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.mcr.bugsmodel.MCRSummaryData;
import se.sead.bugsimport.mcr.seadmodel.MCRSummary;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.repositories.MCRSummaryRepository;

@Component
public class MCRSummaryTableRowConverter implements BugsTableRowConverter<MCRSummaryData, MCRSummary> {

    @Autowired
    private TaxonomicOrderConverter orderConverter;
    @Autowired
    private MCRSummaryRepository summaryRepository;

    @Override
    public MCRSummary convertForDataRow(MCRSummaryData bugsData) {
        TaxaSpecies species = getSpecies(bugsData.getCode());
        return getOrCreate(bugsData, species);
    }

    private TaxaSpecies getSpecies(Double bugsDataCode) {
        TaxonomicOrder taxonomicOrder = orderConverter.convertToSeadType(bugsDataCode);
        if(taxonomicOrder != null){
            return taxonomicOrder.getSpecies();
        }
        return null;
    }

    private MCRSummary getOrCreate(MCRSummaryData bugsData, TaxaSpecies species){
        MCRSummary found = summaryRepository.findBySpecies(species);
        if(found ==  null){
            return new MCRSummaryBuilder(bugsData, species).create();
        }
        return found;
    }

    private static class MCRSummaryBuilder {
        private MCRSummary seadData;
        private MCRSummaryData bugsData;
        private TaxaSpecies species;

        public MCRSummaryBuilder(MCRSummaryData bugsData, TaxaSpecies species) {
            this.bugsData = bugsData;
            this.species = species;
        }

        MCRSummary create(){
            seadData = new MCRSummary();
            setSpecies();
            setData();
            return seadData;
        }

        private void setSpecies(){
            if(species == null){
                seadData.addError("No species found for code: " + bugsData.getCode());
            }
            seadData.setSpecies(species);
        }

        private void setData(){
            seadData.setMaxLo(bugsData.getMaxLo());
            seadData.setMaxHi(bugsData.getMaxHi());
            seadData.setMinLo(bugsData.getMinLo());
            seadData.setMinHi(bugsData.getMinHi());
            seadData.setRangeLo(bugsData.getRangeLo());
            seadData.setRangeHi(bugsData.getRangeHi());
            seadData.setCogMidRange(bugsData.getCogMidTRange());
            seadData.setCogMidMax(bugsData.getCogMidTMax());
        }
    }
}
