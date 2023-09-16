package fxRuokaVinkki;

import javafx.fxml.FXML;
import fi.jyu.mit.fxgui.ModalController;

/**
 * @author tarmo
 * @version 8.9.2023
 *
 */
public class RuokaVinkkiGUIController {
    
    /**
     * Avaa reseptin muokkaamisikkunan
     */
    @FXML private void handleMuokkaaReseptia() {
        ModalController.showModal(RuokaVinkkiGUIController.class.getResource("MuokkaaView.fxml"), "Resepti", null, "");
    }
    
    /**
     * Avaa tietoja-ikkunan
     */
    @FXML private void handleTietoja() {
        // Dialogs.showMessageDialog("Ei osata vielä tietoja");
        ModalController.showModal(RuokaVinkkiGUIController.class.getResource("AboutView.fxml"), "Tietoja", null, "");
    }
    
    /**
     * Avataan aloitusikkuna
     */
    public void avaa() {
        AloitusViewController.kaynnista();
    }
}
