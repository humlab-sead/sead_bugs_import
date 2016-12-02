package se.sead.bugsimport.species.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaOrder;
import se.sead.repositories.TaxaFamilyRepository;
import se.sead.repositories.TaxaOrderRepository;

@Component
public class TaxaFamilyConverter {

    private TaxaFamilyRepository familyRepository;
    private TaxaOrderInstanceCarrier taxaOrderCarrier;

    @Autowired
    public TaxaFamilyConverter(TaxaFamilyRepository familyRepository, TaxaOrderRepository orderRepository){
        this.familyRepository = familyRepository;
        this.taxaOrderCarrier = new TaxaOrderInstanceCarrier(orderRepository);
    }

    public TaxaFamily convertToSeadType(String bugsData) {
        if(bugsData == null || bugsData.isEmpty()){
            return createErrorFamily();
        }
        TaxaFamily bugsFamily = familyRepository.findByFamilyNameAndOrderId(bugsData, taxaOrderCarrier.getDefaultTaxaOrder());
        if(bugsFamily == null){
            return createFamily(bugsData);
        }
        return bugsFamily;
    }

    private TaxaFamily createErrorFamily(){
        TaxaFamily error = new TaxaFamily();
        error.addError("No family specified");
        return error;
    }

    private TaxaFamily createFamily(String bugsData) {
        TaxaFamily family = new TaxaFamily();
        family.setFamilyName(bugsData);
        family.setOrderId(taxaOrderCarrier.getDefaultTaxaOrder());
        return family;
    }

    private static class TaxaOrderInstanceCarrier {
        private TaxaOrderRepository orderRepository;
        private TaxaOrder defaultTaxaOrder;
        TaxaOrderInstanceCarrier(TaxaOrderRepository repository){
            this.orderRepository = repository;
        }

        TaxaOrder getDefaultTaxaOrder(){
            if(defaultTaxaOrder == null){
                loadDefaultTaxaOrder();
            }
            return defaultTaxaOrder;
        }

        private void loadDefaultTaxaOrder(){
            defaultTaxaOrder = orderRepository.getImportOrder();
        }
    }
}
