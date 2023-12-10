package fxRuokaVinkki;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ruokaVinkki.Ainesosa;
import ruokaVinkki.Resepti;
import ruokaVinkki.ReseptinAinesosa;
import ruokaVinkki.RuokaVinkki;
import ruokaVinkki.SailoException;

/**
 * @author tarmo
 * @version 11.12.2023
 *
 */
public class MuokkaaController implements ModalControllerInterface<Resepti>, Initializable {
    
    @FXML
    private GridPane gridTietue;

    @FXML
    private Label labelVirhe;

    @FXML
    private ScrollPane panelTietue;
    
    @FXML
    private TextField textNimi;
    
    @FXML
    private StringGrid<Resepti> gridAinesosat;

    @FXML
    private TextField textAika;

    @FXML
    private TextArea textOhje;
    
    @FXML
    private Button buttonTallenna;
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        gridAinesosat.setPlaceholder(new Label("Ei vielä ainesosia"));
        textAika.textProperty().addListener((obs, vanhaArvo, uusiArvo) -> {
            textAika.getStyleClass().removeAll("virhe", "normaali");
            if (!aikaTarkistus(uusiArvo)) {
                textAika.getStyleClass().add("virhe");
                textAika.getStyleClass().remove("normaali");
                naytaVirhe("Valmistusaika: anna muodossa ( päivää d tuntia h ) minuuttia min");
            } else {
                textAika.getStyleClass().remove("virhe");
                textAika.getStyleClass().add("normaali");
                naytaVirhe("");
            }
        });
        stringGridKuuntelija();
    }
    
    /**
     * Reseptin poistamisen dialogi
     */
    @FXML private void handlePoistaResepti() {
//        Dialogs.showQuestionDialog("Poistetaanko?", "Poistetaanko resepti: Nakki-juustosarvet", "Kyllä", "Ei",
//                dlg -> 
//                    dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
        poistaResepti();
    }
    
    /**
     * Uuden ainesosan lisääminen
     */
    @FXML
    void handleLisaaAinesosa() {
        uusiAinesosa();
    }
    
    /**
     * Ainesosan poistaminen
     */
    @FXML
    void handlePoistaAinesosa() {
        poistaAinesosa();
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
        tallenna();
        ModalController.closeStage(gridTietue);
    }
    
    /**
     * Ei mitään
     */
    @Override
    public void setDefault(Resepti oletus) {
        // Tyhjä toteutus
    }
    
    /**
     * Tyhjää
     */
    @Override
    public Resepti getResult() {
        if (onPoistettu) return null;
        return reseptiKohdalla;
    }
    
    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        // Ei mitään
    }
    
    //===========================================================================================    
    // Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia    
    
    private Resepti reseptiKohdalla;
    private RuokaVinkki rV;
    private boolean onPoistettu = false;
    //private int poistettu = 0;
    private Set<String> poistetutAinesosat = new HashSet<>();
    private Map<Integer, String[]> uudetAinesosat = new HashMap<>();
    
    /**
     * @param modalitystage modaalisuus, null viittaa pääohjelmaan
     * @param resepti käsiteltävä resepti
     * @param ruokaVinkki mistä resepti löytyy
     * @return käsitelty resepti, tai null jos peruutetaan
     */
    public static Resepti muokkaa(Stage modalitystage, Resepti resepti, RuokaVinkki ruokaVinkki) {
        return ModalController.<Resepti, MuokkaaController>showModal(MuokkaaController.class.getResource("MuokkaaView.fxml"), 
                "Muokkaa reseptiä", modalitystage, resepti, ctrl -> {ctrl.setResepti(resepti, ruokaVinkki);});
    }

    /**
     * Käsiteltävän reseptin asettaminen
     * @param resepti käsiteltävä resepti
     * @param ruokaVinkki mistä resepti löytyy
     */
    public void setResepti(Resepti resepti, RuokaVinkki ruokaVinkki) {
        reseptiKohdalla = resepti;
        rV = ruokaVinkki;
        tulosta(resepti);
    }
    
    /**
     * Lisää paikan uudelle ainesosalle StringGridiin
     */
    public void uusiAinesosa() {
        gridAinesosat.add("", "");
        int uusiAinesosaI = gridAinesosat.getItems().size() - 1;
        // Siirrytään muokkaamaan uutta tyhjää riviä
        gridAinesosat.edit(uusiAinesosaI, gridAinesosat.getColumns().get(0));
        // Skrollataan alas asti, jotta uusi ainesosarivi näkyy
        gridAinesosat.scrollTo(uusiAinesosaI);
        //muokatutAinesosat.put(uusiAinesosaI, uusiAinesosa);
    }
    
    /**
     * Poistaa valitun ainesosan StringGridistä
     */
    public void poistaAinesosa() {
        int ainesosaI = gridAinesosat.getSelectionModel().getSelectedIndex();
        // Jos on valittu joku ainesosa
        if (ainesosaI >= 0) {
            // Poistetaan valittu ainesosa
            String ainesosaNimi = gridAinesosat.get(ainesosaI, 0);
            //Ainesosa ainesosa = rV.haeAinesosaNimella(ainesosaNimi);
            gridAinesosat.getItems().remove(ainesosaI);
            // Tämä poistaisi ne myös vaikka peruuttaa
            //rV.poistaReseptinAinesosa(reseptiKohdalla, ainesosa);
            //poistettu += 1;
            gridAinesosat.refresh();
            poistetutAinesosat.add(ainesosaNimi);
            paivitaStringGrid();
        } else {
            Dialogs.showMessageDialog("Valitse ensin poistettava ainesosa",
                    dlg -> dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
        }
    }
    
    /**
     * Poistetaan muokattavana oleva resepti
     */
    public void poistaResepti() {
        Resepti resepti = reseptiKohdalla;
        if (resepti == null) {
            return;
        }
        // Jos käyttäjä valitsee ei, ei poistoa tehdä
        if (!Dialogs.showQuestionDialog("Poistetaanko?", "Poistetaanko resepti: " + resepti.getNimi(), "Kyllä", "Ei",
                dlg -> dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()))) {
            return;
        }
        rV.poista(resepti);
        onPoistettu = true;
        ModalController.closeStage(gridTietue);
    }
    
    /**
     * Kuuntelija, laittaa muokatut ainesosat muistiin
     */
    private void stringGridKuuntelija() {
        // Ainesosien muokkaamisen kuuntelu
        gridAinesosat.getColumns().get(0).setOnEditCommit(event -> {
            int rivi = event.getTablePosition().getRow();
            String[] ainesosa = uudetAinesosat.getOrDefault(rivi, new String[]{"", ""});
            ainesosa[0] = String.valueOf(event.getNewValue());
            uudetAinesosat.put(rivi, ainesosa);
            gridAinesosat.refresh();
        });
        // Määrien muokkaamisen kuuntelu
        gridAinesosat.getColumns().get(1).setOnEditCommit(event -> {
            int rivi = event.getTablePosition().getRow();
            String[] ainesosa = uudetAinesosat.getOrDefault(rivi, new String[]{"", ""});
            ainesosa[1] = String.valueOf(event.getNewValue());
            uudetAinesosat.put(rivi, ainesosa);
            gridAinesosat.refresh();
        });
    }
    
    /**
     * Päivitetään StringGrid nullien eliminoimiseksi
     */
    public void paivitaStringGrid() {
        gridAinesosat.clear();
        for (int i = 0; i < rV.getReseptienAinesosia(); i++) {
            ReseptinAinesosa reseptinAinesosa = rV.annaReseptinAinesosa(i);
            // Jos reseptin id täsmää, siirrytään eteenpäin
            if (reseptiKohdalla.getReseptiId() == reseptinAinesosa.getReseptiId()) {
                Ainesosa ainesosa = rV.annaAinesosa(reseptinAinesosa.getAinesosaId());
                // Jos myös ainesosan id täsmää, lisätään taulukkoon mikäli nimi ei ole poistettujen listalla
                if (reseptinAinesosa.getAinesosaId() == ainesosa.getAinesosaId() && !poistetutAinesosat.contains(ainesosa.getNimi())) {
                   gridAinesosat.add(ainesosa.getNimi(), reseptinAinesosa.getMaara());
                }
            }
        }
        // Uusien ainesosien lisääminen StringGridiin manuaalisesti
        for (Map.Entry<Integer, String[]> entry : uudetAinesosat.entrySet()) {
            String[] ainesosa = entry.getValue();
            if (!poistetutAinesosat.contains(ainesosa[0])) {
                gridAinesosat.add(ainesosa[0], ainesosa[1]);
            }
        }
    }
    
    /**
     * Tallentaa reseptin muokkaukset väliaikaisesti
     */
    public void tallenna() {
        if (reseptiKohdalla == null) {
            return;
        }
        // Päivitetään reseptin tiedot
        try {
            reseptiKohdalla.setNimi(textNimi.getText());
            reseptiKohdalla.setAika(textAika.getText());
            reseptiKohdalla.setOhje(textOhje.getText());
            rV.poistaReseptinAinesosat(reseptiKohdalla);
            int koko = gridAinesosat.getItems().size();
            for (int i = 0; i < koko; i++) {
                String ainesosaNimi = gridAinesosat.get(i, 0);
                if (ainesosaNimi == null || ainesosaNimi.trim().isEmpty()) {
//                    if (poistettu > 0) {
//                        // Väliaikainen korjaus poistettujen indeksien ohitukseen
//                        koko += 1;
//                        poistettu--;
//                    }
                    continue;
                }
                String maara = gridAinesosat.get(i, 1);
                // Haetaan tai luodaan uusi ainesosa
                Collection<Ainesosa> loydetyt = rV.getAinesosat().etsi(ainesosaNimi);
                Ainesosa ainesosa;
                // Jos ei löydy vastinetta, luodaan uusi
                if (loydetyt.isEmpty()) {
                    ainesosa = new Ainesosa();
                    ainesosa.setNimi(ainesosaNimi);
                    ainesosa.rekisteroi();
                    rV.lisaa(ainesosa);
                } else {
                    ainesosa = loydetyt.iterator().next();
                }
                // Luodaan uusi reseptin ainesosa ja lisätään se
                ReseptinAinesosa reseptinAinesosa = new ReseptinAinesosa();
                reseptinAinesosa.setReseptiId(reseptiKohdalla.getReseptiId());
                reseptinAinesosa.setAinesosaId(ainesosa.getAinesosaId());
                reseptinAinesosa.setMaara(maara);
                rV.lisaa(reseptinAinesosa);
            }
            rV.korvaaTaiLisaa(reseptiKohdalla);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia reseptin tallentamisessa: " + e.getMessage(),
                    dlg -> dlg.getDialogPane().getStylesheets().add(getClass().getResource("ruokavinkki.css").toExternalForm()));
            return;
        }
    }
    
    private boolean aikaTarkistus(String aika) {
        if (aika == null || aika.trim().isEmpty()) return true;
        // Regular expression
        String regex = "^"                  // Alku merkkijonolle
                + "(?:(\\d+)\\s*d\\s*)?"    // Valinnainen päivien määrä, esim. "2 d" tai "2d"
                + "(?:(\\d+)\\s*h\\s*)?"    // Valinnainen tuntien määrä, esim. "4 h" tai "4h"
                + "(\\d+)\\s*min"           // Minuuttien määrä, esim. "30 min"
                + "$";                      // Loppu merkkijonolle
        return aika.matches(regex);
    }
    
    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            textAika.getStyleClass().removeAll("virhe");
            // Tallentamisnapin aktivoiminen
            buttonTallenna.setDisable(false);
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
        textAika.getStyleClass().add("virhe");
        // Tallentamisnapin disabloiminen
        buttonTallenna.setDisable(true);
    }

    /**
     * Tulostaa reseptin tiedot niille varatuille paikoille
     * @param resepti käsiteltävä resepti
     */
    public void tulosta(Resepti resepti) {
        textNimi.setText(resepti.getNimi());
        textAika.setText(resepti.getAika());
        textOhje.setText(resepti.getOhje());
        gridAinesosat.clear();
        for (int i = 0; i < rV.getReseptienAinesosia(); i++) {
            ReseptinAinesosa reseptinAinesosa = rV.annaReseptinAinesosa(i);
            // Jos reseptin id täsmää, siirrytään eteenpäin
            if (reseptiKohdalla.getReseptiId() == reseptinAinesosa.getReseptiId()) {
                Ainesosa ainesosa = rV.annaAinesosa(reseptinAinesosa.getAinesosaId());
                // Jos myös ainesosan id täsmää, lisätään taulukkoon
                if (reseptinAinesosa.getAinesosaId() == ainesosa.getAinesosaId()) {
                   gridAinesosat.add(ainesosa.getNimi(), reseptinAinesosa.getMaara());
                }
            }
        }
    }
}