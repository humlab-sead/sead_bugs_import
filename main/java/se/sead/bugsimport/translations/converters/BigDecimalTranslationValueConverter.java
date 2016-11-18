package se.sead.bugsimport.translations.converters;

import java.math.BigDecimal;

class BigDecimalTranslationValueConverter implements TranslationValueConverter<BigDecimal> {
    @Override
    public BigDecimal convert(String source) {
        return new BigDecimal(source);
    }
}
