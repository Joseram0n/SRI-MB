/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sri_simple;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import static javafx.print.PrintColor.COLOR;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;

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

    Boolean linux;
    
    String dir_solr;
    
    Boolean encendido;
    
    /** 
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Holas");
        
        String SO = System.getProperty("os.name");
        if(SO.startsWith("Windows")) 
            linux = false; 
        else 
            linux = true;
        
        encendido = false;
       
    }    
    
    public void abrir_directorio_solr(){
 
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(null);
            if(selectedDirectory != null){
                //Etiqueta
                solr_dir.setText(selectedDirectory.getAbsolutePath());
                //varaible con el path de solr
                dir_solr = selectedDirectory.getAbsolutePath();
                System.out.println(selectedDirectory.getAbsolutePath());
                check_estado();
            }       
    }
    
    /* Cuando esta apagado -> No Solr nodes are running.*/
    public boolean check_estado(){
        try{
            if(!ejecutar("bin/solr status").contains("No Solr nodes are running.")){
                encendido = true;
                return true;
            }else{
                encendido = false;
                return false;
            }   
        }catch(Exception e){
            
        }
        return false;
    }
    
    public void interruptor_Solr(){
        check_estado();
        if(encendido == false){
            boton_OnOff.disableProperty().set(true);
            boton_OnOff.setDisable(true);
            ejecutar("bin/solr start");
            solr_estado.setText("ON");
            solr_estado.setStyle("-fx-text-fill: #20e634;");
            boton_OnOff.disableProperty().set(false);
        }else{
            boton_OnOff.disableProperty().set(true);
            ejecutar("bin/solr stop -all");
            solr_estado.setText("OFF");
            solr_estado.setStyle("-fx-text-fill: #ff2200;");
            boton_OnOff.disableProperty().set(false);
        }
    }
    
    private String ejecutar(String comando){
        String lectura = new String();
        String salida = new String();
        Process p;
        try {
            p = Runtime.getRuntime().exec(comando,null,new File(dir_solr));
            BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
            while ((lectura = br.readLine()) != null){
                System.out.println("line: " + lectura);
                if(!lectura.isEmpty())
                    salida += lectura + " ";
            }
            p.waitFor();
            System.out.println ("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        
        return salida;
    }
    
}
