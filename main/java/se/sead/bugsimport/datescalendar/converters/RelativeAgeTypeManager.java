package se.sead.bugsimport.datescalendar.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.periods.seadmodel.RelativeAgeType;
import se.sead.repositories.RelativeAgeTypeRepository;

@Component
public class RelativeAgeTypeManager {

    @Value("${relative.date.range.type:Calendar date range}")
    private String calendarRangeType;

    @Value("${relative.date.type:Calendar date}")
    private String calendarType;

    @Autowired
    private RelativeAgeTypeRepository repository;

    public RelativeAgeType getCalendarRangeType(){
        RelativeAgeType rangeType = repository.findByType(calendarRangeType);
        if(rangeType == null){
            throw new IllegalStateException("No range type matching provided name.");
        }
        return rangeType;
    }

    public String getCalendarRangeTypeName(){
        if(calendarRangeType == null){
            throw new IllegalStateException("No range type specified");
        }
        return calendarRangeType;
    }

    public String getCalendarDateTypeName(){
        if(calendarType == null){
            throw new IllegalStateException("No single type specified");
        }
        return calendarType;
    }
}
