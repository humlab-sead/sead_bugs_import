package se.sead.taxanotes;

import se.sead.model.TestBiblio;
import se.sead.sead.model.Biblio;

import java.util.HashMap;
import java.util.Map;

class BiblioBuilder {
    private Map<String, Biblio> data;

    BiblioBuilder(){
        data = new HashMap<>();
        setup();
    }

    private void setup(){
        data.put("Hodge & Jones 1995", TestBiblio.create(2977, "Hodge, P.J. & Jones, R.A. (1995)", "Hodge & Jones 1995", "New British Beetles. Species not in Joy's practical handbook. British Entomological and Natural History Society, Reading."));
        data.put("Hurka 1996", TestBiblio.create(3125, "Hurka, K. (1996)", "Hurka 1996", "Carabidae of the Czech and Slovak Republics. Kabourek, Zlin. (565pp.)"));
    }

    Biblio get(String ref){
        return data.get(ref);
    }
}
