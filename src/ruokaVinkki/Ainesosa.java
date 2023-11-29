package ruokaVinkki;

import java.io.*;
import fi.jyu.mit.ohj2.Mjonot;

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
     * @return ainesosan nimi
     */
    public String getNimi() {
        return ainesosaNimi;
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
     * Asettaa ainesosan id:n ja varmistaa, että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param id asetettava tunnusnumero
     */
    private void setAinesosaId(int id) {
        ainesosaId = id;
        if (ainesosaId >= seuraavaId) {
            seuraavaId = ainesosaId + 1;
        }
    }
    
    /**
     * Palauttaa ainesosan tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return ainesosa tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Ainesosa maito = new Ainesosa();
     *   maito.parse("1|maito");
     *   maito.toString().startsWith("1|maito") === true;
     * </pre>  
     */
    @Override
    public String toString() {
        return "" + getAinesosaId() + "|" + ainesosaNimi;
    }
    
    /**
     * Selvittää ainesosan tiedot tolppaerotellusta merkkijonosta
     * @param rivi josta ainesosan tiedot otetaan
     * @example
     * <pre name="test">
     *   Ainesosa maito = new Ainesosa();
     *   maito.parse("1|maito");
     *   maito.getAinesosaId() === 1;
     *   maito.toString().startsWith("1|maito") === true;
     *   maito.rekisteroi();
     *   int n = maito.getAinesosaId();
     *   maito.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
     *   maito.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *   maito.getAinesosaId() === n+20+1;
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setAinesosaId(Mjonot.erota(sb, '|', getAinesosaId()));
        ainesosaNimi = Mjonot.erota(sb, '|', ainesosaNimi);
    }
    
    /**
     * @return ovatko ainesosat samat
     */
    @Override
    public boolean equals(Object ainesosa) {
        if (ainesosa == null) {
            return false;
        }
        return this.toString().equals(ainesosa.toString());
    }
    
    /**
     * @return ainesosan id
     */
    @Override
    public int hashCode() {
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
