package se.sead.ecocodedefinition;

import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;

import java.util.Comparator;

public class TestEcocodeDefinitionComparator implements Comparator<EcocodeDefinition> {
    @Override
    public int compare(EcocodeDefinition o1, EcocodeDefinition o2) {
        return o1.getAbbreviation().compareTo(o2.getAbbreviation());
    }
}
