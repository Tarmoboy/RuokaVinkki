package ruokaVinkki;

/**
 * Luokka resepteille
 * @author tarmo
 * @version 11.11.2023
 *
 */
public class Reseptit {
    private static final int MAX_RESEPTEJA = 5;
    private int lkm = 0;
    private String tiedostonNimi = "";
    private Resepti alkiot[] = new Resepti[MAX_RESEPTEJA];
    
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
     *  reseptit.lisaa(mikropitsa); #THROWS SailoException
     * </pre>
     */
    public void lisaa(Resepti resepti) throws SailoException {
        if (lkm >= alkiot.length) throw new SailoException("Liikaa alkioita");
        alkiot[lkm] = resepti;
        lkm++;
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
     * TODO toteuttamatta
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedostonNimi = hakemisto + "/reseptit.dat";
        throw new SailoException("Ei osata lukea tiedostoa " + tiedostonNimi);
    }

    /**
     * Tallentaa reseptit tiedostoon
     * TODO toteuttamatta
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        throw new SailoException("Ei osata tallettaa tiedostoa " + tiedostonNimi);
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
