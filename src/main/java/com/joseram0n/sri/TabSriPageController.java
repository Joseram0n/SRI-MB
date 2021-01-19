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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;
import solr_io.SolrConsulta;
import solr_io.SolrParser;

/**
 * FXML Controller class
 *
 * @author Joseram0n
 */
public class TabSriPageController implements Initializable {

    @FXML
    private TableView<ModeloTablaSRI> tablaIndex;
    @FXML
    private TableColumn<ModeloTablaSRI, Integer> nFile;
    @FXML
    private TableColumn<ModeloTablaSRI, String> nameFile;
    @FXML
    private TableColumn<ModeloTablaSRI, Integer> nDocuments;
    @FXML
    private TableColumn<ModeloTablaSRI, String> status_index;
    @FXML
    private Button selecParaIndexar;
    @FXML
    private ComboBox<String> core_selec;
    @FXML
    private ComboBox<String> core_selec_nEval;
    @FXML
    private TextField txt_dirPreguntas;
    @FXML
    private CheckBox disableWordCount;
    @FXML
    private CheckBox caracteresEspeciales;
    @FXML
    private Spinner<Integer> wordSpinner;

    FileChooser selecFicheros;

    List<File> docsParaIndex;

    ArrayList<SolrDocumentList> sdl;

    GlobalSingleton gbs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        gbs = GlobalSingleton.getInstance();

        nFile.setCellValueFactory(new PropertyValueFactory<>("n_File"));
        nameFile.setCellValueFactory(new PropertyValueFactory<>("nombreArchivo"));
        nDocuments.setCellValueFactory(new PropertyValueFactory<>("n_docs"));
        status_index.setCellValueFactory(new PropertyValueFactory<>("estado_index"));
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 5);
        wordSpinner.setValueFactory(valueFactory);
    }

    @FXML
    private void seleccionarFicheros() {
        //Crear el filechooser
        selecFicheros = new FileChooser();
        selecFicheros.setTitle("Seleccione los ficheros para indexar");
        selecFicheros.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("All Files", "*.*"));

        docsParaIndex = selecFicheros.showOpenMultipleDialog(null);
        actualizar_tabla_indexar(docsParaIndex);
    }

    private void actualizar_tabla_indexar(List<File> lf) {
        //Obtener datos
        List<ModeloTablaSRI> filaTabla = new ArrayList<ModeloTablaSRI>();
        SolrParser sp = new SolrParser();

        int ndoc = 1;
        for (File f : lf) {
            filaTabla.add(new ModeloTablaSRI(ndoc, f.getName(), sp.leer_doc(f.getAbsolutePath()).size(), "No"));
            ndoc++;
        }

        //atualizar tabla
        tablaIndex.getItems().setAll(filaTabla);
    }

    @FXML
    private void indexar_docs() {
        SolrConsulta sc = new SolrConsulta();

        List<ModeloTablaSRI> filas = tablaIndex.getItems();
        try {
            for (File file : docsParaIndex) {

                if (sc.indexar(file.getAbsolutePath(), core_selec.getValue())) {
                    for (ModeloTablaSRI fila : filas) {
                        if (fila.nombreArchivo == file.getName()) {
                            fila.estado_index = "Si";
                            System.out.println("Coincide");
                        }
                    }
                } else {
                    Alert al = new Alert(Alert.AlertType.ERROR);
                    al.setTitle("Error Indexando Ficheros");
                    al.setHeaderText("Algo va mal!");
                    al.setContentText("No se indexo el fichero " + file.getName() + " correctamente.");
                    al.showAndWait();
                }
            }

            Alert al = new Alert(Alert.AlertType.CONFIRMATION);
            al.setTitle("Ficheros Indexados");
            al.setHeaderText("Todo ok!");
            al.setContentText("Nada ha explotado de momento.");
            al.showAndWait();

            tablaIndex.getItems().setAll(filas);

        } catch (SolrServerException ex) {
            Logger.getLogger(VentanaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VentanaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void actualizar_colecciones() {
        SolrConsulta sc = new SolrConsulta();

        try {
            core_selec.getItems().setAll(sc.obtener_nombre_cores());
            core_selec_nEval.getItems().setAll(sc.obtener_nombre_cores());
        } catch (SolrServerException ex) {
            Logger.getLogger(VentanaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VentanaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void borrar_tabla_index() {
        tablaIndex.getItems().clear();
        docsParaIndex = null;
    }

    @FXML
    private void cargarPreg() {
        SolrConsulta sc = new SolrConsulta();

        selecFicheros = new FileChooser();
        selecFicheros.setTitle("Seleccione el fichero LISA.QUE");
        selecFicheros.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("All Files", "*.*"));

        File p = selecFicheros.showOpenDialog(null);
        int nPalabras;
        if (p != null) {
            try {
                if (wordSpinner.isDisabled() == false) {
                    nPalabras = wordSpinner.getValue();
                } else {
                    nPalabras = -1;
                }
                txt_dirPreguntas.setText(p.getAbsolutePath());
                sdl = sc.buscar(p.getAbsolutePath(), core_selec_nEval.getValue(), nPalabras, caracteresEspeciales.isSelected());

                Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                al.setTitle("Preguntas Cargadas");
                al.setHeaderText("Todo ok!");
                al.setContentText("Nada ha explotado de momento.");
                al.showAndWait();
            } catch (SolrServerException ex) {
                Logger.getLogger(VentanaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(VentanaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void desactivarConteoPalabras() {
        if (!disableWordCount.isSelected()) {
            wordSpinner.setDisable(true);
        } else {
            wordSpinner.setDisable(false);
        }
    }

    @FXML
    private void crear_trec_file() {
        try {
            SolrParser sp = new SolrParser();
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(null);
            if (selectedDirectory != null) {
                sp.crear_trec_file(sdl, selectedDirectory.getAbsolutePath() + "/trec_top_file.test");
                System.out.println(selectedDirectory.getAbsolutePath() + "\\trec_top_file.test");

                Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                al.setTitle("Archivo Trec-rel");
                al.setHeaderText("Todo ok!");
                al.setContentText("Nada ha explotado de momento.");
                al.showAndWait();
            }

        } catch (IOException ex) {
            Logger.getLogger(VentanaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void crear_normal() {
        if (!ejecutar("create -c normal").contains("Re-using existing configuration")) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error Crear Core");
            al.setHeaderText("Algo va mal!");
            al.setContentText("No se ha creado el core.");
            al.showAndWait();
        } else {
            Alert al = new Alert(Alert.AlertType.CONFIRMATION);
            al.setTitle("Crear Core");
            al.setHeaderText("Todo bien!");
            al.setContentText("Se ha creado el core.");
            al.showAndWait();
        }

    }

    @FXML
    private void crear_mejorado() {
        if (!ejecutar("create -c mejorado").contains("Re-using existing configuration")) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error Crear Core");
            al.setHeaderText("Algo va mal!");
            al.setContentText("No se ha creado el core.");
            al.showAndWait();
        } else {
            try {
                Process p = Runtime.getRuntime().exec(
                        "curl --X POST -H \"Content-type:application/json\" --data-binary "
                        + "\"{\\\"add-field\\\": {\\\"name\\\":\\\"title\\\", \\\"type\\\":\\\"text_en\\\", \\\"multiValued\\\":true, \\\"stored\\\":true, \\\"indexed\\\":true},"
                        + "\\\"add-field\\\": {\\\"name\\\":\\\"text\\\", \\\"type\\\":\\\"text_en\\\", \\\"multiValued\\\":true, \\\"stored\\\":true, \\\"indexed\\\":true},"
                        + "\\\"add-field\\\": {\\\"name\\\":\\\"organization\\\", \\\"type\\\":\\\"text_en\\\", \\\"multiValued\\\":true, \\\"stored\\\":true, \\\"indexed\\\":true},"
                        + "\\\"add-field\\\": {\\\"name\\\":\\\"location\\\", \\\"type\\\":\\\"text_en\\\", \\\"multiValued\\\":true, \\\"stored\\\":true, \\\"indexed\\\":true}}\" http://localhost:8983/solr/mejorado/schema");
                p.destroy();
                Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                al.setTitle("Crear Core");
                al.setHeaderText("Todo bien!");
                al.setContentText("Se ha creado el core.");
                al.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(TabSriPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    private void limpiar_core() {
        SolrConsulta sc = new SolrConsulta();
        System.out.println(core_selec.getValue().split("_")[0]);
        if (sc.limpiar(core_selec.getValue().split("_")[0]) == false) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error Limpiar Core");
            al.setHeaderText("Algo va mal!");
            al.setContentText("No se ha limpiado el core.");
            al.showAndWait();
        } else {
            Alert al = new Alert(Alert.AlertType.CONFIRMATION);
            al.setTitle("Limpiar Core");
            al.setHeaderText("Todo bien!");
            al.setContentText("Se ha limpiado el core.");
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

        //consola_out.setText(salida);
        return salida;
    }

    @FXML
    public void crear_archivo_qrels() {
        SolrParser sp = new SolrParser();

        selecFicheros = new FileChooser();
        selecFicheros.setTitle("Seleccione el fichero LISARJ.NUM");
        selecFicheros.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("All Files", "*.*"));

        File p = selecFicheros.showOpenDialog(null);

        if (p != null) {
            try {
                sp.generar_consultas_relevantes(p.getAbsolutePath());

                Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                al.setTitle("Archivo Qrels");
                al.setHeaderText("Todo bien!");
                al.setContentText("Se ha generado en el mismo directorio que LISARJ.NUM");
                al.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(TabSriPageController.class.getName()).log(Level.SEVERE, null, ex);
                Alert al = new Alert(Alert.AlertType.ERROR);
                al.setTitle("Archivo Qrels");
                al.setHeaderText("OPSSSSSSSS!");
                al.setContentText("NO se ha generado bien.");
                al.showAndWait();
            }
        }
    }
}
