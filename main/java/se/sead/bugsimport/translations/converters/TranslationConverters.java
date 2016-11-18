package se.sead.bugsimport.translations.converters;

import java.math.BigDecimal;

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
