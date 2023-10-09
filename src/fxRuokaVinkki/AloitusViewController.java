package fxRuokaVinkki;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * @author tarmo
 * @version 8.9.2023
 *
 */
public class AloitusViewController implements ModalControllerInterface<String> {
    
    // aloita-nappi
    @FXML 
    private Button aloita;
    
    // Nappia painettaesa
    @FXML 
    private void handleAloita() {
        handleSulje();
    }
    
    /**
     * Ikkunan sulkeminen nappia painettaessa
     */
    public void handleSulje() {
        ModalController.closeStage(aloita);
    }
    
    /**
     * Ohjelman käynnistyessä näytetään aloitusikkuna
     */
    public static void kaynnista() {
        ModalController.showModal(AloitusViewController.class.getResource("AloitusView.fxml"), "RuokaVinkki", null, null);
    }
    
    /**
     * Ei mitään
     */
    @Override
    public void setDefault(String oletus) {
        // Tyhjä toteutus
    }
    
    /**
     * Tyhjää
     */
    @Override
    public String getResult() {
        // Ei mitään
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