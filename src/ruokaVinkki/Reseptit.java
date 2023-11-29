package ruokaVinkki;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Luokka resepteille
 * @author tarmo
 * @version 29.11.2023
 *
 */
public class Reseptit {
    private static int MAX_RESEPTEJA = 5;
    private int lkm = 0;
    private static String tiedostonSijainti = "data/reseptit.dat";
    private Resepti alkiot[] = new Resepti[MAX_RESEPTEJA];
    private static boolean muutettu = false;
    
    /**
     * Tyhjä muodostaja
     */
    public Reseptit() {
    }
    
    /**
     * Palauttaa reseptien lukumäärän
     * @return reseptien lukumäärä
     */
    public int getLkm() {
        return lkm;
    }
    
    /**
     * Lisää uuden reseptin tietorakenteeseen 
     * @param resepti lisättävän reseptin viite
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     *  #THROWS SailoException
     *  Reseptit reseptit = new Reseptit();
     *  Resepti mikropitsa = new Resepti();
     *  Resepti makaroni = new Resepti();
     *  reseptit.lisaa(mikropitsa); reseptit.getLkm() === 1;
     *  reseptit.lisaa(makaroni); reseptit.getLkm() === 2;
     *  reseptit.anna(0) === mikropitsa;
     *  reseptit.anna(1) === makaroni;
     *  reseptit.anna(2) === mikropitsa; #THROWS IndexOutOfBoundsException
     *  reseptit.lisaa(mikropitsa); reseptit.getLkm() === 3;
     *  reseptit.lisaa(mikropitsa); reseptit.getLkm() === 4;
     *  reseptit.lisaa(mikropitsa); reseptit.getLkm() === 5;
     *  reseptit.lisaa(mikropitsa); 
     * </pre>
     */
    public void lisaa(Resepti resepti) throws SailoException {
        if (lkm >= alkiot.length) {
            MAX_RESEPTEJA = MAX_RESEPTEJA + 5;
            Reseptit uusi = new Reseptit();
            for (int i = 0; i < alkiot.length; i++) {
                uusi.alkiot[i]=this.alkiot[i];
            }
            this.alkiot = uusi.alkiot;
        }
        alkiot[lkm] = resepti;
        lkm++;
        muutettu = true;
    }
    
    /**
     * Poistaa reseptin tietorakenteesta
     * @param resepti poistettava resepti
     */
    public void poista(Resepti resepti) {
        // Uusi lista, joka yhtä pienempi
        Resepti[] uusi = new Resepti[alkiot.length-1];
        int j = 0;
        for (int i = 0; i < alkiot.length; i++) {
            int id = alkiot[i].getReseptiId();
            // Poistettavan reseptin tullessa kohdalle ei lisätä uuteen listaan
            if (id == resepti.getReseptiId()) {
                lkm--;
                continue;
            }
            uusi[j] = alkiot[i];
            j++;
        }
        this.alkiot = uusi;
        muutettu = true;
    }
    
    /**
     * Palauttaa viitteen i:teen reseptiin
     * @param i monennenko reseptin viite halutaan
     * @return viite reseptiin, jonka id on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public Resepti anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    /**
     * Lukee reseptit tiedostosta
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedosto() throws SailoException {
        File tiedosto = new File(tiedostonSijainti);
        try (Scanner fi = new Scanner(new FileInputStream(tiedosto))) {
            String rivi = null;
            while (fi.hasNext()) {
                rivi = fi.nextLine();
                rivi = rivi.trim();
                if ("".equals(rivi)) {
                    continue;
                }
                Resepti resepti = new Resepti();
                resepti.parse(rivi);
                try {
                    lisaa(resepti);
                } catch (SailoException e) {
                    System.err.println(e.getMessage());
                }
            } 
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + tiedostonSijainti + " ei aukea");
        } 
    }

    /**
     * Tallentaa reseptit tiedostoon
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        // Jos ei muutoksia, palataan suoraan
        if (!muutettu) {
            return;
        }
        File tiedosto = new File(tiedostonSijainti);
        try (PrintStream file = new PrintStream((tiedosto.getCanonicalPath()))) {
            for (int i = 0; i< this.getLkm(); i++) {
                Resepti resepti = anna(i);
                file.println(resepti.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + tiedostonSijainti + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + tiedostonSijainti + " kirjoittamisessa ongelmia");
        }
        // muutettu takaisin falseksi tallentamisen jälkeen
        muutettu = false;
    }
    
    /**
     * Testiohjelma resepteille
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Reseptit reseptit = new Reseptit();
        Resepti nakkijuustosarvet = new Resepti();
        nakkijuustosarvet.rekisteroi();
        nakkijuustosarvet.testiResepti();
        Resepti makaroni = new Resepti();
        makaroni.rekisteroi();
        try {
            reseptit.lisaa(nakkijuustosarvet);
            reseptit.lisaa(makaroni);
            System.out.println("============= Reseptit testi =================");
            for (int i = 0; i < reseptit.getLkm(); i++) {
                Resepti resepti = reseptit.anna(i);
                System.out.println("  Resepti nro: " + i);
                resepti.tulosta(System.out);
            }
        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
