package se.sead.translations;

import org.junit.Test;
import se.sead.bugsimport.translations.converters.TranslationHelper;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TranslationHelperTest {

    @Test
    public void strings(){
        assertEquals("test", TranslationHelper.convertToType(String.class, "test"));
    }

    @Test
    public void integers(){
        assertEquals(1, TranslationHelper.convertToType(Integer.class, "1"));
    }

    @Test
    public void doubles(){
        assertEquals(1.00d, TranslationHelper.convertToType(Double.class, "1.00"));
    }

    @Test
    public void floats(){
        assertEquals(1.0f, TranslationHelper.convertToType(Float.class, "1.0"));
    }

    @Test
    public void nulls(){
        assertEquals(TranslationHelper.ERROR_OBJECT, TranslationHelper.convertToType(Object.class, null));
        assertEquals(TranslationHelper.ERROR_OBJECT, TranslationHelper.convertToType(null, "This cannot be converted"));
    }

    @Test
    public void bigDecimals(){
        assertEquals(BigDecimal.ONE, TranslationHelper.convertToType(BigDecimal.class, "1"));
    }
}
