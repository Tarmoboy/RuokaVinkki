package ruokaVinkki;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille
 * @author Tarmo
 * @version 11.11.2023
 * 
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Poikkeuksen muodostaja jolle tuodaan poikkeuksessa käytettävä viesti
     * @param viesti poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
}
