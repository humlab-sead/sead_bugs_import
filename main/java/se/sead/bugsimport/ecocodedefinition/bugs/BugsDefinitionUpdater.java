package se.sead.bugsimport.ecocodedefinition.bugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugs;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;
import se.sead.repositories.EcocodeGroupRepository;

import java.util.Objects;

@Component
public class BugsDefinitionUpdater {

    @Autowired
    private EcocodeGroupRepository groupRepository;

    public void update(EcocodeDefinition original, EcoDefBugs bugsData){
        new Updater(original, bugsData).update();
    }

    private class Updater {
        private EcocodeDefinition original;
        private EcoDefBugs bugsData;

        Updater(EcocodeDefinition original, EcoDefBugs bugsData) {
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
            original.setAbbreviation(bugsData.getBugsEcoCODE());
            return !Objects.equals(originalAbbreviation, bugsData.getBugsEcoCODE());
        }

        private boolean setLabel() {
            if(bugsData.getEcoLabel() == null || bugsData.getEcoLabel().isEmpty()){
                original.addError("No label specified");
                return false;
            }
            String originalLabel = original.getName();
            original.setName(bugsData.getEcoLabel());
            return !Objects.equals(originalLabel, bugsData.getEcoLabel());
        }

        private boolean setDefinition() {
            if(bugsData.getDefinition() == null || bugsData.getDefinition().isEmpty()){
                original.addError("No definition specified");
                return false;
            }
            String originalDefinition = original.getDefinition();
            original.setDefinition(bugsData.getDefinition());
            return !Objects.equals(originalDefinition, bugsData.getDefinition());
        }

        private boolean setNotes() {
            String originalNotes = original.getNotes();
            original.setNotes(bugsData.getNotes());
            return !Objects.equals(originalNotes, bugsData.getNotes());
        }

        private boolean setGroup() {
            EcocodeGroup originalGroup = original.getGroup();
            EcocodeGroup fromBugsData = groupRepository.getBugsGroup();
            original.setGroup(fromBugsData);
            return !Objects.equals(originalGroup, fromBugsData);
        }
    }
}
