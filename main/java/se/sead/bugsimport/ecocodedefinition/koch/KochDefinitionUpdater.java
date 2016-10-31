package se.sead.bugsimport.ecocodedefinition.koch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.ecocodedefinition.koch.bugsmodel.EcoDefKoch;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.ecocodedefinitiongroups.converters.KochSystemManager;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;
import se.sead.repositories.EcocodeGroupRepository;

import java.util.Objects;

@Component
public class KochDefinitionUpdater {

    @Autowired
    private EcocodeGroupRepository groupRepository;
    @Autowired
    private KochSystemManager kochSystemManager;

    public void update(EcocodeDefinition original, EcoDefKoch bugsData){
        new Updater(original, bugsData).update();
    }

    private class Updater {
        private EcocodeDefinition original;
        private EcoDefKoch bugsData;
        Updater(EcocodeDefinition original, EcoDefKoch bugsData){
            this.original = original;
            this.bugsData = bugsData;
        }

        void update(){
            boolean updated = setAbbreviation();
            updated = setLabel() || updated;
            updated = setDefinition() || updated;
            updated = setNotes() || updated;
            updated = setGroup() || updated;
            original.setUpdated(updated);
        }

        private boolean setAbbreviation() {
            String originalAbbreviation = original.getAbbreviation();
            original.setAbbreviation(bugsData.getBugsKochCode());
            return !Objects.equals(originalAbbreviation, bugsData.getBugsKochCode());
        }

        private boolean setLabel() {
            String originalLabel = original.getName();
            original.setName(bugsData.getFullName());
            return !Objects.equals(originalLabel, bugsData.getFullName());
        }

        private boolean setDefinition() {
            String originalDefinition = original.getDefinition();
            original.setDefinition(bugsData.getDescription());
            return !Objects.equals(originalDefinition, bugsData.getDescription());
        }

        private boolean setNotes() {
            String originalNotes = original.getNotes();
            original.setNotes(bugsData.getNotes());
            return !Objects.equals(originalNotes, bugsData.getNotes());
        }

        private boolean setGroup() {
            if(bugsData.getKochGroup() == null || bugsData.getKochGroup().isEmpty()){
                original.addError("No group specified");
                return false;
            }
            EcocodeGroup originalGroup = original.getGroup();
            EcocodeGroup fromBugsData = groupRepository.findBySystemAndAbbreviation(kochSystemManager.getKochSystem(), bugsData.getKochGroup());
            if(fromBugsData == null){
                original.addError("No group found for code");
                return false;
            }
            original.setGroup(fromBugsData);
            return !Objects.equals(originalGroup, fromBugsData);
        }


    }
}
