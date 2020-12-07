/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joseram0n.sri;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
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

    Boolean linux;

    String dir_solr;

    Boolean encendido;

    FileChooser selecFicheros;

    List<File> docsParaIndex;
    
    @FXML
    Tab sriNormalTab;
    
    @FXML
    Tab sriMejoradoTab;
    
    @FXML
    Tab buscadorTab;
    
    @FXML
    TableView tablaIndex;

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
        String SO = System.getProperty("os.name");
        if (SO.startsWith("Windows")) {
            linux = false;
        } else {
            linux = true;
        }

        encendido = false;

    }

    //[fcom] para enconjer en netbeans
//<editor-fold defaultstate="collapsed" desc="Ventana: Servidor Solr">
    public void abrir_directorio_solr() {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            //Etiqueta
            solr_dir.setText(selectedDirectory.getAbsolutePath());
            //varaible con el path de solr
            dir_solr = selectedDirectory.getAbsolutePath();
            System.out.println(selectedDirectory.getAbsolutePath());
            check_estado();
        }
    }

    /* Cuando esta apagado -> No Solr nodes are running.*/
    public boolean check_estado() {
        try {
            if (!ejecutar("bin/solr status").contains("No Solr nodes are running.")) {
                encendido = true;
                return true;
            } else {
                encendido = false;
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public void interruptor_Solr() {
        if (solr_dir.getText().isEmpty()) {
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("Error Directorio Solr");
            al.setHeaderText("Algo va mal!");
            al.setContentText("No se encuentra el directorio de Solr.");
            al.showAndWait();
        }
        check_estado();
        if (encendido == false) {
            boton_OnOff.disableProperty().set(true);
            boton_OnOff.setDisable(true);
            ejecutar("bin/solr start -c");
            solr_estado.setText("ON");
            solr_estado.setStyle("-fx-text-fill: #20e634;");
            boton_OnOff.disableProperty().set(false);
            mostrarPanelAdmin();
        } else {
            boton_OnOff.disableProperty().set(true);
            ejecutar("bin/solr stop -all");
            solr_estado.setText("OFF");
            solr_estado.setStyle("-fx-text-fill: #eb0e0e;");
            boton_OnOff.disableProperty().set(false);
        }
    }

    private String ejecutar(String comando) {
        String lectura = new String();
        String salida = new String();
        Process p;
        try {
            p = Runtime.getRuntime().exec(comando, null, new File(dir_solr));
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((lectura = br.readLine()) != null) {
                System.out.println("line: " + lectura);
                if (!lectura.isEmpty()) {
                    salida += lectura + "\n";
                }
            }
            p.waitFor();
            System.out.println("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
            //e.printStackTrace();
        }

        consola_out.setText(salida);
        return salida;
    }

    private void mostrarPanelAdmin() {
        this.vistaWeb.getEngine().load("http://localhost:8983/solr/");
    }
    
    public void ejecutar_comando() {
        String c = consola_in.getText();
        consola_in.clear();
        if (c.contains("start") || c.contains("stop")) {
            interruptor_Solr();
        } else {
            ejecutar(c);
        }

    }
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

    public void actualizar_colecciones() {
        //tablaIndex.
        
        //TODO: movida de la tabla
    }

    //TODO: terminar indexacion de ficheros mediante GUI
    public void seleccionarFicheros() {
        //Crear el filechooser
        selecFicheros = new FileChooser();
        selecFicheros.setTitle("Seleccione los ficheros para indexar");
        selecFicheros.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("All Files", "*.*"));
        
        docsParaIndex = selecFicheros.showOpenMultipleDialog(null);
        
    }
    

}
