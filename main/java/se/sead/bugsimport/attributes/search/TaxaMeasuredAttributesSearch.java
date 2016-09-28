package se.sead.bugsimport.attributes.search;

import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;

public interface TaxaMeasuredAttributesSearch {

    TaxaMeasuredAttributes NO_ATTRIBUTES_FOUND = new TaxaMeasuredAttributes();

    TaxaMeasuredAttributes findFor(BugsAttributes bugsData);

}
