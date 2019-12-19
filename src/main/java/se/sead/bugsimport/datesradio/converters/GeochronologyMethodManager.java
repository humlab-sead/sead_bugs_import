package se.sead.bugsimport.datesradio.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.repositories.MethodGroupRepository;
import se.sead.repositories.MethodRepository;
import se.sead.sead.methods.Method;
import se.sead.sead.methods.MethodGroup;

@Component
public class GeochronologyMethodManager {

    private MethodRepository methodRepository;
    private Method defaultMethodIfEmpty;
    private MethodGroup geochronologyMethodGroup;

    @Autowired
    public GeochronologyMethodManager(
            @Value("${geochronology.method.group:Dating by radiometric methods}")
                    String geochronologyMethodGroupName,
            @Value("${geochronology.default.method.abbrev:}")
                    String defaultGeochronologyMethodAbbrev,
            MethodGroupRepository methodGroupRepository,
            MethodRepository methodRepository
    ){
        geochronologyMethodGroup = methodGroupRepository.findByName(geochronologyMethodGroupName);
        this.methodRepository = methodRepository;
        setDefaultMethod(defaultGeochronologyMethodAbbrev);
    }

    private void setDefaultMethod(String defaultMethodName){
        if(defaultMethodName == null || defaultMethodName.isEmpty()){
            defaultMethodIfEmpty = null;
        } else {
            defaultMethodIfEmpty = methodRepository.getByAbbreviation(defaultMethodName);
        }
    }

    public Method get(String datingMethod){
        assert geochronologyMethodGroup != null;
        if(isConfiguredForAllowedEmptyMethod() &&
                (datingMethod == null || datingMethod.isEmpty())){
           return defaultMethodIfEmpty;
        }
        return methodRepository.getByAbbreviation(datingMethod);
    }

    private boolean isConfiguredForAllowedEmptyMethod() {
        return defaultMethodIfEmpty != null;
    }


}
