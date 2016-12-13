package se.sead.species;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

import java.util.Comparator;

public class SpeciesComparator implements Comparator<TaxaSpecies> {

    private static final String NAME_TEMPLATE = "%s %s %s(%s) %s";

    @Override
    public int compare(TaxaSpecies o1, TaxaSpecies o2) {
        String o1String = createTemplateValue(o1);
        String o2String = createTemplateValue(o2);
        return o1String.compareTo(o2String);
    }

    private String createTemplateValue(TaxaSpecies species) {
        return String.format(
                NAME_TEMPLATE,
                species.getSpeciesName(),
                species.getGenus().getGenusName(),
                species.getGenus().getFamily().getFamilyName(),
                species.getGenus().getFamily().getOrder().getOrderName(),
                (species.getAuthor() != null ? species.getAuthor().getAuthorName() : "")
                );
    }
}
