/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sri_simple;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author joseram0n
 */
public class VentanaController implements Initializable {

    /** 
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void test(){
 
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(null);
            if(selectedDirectory != null)
                System.out.println(selectedDirectory.getAbsolutePath());
                System.out.println("Holas");
    }
    
}
