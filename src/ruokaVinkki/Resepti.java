package ruokaVinkki;

import java.io.*;

/**
 * Luokka yksittäiselle reseptille
 * @author tarmo
 * @version 11.11.2023
 *
 */
public class Resepti {
    private int reseptiId;
    private String reseptiNimi = "";
    private String reseptiAika = "";
    private String reseptiOhje = "";
    private static int seuraavaId = 1;
    
    /**
     * @return reseptin id
     */
    public int getReseptiId() {
        return reseptiId;
    }
    
    /**
     * @return reseptin nimi
     * @example
     * <pre name="test">
     *   Resepti mikropitsa = new Resepti();
     *   mikropitsa.testiResepti();
     *   mikropitsa.getNimi() === "Mikropitsa";
     * </pre>
     */
    public String getNimi() {
        return reseptiNimi;
    }
    
    /**
     * Antaa reseptille seuraavan id:n
     * @return uusi reseptin id
     * @example
     * <pre name="test">
     *   Resepti mikropitsa = new Resepti();
     *   mikropitsa.getReseptiId() === 0;
     *   mikropitsa.rekisteroi();
     *   Resepti makaroni = new Resepti();
     *   makaroni.rekisteroi();
     *   int n1 = mikropitsa.getReseptiId();
     *   int n2 = makaroni.getReseptiId();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        reseptiId = seuraavaId;
        seuraavaId++;
        return reseptiId;
    }
    
    /**
     * Tulostetaan reseptin tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("  Reseptin ID: " + String.format("%03d", reseptiId, 3));
        out.println("         Nimi: " + reseptiNimi);
        out.println("Valmistusaika: " + reseptiAika);
        out.println("         Ohje: " + reseptiOhje);
        out.println("");
    }
    
    /**
     * Tulostetaan reseptin tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot reseptille
     */
    public void testiResepti() {
        reseptiNimi = "Nakki-juustosarvet";
        reseptiAika = "1 h 30 min";
        reseptiOhje = "Liuota hiiva kädenlämpöiseen maitoon.";
    }
    
    /**
     * Testiohjelma reseptille
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Resepti nakkijuustosarvet = new Resepti();
        nakkijuustosarvet.rekisteroi();
        nakkijuustosarvet.tulosta(System.out);
        nakkijuustosarvet.testiResepti();
        nakkijuustosarvet.tulosta(System.out);
        Resepti makaroni = new Resepti();
        makaroni.rekisteroi();
        makaroni.tulosta(System.out);
    }
}
