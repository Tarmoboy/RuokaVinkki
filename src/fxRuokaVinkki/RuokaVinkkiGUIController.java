package fxRuokaVinkki;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Collection;

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
 * @version 11.12.2023
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
    @FXML
    private TextField textAika;

    
    /**
     * Avaa reseptin muokkaamisikkunan
     */
    @FXML 
    private void handleMuokkaaReseptia() {
        //ModalController.showModal(RuokaVinkkiGUIController.class.getResource("MuokkaaView.fxml"), "Resepti", null, "");
        muokkaa();
    }
    
    /**
     * Avaa tietoja-ikkunan
     */
    @FXML 
    private void handleTietoja() {
        ModalController.showModal(RuokaVinkkiGUIController.class.getResource("AboutView.fxml"), "Tietoja", null, "");
    }
    
    /**
     * Avaa aloitusikkunan
     * @throws SailoException jos tulee virhe
     */
    public void avaa() throws SailoException {
        AloitusViewController.kaynnista();
        lueTiedosto();
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
        hae(0);
    }
    
    /**
     * Avaa uuden reseptin lisäysikkunan
     */
    @FXML
    private void handleLisaaResepti() {
        uusiResepti();
    }
    
    /**
     * Sulkee ohjelman
     * @throws SailoException jos tallennus ei onnistu
     */
    @FXML
    private void handleLopeta() throws SailoException {
        ruokaVinkki.talleta();
        Platform.exit();
    }
    
    /**
     * Tallentaa tehdyt muokkaukset
     * @throws SailoException jos tallennus ei onnistu
     */
    @FXML
    private void handleTallenna() throws SailoException {
        ruokaVinkki.talleta();
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
    
    private void lueTiedosto() throws SailoException {
        ruokaVinkki.lueTiedosto();
        hae(0);
    }
    
    /**
     * Tekee tarvittavat muut alustukset, alustetaan 
     * reseptilistan kuuntelija
     */
    protected void alusta() {
        tableResepti.setPlaceholder(new Label("Ei vielä ainesosia"));
        chooserReseptit.clear();
        tableResepti.clear();
        textOhje.setText("");
        textAika.setText("");
        chooserReseptit.addSelectionListener(e -> naytaResepti());
    }
    
    /**
     * Näyttää listasta valitun reseptin tiedot
     */
    protected void naytaResepti() {
        reseptiKohdalla = chooserReseptit.getSelectedObject();
        if (reseptiKohdalla == null) return;
        textOhje.setText("");
        tableResepti.clear();
        tulosta();
//        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(textOhje)) {
//            tulosta(os, reseptiKohdalla);  
//        }
    }
    
    /**
     * Hakee reseptien tiedot listaan
     * @param reseptiId reseptin viite, joka aktivoidaan haun jälkeen
     */
    protected void hae(int reseptiId) {
//        chooserReseptit.clear();
//        tableResepti.clear();
//        int index = 0;
//        for (int i = 0; i < ruokaVinkki.getResepteja(); i++) {
//            Resepti resepti = ruokaVinkki.annaResepti(i);
//            if (resepti.getReseptiId() == reseptiId) index = i;
//            chooserReseptit.add(resepti.getNimi(), resepti);
//        }
//        chooserReseptit.setSelectedIndex(index);
        if (reseptiId <= 0) {
            int rId = reseptiId;
            Resepti kohdalla = reseptiKohdalla;
            if (kohdalla != null) {
                rId = kohdalla.getReseptiId();
            }
            int kentta = cbKentta.getSelectionModel().getSelectedIndex();
            String ehto = hakuehto.getText();
            if (ehto.indexOf("*") < 0) {
                ehto = "*" + ehto + "*";
            }
            chooserReseptit.clear();
            int index = 0;
            Collection<Resepti> reseptit;
            try {
                reseptit = ruokaVinkki.etsi(ehto, kentta);
                int i = 0;
                for (Resepti resepti: reseptit) {
                    if (resepti.getReseptiId() == rId) {
                        index = i;
                    }
                    chooserReseptit.add(resepti.getNimi(), resepti);
                    i++;
                }
            } catch (SailoException ex) {
                Dialogs.showMessageDialog("Ongelmia reseptihaussa: " + ex.getMessage(),
                        dlg -> dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
            }
            chooserReseptit.setSelectedIndex(index);
        }
    }

    /**
     * Luo uuden reseptin, jota aletaan editoimaan 
     */
    private void uusiResepti() {
//        Resepti uusiR = new Resepti();
//        uusiR.rekisteroi();
//        uusiR.testiResepti();
//        Ainesosa uusiA = new Ainesosa();
//        uusiA.rekisteroi();
//        uusiA.testiAinesosa();
//        ReseptinAinesosa uusiRA = new ReseptinAinesosa();
//        uusiRA.testiReseptinAinesosa();
//        try {
//            ruokaVinkki.lisaa(uusiR);
//            ruokaVinkki.lisaa(uusiA);
//            ruokaVinkki.lisaa(uusiRA);
//        } catch (SailoException e) {
//            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
//            return;
//        }
//        hae(uusiRA.getReseptiId());
        try {
            Resepti uusiR = new Resepti();
            uusiR.rekisteroi();
            uusiR = MuokkaaController.muokkaa(null, uusiR.clone(), ruokaVinkki);
            if (uusiR.getNimi() == null) {
                return;
            }
            //ruokaVinkki.lisaa(uusiR);
            hae(0); 
        } catch (CloneNotSupportedException e) { 
            // 
        }
//        } catch (SailoException e) {
//            Dialogs.showMessageDialog("Ongelmia uuden reseptin luomisessa: " + e.getMessage(),
//                    dlg -> dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
//        }
    }
    
    /**
     * Siirtyy muokkaamaan valittua reseptiä
     */
    private void muokkaa() {
        if (reseptiKohdalla == null) {
            return;
        }
        try { 
            Stage muokkaaStage = new Stage();
            Resepti resepti; 
            resepti = MuokkaaController.muokkaa(muokkaaStage, reseptiKohdalla.clone(), ruokaVinkki);     
            if (resepti == null) {
                // Jos resepti on poistettu, haetaan kaikki reseptit uudelleen
                chooserReseptit.clear();
                tableResepti.clear();
                textOhje.setText("");
                textAika.setText("");
                hae(0);
                return;
            }
            ruokaVinkki.korvaaTaiLisaa(resepti);
            //hae(resepti.getReseptiId());
            hae(0);
        } catch (CloneNotSupportedException e) { 
            // 
        } catch (SailoException e) { 
            Dialogs.showMessageDialog("Ongelmia reseptin muokkaamisessa: " + e.getMessage(),
                    dlg -> dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
        } 
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
     */
    private void tulosta() {
        tableResepti.clear();
        textAika.setText(reseptiKohdalla.getAika());
        textOhje.setText(reseptiKohdalla.getOhje());
        for (int i = 0; i < ruokaVinkki.getReseptienAinesosia(); i++) {
            ReseptinAinesosa reseptinAinesosa = ruokaVinkki.annaReseptinAinesosa(i);
            // Jos reseptin id täsmää, siirrytään eteenpäin
            if (reseptiKohdalla.getReseptiId() == reseptinAinesosa.getReseptiId()) {
                Ainesosa ainesosa = ruokaVinkki.annaAinesosa(reseptinAinesosa.getAinesosaId());
                // Jos myös ainesosan id täsmää, lisätään taulukkoon
                if (reseptinAinesosa.getAinesosaId() == ainesosa.getAinesosaId()) {
                   tableResepti.add(ainesosa.getNimi(), reseptinAinesosa.getMaara());
                }
            }
        }
    }
    
    /**
     * Tulostaa reseptin tiedot väliaikaisesti ohjekenttään
     * @param os tietovirta johon tulostetaan
     * @param resepti tulostettava resepti
     */
    public void tulosta(PrintStream os, final Resepti resepti) {
        textAika.setText(resepti.getAika());
        
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
