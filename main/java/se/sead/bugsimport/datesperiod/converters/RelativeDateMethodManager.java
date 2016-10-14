package se.sead.bugsimport.datesperiod.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.repositories.MethodGroupRepository;
import se.sead.repositories.MethodRepository;
import se.sead.sead.methods.Method;
import se.sead.sead.methods.MethodGroup;

@Component
public class RelativeDateMethodManager {

    private MethodGroup relativeDateMethodGroup;
    @Autowired
    private MethodRepository methodRepository;

    public RelativeDateMethodManager(
            @Value("${relative.date.method.group.name:Dating to period}")
            String methodGroupName,
            MethodGroupRepository methodGroupRepository
    ){
        relativeDateMethodGroup = methodGroupRepository.findByName(methodGroupName);
    }

    public Method getRelativeDateMethod(String methodName){
        assert relativeDateMethodGroup != null;
        if(methodName == null || methodName.isEmpty()){
            return null;
        }
        Method method = methodRepository.getByAbbreviationAndGroup(methodName, relativeDateMethodGroup);
        if(method == null){
            return new NoMethodFound();
        }
        return method;
    }

    private static class NoMethodFound extends Method {
        NoMethodFound(){
            addError("No dating method found");
        }
    }
}
