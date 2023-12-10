package ruokaVinkki;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import fi.jyu.mit.ohj2.WildChars;

/**
 * Luokka ainesosille
 * @author tarmo
 * @version 10.12.2023
 *
 */
public class Ainesosat implements Iterable<Ainesosa> {
    private static String tiedostonSijainti = "data/ainesosat.dat";
    private ArrayList<Ainesosa> alkiot = new ArrayList<Ainesosa>();
    private static boolean muutettu = false;
    
    /**
     * Tyhjä muodostaja
     */
    public Ainesosat() {
    }
    
    /**
     * Palauttaa ainesosien lukumäärän
     * @return ainesosien lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    /**
     * Lisää uuden ainesosan tietorakenteeseen
     * @param ainesosa lisättävä ainesosa
     */
    public void lisaa(Ainesosa ainesosa) {
        if (ainesosa == null) return;
        alkiot.add(ainesosa);
        muutettu = true;
    }
    
    /**
     * Poistaa ainesosan tietorakenteesta
     * @param ainesosa poistettava ainesosa
     */
    public void poista(Ainesosa ainesosa) {
        alkiot.remove(ainesosa);
        muutettu = true;
    }
    
    /**
     * Palauttaa hakoehtoa vastaavat ainesosat, etsitään nimen perusteella
     * @param hakuehto ainesosat etsimiseen käytettävä hakuehto
     * @return löydetyt ainesosat
     */
    public Collection<Ainesosa> etsi(String hakuehto) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0) {
            ehto = hakuehto;
        }
        // HashSet duplikaattien eliminoimiseksi
        Set<Ainesosa> loytyneet = new HashSet<>();
        for (Ainesosa ainesosa : this) {
            if (ainesosa != null && WildChars.onkoSamat(ainesosa.getNimi(), ehto)) {
                loytyneet.add(ainesosa);
            }
        }
        return loytyneet;
    }
    
    /**
     * Lukee ainesosat tiedostosta
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
                Ainesosa ainesosa = new Ainesosa();
                ainesosa.parse(rivi);
                lisaa(ainesosa);
            }
        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + tiedostonSijainti + " ei aukea");
        } 
    }

    /**
     * Tallentaa ainesosat tiedostoon
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        // Jos ei muutoksia, palataan suoraan
        if (!muutettu) {
            return;
        }
        File tiedosto = new File(tiedostonSijainti);
        try (PrintStream fi = new PrintStream((tiedosto.getCanonicalPath()))) {
            for (int i = 0; i< alkiot.size(); i++) {
                Ainesosa ainesosa = alkiot.get(i);
                fi.println(ainesosa.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new SailoException("Tiedosto " + tiedostonSijainti + " ei aukea");
        } catch (IOException ex) {
            throw new SailoException("Tiedoston " + tiedostonSijainti + " kirjoittamisessa ongelmia");
        }
        // muutettu takaisin falseksi, kun on tallennettu
        muutettu = false;
    }
    
    /**
     * Palauttaa listan ainesosista, jotka kuuluvat tiettyyn reseptiin
     * @param reseptinAinesosat taulukko, joka kertoo mitkä ainesosat kuuluvat tietylle reseptille
     * @return lista ainesosista, jotka kuuluvat tietylle reseptille
     * @example
     * <pre name="test">
     *  #import java.util.*;
     *  Ainesosat ainesosat = new Ainesosat();
     *  Ainesosa maito = new Ainesosa(1); ainesosat.lisaa(maito);
     *  Ainesosa hiiva = new Ainesosa(2); ainesosat.lisaa(hiiva);
     *  List<Ainesosa> loydetyt;
     *  int[] reseptienAinesosat = {1, 2};
     *  loydetyt = ainesosat.annaAinesosat(reseptienAinesosat);
     *  loydetyt.size() === 2;
     * </pre> 
     */
    public List<Ainesosa> annaAinesosat(int[] reseptinAinesosat) {
        List<Ainesosa> loydetyt = new ArrayList<Ainesosa>();
        for (int i = 0; i < reseptinAinesosat.length; i++) {
            loydetyt.add(annaAinesosa(reseptinAinesosat[i]));
        }
        return loydetyt;
    }
    
    /**
     * @param id ainesosan id
     * @return viite löydettyyn ainesosaan
     */
    public Ainesosa annaAinesosa(int id) {
        for (Ainesosa i : alkiot) {
            if (i.getAinesosaId() == id) return i;
        }
        return null;
    }
    
    /**
     * Iteraattori
     */
    @Override
    public Iterator<Ainesosa> iterator() {
            return alkiot.iterator();
    }
    
    /**
     * Testiohjelma ainesosille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
//        Ainesosat ainesosat = new Ainesosat();
//        Ainesosa maito = new Ainesosa(1); 
//        Ainesosa hiiva = new Ainesosa(2); 
//        maito.testiAinesosa();
//        ainesosat.lisaa(maito);
//        ainesosat.lisaa(hiiva);
//        System.out.println("============= Ainesosat testi =================");
//        int[] reseptinAinesosat = {1, 2};
//        List<Ainesosa> ainesosat2 = ainesosat.annaAinesosat(reseptinAinesosat);
//        for (Ainesosa ainesosa : ainesosat2) {
//            System.out.println("Ainesosa nro: " + ainesosa.getAinesosaId());
//            ainesosa.tulosta(System.out);
//        }
    }
}
