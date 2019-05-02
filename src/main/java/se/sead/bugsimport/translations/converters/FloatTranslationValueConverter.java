package se.sead.bugsimport.translations.converters;

class FloatTranslationValueConverter implements TranslationValueConverter<Float> {
    @Override
    public Float convert(String source) {
        return Float.parseFloat(source);
    }
}
