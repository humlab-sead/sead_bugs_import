package se.sead.utils;

import se.sead.sead.model.LoggableEntity;

public class ErrorCopier {

    public static <ErrorReceiver extends LoggableEntity, ErrorProvider extends LoggableEntity>
        void copyPotentialErrors(ErrorReceiver errorReceiver, ErrorProvider errorCarrier){
        if(errorCarrier.isErrorFree()){
            return;
        }
        errorCarrier.getErrors().forEach(error -> errorReceiver.addError(error));
    }
}
