package fxRuokaVinkki;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import ruokaVinkki.RuokaVinkki;
import ruokaVinkki.Resepti;
import ruokaVinkki.Ainesosa;
import ruokaVinkki.ReseptinAinesosa;
import ruokaVinkki.SailoException;


/**
 * @author tarmo
 * @version 13.11.2023
 *
 */
public class RuokaVinkkiGUIController implements Initializable {
    
    @FXML
    private ComboBoxChooser<String> cbKentta;
    @FXML
    private TextField hakuehto;
    @FXML
    private StringGrid<Resepti> tableResepti;
    @FXML
    private ListChooser<Resepti> chooserReseptit;
    @FXML
    private TextArea textOhje;

    
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
//        Dialogs.showMessageDialog("Ei osata vielä lisätä reseptiä",
//                dlg -> 
//                    dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
        uusiResepti();
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
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();      
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
    
    //===========================================================================================    
    // Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia    
    
    private RuokaVinkki ruokaVinkki;
    private Resepti reseptiKohdalla;
    
    /**
     * Tekee tarvittavat muut alustukset, tulostetaan reseptin tiedot 
     * ohje-kenttään ja alustetaan myös reseptilistan kuuntelija
     */
    protected void alusta() {
        textOhje.setFont(new Font("Arial", 16));
        textOhje.setWrapText(true);
        chooserReseptit.clear();
        chooserReseptit.addSelectionListener(e -> naytaResepti());
    }
    
    /**
     * Näyttää listasta valitun reseptin tiedot, tilapäisesti yhteen isoon edit-kenttään
     */
    protected void naytaResepti() {
        reseptiKohdalla = chooserReseptit.getSelectedObject();
        if (reseptiKohdalla == null) return;
        textOhje.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(textOhje)) {
            tulosta(os, reseptiKohdalla);  
        }
    }
    
    /**
     * Hakee reseptien tiedot listaan
     * @param reseptiId reseptin viite, joka aktivoidaan haun jälkeen
     */
    protected void hae(int reseptiId) {
        chooserReseptit.clear();
        int index = 0;
        for (int i = 0; i < ruokaVinkki.getResepteja(); i++) {
            Resepti resepti = ruokaVinkki.annaResepti(i);
            if (resepti.getReseptiId() == reseptiId) index = i;
            chooserReseptit.add(resepti.getNimi(), resepti);
        }
        chooserReseptit.setSelectedIndex(index);
    }

    /**
     * Luo uuden reseptin, jota aletaan editoimaan 
     */
    protected void uusiResepti() {
        Resepti uusiR = new Resepti();
        uusiR.rekisteroi();
        uusiR.testiResepti();
        Ainesosa uusiA = new Ainesosa();
        uusiA.rekisteroi();
        uusiA.testiAinesosa();
        ReseptinAinesosa uusiRA = new ReseptinAinesosa();
        uusiRA.testiReseptinAinesosa();
        try {
            ruokaVinkki.lisaa(uusiR);
            ruokaVinkki.lisaa(uusiA);
            ruokaVinkki.lisaa(uusiRA);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
        hae(uusiRA.getReseptiId());
    }

    /**
     * @param ruokaVinkki reseptit ja ainesosat, joita käytetään tässä käyttöliittymässä
     */
    public void setRuokaVinkki(RuokaVinkki ruokaVinkki) {
        this.ruokaVinkki = ruokaVinkki;
        naytaResepti();
    }
    
    /**
     * Tulostaa reseptin tiedot
     * @param os tietovirta johon tulostetaan
     * @param resepti tulostettava resepti
     */
    public void tulosta(PrintStream os, final Resepti resepti) {
        os.println("----------------------------------------------");
        resepti.tulosta(os);
        os.println("----------------------------------------------");
        List<Ainesosa> ainesosat = ruokaVinkki.annaAinesosat(resepti.getReseptiId());

        // Tulostetaan jokaisen ainesosan tiedot
        for (Ainesosa ainesosa : ainesosat) {
            if (ainesosa != null) {
                ainesosa.tulosta(os);
            }
        }
    }
    
    /**
     * Tulostaa listassa olevat reseptit tekstialueeseen
     * @param text alue johon tulostetaan
     */
    public void tulostaValitut(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki reseptit");
            for (int i = 0; i < ruokaVinkki.getResepteja(); i++) {
                Resepti resepti = ruokaVinkki.annaResepti(i);
                tulosta(os, resepti);
                os.println("\n\n");
            }
        }
    }

}
