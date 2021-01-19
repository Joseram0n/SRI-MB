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
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebView;
import org.apache.solr.client.solrj.SolrServerException;
import solr_io.SolrConsulta;

/**
 * FXML Controller class
 *
 * @author Joseram0n
 */
public class TabBuscadorPageController implements Initializable {

    @FXML
    private WebView buscador;

    @FXML
    private ComboBox<String> core_sel;

    GlobalSingleton gbs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        gbs = GlobalSingleton.getInstance();
    }

    @FXML
    private void actualizar_colecciones() {
        SolrConsulta sc = new SolrConsulta();
        System.out.println("HOLA");
        try {
            core_sel.getItems().setAll(sc.obtener_nombre_cores());
        } catch (SolrServerException ex) {
            Logger.getLogger(VentanaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VentanaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void mostrarPanelBuscador() {
        this.buscador.getEngine().load("http://localhost:8983/solr/" + core_sel.getValue().split("_")[0] + "/browse");
    }

    @FXML
    private void start_browser() {
        try {
            
            ejecutar("package add-repo solritas https://raw.githubusercontent.com/erikhatcher/solritas/master/repo");
            ejecutar("package install solritas");
            ejecutar("package deploy -y solritas -collections " + core_sel.getValue().split("_")[0] + " -p DF=title");
            
            mostrarPanelBuscador();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Archivo Qrels");
            al.setHeaderText("OPSSSSSSSS!");
            al.setContentText("NO se ha generado bien.");
            al.showAndWait();
        }
    }

    private String ejecutar(String comando) {
        String lectura = new String();
        String salida = new String();
        System.out.println("Comando: " + comando + "  Directorio: " + gbs.getDir_solr());
        Process p;
        try {
            if (gbs.getIsUnix()) {
                p = Runtime.getRuntime().exec(comando, null, new File(gbs.getDir_solr() + "\\bin\\solr"));
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
                if (!gbs.getIsUnix() && lectura.contains("Happy searching")) {
                    System.out.println("Salida Forzada");
                    break;
                }

            }

            p.waitFor(30L, TimeUnit.SECONDS);
            System.out.println("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
            //e.printStackTrace();
            e.printStackTrace();
        }
        return salida;
    }
}
