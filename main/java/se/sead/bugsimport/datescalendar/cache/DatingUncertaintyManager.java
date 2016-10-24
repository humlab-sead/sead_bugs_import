package se.sead.bugsimport.datescalendar.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.repositories.DatingUncertaintyRepository;

@Component
public class DatingUncertaintyManager {

    @Value("${dating.uncertainty.from.name:From}")
    private String datingUncertaintyFromName;
    @Value("${dating.uncertainty.to.name:To}")
    private String datingUncertaintyToName;
    @Value("${dating.uncertainty.ca.name:Ca.}")
    private String datingUncertaintyCaName;
    @Value("${dating.uncertainty.fromca.name:From ca.}")
    private String datingUncertaintyFromCaName;
    @Value("${dating.uncertainty.toca.name:To ca.}")
    private String datingUncertaintyToCaName;

    @Autowired
    private DatingUncertaintyRepository datingUncertaintyRepository;

    private DatingUncertainty fromUncertainty;
    private DatingUncertainty toUncertainty;
    private DatingUncertainty caUncertainty;
    private DatingUncertainty fromCaUncertainty;
    private DatingUncertainty toCaUncertainty;

    DatingUncertainty getOpposite(DatingUncertainty currentUncertainty){
        if(currentUncertainty == null){
            return null;
        }
        initUncertainties();
        if(isFromUncertainty(currentUncertainty)) {
            return toUncertainty;
        } else if(isFromCaUncertainty(currentUncertainty)){
            return toCaUncertainty;
        } else if(isToUncertainty(currentUncertainty)) {
            return fromUncertainty;
        } else if(isToCaUncertainty(currentUncertainty)){
            return fromCaUncertainty;
        } else {
            return currentUncertainty;
        }
    }

    boolean isFromCaUncertainty(DatingUncertainty currentUncertainty){
        initUncertainties();
        return currentUncertainty.equals(fromCaUncertainty);
    }

    boolean isFromUncertainty(DatingUncertainty currentUncertainty) {
        initUncertainties();
        return currentUncertainty.equals(fromUncertainty);
    }

    boolean isToUncertainty(DatingUncertainty currentUncertainty) {
        initUncertainties();
        return currentUncertainty.equals(toUncertainty);
    }

    boolean isToCaUncertainty(DatingUncertainty currentUncertainty){
        initUncertainties();
        return currentUncertainty.equals(toCaUncertainty);
    }

    boolean isToUncertaintyWithoutCaValidation(DatingUncertainty uncertainty){
        return isToUncertainty(uncertainty) || isToCaUncertainty(uncertainty);
    }

    boolean isFromUncertaintyWithoutCaValidation(DatingUncertainty uncertainty){
        return isFromUncertainty(uncertainty) || isFromCaUncertainty(uncertainty);
    }

    private void initUncertainties(){
        if(fromUncertainty == null){
            fromUncertainty = datingUncertaintyRepository.findByName(datingUncertaintyFromName);
            toUncertainty = datingUncertaintyRepository.findByName(datingUncertaintyToName);
            caUncertainty = datingUncertaintyRepository.findByName(datingUncertaintyCaName);
            fromCaUncertainty = datingUncertaintyRepository.findByName(datingUncertaintyFromCaName);
            toCaUncertainty = datingUncertaintyRepository.findByName(datingUncertaintyToCaName);
            if(fromUncertainty == null ||
                    toUncertainty == null ||
                    caUncertainty == null ||
                    fromCaUncertainty == null ||
                    toCaUncertainty == null){
                throw new IllegalStateException("No uncertainty for From, To or From Ca., To Ca., or Ca. types found. Check names or configuration.");
            }
        }
    }

    DatingUncertainty convertUncertainty(DatingUncertainty uncertainty){
        if(isCaUncertainty(uncertainty)){
            return caUncertainty;
        } else {
            return null;
        }
    }

    private boolean isCaUncertainty(DatingUncertainty uncertainty){
        return fromCaUncertainty.equals(uncertainty) ||
                toCaUncertainty.equals(uncertainty) ||
                caUncertainty.equals(uncertainty);
    }
}
