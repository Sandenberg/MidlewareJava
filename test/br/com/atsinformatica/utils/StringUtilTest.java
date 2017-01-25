package br.com.atsinformatica.utils;

import junit.framework.ComparisonFailure;
import junit.framework.TestCase;

/**
 *
 * @author niwrodrigues
 */
public class StringUtilTest extends TestCase {

    public void testUnir(){
        String[] itens = new String[]{"1", "2", "3", "4"};
        
        assertEquals("1, 2, 3, 4", StringUtil.unir(", ", itens));
        assertEquals("1, 2, 3 e 4", StringUtil.unir(", ", " e ", itens));
        
        try {
            assertEquals("1, 2, 3 e 4", StringUtil.unir(", ", itens));
            fail("Era para ter dado erro.");
        } catch (ComparisonFailure e) {
            // Tudo normal
        }
        
        try {
            assertEquals("1, 2, 3, 4", StringUtil.unir(", ", " e ", itens));
            fail("Era para ter dado erro.");
        } catch (ComparisonFailure e) {
            // Tudo normal
        }
    }
}
