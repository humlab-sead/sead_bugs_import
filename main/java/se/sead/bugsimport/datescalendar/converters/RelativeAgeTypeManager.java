package se.sead.bugsimport.datescalendar.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RelativeAgeTypeManager {

    private String calendarRangeType;
    private String calendarType;

    @Autowired
    public RelativeAgeTypeManager(
            @Value("${relative.date.range.type:Calendar date range}")
            String calendarRangeType,
            @Value("${relative.date.type:Calendar date}")
            String calendarType){
        this.calendarRangeType = calendarRangeType;
        this.calendarType = calendarType;
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
