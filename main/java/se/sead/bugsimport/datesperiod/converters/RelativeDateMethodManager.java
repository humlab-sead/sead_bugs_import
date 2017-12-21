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

import java.util.Arrays;
import java.util.List;

@Component
public class RelativeDateMethodManager {

    private MethodGroup relativeDateMethodGroup;

    private List<MethodAbbreviationMatching> methodMatchers;

    @Autowired
    public RelativeDateMethodManager(
            @Value("${relative.date.method.group.name:Dating to period}")
                    String methodGroupName,
            @Value("${relative.date.default.unknown.cal.method:UnknownCal}")
                    String defaultUnknownCAlMethodName,
            MethodGroupRepository methodGroupRepository,
            MethodRepository methodRepository,
            PeriodTraceHelper periodTraceHelper){
        relativeDateMethodGroup = methodGroupRepository.findByName(methodGroupName);

        methodMatchers = Arrays.asList(
                new EmptyBugsMethodAbbreviationName(methodRepository.getByAbbreviation(defaultUnknownCAlMethodName)),
                new DirectAbbreviationMethodAbbreviation(methodRepository),
                new ComputedMethodAbbreviationNameFromPeriodYearType(methodRepository, periodTraceHelper)
        );
    }

    public Method getRelativeDateMethod(String bugsMethodName, RelativeAge relativeAge){
        assert relativeDateMethodGroup != null;
        Method method = getMethod(bugsMethodName, relativeAge);
        if(method == null){
            return new NoMethodFound();
        }
        return method;
    }

    private Method getMethod(String bugsMethodName, RelativeAge relativeAge) {
        for (MethodAbbreviationMatching matcher :
                methodMatchers) {
            Method foundMethod = matcher.getMethod(bugsMethodName, relativeAge);
            if(foundMethod != null){
                return foundMethod;
            }
        }
        return null;
    }

    private static class NoMethodFound extends Method {
        NoMethodFound(){
            addError("No dating method found");
        }
    }

    private interface MethodAbbreviationMatching {
        Method getMethod(String methodAbbreviation, RelativeAge relativeAge);
    }

    private static class EmptyBugsMethodAbbreviationName implements MethodAbbreviationMatching {

        private Method defaultEmptyMethod;

        public EmptyBugsMethodAbbreviationName(Method defaultEmptyMethod) {
            this.defaultEmptyMethod = defaultEmptyMethod;
        }

        @Override
        public Method getMethod(String methodAbbreviation, RelativeAge relativeAge) {
            if(methodAbbreviation == null || methodAbbreviation.trim().isEmpty()){
                return defaultEmptyMethod;
            }
            return null;
        }
    }

    private static class DirectAbbreviationMethodAbbreviation implements MethodAbbreviationMatching {

        private MethodRepository repository;

        private DirectAbbreviationMethodAbbreviation(MethodRepository repository) {
            this.repository = repository;
        }

        @Override
        public Method getMethod(String methodAbbreviation, RelativeAge relativeAge) {
            return repository.getByAbbreviation(methodAbbreviation);
        }
    }

    private static class ComputedMethodAbbreviationNameFromPeriodYearType implements MethodAbbreviationMatching {

        private MethodRepository repository;
        private PeriodTraceHelper periodTraceHelper;

        private ComputedMethodAbbreviationNameFromPeriodYearType(MethodRepository repository, PeriodTraceHelper periodTraceHelper) {
            this.repository = repository;
            this.periodTraceHelper = periodTraceHelper;
        }

        @Override
        public Method getMethod(String methodAbbreviation, RelativeAge relativeAge) {
            return repository.getByAbbreviation(buildMethodName(methodAbbreviation, relativeAge));
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
    }
}
