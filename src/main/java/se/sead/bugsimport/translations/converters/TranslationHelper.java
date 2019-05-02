package se.sead.bugsimport.translations.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslationHelper {

    private static final Logger logger = LoggerFactory.getLogger(TranslationHelper.class);

    public static final Object ERROR_OBJECT = new Object();

    public static Object convertToType(Class targetType, String source){
        try {
            TranslationValueConverter converter = TranslationConverters.getConverterFor(targetType);
            if(converter.canConvertValue(source)){
                return converter.convert(source);
            }
        } catch (UnsupportedOperationException uoe){
            if(logger.isErrorEnabled()){
                logger.error("Unsupported converter type requested: " + targetType);
            }
        }
        return ERROR_OBJECT;
    }

    public static Object convertToType(Object sourceTypeCarrier, String value){
        if(sourceTypeCarrier == null){
            return ERROR_OBJECT;
        }
        return convertToType(sourceTypeCarrier.getClass(), value);
    }


}
