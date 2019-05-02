package se.sead.bugsimport.lab.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.repositories.LocationRepository;
import se.sead.utils.errorlog.IgnoredItemErrorLog;

import java.util.Objects;

@Component
public class DatingLabUpdater {

    @Autowired
    private LocationRepository locationRepository;

    public void update(DatingLab original, BugsLab bugsData){
        new Updater(original, bugsData).update();
    }

    private class Updater {
        private DatingLab original;
        private BugsLab bugsData;
        private boolean updated = false;

        public Updater(DatingLab original, BugsLab bugsData) {
            this.original = original;
            this.bugsData = bugsData;
        }

        void update(){
            updated = setLabId();
            updated = setLabName() || updated;
            updated = setCountry() || updated;
            original.setUpdated(updated);
        }

        private boolean setLabId(){
            String originalLabId = original.getLabId();
            original.setLabId(bugsData.getLabId());
            return !Objects.equals(originalLabId, bugsData.getLabId());
        }

        private boolean setLabName(){
            String originalName = original.getName();
            if(bugsData.getLabName() == null || bugsData.getLabName().isEmpty()){
                original.addError("No lab name");
                return false;
            }
            original.setName(bugsData.getLabName());
            return !Objects.equals(originalName, bugsData.getLabName());
        }

        private boolean setCountry(){
            Location originalCountry = original.getCountry();
            if(bugsData.getCountry() == null || bugsData.getCountry().isEmpty()){
                original.addError("No country specified");
                return false;
            }
            if(bugsData.getCountry().equals("Country")){
                original.addError(new IgnoredItemErrorLog(""));
                return false;
            }
            Location countryByName = locationRepository.findCountryByName(bugsData.getCountry());
            if(countryByName == null){
                original.addError("No country found");
                return false;
            }
            original.setCountry(countryByName);
            return !Objects.equals(originalCountry, countryByName);
        }
    }
}
