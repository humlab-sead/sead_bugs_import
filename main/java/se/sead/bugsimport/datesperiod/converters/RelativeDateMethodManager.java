package se.sead.bugsimport.datesperiod.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.PeriodTraceHelper;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.repositories.MethodGroupRepository;
import se.sead.repositories.MethodRepository;
import se.sead.sead.methods.Method;
import se.sead.sead.methods.MethodGroup;

@Component
public class RelativeDateMethodManager {

    private MethodGroup relativeDateMethodGroup;
    @Autowired
    private MethodRepository methodRepository;
    @Autowired
    private PeriodTraceHelper periodTraceHelper;

    public RelativeDateMethodManager(
            @Value("${relative.date.method.group.name:Dating to period}")
            String methodGroupName,
            MethodGroupRepository methodGroupRepository
    ){
        relativeDateMethodGroup = methodGroupRepository.findByName(methodGroupName);
    }

    public Method getRelativeDateMethod(String bugsMethodName, RelativeAge relativeAge){
        assert relativeDateMethodGroup != null;
        if(bugsMethodName == null || bugsMethodName.isEmpty()){
            return null;
        }
        String seadMethodName = buildMethodName(bugsMethodName, relativeAge);
        Method method = methodRepository.getByAbbreviationAndGroup(seadMethodName, relativeDateMethodGroup);
        if(method == null){
            return new NoMethodFound();
        }
        return method;
    }

    private String buildMethodName(String bugsMethodName, RelativeAge relativeAge) {
        Period periodFromTrace = periodTraceHelper.getPeriodFromTrace(relativeAge);
        String suffix = "";
        switch(periodFromTrace.getYearsType()){
            case "Calendar": suffix = "Cal";break;
            case "C14": suffix = "C14";break;
            case "Radiometric": suffix = "Radio";break;
        }
        return bugsMethodName + suffix;
    }

    private static class NoMethodFound extends Method {
        NoMethodFound(){
            addError("No dating method found");
        }
    }
}
