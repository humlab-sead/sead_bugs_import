package se.sead.bugsimport.translations.converters;

interface TranslationValueConverter<TargetType>{
    TargetType convert(String source);
    default boolean canConvertValue(String source){
        return source != null;
    }
}
