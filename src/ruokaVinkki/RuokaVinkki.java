package ruokaVinkki;

import java.util.*;

/**
 * RuokaVinkki-luokka, huolehtii resepteistä
 * @author tarmo
 * @version 11.12.2023
 *
 */
public class RuokaVinkki {
    private Reseptit reseptit = new Reseptit();
    private Ainesosat ainesosat = new Ainesosat();
    private ReseptienAinesosat reseptienAinesosat = new ReseptienAinesosat();
    
    /**
     * @return reseptien lukumäärä
     */
    public int getResepteja() {
        return reseptit.getLkm();
    }
    
    /**
     * @return ainesosien lukumäärä
     */
    public int getAinesosia() {
        return ainesosat.getLkm();
    }
    
    /**
     * @return reseptien ainesosien lukumäärä
     */
    public int getReseptienAinesosia() {
        return reseptienAinesosat.getLkm();
    }
    
    /**
     * @return ainesosat
     */
    public Ainesosat getAinesosat() {
        return ainesosat;
    }
    
    /**
     * Lisää uuden reseptin
     * @param resepti lisättävä resepti
     * @throws SailoException jos lisäystä ei voida tehdä
     */
    public void lisaa(Resepti resepti) throws SailoException {
        reseptit.lisaa(resepti);
    }
    
    /**
     * Lisää uuden ainesosan
     * @param ainesosa lisättävä ainesosa
     */
    public void lisaa(Ainesosa ainesosa) {
        ainesosat.lisaa(ainesosa);
    }
    
    /**
     * Lisää uuden reseptin ainesosan
     * @param reseptinAinesosa lisättävä reseptin ainesosa
     * @throws SailoException jos lisäystä ei voida tehdä
     */
    public void lisaa(ReseptinAinesosa reseptinAinesosa) throws SailoException {
        reseptienAinesosat.lisaa(reseptinAinesosa);
    }
    
    /**
     * Korvataanko olemassaoleva resepti vai lisätäänkö uusi
     * @param resepti tarkasteltava resepti
     * @throws SailoException epäonnistuessa
     */
    public void korvaaTaiLisaa(Resepti resepti) throws SailoException {
        reseptit.korvaaTaiLisaa(resepti);
    }
    
    /**
     * Poistaa reseptin ja siihen linkitetyt ainesosarivit
     * @param resepti poistettava resepti
     */
    public void poista(Resepti resepti) {
        // Jos resepti tyhjä, ei tehdä mitään
        if (resepti == null) {
            return;
        }
        reseptienAinesosat.poista(resepti);
        reseptit.poista(resepti);
    }
    
    /**
     * Poistaa reseptiin linkitetyt ainesosat
     * @param resepti käsiteltävä resepti
     */
    public void poistaReseptinAinesosat(Resepti resepti) {
        reseptienAinesosat.poista(resepti);
    }
    
    /**
     * Poistaa reseptiin linkitetyn tietyn ainesosan
     * @param resepti käsiteltävä resepti
     * @param ainesosa poistettava ainesosa
     */
    public void poistaReseptinAinesosa(Resepti resepti, Ainesosa ainesosa) {
        int reseptiId = resepti.getReseptiId();
        int ainesosaId = ainesosa.getAinesosaId();
        reseptienAinesosat.poistaReseptinAinesosa(reseptiId, ainesosaId);
    }
    
    /**
     * @param hakuehto käytettävä hakuehto
     * @param kentta indeksi käytettävälle kentälle
     * @return löydetyt reseptit
     * @throws SailoException epäonnistuessa
     */
    public Collection<Resepti> etsi(String hakuehto, int kentta) throws SailoException {
        // Jos kentän id on 0, etsitään ainesosien nimien mukaan
        if (kentta == 0) {
            if (hakuehto == null || hakuehto.isEmpty() || hakuehto.equals("**")) {
                // Jos hakuehto on tyhjä tai "**", palautetaan kaikki reseptit
                return new ArrayList<>(Arrays.asList(reseptit.annaKaikki()));
            }
            // Etsitään ensin kaikki hakuehtoa vastaavat ainesosat
            Collection<Ainesosa> loytyneetAinesosat = ainesosat.etsi(hakuehto);
            //System.out.println(loytyneetAinesosat);
            // Sitten näitä ainesosia vastaavat uniikit id:t
            Set<Integer> ainesosaIdt = new HashSet<>();
            for (Ainesosa ainesosa : loytyneetAinesosat) {
                ainesosaIdt.add(ainesosa.getAinesosaId());
            }
            //System.out.println(ainesosaIdt);
            // Sitten etsitään kaikki reseptit, jotka sisältävät jonkun näistä ainesosien id:stä
            Set<Resepti> loytyneet = new HashSet<>();
            for (int i = 0; i < reseptienAinesosat.getLkm(); i++) {
                ReseptinAinesosa ra = reseptienAinesosat.anna(i);
                //System.out.println(reseptienAinesosat.anna(i));
                if (ainesosaIdt.contains(ra.getAinesosaId())) {
                    //System.out.println(reseptit.anna(ra.getReseptiId()));
                    //System.out.println(reseptit.anna(ra.getReseptiId()));
                    Resepti resepti = reseptit.haeReseptiIdlla(ra.getReseptiId());
                    //System.out.println(reseptit.anna(ra.getReseptiId()));
                    if (resepti != null) {
                        loytyneet.add(resepti);
                    }
                }
            }
            return loytyneet;
        }
        // Muuten etsitään reseptien nimien mukaan
        return reseptit.etsi(hakuehto);
    }
    
    /**
     * Hakee ainesosan annetulla nimellä
     * @param nimi ainesosan nimi
     * @return löydetty ainesosa tai null jos ei löydy
     */
    public Ainesosa haeAinesosaNimella(String nimi) {
        for (Ainesosa ainesosa : ainesosat) {
            if (ainesosa.getNimi().equalsIgnoreCase(nimi)) {
                return ainesosa;
            }
        }
        return null;
    }
    
    /**
     * Palauttaa i:n reseptin
     * @param i monesko resepti palautetaan
     * @return viite i:teen reseptiin
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Resepti annaResepti(int i) throws IndexOutOfBoundsException {
        return reseptit.anna(i);
    }
    
    /**
     * Palauttaa i:n ainesosan
     * @param i monesko ainesosa palautetaan
     * @return viite i:teen ainesosaan
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Ainesosa annaAinesosa(int i) throws IndexOutOfBoundsException {
        return ainesosat.annaAinesosa(i);
    }
    
    /**
     * Palauttaa i:n reseptin ainesosan
     * @param i monesko reseptin ainesosa palautetaan
     * @return viite i:teen reseptin ainesosaan
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public ReseptinAinesosa annaReseptinAinesosa(int i) throws IndexOutOfBoundsException {
        return reseptienAinesosat.anna(i);
    }
    
    /**
     * Palauttaa reseptin ainesosat listana
     * @param i reseptin id
     * @return reseptin ainesosien lista
     * @example
     * <pre name="test">
     *  #import java.util.*;
     *  #THROWS SailoException
     *  RuokaVinkki ruokaVinkki = new RuokaVinkki();
     *  try {
     *      Resepti nakkijuustosarvet = new Resepti();
     *      Resepti makaroni = new Resepti();
     *      nakkijuustosarvet.rekisteroi();
     *      nakkijuustosarvet.testiResepti();
     *      makaroni.rekisteroi();
     *      ruokaVinkki.lisaa(nakkijuustosarvet);
     *      ruokaVinkki.lisaa(makaroni);
     *      Ainesosa maito = new Ainesosa();
     *      maito.rekisteroi();
     *      maito.testiAinesosa();
     *      Ainesosa hiiva = new Ainesosa();
     *      hiiva.rekisteroi();
     *      ruokaVinkki.lisaa(maito);
     *      ruokaVinkki.lisaa(hiiva);
     *      ReseptinAinesosa maitoLiitetty = new ReseptinAinesosa();
     *      maitoLiitetty.testiReseptinAinesosa();
     *      ruokaVinkki.lisaa(maitoLiitetty);
     *      List<Ainesosa> lista = new ArrayList<Ainesosa>();
     *      lista = ruokaVinkki.annaAinesosat(1);
     *      lista.size() === 1;
     *  } catch (SailoException ex) {
     *      System.out.println(ex.getMessage());
     *  }
     * </pre>
     */
    public List<Ainesosa> annaAinesosat(int i) {
        int[] t = reseptienAinesosat.annaReseptinAinesosat(i);
        return ainesosat.annaAinesosat(t);
    }
    
    /**
     * Hakee reseptit, ainesosat ja reseptien ainesosat hakemistosta
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedosto() throws SailoException {
        reseptit.lueTiedosto();
        ainesosat.lueTiedosto();
        reseptienAinesosat.lueTiedosto();
    }
    
    /**
     * Tallentaa reseptit, ainesosat ja reseptien ainesosat
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void talleta() throws SailoException {
        String virhe = "";
        try {
            reseptit.talleta();
        } catch (SailoException ex) {
            virhe = ex.getMessage();
        }
        try {
            ainesosat.talleta();
        } catch (SailoException ex) {
        virhe = ex.getMessage();
        }
        try {
            reseptienAinesosat.talleta();
        } catch (SailoException ex) {
            virhe = ex.getMessage();
        }
        if ( !"".equals(virhe) ) {
            throw new SailoException(virhe);
        }
    }
    
    /**
     * Testiohjelma
     * @param args ei käytössä
     */
    public static void main(String args[]) {
//        RuokaVinkki ruokaVinkki = new RuokaVinkki();
//        try {
//            // ruokaVinkki.lueTiedostosta("data");
//            Resepti nakkijuustosarvet = new Resepti();
//            Resepti makaroni = new Resepti();
//            nakkijuustosarvet.rekisteroi();
//            nakkijuustosarvet.testiResepti();
//            makaroni.rekisteroi();
//            ruokaVinkki.lisaa(nakkijuustosarvet);
//            ruokaVinkki.lisaa(makaroni);
//            Ainesosa maito = new Ainesosa();
//            maito.rekisteroi();
//            maito.testiAinesosa();
//            Ainesosa hiiva = new Ainesosa();
//            hiiva.rekisteroi();
//            ruokaVinkki.lisaa(maito);
//            ruokaVinkki.lisaa(hiiva);
//            ReseptinAinesosa maitoLiitettyna = new ReseptinAinesosa();
//            maitoLiitettyna.testiReseptinAinesosa();
//            ruokaVinkki.lisaa(maitoLiitettyna);
//            System.out.println("============= RuokaVinkki testi =================");
//            for (int i = 0; i < ruokaVinkki.getResepteja(); i++) {
//                Resepti resepti = ruokaVinkki.annaResepti(i);
//                System.out.println("  Resepti nro: " + i);
//                resepti.tulosta(System.out);
//                int[] ainesosaIdt = ruokaVinkki.reseptienAinesosat.annaReseptinAinesosat(resepti.getReseptiId());
//                for (int ainesosaId : ainesosaIdt) {
//                    Ainesosa ainesosa = ruokaVinkki.annaAinesosa(ainesosaId);
//                    ainesosa.tulosta(System.out);
//                } 
//            }
//        } catch (SailoException ex) {
//            System.out.println(ex.getMessage());
//        }
    }
}
