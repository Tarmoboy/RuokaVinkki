package ruokaVinkki;

import java.io.*;
import fi.jyu.mit.ohj2.Mjonot;

/**
 * Luokka yksittäisen reseptin yksittäiselle ainesosalle
 * @author tarmo
 * @version 10.12.2023
 * 
 */
public class ReseptinAinesosa {
    private int reseptiId;
    private int ainesosaId;
    private String maara ="";
    
    /**
     * Tyhjä muodostaja
     */
    public ReseptinAinesosa() {
    }
    
    /**
     * @return reseptin id
     */
    public int getReseptiId() {
        return reseptiId;
    }
    
    /**
     * @return ainesosan id
     */
    public int getAinesosaId() {
        return ainesosaId;
    }
    
    /**
     * @return ainesosan määrä reseptissä
     */
    public String getMaara() {
        return maara;
    }
    
    /**
     * @param rId reseptille asetettava id
     */
    public void setReseptiId(int rId) {
        reseptiId = rId;
    }
    
    /**
     * @param aId ainesosalle asetettava id
     */
    public void setAinesosaId(int aId) {
        ainesosaId = aId;
    }
    
    /**
     * @param mr asetettava määrä
     */
    public void setMaara(String mr) {
        maara = mr;
    }
    
    /**
     * Palauttaa reseptin tiedot tiedostoon tallennettavaksi.
     * @return resepti tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   ReseptinAinesosa maito = new ReseptinAinesosa();
     *   maito.parse("1|1|2 1/2 dl");
     *   maito.toString().startsWith("1|1|2 1/2 dl") === true;
     * </pre>  
     */
    @Override
    public String toString() {
        return "" + getReseptiId() + "|" + 
                    getAinesosaId() + "|" + 
                    getMaara();
    }
    
    /**
     * Selvittää reseptin ainesosan tiedot tolppaerotellusta merkkijonosta
     * @param rivi jota käsitellään
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        reseptiId = Mjonot.erota(sb, '|', reseptiId);
        ainesosaId = Mjonot.erota(sb, '|', ainesosaId);
        maara = Mjonot.erota(sb, '|', maara);
    }
    
    /**
     * Tulostetaan reseptin ainesosan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(" Reseptin ID: " + String.format("%03d", reseptiId, 3));
        out.println("Ainesosan ID: " + String.format("%03d", ainesosaId, 3));
        out.println("       Määrä: " + maara);
        out.println("");
    }

    /**
     * Tulostetaan reseptin ainesosan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot reseptin ainesosalle
     */
    public void testiReseptinAinesosa() {
        reseptiId = 1;
        ainesosaId = 1;
        maara = "2 1/2 dl";
    }
    
    /**
     * Testiohjelma reseptin ainesosalle
     * @param args ei käytössä
     */
    public static void main(String[] args) {
//        ReseptinAinesosa maito = new ReseptinAinesosa();
//        maito.testiReseptinAinesosa();
//        maito.tulosta(System.out);
    }
}
