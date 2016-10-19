package se.sead.bugsimport.fossil.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.repositories.MethodGroupRepository;
import se.sead.repositories.MethodRepository;
import se.sead.sead.methods.Method;
import se.sead.sead.methods.MethodGroup;

@Component
public class AbundanceMethodManager {

    @Autowired
    private MethodGroupRepository methodGroupRepository;
    @Autowired
    private MethodRepository methodRepository;

    @Value("${palaeoentomology.method.group:Palaeobiological}")
    private String methodGroupName;
    @Value("${palaeoentomology.method.name:Palaeoentomology}")
    private String methodName;

    private Method palaeoentomology;


    public Method getPalaeoentomology() {
        if(palaeoentomology == null){
            initMethod();
        }
        return palaeoentomology;
    }

    private void initMethod(){
        MethodGroup abundanceMethodGroup = methodGroupRepository.findByName(methodGroupName);
        palaeoentomology = methodRepository.getByNameAndGroup(methodName, abundanceMethodGroup);
        if(abundanceMethodGroup == null || palaeoentomology == null){
            throw new IllegalStateException("Correct method group and method for bugs abundance data is not configured");
        }
    }
}
