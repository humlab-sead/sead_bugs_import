package se.sead.bugsimport.ecocodedefinitiongroups.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroups;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;
import se.sead.repositories.EcocodeSystemRepository;

import java.util.Objects;

@Component
public class EcocodeGroupUpdater {

    @Autowired
    private EcocodeSystemRepository systemRepository;

    public void update(EcocodeGroup original, EcoDefGroups bugsData){
        new Updater(original, bugsData).update();
    }

    private class Updater {
        private EcocodeGroup original;
        private EcoDefGroups bugsData;

        Updater(EcocodeGroup original, EcoDefGroups bugsData) {
            this.original = original;
            this.bugsData = bugsData;
        }

        void update(){
            boolean updated = setName();
            updated = setAbbreviation() || updated;
            setKochSystem();
            original.setUpdated(updated);
        }

        private boolean setName(){
            if(bugsData.getEcoName() == null || bugsData.getEcoName().isEmpty()){
                original.addError("No ecocode group name");
                return false;
            }
            String originalName = original.getName();
            original.setName(bugsData.getEcoName());
            return !Objects.equals(originalName, bugsData.getEcoName());
        }

        private boolean setAbbreviation(){
            String originalAbbreviation = original.getAbbreviation();
            original.setAbbreviation(bugsData.getEcoGroupCode());
            return !Objects.equals(originalAbbreviation, bugsData.getEcoGroupCode());
        }

        private void setKochSystem(){
            if(original.getSystem() == null){
                original.setSystem(systemRepository.findKochSystem());
            }
        }
    }
}
