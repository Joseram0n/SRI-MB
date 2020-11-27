/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solr_io;

import java.io.IOException;

/**
 *
 * @author joseram0n
 */
public class test {

    public static void main(String[] args) throws IOException {
        SolrParser sp =  new SolrParser();
        
        sp.leer_preguntas("/media/joseram0n/PEN32/Motores de Busqueda/Practica_1/corpus/LISA.QUE");
        System.out.println("Preguntas leidas...\n");
        sp.print_pregunta(2);
        String t = sp.preg.get(0).pre;
        System.out.println("\n" + t.split(" ").toString());
    }
}
