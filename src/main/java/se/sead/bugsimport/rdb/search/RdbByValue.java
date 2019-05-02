package se.sead.bugsimport.rdb.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.country.CountryImportTraceHelper;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.seadmodel.Rdb;
import se.sead.bugsimport.rdbcodes.converter.BugsRdbCodeTraceHelper;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.repositories.RdbRepository;

@Component
@Order(3)
public class RdbByValue implements RdbSearch {

    @Autowired
    private CountryImportTraceHelper countryImportTraceHelper;
    @Autowired
    private TaxonomicOrderConverter orderConverter;
    @Autowired
    private BugsRdbCodeTraceHelper codeTraceHelper;
    @Autowired
    private RdbRepository rdbRepository;

    @Override
    public Rdb findFor(BugsRDB rdb) {
        Location country = countryImportTraceHelper.getFromLastTrace(rdb.getCountryCode());
        TaxonomicOrder taxonomicOrder = orderConverter.convertToSeadType(rdb.getCode());
        RdbCode codeFromTrace = codeTraceHelper.getFromLastTrace(rdb.getRdbCode());
        if(country == null || taxonomicOrder == null || codeFromTrace == null){
            return NO_RDB_FOUND;
        }
        Rdb byValue = rdbRepository.findByRdbCodeAndSpeciesAndCountry(codeFromTrace, taxonomicOrder.getSpecies(), country);
        if(byValue == null){
            return NO_RDB_FOUND;
        } else {
            return byValue;
        }
    }
}
