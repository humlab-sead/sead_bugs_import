package se.sead.bugsimport.periods.converters.age;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class CalendarAgeConverter extends AgeConverter{

    public CalendarAgeConverter(){
        super(Arrays.asList(
            new NullAgeConverter(),
            new CalendarBPConverter(),
                new CalendarBCConverter(),
                new CalendarADConverter()
        ));
    }

    private static class CalendarBPConverter implements AgeConversion {
        @Override
        public boolean handleValue(String bcad, Integer bugsData) {
            return bcad == null ||
                    (bcad != null && bcad.equals("BP"));
        }

        @Override
        public BigDecimal convert(String bcad, Integer bugsData) {
            return BigDecimal.valueOf(bugsData);
        }
    }

    private static class CalendarBCConverter implements AgeConversion {
        @Override
        public boolean handleValue(String bcad, Integer bugsData) {
            return bcad != null && bcad.equals("BC");
        }

        @Override
        public BigDecimal convert(String bcad, Integer bugsData) {
            return AgeBigDecimalHelper.createAge(bugsData + 1950);
        }
    }

    private static class CalendarADConverter implements AgeConversion {
        @Override
        public boolean handleValue(String bcad, Integer bugsData) {
            return bcad != null && bcad.equals("AD");
        }

        @Override
        public BigDecimal convert(String bcad, Integer bugsData) {
            return AgeBigDecimalHelper.createAge(1950 - bugsData);
        }
    }
}
