package se.sead.bugsimport.translations.converters;

class FloatTranslationValueConverter implements TranslationHelper.TranslationValueConverter<Float> {
    @Override
    public Float convert(String source) {
        return Float.parseFloat(source);
    }
}
