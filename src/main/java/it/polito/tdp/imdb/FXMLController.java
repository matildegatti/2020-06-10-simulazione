/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(this.model.getGrafo()==false) {
    		this.txtResult.setText("Creare prima il grafo");
    		return;
    	}
    	
    	Actor attore=this.boxAttore.getValue();
    	if(attore==null) {
    		this.txtResult.setText("Selezionare un attore");
    		return;
    	}
    	
    	List<Actor> list=this.model.doAttoriSimili(attore);
    	this.txtResult.appendText("Attori simili a: "+attore.toString()+"\n");
    	for(Actor a:list) {
    		this.txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	this.boxAttore.getItems().clear();
    	
    	String genere=this.boxGenere.getValue();
    	
    	if(genere==null) {
    		this.txtResult.setText("Selezionare un genere");
    		return;
    	}
    	
    	this.model.creaGrafo(genere);
    	
    	List<Actor> attori=this.model.getVertici();
    	Collections.sort(attori, new Comparator<Actor>(){
			@Override
			public int compare(Actor a1, Actor a2) {
				return a1.getLastName().compareTo(a2.getLastName());
			}
		});
    	
    	this.boxAttore.getItems().addAll(attori);
    	this.txtResult.setText("GRAFO CREATO \n"+"# vertici: "+this.model.nVertici()+"\n"+"# archi: "+this.model.nArchi());
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	this.txtResult.clear();
    	
    	int n;
    	try{
    		n=Integer.parseInt(this.txtGiorni.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero intero di giorni");
    		return;
    	}
    	
    	this.model.simula(n);
    	
    	this.txtResult.appendText("Numero pause: "+this.model.giorniPausa()+"\n");
    	for(Actor a:this.model.listIntervistati()) {
    		this.txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.boxGenere.getItems().addAll(this.model.getGeneri());
    }
}
