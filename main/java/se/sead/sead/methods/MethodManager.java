package se.sead.sead.methods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.sead.repositories.MethodGroupRepository;
import se.sead.repositories.MethodRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class MethodManager {

    @Value("${sampling.methodname:Temporary record}")
    private String samplingMethodName;
    @Value("${sampling.methodgroup:Sampling}")
    private String samplingMethodGroup;

    @Autowired
    private MethodRepository methodRepository;
    @Autowired
    private MethodGroupRepository methodGroupRepository;

    private Map<String, Method> methodCache;

    public MethodManager(){
        methodCache = new HashMap<>();
    }

    public Method getSamplingMethodName(){
        Method importSamplingMethod = methodCache.get(samplingMethodName);
        if(importSamplingMethod == null) {
            MethodGroup group = methodGroupRepository.findByName(samplingMethodGroup);
            importSamplingMethod = methodRepository.getByNameAndGroup(samplingMethodName, group);
            if (importSamplingMethod == null) {
                throw new IllegalStateException("No sampling method found with name: " + samplingMethodName);
            } else {
                methodCache.put(samplingMethodName, importSamplingMethod);
            }
        }
        return importSamplingMethod;
    }

}
