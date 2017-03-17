package se.sead.bugsimport.taxanotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotes;
import se.sead.bugsimport.taxanotes.seadmodel.TaxonomicNotes;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.TaxonomicNotesRepository;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;

@Component
public class TaxoNotesTableRowConverter implements BugsTableRowConverter<TaxoNotes, TaxonomicNotes> {

    @Autowired
    private TaxonomicOrderConverter orderConverter;
    @Autowired
    private BiblioDataRepository biblioDataRepository;
    @Autowired
    private TaxonomicNotesRepository taxonomicNotesRepository;

    @Override
    public TaxonomicNotes convertForDataRow(TaxoNotes bugsData) {
        TaxaSpecies species = getSpecies(bugsData.getCode());
        Biblio reference = getReference(bugsData.getReference());
        return getOrCreate(bugsData, species, reference);
    }

    private TaxaSpecies getSpecies(Double code) {
        TaxonomicOrder taxonomicOrder = orderConverter.convertToSeadType(code);
        if(taxonomicOrder != null){
            return taxonomicOrder.getSpecies();
        }
        return null;
    }

    private Biblio getReference(String reference) {
        return biblioDataRepository.getByBugsReferenceIgnoreCase(reference);
    }

    private TaxonomicNotes getOrCreate(TaxoNotes bugsData, TaxaSpecies species, Biblio reference){
        TaxonomicNotes alreadyStored = find(bugsData.getData(), species, reference);
        if(alreadyStored == null){
            return new TaxonomicNotesBuilder(bugsData, species, reference).create();
        }
        return alreadyStored;
    }

    private TaxonomicNotes find(String note, TaxaSpecies species, Biblio reference){
        return taxonomicNotesRepository.findByNotesAndSpeciesAndReference(note, species, reference);
    }

    private static class TaxonomicNotesBuilder {
        private TaxonomicNotes seadVersion;
        private TaxoNotes bugsData;
        private TaxaSpecies species;
        private Biblio reference;

        TaxonomicNotesBuilder(TaxoNotes bugsData, TaxaSpecies species, Biblio reference) {
            this.bugsData = bugsData;
            this.species = species;
            this.reference = reference;
        }

        TaxonomicNotes create(){
            seadVersion = new TaxonomicNotes();
            setSpecies();
            setReference();
            setNote();
            return seadVersion;
        }

        private void setSpecies() {
            if(bugsData.getCode() == null){
                seadVersion.addError("No species provided");
            } else if(species == null){
                seadVersion.addError("Species does not exist");
            }
            seadVersion.setSpecies(species);
        }

        private void setReference() {
            if(bugsData.getReference() == null || bugsData.getReference().isEmpty()){
                seadVersion.addError("No reference provided");
            } else if(reference == null){
                seadVersion.addError("Reference does not exist");
            }
            seadVersion.setReference(reference);
        }

        private void setNote() {
            if(bugsData.getData() == null || bugsData.getData().isEmpty()){
                seadVersion.addError("No note provided");
            }
            seadVersion.setNotes(bugsData.getData());
        }
    }
}
