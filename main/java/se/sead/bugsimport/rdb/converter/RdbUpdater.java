package se.sead.bugsimport.rdb.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.country.CountryImportTraceHelper;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.seadmodel.Rdb;
import se.sead.bugsimport.rdbcodes.converter.BugsRdbCodeTraceHelper;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.utils.ErrorCopier;

import java.util.Objects;

@Component
public class RdbUpdater {

    @Autowired
    private BugsRdbCodeTraceHelper codeTraceHelper;
    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;
    @Autowired
    private CountryImportTraceHelper countryTraceHelper;

    public void update(Rdb original, BugsRDB bugsData){
        new Updater(original, bugsData).update();
    }

    private class Updater {
        private Rdb original;
        private BugsRDB bugsData;
        private boolean updated = false;

        Updater(Rdb original, BugsRDB bugsData) {
            this.original = original;
            this.bugsData = bugsData;
        }

        public void update(){
            updated = setCode();
            updated = setSpecies() || updated;
            updated = setCountry() || updated;
            original.setUpdated(updated);
        }

        private boolean setCode(){
            RdbCode originalCode = original.getRdbCode();
            RdbCode codeFromLastTrace = codeTraceHelper.getFromLastTrace(bugsData.getRdbCode());
            if(codeFromLastTrace == null){
                original.addError("No RDB code found");
                return false;
            }
            if(!codeFromLastTrace.isErrorFree()){
                ErrorCopier.copyPotentialErrors(original, codeFromLastTrace);
            }
            original.setRdbCode(codeFromLastTrace);
            return !Objects.equals(originalCode, codeFromLastTrace);
        }

        private boolean setSpecies(){
            TaxaSpecies originalSpecies = original.getSpecies();
            if(bugsData.getCode() == null){
                return noSpeciesError();
            }
            TaxaSpecies species = taxonomicOrderRepository.findBugsSpeciesByCode(bugsData.getCode());
            if(species == null){
                return noSpeciesError();
            }
            if(!species.isErrorFree()){
                ErrorCopier.copyPotentialErrors(original, species);
            }
            original.setSpecies(species);
            return !Objects.equals(originalSpecies, species);
        }

        private boolean noSpeciesError() {
            original.addError("No species found");
            return false;
        }

        private boolean setCountry(){
            Location oldCountry = original.getCountry();
            Location countryFromTrace = countryTraceHelper.getFromLastTrace(bugsData.getCountryCode());
            if(countryFromTrace == null){
                original.addError("No country found");
                return false;
            }
            if(!countryFromTrace.isErrorFree()){
                ErrorCopier.copyPotentialErrors(original, countryFromTrace);
            }
            original.setCountry(countryFromTrace);
            return !Objects.equals(oldCountry, countryFromTrace);
        }
    }
}
