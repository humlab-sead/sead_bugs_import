package se.sead.bugsimport.attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.attributes.converters.TaxaMeasuredAttributesUpdater;
import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;
import se.sead.bugsimport.attributes.search.TaxaMeasuredAttributesSearch;

import java.util.List;

import static se.sead.bugsimport.attributes.search.TaxaMeasuredAttributesSearch.NO_ATTRIBUTES_FOUND;

@Component
public class TaxaMeasuredAttributesRowConverter implements BugsTableRowConverter<BugsAttributes, TaxaMeasuredAttributes> {

    @Autowired
    private List<TaxaMeasuredAttributesSearch> searchStrategies;
    @Autowired
    private TaxaMeasuredAttributesUpdater updater;

    @Override
    public TaxaMeasuredAttributes convertForDataRow(BugsAttributes bugsData) {
        TaxaMeasuredAttributes found = NO_ATTRIBUTES_FOUND;
        for (TaxaMeasuredAttributesSearch search :
                searchStrategies) {
            found = search.findFor(bugsData);
            if(found != NO_ATTRIBUTES_FOUND){
                break;
            }
        }
        if(found == NO_ATTRIBUTES_FOUND){
            return create(bugsData);
        } else {
            return update(found, bugsData);
        }
    }

    private TaxaMeasuredAttributes create(BugsAttributes bugsData){
        return update(new TaxaMeasuredAttributes(), bugsData);
    }

    private TaxaMeasuredAttributes update(TaxaMeasuredAttributes original, BugsAttributes attributes){
        updater.update(original, attributes);
        return original;
    }
}
