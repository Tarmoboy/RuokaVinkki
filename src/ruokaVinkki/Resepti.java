package ruokaVinkki;

import java.io.*;
import java.io.OutputStream;
import java.io.PrintStream;
import fi.jyu.mit.ohj2.Mjonot;

/**
 * Luokka yksittäiselle reseptille
 * @author tarmo
 * @version 29.11.2023
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
     *   Resepti nakkijuustosarvet = new Resepti();
     *   nakkijuustosarvet.testiResepti();
     *   nakkijuustosarvet.getNimi() === "Nakki-juustosarvet";
     * </pre>
     */
    public String getNimi() {
        return reseptiNimi;
    }
    
    /**
     * @return reseptiin tarvittava aika
     * </pre>
     */
    public String getAika() {
        return reseptiAika;
    }
    
    /**
     * @return reseptin ohje
     * </pre>
     */
    public String getOhje() {
        return reseptiOhje;
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
     * Asettaa reseptille id:n ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param id asetettava tunnusnumero
     */
    private void setReseptiId(int id) {
        reseptiId = id;
        if (reseptiId >= seuraavaId) seuraavaId = reseptiId + 1;
    }
    
    /**
     * Palauttaa reseptin tiedot merkkijonona tiedostoon tallentamiseksi
     * @return resepti tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Resepti mikropitsa = new Resepti();
     *   mikropitsa.parse("1|Mikropitsa|2 min| Laita mikropitsa mikroon.");
     *   mikropitsa.toString().startsWith("1|Mikropitsa|2 min") === true;
     * </pre>  
     */
    @Override
    public String toString() {
        return "" + getReseptiId() + "|" + 
                    getNimi() + "|" + 
                    getAika() + "|" + 
                    getOhje().replaceAll("\\n", "_");
    }
    
    /**
     * Selvittää reseptin tiedot tolppaerotellusta merkkijonosta
     * @param rivi käsiteltävä rivi
     * @example
     * <pre name="test">
     *   Resepti mikropitsa = new Resepti();
     *   mikropitsa.parse("1|Mikropitsa|2 min| Laita mikropitsa mikroon.");
     *   mikropitsa.toString().startsWith("1|Mikropitsa|2 min") === true;
     *   mikropitsa.rekisteroi();
     *   int n = mikropitsa.getReseptiId();
     *   mikropitsa.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
     *   mikropitsa.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *   mikropitsa.getReseptiId() === n+20+1;
     * </pre>  
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setReseptiId(Mjonot.erota(sb, '|', getReseptiId()));
        reseptiNimi = Mjonot.erota(sb, '|', getNimi());
        reseptiAika = Mjonot.erota(sb, '|', getAika());
        reseptiOhje = Mjonot.erota(sb, '|', getOhje());
    }
    
    /**
     * @return ovatko reseptit samat
     */
    @Override
    public boolean equals(Object resepti) {
        if (resepti == null) {
            return false;
        }
        return this.toString().equals(resepti.toString());
    }
    
    /**
     * @return reseptin id
     */
    @Override
    public int hashCode() {
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
