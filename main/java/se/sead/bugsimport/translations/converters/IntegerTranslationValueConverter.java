package se.sead.bugsimport.translations.converters;

class IntegerTranslationValueConverter implements TranslationHelper.TranslationValueConverter<Integer> {
    @Override
    public Integer convert(String source) {
        return Integer.parseInt(source);
    }
}
