package se.sead.bugsimport.attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.attributes.bugsmodel.AttributesBugsTable;
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;

@Component
public class TaxaMeasuredAttributesBugsSeadMapper extends BugsSeadMapper<BugsAttributes, TaxaMeasuredAttributes> {

    @Autowired
    public TaxaMeasuredAttributesBugsSeadMapper(
            TaxaMeasuredAttributesRowConverter rowConverter) {
        super(new AttributesBugsTable(), rowConverter);
    }
}
