package se.sead.bugsimport.rdbcodes.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.rdbsystems.converters.RdbSystemFromTrace;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.utils.ErrorCopier;

import java.util.Objects;

@Component
public class RdbCodeUpdater {

    @Autowired
    private RdbSystemFromTrace rdbSystemFromTraceHelper;

    public void update(RdbCode code, BugsRDBCodes bugsData){
        Doer doer = new Doer(code, bugsData);
        doer.update();
    }

    private class Doer {

        private RdbCode original;
        private BugsRDBCodes bugsData;
        private boolean updated = true;

        Doer(RdbCode original, BugsRDBCodes bugsData){
            this.original = original;
            this.bugsData = bugsData;
        }

        void update(){
            updated = setCategory();
            updated = setDefinition() || updated;
            updated = setSystem() || updated;
            setUpdated();
        }

        private boolean setCategory() {
            String oldCategory = original.getCategory();
            if(bugsData.getCategory() == null || bugsData.getCategory().isEmpty()){
                original.addError("Empty category");
            }
            original.setCategory(bugsData.getCategory());
            return !Objects.equals(oldCategory, bugsData.getCategory());
        }

        private boolean setDefinition() {
            String oldDefinition = original.getDefinition();
            original.setDefinition(bugsData.getRdbDefinition());
            return !Objects.equals(oldDefinition, bugsData.getRdbDefinition());
        }

        private boolean setSystem() {
            RdbSystem oldSystem = original.getSystem();
            RdbSystem systemFromTrace = rdbSystemFromTraceHelper.getFromLastTrace(String.valueOf(bugsData.getRdbSystemCode()));
            if(systemFromTrace == null || !systemFromTrace.isErrorFree()){
                addErrors(systemFromTrace);
            } else {
                original.setSystem(systemFromTrace);
            }
            return !Objects.equals(oldSystem, systemFromTrace);
        }

        private void addErrors(RdbSystem fromSystem){
            if(fromSystem != null){
                ErrorCopier.copyPotentialErrors(original, fromSystem);
            } else {
                original.addError("No rdb system found");
            }
        }

        private void setUpdated() {
            if(updated){
                original.setUpdated(true);
            }
        }

    }
}
