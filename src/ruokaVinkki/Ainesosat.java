package ruokaVinkki;

import java.util.*;

/**
 * Luokka ainesosille
 * @author tarmo
 * @version 11.11.2023
 *
 */
public class Ainesosat {
    private String tiedostonNimi = "";
    private final ArrayList<Ainesosa> alkiot = new ArrayList<Ainesosa>();
    
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
        alkiot.add(ainesosa);
    }
    
    /**
     * Lukee ainesosat tiedostosta
     * TODO toteuttamatta
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedostonNimi = hakemisto + "/reseptit.dat";
        throw new SailoException("Ei osata lukea tiedostoa " + tiedostonNimi);
    }

    /**
     * Tallentaa ainesosat tiedostoon
     * TODO toteuttamatta
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        throw new SailoException("Ei osata tallettaa tiedostoa " + tiedostonNimi);
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
     * Testiohjelma ainesosille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Ainesosat ainesosat = new Ainesosat();
        Ainesosa maito = new Ainesosa(1); 
        Ainesosa hiiva = new Ainesosa(2); 
        maito.testiAinesosa();
        ainesosat.lisaa(maito);
        ainesosat.lisaa(hiiva);
        System.out.println("============= Ainesosat testi =================");
        int[] reseptinAinesosat = {1, 2};
        List<Ainesosa> ainesosat2 = ainesosat.annaAinesosat(reseptinAinesosat);
        for (Ainesosa ainesosa : ainesosat2) {
            System.out.println("Ainesosa nro: " + ainesosa.getAinesosaId());
            ainesosa.tulosta(System.out);
        }
    }
}
