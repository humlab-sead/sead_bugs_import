package se.sead.bugsimport.datasetcontacts.updater;

import org.junit.Before;
import org.junit.Test;

import javax.persistence.Temporal;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MultipleIdentifiedByContactsParserTest {

    private MultipleIdentifiedByContactsParser parser;

    @Before
    public void setup(){
        parser = new MultipleIdentifiedByContactsParser();
    }

    @Test
    public void noSeparatorFound(){
        assertFalse(parser.isMultipleContacts("Author name"));
    }

    @Test
    public void canHandleCommaSeparatedAuthors(){
        assertTrue(parser.isMultipleContacts("Author, Next Author"));
    }

    @Test
    public void canHandleSemiColonSeparatedAuthors(){
        assertTrue(parser.isMultipleContacts("Author; Next Author"));
    }

    @Test
    public void canHandleAmpersandSeparatedAuthors(){
        assertTrue(parser.isMultipleContacts("Author & Next Author"));
    }

    @Test
    public void splitOnComma(){
        List<String> authors = parser.splitContacts("Author,Next Author");
        assertEquals(2, authors.size());
        assertEquals("Author", authors.get(0));
        assertEquals("Next Author", authors.get(1));
    }

    @Test
    public void splitOnSemiColon(){
        List<String> authors = parser.splitContacts("Author;Next Author");
        assertEquals(2, authors.size());
        assertEquals("Author", authors.get(0));
        assertEquals("Next Author", authors.get(1));
    }

    @Test
    public void splitOnAmpersand(){
        List<String> authors = parser.splitContacts("Author&Next Author");
        assertEquals(2, authors.size());
        assertEquals("Author", authors.get(0));
        assertEquals("Next Author", authors.get(1));
    }

    @Test
    public void splitThreeAuthorsCommaSeparated(){
        List<String> authors = parser.splitContacts("Author,Next Author,Last Author");
        assertEquals(3, authors.size());
        assertEquals("Author", authors.get(0));
        assertEquals("Next Author", authors.get(1));
        assertEquals("Last Author", authors.get(2));
    }

    @Test
    public void mixedSeparators(){
        List<String> authors = parser.splitContacts("Author,Next Author & Last Author");
        assertEquals(2, authors.size());
        assertEquals("Author", authors.get(0));
        assertEquals("Next Author & Last Author", authors.get(1));
    }
}
