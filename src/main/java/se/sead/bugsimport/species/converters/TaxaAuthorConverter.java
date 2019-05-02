package se.sead.bugsimport.species.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.seadmodel.TaxaAuthor;
import se.sead.repositories.TaxaAuthorRepository;

/**
 * Created by erer0001 on 2016-04-28.
 */
@Component
public class TaxaAuthorConverter{

    @Autowired
    private TaxaAuthorRepository authorRepository;

    public TaxaAuthor convertToSeadType(String authorName) {
        if(authorName == null){
            return null;
        }
        TaxaAuthor byAuthorName = authorRepository.findByAuthorName(authorName);
        if(byAuthorName == null){
            return createAuthor(authorName);
        }
        return byAuthorName;
    }

    private TaxaAuthor createAuthor(String authorName) {
        TaxaAuthor author = new TaxaAuthor();
        author.setAuthorName(authorName);
        return author;
    }
}
