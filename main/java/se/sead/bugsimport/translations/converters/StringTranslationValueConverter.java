package se.sead.bugsimport.translations.converters;

class StringTranslationValueConverter implements TranslationHelper.TranslationValueConverter<String> {
    @Override
    public String convert(String source) {
        return source;
    }
}
