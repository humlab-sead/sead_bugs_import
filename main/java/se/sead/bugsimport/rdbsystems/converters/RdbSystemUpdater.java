package se.sead.bugsimport.rdbsystems.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.repositories.BiblioDataRepository;
import se.sead.sead.model.Biblio;
import se.sead.utils.ErrorCopier;

import java.util.Objects;

@Component
public class RdbSystemUpdater {

    @Autowired
    private BiblioDataRepository bibliographyRepository;
    @Autowired
    private RdbSystemCountryConverter countryConverter;

    public RdbSystem update(RdbSystem original, BugsRDBSystem bugsData){
        UpdateDoer updater = new UpdateDoer(original, bugsData);
        updater.update();
        return original;
    }

    private class UpdateDoer {
        private RdbSystem original;
        private BugsRDBSystem bugsData;

        public UpdateDoer(RdbSystem original, BugsRDBSystem bugsData) {
            this.original = original;
            this.bugsData = bugsData;
        }

        void update(){
            boolean updated = false;
            updated = setName() || updated;
            updated = setSystemDate() || updated;
            updated = setFirstPublishedDate() || updated;
            updated = setVersion() || updated;
            updated = setReference() || updated;
            updated = setLocation() || updated;
            if(!original.isNewItem() && updated){
                original.setUpdated(true);
            }
        }

        private boolean setName() {
            if(bugsData.getRdbSystem() == null || bugsData.getRdbSystem().isEmpty()){
                original.addError("No system name found");
            }
            String oldName = original.getSystemName();
            original.setSystemName(bugsData.getRdbSystem());
            return !Objects.equals(oldName, bugsData.getRdbSystem());
        }

        private boolean setSystemDate() {
            Integer oldDate = original.getSystemDate();
            original.setSystemDate(bugsData.getRdbSystemDate());
            return !Objects.equals(oldDate, bugsData.getRdbSystemDate());
        }

        private boolean setFirstPublishedDate() {
            Short oldValue = original.getFirstPublished();
            original.setFirstPublished(bugsData.getRdbFirstPublished());
            return !Objects.equals(oldValue, bugsData.getRdbFirstPublished());
        }

        private boolean setVersion() {
            String oldVersion = original.getVersion();
            original.setVersion(bugsData.getRdbVersion());
            return !Objects.equals(oldVersion, bugsData.getRdbVersion());
        }

        private boolean setReference() {
            Biblio reference = bibliographyRepository.getByBugsReferenceIgnoreCase(bugsData.getRef());
            Biblio oldReference = original.getReference();
            original.setReference(reference);
            if(reference == null){
                original.addError("Empty bibliography");
            }
            return !Objects.equals(oldReference, reference);
        }

        private boolean setLocation() {
            Location country = countryConverter.getLocation(bugsData);
            ErrorCopier.copyErrors(original, country);
            Location oldLocation = original.getLocation();
            original.setLocation(country);
            return !Objects.equals(oldLocation, country);
        }
    }


}
