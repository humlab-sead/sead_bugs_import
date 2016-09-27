package se.sead.bugsimport.mcrnames.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.mcrnames.seadmodel.MCRName;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.utils.BigDecimalDefinition;

import java.util.Objects;

@Component
public class McrNameUpdater {

    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;

    public void update(MCRName original, BugsMCRNames bugsData){
        new Updater(original, bugsData).update();
    }

    private class Updater {
        private MCRName original;
        private BugsMCRNames bugsData;
        private boolean updated = false;

        Updater(MCRName original, BugsMCRNames bugsData) {
            this.original = original;
            this.bugsData = bugsData;
        }

        void update(){
            updated = setSpecies();
            updated = setMCRNumber() || updated;
            updated = setMCRName() || updated;
            updated = setStatus() || updated;
            updated = setTrimName() || updated;
            original.setUpdated(updated);
        }

        private boolean setSpecies(){
            Integer originalId = original.getId();
            TaxaSpecies bugsSpeciesByCode = taxonomicOrderRepository.findBugsSpeciesByCode(BigDecimalDefinition.convertToSeadCode(bugsData.getCode()));
            if(bugsSpeciesByCode == null){
                original.addError("No species found for code");
                return false;
            }
            original.setSpecies(bugsSpeciesByCode);
            return !Objects.equals(originalId, bugsSpeciesByCode.getId());
        }

        private boolean setMCRNumber(){
            Short originalMcrNumber = original.getMcrNumber();
            original.setMcrNumber(bugsData.getMcrNumber());
            return !Objects.equals(originalMcrNumber, bugsData.getMcrNumber());
        }

        private boolean setMCRName(){
            String originalMcrName = original.getMcrName();
            original.setMcrName(bugsData.getMcrName());
            return !Objects.equals(originalMcrName, bugsData.getMcrName());
        }

        private boolean setStatus(){
            String originalNotes = original.getNotes();
            original.setNotes(bugsData.getCompareStatus());
            return !Objects.equals(originalNotes, bugsData.getCompareStatus());
        }

        private boolean setTrimName(){
            String originalShortName = original.getShortName();
            original.setShortName(bugsData.getMcrNameTrim());
            return !Objects.equals(originalShortName, bugsData.getMcrNameTrim());
        }
    }
}
