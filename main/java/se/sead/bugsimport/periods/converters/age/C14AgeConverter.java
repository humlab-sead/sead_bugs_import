package se.sead.bugsimport.periods.converters.age;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class C14AgeConverter extends AgeConverter{

    public C14AgeConverter(){
        super(Arrays.asList(
                new NullAgeConverter(),
                new C14BPZeroValueHandler(),
                new C14BPDateConversion(),
                new C14NotBPConverter()
        ));
    }

    private static class C14BPZeroValueHandler implements AgeConversion {
        @Override
        public boolean handleValue(String bcad, Integer bugsData) {
            return bcad == null && bugsData == 0;
        }

        @Override
        public BigDecimal convert(String bcad, Integer bugsData) {
            return BigDecimal.ZERO;
        }
    }

    private static class C14BPDateConversion implements AgeConversion {
        @Override
        public boolean handleValue(String bcad, Integer bugsData) {
            return bcad != null && bcad.equals("BP");
        }

        @Override
        public BigDecimal convert(String bcad, Integer bugsData) {
            return AgeBigDecimalHelper.createAge(bugsData);
        }
    }

    private static class C14NotBPConverter implements AgeConversion {
        @Override
        public boolean handleValue(String bcad, Integer bugsData) {
            return bcad != null && !bcad.equals("BP");
        }

        @Override
        public BigDecimal convert(String bcad, Integer bugsData) {
            return null;
        }
    }

}
