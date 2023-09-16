package fxRuokaVinkki;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author tarmo
 * @version 8.9.2023
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