/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solr_io;

/**
 *
 * @author joseram0n
 */
import java.io.IOException;
import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

public class SolrAdd {

    public static void main(String[] args) throws IOException, SolrServerException {
        System.out.println("Hola");
        
        SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/corpus").build();
        
        SolrParser sp = new SolrParser();
        
        sp.leer_doc("/media/joseram0n/PEN32/Motores de Busqueda/Practica_1/corpus/LISA0.001");
        
        sp.print_doc(0);
        
        
        System.out.println("Hola2");
        //Create solr document
        ArrayList<SolrParser.Documento> rawDocs = sp.get_docs();
        
        for(SolrParser.Documento d : rawDocs){
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", d.id.replaceAll("[^0-9]", ""));
            doc.addField("title", d.titulo);
            doc.addField("texto", d.texto);
            client.add(doc);
        }

        client.commit();
        
        
        
                
    }

}
