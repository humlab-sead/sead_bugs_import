package se.sead.translations.utils;

import se.sead.bugsimport.translations.model.IdBasedTranslation;

public class IdTranslationBuilder {

    public static IdBasedTranslation create(
            String bugsTable,
            String bugsDefinition,
            String targetColumn,
            String replacementValue
    ){
        IdBasedTranslation translation = new IdBasedTranslation();
        translation.setBugsDefinition(bugsDefinition);
        translation.setTargetColumn(targetColumn);
        translation.setReplacementValue(replacementValue);
        translation.setBugsTable(bugsTable);
        return translation;
    }
}
