package se.sead.bugsimport.datescalendar.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.repositories.DatingUncertaintyRepository;

@Component
public class DatingUncertaintyManager {

    private String datingUncertaintyFromName;
    private String datingUncertaintyToName;
    private String datingUncertaintyCaName;
    private String datingUncertaintyFromCaName;
    private String datingUncertaintyToCaName;
    private DatingUncertaintyRepository datingUncertaintyRepository;

    protected DatingUncertainty fromUncertainty;
    protected DatingUncertainty toUncertainty;
    protected DatingUncertainty caUncertainty;
    protected DatingUncertainty fromCaUncertainty;
    protected DatingUncertainty toCaUncertainty;

    @Autowired
    public DatingUncertaintyManager(
            @Value("${dating.uncertainty.from.name:From}")
            String datingUncertaintyFromName,
            @Value("${dating.uncertainty.to.name:To}")
            String datingUncertaintyToName,
            @Value("${dating.uncertainty.ca.name:Ca.}")
            String datingUncertaintyCaName,
            @Value("${dating.uncertainty.fromca.name:From ca.}")
            String datingUncertaintyFromCaName,
            @Value("${dating.uncertainty.toca.name:To ca.}")
            String datingUncertaintyToCaName,
            DatingUncertaintyRepository datingUncertaintyRepository){
        this.datingUncertaintyFromCaName = datingUncertaintyFromCaName;
        this.datingUncertaintyFromName = datingUncertaintyFromName;
        this.datingUncertaintyToName = datingUncertaintyToName;
        this.datingUncertaintyCaName = datingUncertaintyCaName;
        this.datingUncertaintyToCaName = datingUncertaintyToCaName;
        this.datingUncertaintyRepository = datingUncertaintyRepository;
    }

    public DatingUncertainty getOpposite(DatingUncertainty currentUncertainty){
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

    public boolean isFromCaUncertainty(DatingUncertainty currentUncertainty){
        initUncertainties();
        return currentUncertainty.equals(fromCaUncertainty);
    }

    public boolean isFromUncertainty(DatingUncertainty currentUncertainty) {
        initUncertainties();
        return currentUncertainty.equals(fromUncertainty);
    }

    public boolean isToUncertainty(DatingUncertainty currentUncertainty) {
        initUncertainties();
        return currentUncertainty.equals(toUncertainty);
    }

    public boolean isToCaUncertainty(DatingUncertainty currentUncertainty){
        initUncertainties();
        return currentUncertainty.equals(toCaUncertainty);
    }

    public boolean isToUncertaintyWithoutCaValidation(DatingUncertainty uncertainty){
        return isToUncertainty(uncertainty) || isToCaUncertainty(uncertainty);
    }

    public boolean isFromUncertaintyWithoutCaValidation(DatingUncertainty uncertainty){
        return isFromUncertainty(uncertainty) || isFromCaUncertainty(uncertainty);
    }

    public boolean isToOrFromUncertaintyWithoutCaValidation(DatingUncertainty uncertainty){
        return isToUncertaintyWithoutCaValidation(uncertainty) || isFromUncertaintyWithoutCaValidation(uncertainty);
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

    public DatingUncertainty convertUncertainty(DatingUncertainty uncertainty){
        if(isCaUncertainty(uncertainty)){
            return caUncertainty;
        } else {
            return null;
        }
    }

    public boolean isCaUncertainty(DatingUncertainty uncertainty){
        return fromCaUncertainty.equals(uncertainty) ||
                toCaUncertainty.equals(uncertainty) ||
                caUncertainty.equals(uncertainty);
    }
}
