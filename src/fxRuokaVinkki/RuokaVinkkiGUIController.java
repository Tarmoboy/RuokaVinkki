package fxRuokaVinkki;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import fi.jyu.mit.fxgui.ModalController;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;

/**
 * @author tarmo
 * @version 8.9.2023
 *
 */
public class RuokaVinkkiGUIController {
    
    @FXML
    private ComboBoxChooser<String> cbKentta;
    @FXML
    private TextField hakuehto;
    
    /**
     * Avaa reseptin muokkaamisikkunan
     */
    @FXML 
    private void handleMuokkaaReseptia() {
        ModalController.showModal(RuokaVinkkiGUIController.class.getResource("MuokkaaView.fxml"), "Resepti", null, "");
    }
    
    /**
     * Avaa tietoja-ikkunan
     */
    @FXML 
    private void handleTietoja() {
        // Dialogs.showMessageDialog("Ei osata vielä tietoja");
        ModalController.showModal(RuokaVinkkiGUIController.class.getResource("AboutView.fxml"), "Tietoja", null, "");
    }
    
    /**
     * Avaa aloitusikkunan
     */
    public void avaa() {
        AloitusViewController.kaynnista();
    }
    
    /**
     * Avaa käyttöohjeen selaimeen
     */
    @FXML
    private void handleApua() {
        apua();
    }
    
    /**
     * Hakuehdon toiminta
     */
    @FXML
    private void handleHakuehto() {
        Dialogs.showMessageDialog("Ei osata vielä hakea",
                dlg -> 
                    dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
    }
    
    /**
     * Avaa uuden reseptin lisäysikkunan
     */
    @FXML
    private void handleLisaaResepti() {
        Dialogs.showMessageDialog("Ei osata vielä lisätä reseptiä",
                dlg -> 
                    dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
    }
    
    /**
     * Sulkee ohjelman
     */
    @FXML
    private void handleLopeta() {
        Platform.exit();
    }
    
    /**
     * Tallentaa tehdyt muokkaukset
     */
    @FXML
    private void handleTallenna() {
        Dialogs.showMessageDialog("Ei osata vielä tallentaa",
                dlg -> 
                    dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
    }
    
    /**
     * Avaa tulostusikkunan
     */
    @FXML
    void handleTulosta() {
        Dialogs.showMessageDialog("Ei osata vielä tulostaa",
                dlg -> 
                    dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
    }
    
    /**
     * Ohjelman suunnitelman avaaminen selaimeen
     */
    private void apua() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2023s/ht/teilvevz");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
}
