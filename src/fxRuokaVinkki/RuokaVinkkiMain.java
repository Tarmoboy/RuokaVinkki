package fxRuokaVinkki;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import ruokaVinkki.RuokaVinkki;


/**
 * ______                _            _   _  _         _     _     _ 
 * | ___ \              | |          | | | |(_)       | |   | |   (_)
 * | |_/ / _   _   ___  | | __  __ _ | | | | _  _ __  | | __| | __ _ 
 * |    / | | | | / _ \ | |/ / / _` || | | || || '_ \ | |/ /| |/ /| |
 * | |\ \ | |_| || (_) ||   < | (_| |\ \_/ /| || | | ||   < |   < | |
 * \_| \_| \__,_| \___/ |_|\_\ \__,_| \___/ |_||_| |_||_|\_\|_|\_\|_|
 * 
 * @author tarmo
 * @version 10.12.2023
 *
 */
public class RuokaVinkkiMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("RuokaVinkkiGUIView.fxml"));
            final Pane root = ldr.load();
            final RuokaVinkkiGUIController ruokaVinkkiCtrl = (RuokaVinkkiGUIController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("RuokaVinkki");
            RuokaVinkki ruokaVinkki = new RuokaVinkki();
            ruokaVinkkiCtrl.setRuokaVinkki(ruokaVinkki);
            primaryStage.show();
            
            // Avataan aloitusikkuna
            ruokaVinkkiCtrl.avaa();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei kaytossa
     */
    public static void main(String[] args) {
        launch(args);
    }
}