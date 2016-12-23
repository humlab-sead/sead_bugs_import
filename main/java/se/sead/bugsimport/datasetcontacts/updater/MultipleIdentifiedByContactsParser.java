package se.sead.bugsimport.datasetcontacts.updater;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class MultipleIdentifiedByContactsParser {

    private enum Splitters {
        COMMA(","),
        SEMICOLON(";"),
        AMPERSAND("&")
        ;
        private String separator;
        Splitters(String separator){
            this.separator = separator;
        }

        static Optional<Splitters> getSeparator(String queryString){
            return Arrays.asList(values())
                    .stream()
                    .filter(value -> queryString.contains(value.separator))
                    .findFirst();
        }

        String getSeparator(){
            return separator;
        }
    }

    boolean isMultipleContacts(String identifiedBy){
        return Splitters.getSeparator(identifiedBy).isPresent();
    }

    List<String> splitContacts(String identifiedBy){
        Optional<Splitters> splitter = Splitters.getSeparator(identifiedBy);
        if(splitter.isPresent()){
            return Arrays.asList(identifiedBy.split(splitter.get().getSeparator()));
        } else {
            throw new UnsupportedOperationException("Calling split without testing if supported yet. Call isMultipleContacts() first");
        }
    }
}
