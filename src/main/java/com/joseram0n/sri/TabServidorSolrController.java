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
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author Joseram0n
 */
public class TabServidorSolrController implements Initializable {

    @FXML
    private Button btest;
    @FXML
    private TextField solr_dir;
    @FXML
    private Label solr_estado;
    @FXML
    private Button boton_OnOff;
    @FXML
    private TextArea consola_out;
    @FXML
    private TextField consola_in;
    @FXML
    private Button bt_ejecutar;
    @FXML
    private WebView vistaWeb;

    GlobalSingleton gbs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gbs = GlobalSingleton.getInstance();

    }

    @FXML
    public void abrir_directorio_solr() {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            //Etiqueta
            solr_dir.setText(selectedDirectory.getAbsolutePath());
            //varaible con el path de solr
            gbs.setDir_solr(selectedDirectory.getAbsolutePath());

            System.out.println(selectedDirectory.getAbsolutePath());
            check_estado();
        }
    }

    public void interruptor_Solr() {
        if (solr_dir.getText().isEmpty()) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error Directorio Solr");
            al.setHeaderText("Algo va mal!");
            al.setContentText("No se encuentra el directorio de Solr.");
            al.showAndWait();
        }
        check_estado();
        System.out.println("Estado Enencedido: " + gbs.getEncendido());
        if (gbs.getEncendido() == false) {
            boton_OnOff.disableProperty().set(true);
            if (gbs.getIsUnix()) {
                ejecutar("bin/solr start -c");
            } else {
                ejecutar("start -c");
            }
            solr_estado.setText("ON");
            solr_estado.setStyle("-fx-text-fill: #20e634;");
            boton_OnOff.disableProperty().set(false);
            mostrarPanelAdmin();
        } else {
            boton_OnOff.disableProperty().set(true);
            if (gbs.getIsUnix()) {
                ejecutar("bin/solr stop -all");
            } else {
                ejecutar("stop -all");
            }
            solr_estado.setText("OFF");
            solr_estado.setStyle("-fx-text-fill: #eb0e0e;");
            boton_OnOff.disableProperty().set(false);
        }
    }

    @FXML
    public void ejecutar_comando() {
        String c = consola_in.getText();
        consola_in.clear();
        if (c.contains("start") || c.contains("stop")) {
            interruptor_Solr();
        } else {
            ejecutar(c);
        }

    }

    /* Cuando esta apagado -> No Solr nodes are running.*/
    public boolean check_estado() {

        String comandoEstadoSolr = new String();

        System.out.println("gbs_UNIX: " + gbs.getIsUnix());

        if (gbs.getIsUnix() == false) {
            comandoEstadoSolr = "status";
        } else {
            comandoEstadoSolr = "bin/solr status";
        }
        try {
            //linux:"No Solr nodes are running." Win:"No running Solr nodes found."
            String comprobar = ejecutar(comandoEstadoSolr);
            comprobar.toLowerCase();
            if (!(comprobar.contains("No Solr nodes are running.") || comprobar.contains("No running Solr nodes found."))){
                gbs.setEncendido(true);
                return true;
            } else {
                gbs.setEncendido(false);
                return false;
            }
        } catch (Exception e) {
            //TODO: Controlar excepcion
            e.printStackTrace();
        }
        return false;
    }

    private String ejecutar(String comando) {
        String lectura = new String();
        String salida = new String();
        System.out.println("Comando: " + comando + "  Directorio: " + gbs.getDir_solr());
        Process p;
        try {
            if (gbs.getIsUnix()) {
                p = Runtime.getRuntime().exec(comando, null, new File(gbs.getDir_solr()));
            } else {
                p = Runtime.getRuntime().exec(gbs.getDir_solr() + "\\bin\\solr.cmd " + comando);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            
            while ((lectura = br.readLine()) != null) {
                System.out.println("line: " + lectura);
                if (!lectura.isEmpty()) {
                    salida += lectura + "\n";
                }
                //En win10 el "hilo" se congela por algun motivo desconocido
                if(!gbs.getIsUnix() && lectura.contains("Happy searching")){
                    System.out.println("Salida Forzada");
                    break;
                }
                    
            }
            
            p.waitFor(30L,TimeUnit.SECONDS);
            System.out.println("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
            //e.printStackTrace();
            e.printStackTrace();
        }

        consola_out.setText(salida);
        return salida;
    }

    private void mostrarPanelAdmin() {
        this.vistaWeb.getEngine().load("http://localhost:8983/solr/#/");
    }
}
