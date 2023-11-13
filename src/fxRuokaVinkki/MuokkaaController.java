package fxRuokaVinkki;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.fxgui.ModalController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

/**
 * @author tarmo
 * @version 13.11.2023
 *
 */
public class MuokkaaController implements ModalControllerInterface<Object> {
    
    @FXML
    private GridPane gridTietue;

    @FXML
    private Label labelVirhe;

    @FXML
    private ScrollPane panelTietue;
    
    /**
     * Reseptin poistamisen dialogi
     */
    @FXML private void handlePoistaResepti() {
        Dialogs.showQuestionDialog("Poistetaanko?", "Poistetaanko resepti: Nakki-juustosarvet", "Kyllä", "Ei",
                dlg -> 
                    dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
    }
    
    /**
     * Uuden ainesosan lisääminen
     */
    @FXML
    void handleLisaaAinesosa() {
        Dialogs.showMessageDialog("Ei osata vielä lisätä ainesosaa",
                dlg -> 
                    dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
    }
    
    /**
     * Ainesosan poistaminen
     */
    @FXML
    void handlePoistaAinesosa() {
        Dialogs.showMessageDialog("Ei osata vielä poistaa ainesosaa",
                dlg -> 
                    dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
    }
    
    /**
     * Poistuminen muokkausikkunasta ilman tallentamista
     */
    @FXML
    void handlePeruuta() {
        ModalController.closeStage(gridTietue);
    }
    
    /**
     * Poistuminen muokkausikkunasta tallentaen
     */
    @FXML
    void handleTallenna() {
        ModalController.closeStage(gridTietue);
    }
    
    /**
     * Ei mitään
     */
    @Override
    public void setDefault(Object oletus) {
        // Tyhjä toteutus
    }
    
    /**
     * Tyhjää vielä
     */
    @Override
    public Object getResult() {
        // Ei vielä mitään
        return null;
    }
    
    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        // Ei mitään
    }
    
}