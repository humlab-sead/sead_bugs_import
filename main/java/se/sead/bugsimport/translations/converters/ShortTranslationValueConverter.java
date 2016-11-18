package se.sead.bugsimport.translations.converters;

public class ShortTranslationValueConverter implements TranslationValueConverter<Short> {
    @Override
    public Short convert(String source) {
        return Short.decode(source);
    }
}
