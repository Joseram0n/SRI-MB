/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joseram0n.sri;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import uk.ac.gla.terrier.jtreceval.trec_eval;

/**
 *
 * @author Joseram0n
 */
public class TabTrecEvalPageController implements Initializable {

    @FXML
    TextField txt_qrels;
    @FXML
    TextField txt_rel;
    @FXML
    private TableView<ModeloTablaTREC> tablaTrecEval;
    @FXML
    private TableColumn<ModeloTablaTREC, String> col_medida;
    @FXML
    private TableColumn<ModeloTablaTREC, String> col_valor;


    GlobalSingleton gbs;

    FileChooser selecFicheros;

    File qrels;

    File res;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gbs = GlobalSingleton.getInstance();
        
        col_medida.setCellValueFactory(new PropertyValueFactory<>("medida"));
        col_valor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }
    
    @FXML
    private void selec_qrels() {

        selecFicheros = new FileChooser();
        selecFicheros.setTitle("Seleccione el fichero qrels.");
        selecFicheros.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("All Files", "*.*"));

        qrels = selecFicheros.showOpenDialog(null);
        txt_qrels.setText(qrels.getName());
    }
    
    @FXML
    private void selec_res() {

        selecFicheros = new FileChooser();
        selecFicheros.setTitle("Seleccione el fichero res.");
        selecFicheros.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("All Files", "*.*"));

        res = selecFicheros.showOpenDialog(null);
        txt_rel.setText(res.getName());
    }

    @FXML
    void realizar_eval() {
        trec_eval te = new trec_eval();
        String[][] output = te.runAndGetOutput(new String[]{"-q", qrels.getAbsolutePath(), res.getAbsolutePath()});

        ArrayList<Integer> todo = new ArrayList<>();
        List<ModeloTablaTREC> filaTabla = new ArrayList<ModeloTablaTREC>();
        tablaTrecEval.getItems().clear();

        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < output[i].length; j++) {
                if (output[i][j].equals("all")) {
                    todo.add(i);
                }
            }
        }

        for (Integer integer : todo) {
            //System.out.println("Medida: " + output[integer][0] + "   Valor: " + output[integer][2]);
            filaTabla.add(new ModeloTablaTREC(output[integer][0], output[integer][2]));
        }
        
        tablaTrecEval.getItems().addAll(filaTabla);
    }

}
