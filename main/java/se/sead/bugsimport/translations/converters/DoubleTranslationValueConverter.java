package se.sead.bugsimport.translations.converters;

class DoubleTranslationValueConverter implements TranslationHelper.TranslationValueConverter<Double> {
    @Override
    public Double convert(String source) {
        return Double.parseDouble(source);
    }
}
