package se.sead.translations.utils;

import se.sead.bugsimport.translations.model.TypeTranslation;

public class TypeTranslationBuilder {

    public static TypeTranslation create(
            String bugsTable,
            String sourceColumnName,
            String replacementValue,
            String targetColumn,
            String triggeringColumnValue
    ){
        TypeTranslation translation = new TypeTranslation();
        translation.setBugsTable(bugsTable);
        translation.setBugsColumn(sourceColumnName);
        translation.setReplacementValue(replacementValue);
        translation.setTargetColumn(targetColumn);
        translation.setTriggeringColumnValue(triggeringColumnValue);
        return translation;
    }
}
