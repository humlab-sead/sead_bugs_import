package se.sead.bugsimport.attributes.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;

@Component
@Order(1)
public class TaxaMeasuredAttributeLastTraceSearch implements TaxaMeasuredAttributesSearch {

    @Autowired
    private TaxaMeasuredAttributesTraceHelper traceHelper;

    @Override
    public TaxaMeasuredAttributes findFor(BugsAttributes bugsData) {
        TaxaMeasuredAttributes fromLastTrace = traceHelper.getFromLastTrace(bugsData.compressToString());
        if(fromLastTrace == null){
            return NO_ATTRIBUTES_FOUND;
        }
        return fromLastTrace;
    }
}
