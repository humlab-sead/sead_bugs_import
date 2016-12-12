package se.sead.bugsimport.countsheets.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.sead.repositories.MethodGroupRepository;
import se.sead.repositories.MethodRepository;
import se.sead.sead.methods.Method;
import se.sead.sead.methods.MethodGroup;

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

    private Method samplingMethod;

    public Method getSamplingMethodName(){
        if(samplingMethod == null) {
            MethodGroup group = methodGroupRepository.findByName(samplingMethodGroup);
            samplingMethod = methodRepository.getByNameAndGroup(samplingMethodName, group);
            if (samplingMethod == null) {
                throw new IllegalStateException("No sampling method found with name: " + samplingMethodName);
            }
        }
        return samplingMethod;
    }

}
