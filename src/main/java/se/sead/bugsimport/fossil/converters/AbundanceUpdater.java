package se.sead.bugsimport.fossil.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.sead.data.Abundance;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.utils.BigDecimalDefinition;

import java.util.Objects;

@Component
public class AbundanceUpdater {

    @Autowired
    private AnalysisEntityManager analysisEntityManager;
    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;

    public void update(Abundance original, Fossil bugsData){
        new Updater(original, bugsData).update();
    }

    private class Updater {
        private Abundance original;
        private Fossil bugsData;

        Updater(Abundance original, Fossil bugsData) {
            this.original = original;
            this.bugsData = bugsData;
        }

        void update(){
            boolean updated = setAbundance();
            updated = setAnalysisEntity(updated) || updated;
            updated = setSpecies() || updated;
            original.setUpdated(updated);
        }

        private boolean setAbundance(){
            Integer originalAbundance = original.getAbundance();
            original.setAbundance(bugsData.getAbundance());
            return !Objects.equals(originalAbundance, bugsData.getAbundance());
        }

        private boolean setAnalysisEntity(boolean abundancesChanged){
            return analysisEntityManager.setAnalysisEntity(original, bugsData, abundancesChanged);
        }

        private boolean setSpecies(){
            if(bugsData.getCode() == null){
                original.addError("No species specified");
                return false;
            }
            TaxaSpecies originalSpecies = original.getSpecies();
            TaxaSpecies bugsSpeciesByCode = taxonomicOrderRepository.findBugsSpeciesByCode(BigDecimalDefinition.convertToSeadCode(bugsData.getCode()));
            if(bugsSpeciesByCode == null){
                original.addError("No species found for code");
                return false;
            }
            original.setSpecies(bugsSpeciesByCode);
            return !Objects.equals(originalSpecies, bugsSpeciesByCode);

        }
    }
}
