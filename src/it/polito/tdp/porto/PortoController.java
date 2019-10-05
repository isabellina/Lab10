package it.polito.tdp.porto;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
          Author a = boxPrimo.getValue();
          txtResult.appendText(model.getCoautori(a));
    }

    @FXML
    void handleSequenza(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";
        
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.init();
    }
    
    private void init() {
    	ObservableList<Author> listaAutori = FXCollections.observableList(model.getAutori());
        boxPrimo.setItems(listaAutori);
        boxPrimo.setValue(listaAutori.get(0));
    	ObservableList<Author> listaNoCoautore = FXCollections.observableList(model.getNoCoautori(boxPrimo.getValue()));
        boxSecondo.setItems(listaNoCoautore);
        boxSecondo.setValue(listaNoCoautore.get(0));
    }
}
