package fxRuokaVinkki;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;

/**
 * @author tarmo
 * @version 8.9.2023
 *
 */
public class MuokkaaController implements ModalControllerInterface<Object> {
    
    /**
     * Reseptin poistamisen dialogi
     */
    @FXML private void handlePoistaResepti() {
        Dialogs.showQuestionDialog("Poistetaanko?", "Poistetaanko resepti: Nakki-juustosarvet", "Kyllä", "Ei",
                dlg -> 
                    dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
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