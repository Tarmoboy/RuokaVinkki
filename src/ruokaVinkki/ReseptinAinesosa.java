package ruokaVinkki;

import java.io.*;

/**
 * Luokka yksittäisen reseptin yksittäiselle ainesosalle
 * @author tarmo
 * @version 11.11.2023
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
     * </pre>
     */
    public int getAinesosaId() {
        return ainesosaId;
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
        ReseptinAinesosa maito = new ReseptinAinesosa();
        maito.testiReseptinAinesosa();
        maito.tulosta(System.out);
    }
}
