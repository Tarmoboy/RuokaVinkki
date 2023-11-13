package ruokaVinkki;

import java.io.*;

/**
 * Luokka yksittäiselle ainesosalle
 * @author tarmo
 * @version 11.11.2023
 * 
 */
public class Ainesosa {
    private int ainesosaId;
    private String ainesosaNimi;
    private static int seuraavaId = 1;
    
    /**
     * Tyhjä muodostaja
     */
    public Ainesosa() {
    }
    
    /**
     * Tietyn ainesosan alustus
     * @param ainesosaId ainesosan id
     */
    public Ainesosa(int ainesosaId) {
        this.ainesosaId = ainesosaId;
    }
    
    /**
     * @return ainesosan id
     */
    public int getAinesosaId() {
        return ainesosaId;
    }
    
    /**
     * Antaa ainesosalle seuraavan id:n
     * @return uusi ainesosan id
     * @example
     * <pre name="test">
     *   Ainesosa maito = new Ainesosa();
     *   maito.getAinesosaId() === 0;
     *   maito.rekisteroi();
     *   Ainesosa hiiva = new Ainesosa();
     *   hiiva.rekisteroi();
     *   int n1 = maito.getAinesosaId();
     *   int n2 = hiiva.getAinesosaId();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        ainesosaId = seuraavaId;
        seuraavaId++;
        return ainesosaId;
    }
    
    /**
     * Tulostetaan ainesosan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(" Ainesosan ID: " + String.format("%03d", ainesosaId, 3));
        out.println("         Nimi: " + ainesosaNimi);
        out.println("");
    }

    /**
     * Tulostetaan ainesosan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Apumetodi, jolla saadaan täytettyä testinimi ainesosalle
     */
    public void testiAinesosa() {
        ainesosaNimi = "maito";
    }
    
    /**
     * Testiohjelma ainesosalle
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Ainesosa maito = new Ainesosa();
        maito.testiAinesosa();
        maito.rekisteroi();
        maito.tulosta(System.out);
    }
}
