package ruokaVinkki;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Set;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import fi.jyu.mit.ohj2.WildChars;

/**
 * Luokka resepteille
 * @author tarmo
 * @version 12.12.2023
 *
 */
public class Reseptit implements Iterable<Resepti> {
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
     * Korvataan olemassaoleva resepti tai lisätään uusi
     * @param resepti korvattava tai lisättävä resepti
     * @throws SailoException jos tietorakenne on täynnä
     */
    public void korvaaTaiLisaa(Resepti resepti) throws SailoException {
        int id = resepti.getReseptiId();
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getReseptiId() == id) {
                alkiot[i] = resepti;
                muutettu = true;
                return;
            }
        }
        lisaa(resepti);
    }
    
    /**
     * Poistaa reseptin tietorakenteesta
     * @param resepti poistettava resepti
     */
    public void poista(Resepti resepti) {
        // Varmistetaan, että annettu resepti ei ole null
        if (resepti == null) return; 
        // Uusi lista, joka yhtä pienempi
        Resepti[] uusi = new Resepti[alkiot.length-1];
        int j = 0;
        for (int i = 0; i < alkiot.length; i++) {
            // Ohitetaan null-arvot
            if (alkiot[i] == null) continue; 
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
     * Palauttaa hakoehtoa vastaavat reseptit, etsitään nimen perusteella
     * @param hakuehto reseptien etsimiseen käytettävä hakuehto
     * @return löydetyt reseptit
     */
    public Collection<Resepti> etsi(String hakuehto) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0) {
            ehto = hakuehto;
        }
        Set<Resepti> loytyneet = new HashSet<>();
        for (Resepti resepti : this) {
            if (resepti != null && WildChars.onkoSamat(resepti.getNimi(), ehto)) {
                loytyneet.add(resepti);
            }
        }
        return loytyneet;
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
     * @return kaikki reseptit
     */
    public Resepti[] annaKaikki() {
        return Arrays.copyOf(alkiot, lkm);
    }
    
    /**
     * Etsii ja palauttaa reseptin annetulla reseptiId:llä.
     * @param reseptiId haettavan reseptin id
     * @return löydetty resepti, tai null jos reseptiä ei löydy
     */
    public Resepti haeReseptiIdlla(int reseptiId) {
        for (Resepti resepti : alkiot) {
            if (resepti != null && resepti.getReseptiId() == reseptiId) {
                return resepti;
            }
        }
        return null;
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
     * Reseptien iterointia varten
     */
    public class ReseptitIterator implements Iterator<Resepti> {
        private int kohdalla = 0;

        /**
         * Onko olemassa vielä seuraavaa reseptiä
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä reseptejä
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava resepti
         * @return seuraava resepti
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Resepti next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("Ei ole");
            }
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Ei poisteta");
        }
    }
    
    @Override
    public Iterator<Resepti> iterator() {
            return new ReseptitIterator();
    }
    
    /**
     * Testiohjelma resepteille
     * @param args ei käytössä
     */
    public static void main(String args[]) {
//        Reseptit reseptit = new Reseptit();
//        Resepti nakkijuustosarvet = new Resepti();
//        nakkijuustosarvet.rekisteroi();
//        nakkijuustosarvet.testiResepti();
//        Resepti makaroni = new Resepti();
//        makaroni.rekisteroi();
//        try {
//            reseptit.lisaa(nakkijuustosarvet);
//            reseptit.lisaa(makaroni);
//            System.out.println("============= Reseptit testi =================");
//            for (int i = 1; i < reseptit.getLkm(); i++) {
//                Resepti resepti = reseptit.anna(i);
//                System.out.println("  Resepti nro: " + i);
//                resepti.tulosta(System.out);
//            }
//        } catch (SailoException ex) {
//            System.out.println(ex.getMessage());
//        }
    }
}
