package se.sead.bugsimport.translations.converters;

class StringTranslationValueConverter implements TranslationValueConverter<String> {
    @Override
    public String convert(String source) {
        return source;
    }

    @Override
    public boolean canConvertValue(String source) {
        return true;
    }
}
