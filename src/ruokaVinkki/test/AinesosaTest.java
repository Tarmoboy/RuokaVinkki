package ruokaVinkki.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
import ruokaVinkki.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2023.11.29 15:41:53 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class AinesosaTest {



  // Generated by ComTest BEGIN
  /** testRekisteroi42 */
  @Test
  public void testRekisteroi42() {    // Ainesosa: 42
    Ainesosa maito = new Ainesosa(); 
    assertEquals("From: Ainesosa line: 44", 0, maito.getAinesosaId()); 
    maito.rekisteroi(); 
    Ainesosa hiiva = new Ainesosa(); 
    hiiva.rekisteroi(); 
    int n1 = maito.getAinesosaId(); 
    int n2 = hiiva.getAinesosaId(); 
    assertEquals("From: Ainesosa line: 50", n2-1, n1); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testToString75 */
  @Test
  public void testToString75() {    // Ainesosa: 75
    Ainesosa maito = new Ainesosa(); 
    maito.parse("1|maito"); 
    assertEquals("From: Ainesosa line: 78", true, maito.toString().startsWith("1|maito")); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testParse90 */
  @Test
  public void testParse90() {    // Ainesosa: 90
    Ainesosa maito = new Ainesosa(); 
    maito.parse("1|maito"); 
    assertEquals("From: Ainesosa line: 93", 1, maito.getAinesosaId()); 
    assertEquals("From: Ainesosa line: 94", true, maito.toString().startsWith("1|maito")); 
    maito.rekisteroi(); 
    int n = maito.getAinesosaId(); 
    maito.parse(""+(n+20));  // Otetaan merkkijonosta vain tunnusnumero
    maito.rekisteroi();  // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
    assertEquals("From: Ainesosa line: 99", n+20+1, maito.getAinesosaId()); 
  } // Generated by ComTest END
}