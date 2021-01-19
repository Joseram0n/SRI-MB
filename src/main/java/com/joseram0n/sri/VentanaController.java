/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joseram0n.sri;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.swing.text.TabableView;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;
import solr_io.*;

/**
 * FXML Controller class
 *
 * @author joseram0n
 */
public class VentanaController implements Initializable {

    @FXML
    TextField solr_dir;

    @FXML
    Label solr_estado;

    @FXML
    Button boton_OnOff;

    @FXML
    WebView vistaWeb;

    @FXML
    TextArea consola_out;

    @FXML
    TextField consola_in;

    @FXML
    Button bt_ejecutar;

    @FXML
    WebView buscador;

    FileChooser selecFicheros;

    List<File> docsParaIndex;

    @FXML
    Tab sriNormalTab;

    @FXML
    Tab sriMejoradoTab;

    @FXML
    Tab buscadorTab;

    @FXML
    TableView<ModeloTablaSRI> tablaIndex;
    
    
    @FXML
    TableColumn<ModeloTablaSRI, String> nameFile;

    @FXML
    TableColumn<ModeloTablaSRI, Integer> nDocuments;

    @FXML
    TableColumn<ModeloTablaSRI, String> status_index;

    @FXML
    ComboBox<String> core_selec;
    
    ArrayList<SolrDocumentList> sdl;
    
    //########### Otras ventanas ############
    //Servidor Solr
    @FXML
    AnchorPane tabServidorSolrPage;
    @FXML
    TabServidorSolrController tabservidorSolrController;
    //SRI - Normal
    @FXML
    AnchorPane sriNormalTabPage;
    @FXML
    TabSriPageController tabSriNormalPageController;

    GlobalSingleton gbs;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("OK");
        //TODO: Desabilitar pestañas antes de cargar Solr
        /*
        sriNormalTab.setDisable(true);
        sriMejoradoTab.setDisable(true);
        buscadorTab.setDisable(true);
         */
        
        gbs = GlobalSingleton.getInstance();
        String SO = System.getProperty("os.name");
        //Si es windows true, en otro caso false
        gbs.setIsUnix(!SO.startsWith("Windows"));
        //Solr esta apagado en un estado inicial
        gbs.setEncendido(false);
        /*
        nameFile.setCellValueFactory(new PropertyValueFactory<>("nombreArchivo"));
        nDocuments.setCellValueFactory(new PropertyValueFactory<>("n_docs"));
        status_index.setCellValueFactory(new PropertyValueFactory<>("estado_index"));
        */
    }

    //[fcom] para enconjer en netbeans
//<editor-fold defaultstate="collapsed" desc="Ventana: Servidor Solr">

//</editor-fold>

    public void acerca() {
        Alert al = new Alert(AlertType.INFORMATION);
        al.setTitle("Acerca de ");
        al.setHeaderText("Informacion sobre el programa");
        al.setContentText("Este es un programa para la practica de\n"
                + "la asignatura Motores de Busqueda\nde la universidad de Huelva."
                + "\n\n\nAutor: Joseram0n");

        al.showAndWait();
    }
}


