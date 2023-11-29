package ruokaVinkki;

import java.util.*;
import java.io.*;

/**
 * Luokka reseptien ainesosille
 * @author tarmo
 * @version 29.11.2023
 * 
 */
public class ReseptienAinesosat {
    private static int MAX_RESEPTIENAINESOSIA = 5;
    private int lkm = 0;
    private static String tiedostonSijainti = "data/reseptienainesosat.dat";
    private ReseptinAinesosa[] alkiot = new ReseptinAinesosa[MAX_RESEPTIENAINESOSIA];
    private boolean muutettu = false;
    
    /**
     * Tyhjä muodostaja
     */
    public ReseptienAinesosat() {
    }
    
    /**
     * Palauttaa reseptien ainesosien lukumäärän
     * @return reseptien ainesosien lukumäärä
     */
    public int getLkm() {
        return lkm;
    }
    
    /**
     * Lisää uuden reseptin ainesosan tietorakenteeseen 
     * @param reseptinAinesosa lisättävän reseptin ainesosan viite
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     *  #THROWS SailoException
     *  ReseptienAinesosat reseptienAinesosat = new ReseptienAinesosat();
     *  ReseptinAinesosa maito = new ReseptinAinesosa();
     *  maito.testiReseptinAinesosa();
     *  ReseptinAinesosa hiiva = new ReseptinAinesosa();
     *  reseptienAinesosat.lisaa(maito); reseptienAinesosat.getLkm() === 1;
     *  reseptienAinesosat.lisaa(hiiva); reseptienAinesosat.getLkm() === 2;
     *  reseptienAinesosat.anna(0) === maito;
     *  reseptienAinesosat.anna(1) === hiiva;
     *  reseptienAinesosat.anna(2) === maito; #THROWS IndexOutOfBoundsException
     *  reseptienAinesosat.lisaa(maito); reseptienAinesosat.getLkm() === 3;
     *  reseptienAinesosat.lisaa(maito); reseptienAinesosat.getLkm() === 4;
     *  reseptienAinesosat.lisaa(maito); reseptienAinesosat.getLkm() === 5;
     *  reseptienAinesosat.lisaa(maito); 
     * </pre>
     */
    public void lisaa(ReseptinAinesosa reseptinAinesosa) throws SailoException {
        if (lkm >= alkiot.length) {
            MAX_RESEPTIENAINESOSIA = MAX_RESEPTIENAINESOSIA + 5;
            ReseptienAinesosat uusi = new ReseptienAinesosat();
            for (int i = 0; i < alkiot.length; i++) {
                uusi.alkiot[i]=this.alkiot[i];
            }
            this.alkiot = uusi.alkiot;
        }
        alkiot[lkm] = reseptinAinesosa;
        lkm++;
        muutettu = true;
    }
    
    /**
     * Poistaa reseptin ainesosat tietorakenteesta
     * @param resepti poistettaviin ainesosiin liittyvä resepti
     */
    public void poista(Resepti resepti) {
        // Uusi lista, joka yhtä pienempi
        ReseptinAinesosa[] uusi = new ReseptinAinesosa[alkiot.length-1];
        int j = 0;
        for (int i = 0; i < alkiot.length; i++) {
            int id = alkiot[i].getReseptiId();
            // Poistettavan reseptin ainesosan tullessa kohdalle ei lisätä uuteen listaan
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
     * Palauttaa viitteen i:teen reseptin ainesosaan
     * @param i monennenko reseptin ainesosan viite halutaan
     * @return viite reseptin ainesosaan, jonka id on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public ReseptinAinesosa anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    /**
     * Palauttaa reseptin ainesosien viitteet taulukkona
     * @param reseptiId reseptin id jonka ainesosien viitteet halutaan
     * @return reseptin ainesosien viitteet
     * @example
     * <pre name="test">
     *  #import java.util.*;
     *  ReseptienAinesosat reseptienAinesosat = new ReseptienAinesosat();
     *  ReseptinAinesosa maito = new ReseptinAinesosa();
     *  maito.testiReseptinAinesosa();
     *  ReseptinAinesosa hiiva = new ReseptinAinesosa();
     *  ReseptinAinesosa maito2 = new ReseptinAinesosa();
     *  maito2.testiReseptinAinesosa();
     *  try {
     *      reseptienAinesosat.lisaa(maito);
     *      reseptienAinesosat.lisaa(hiiva);
     *      reseptienAinesosat.lisaa(maito2);
     *  } catch (SailoException ex) {
     *      System.out.println(ex.getMessage());
     *  }
     *  int[] t = reseptienAinesosat.annaReseptinAinesosat(1);
     *  Arrays.toString(t) === "[1, 1]";
     * </pre>
     */
    public int[] annaReseptinAinesosat(int reseptiId) {
        // Ensin ArrayListiin ainesosat, koska kokonaismäärää ei tiedetä
        ArrayList<Integer> lista = new ArrayList<Integer>();
        for (int i = 0; i < lkm; i++) {
            // Jos reseptiId täsmää, lisätään ainesosan id listaan
            if (alkiot[i].getReseptiId() == reseptiId) {
                lista.add(alkiot[i].getAinesosaId());
            }
        }
        // Sitten oikean kokoinen int[] johon kerätään reseptin ainesosien id:t
        int[] ainesosaIdt = new int[lista.size()];
        for (int i = 0; i < ainesosaIdt.length; i++) {
            ainesosaIdt[i] = lista.get(i).intValue();
        }
        return ainesosaIdt; 
    }
    
    /**
     * Lukee reseptien ainesosat tiedostosta
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
                ReseptinAinesosa reseptinAinesosa = new ReseptinAinesosa();
                reseptinAinesosa.parse(rivi);
                try {
                    lisaa(reseptinAinesosa);
                } catch (SailoException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + tiedostonSijainti + " ei aukea");
        } 
    }

    /**
     * Tallentaa reseptien ainesosat tiedostoon
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        // Jos ei muutoksia, palataan
        if (!muutettu) {
            return;
        }
        File tiedosto = new File(tiedostonSijainti);
        try (PrintStream file = new PrintStream((tiedosto.getCanonicalPath()))) {
            for (int i = 0; i < this.getLkm(); i++) {
                ReseptinAinesosa reseptinAinesosa = anna(i);
                file.println(reseptinAinesosa.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + tiedostonSijainti + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + tiedostonSijainti + " kirjoittamisessa ongelmia");
        }
        // muutettu takaisin falseksi tallentamisen jälkeen
        muutettu=false;
    }
    
    /**
     * Testiohjelma reseptien ainesosille
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        ReseptienAinesosat reseptienAinesosat = new ReseptienAinesosat();
        ReseptinAinesosa maito = new ReseptinAinesosa();
        maito.testiReseptinAinesosa();
        ReseptinAinesosa hiiva = new ReseptinAinesosa();
        try {
            reseptienAinesosat.lisaa(maito);
            System.out.println(reseptienAinesosat.getLkm());
            reseptienAinesosat.lisaa(hiiva);
            System.out.println(reseptienAinesosat.getLkm());
            System.out.println("============= ReseptinAinesosat testi =================");
            for (int i = 0; i < reseptienAinesosat.getLkm(); i++) {
                ReseptinAinesosa reseptinAinesosa = reseptienAinesosat.anna(i);
                System.out.println(" Resepti nro: " + i);
                reseptinAinesosa.tulosta(System.out);
            }
        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
