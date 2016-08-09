package se.sead.bugsimport.translations.converters;

import java.math.BigDecimal;

public class TranslationHelper {

    public static final Object ERROR_OBJECT = new Object();

    interface TranslationValueConverter<TargetType>{
        TargetType convert(String source);
    }

    enum TranslationConverters {
          INTEGER(new IntegerTranslationValueConverter(), Integer.class)
        , BIG_DECIMAL(new BigDecimalTranslationValueConverter(), BigDecimal.class)
        , DOUBLE(new DoubleTranslationValueConverter(), Double.class)
        , FLOAT(new FloatTranslationValueConverter(), Float.class)
        , STRING(new StringTranslationValueConverter(), String.class)
        ;
        private TranslationValueConverter converter;
        private Class targetType;
        TranslationConverters(TranslationValueConverter converter, Class targetType){
            this.converter = converter;
            this.targetType = targetType;
        }

        TranslationValueConverter getConverter(){
            return converter;
        }

        static TranslationValueConverter getConverterFor(Class targetType){
            for (TranslationConverters converter :
                    values()) {
                if (converter.targetType == targetType) {
                    return converter.getConverter();
                }
            }
            throw new UnsupportedOperationException("no converter for " + targetType);
        }
    }

    public static Object convertToType(Class targetType, String source){
        if(source == null || targetType == null){
            return ERROR_OBJECT;
        }
        return TranslationConverters.getConverterFor(targetType).convert(source);
    }

    public static Object convertToType(Object sourceTypeCarrier, String value){
        if(sourceTypeCarrier == null || value == null){
            return ERROR_OBJECT;
        }
        return convertToType(sourceTypeCarrier.getClass(), value);
    }


}
