package se.sead.bugsimport.attributes.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.attributes.bugsmodel.AttributesBugsTable;
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.MeasuredAttributesRepository;

@Component
public class TaxaMeasuredAttributesTraceHelper extends SeadDataFromTraceHelper<BugsAttributes, TaxaMeasuredAttributes> {

    @Autowired
    public TaxaMeasuredAttributesTraceHelper(MeasuredAttributesRepository repository){
        super(AttributesBugsTable.TABLE_NAME, true, repository);
    }
}
