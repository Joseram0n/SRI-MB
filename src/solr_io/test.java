/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solr_io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author joseram0n
 */
public class test {
    // TEST - v0.1 (8/11/2020) App que lea el fichero del corpus Lisa0.001 y sea capaz de indexarlo desde Java usando Solrj.
    public static void test1(){
        //Parsear documento
        SolrParser sp = new SolrParser();
        
        ArrayList<Documento> d = sp.leer_doc("/media/joseram0n/PEN32/Motores de Busqueda/Practica_1/corpus/LISA0.001");
        
        System.out.println(d);
        
        System.out.println("\n" + d.get(23).id.replaceAll("[^0-9]", ""));
        
        //Indexar 
        SolrConsulta sc = new SolrConsulta();

        try {
            sc.indexar("/media/joseram0n/PEN32/Motores de Busqueda/Practica_1/corpus/LISA0.001");
        } catch (SolrServerException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    // TEST - v0.2 (23/11/2020) La app lee el fichero de consultas LISA.QUE, toma las primeras 5 palabras y lanza consulta a Apache Solr. Recorremos la respuesta de Solr y la mostramos.
    public static void test2(){
        //Parsear preguntas
        SolrParser sp = new SolrParser();
        
        ArrayList<Pregunta> p = sp.leer_preguntas("/media/joseram0n/PEN32/Motores de Busqueda/Practica_1/corpus/LISA.QUE");
        
        System.out.println(p);
        
        //Buscar
        SolrConsulta sc = new SolrConsulta();
        
        try {
            sc.buscar("/media/joseram0n/PEN32/Motores de Busqueda/Practica_1/corpus/LISA.QUE");
        } catch (SolrServerException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    // TEST - v0.3 (1/12/2020) Esta versión de la app construye un fichero en formato 'trec_top_file', para poder ejecutar la evaluación con trec_eval.
    public static void test3(){
        //Buscar
        SolrConsulta sc = new SolrConsulta();
        
        try {
            ArrayList<SolrDocumentList> sdl = sc.buscar("/media/joseram0n/PEN32/Motores de Busqueda/Practica_1/corpus/LISA.QUE");
            SolrParser sp = new SolrParser();
            sp.crear_trec_file(sdl, "/media/joseram0n/PEN32/Motores de Busqueda/Practica_1/corpus/trec_top_file.txt");
        } catch (SolrServerException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public static void main(String[] args) throws IOException {
        
        //test1();
        //test2();
        test3();
        
        //SolrParser sp =  new SolrParser();
        
        //sp.leer_preguntas("/media/joseram0n/PEN32/Motores de Busqueda/Practica_1/corpus/LISA.QUE");
        //System.out.println("Preguntas leidas...\n");
        //sp.print_pregunta(34);
        //String t = sp.preg.get(0).pre;
        //System.out.println("\n" + t.split(" ").toString());
    }
}
