/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solr_io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author joseram0n
 */
public class SolrConsulta {

    SolrClient client;

    SolrParser sp;

    public SolrConsulta() {

        client = new HttpSolrClient.Builder("http://localhost:8983/solr/corpus").build();

        sp = new SolrParser();

    }

    public boolean indexar(String rutaFichero) throws SolrServerException, IOException {

        try {
            //Obtener los documentos del parser
            ArrayList<Documento> rawDocs = sp.leer_doc(rutaFichero);
            //Indexar todos los documentos 
            for (Documento d : rawDocs) {
                SolrInputDocument doc = new SolrInputDocument();
                doc.addField("id", d.id.replaceAll("[^0-9]", ""));
                doc.addField("title", d.titulo);
                doc.addField("text", d.texto);
                client.add(doc);
            }
            //Commit a solr
            client.commit();

            return true;

        } catch (Exception e) {
            System.out.println("Error al indexar. \n" + e.getMessage());
        }
        
        return false;
    }

    public ArrayList<SolrDocumentList> buscar(String rutaFichero) throws SolrServerException, IOException {
        //TEST
        //sp.leer_preguntas("/media/joseram0n/PEN32/Motores de Busqueda/Practica_1/corpus/LISA.QUE");

        ArrayList<Pregunta> prg = sp.leer_preguntas(rutaFichero);
        
        ArrayList<SolrDocumentList> resp_docs = new ArrayList<>();
                
        for (Pregunta p : prg) {
            SolrQuery query = new SolrQuery();
            String[] aux = p.texto.split(" ");
            String preg5 = new String();
            for (int i = 0; i < 5; i++) {
                preg5 = preg5 + " " + aux[i];
            }
            query.setQuery("text:" + preg5);
            query.setFields("fl", "*,score");
            //System.out.println(preg5);
            //query.setFields("id", "title", "text");
            QueryResponse rsp = client.query(query);
            SolrDocumentList docs = rsp.getResults();
            resp_docs.add(docs);
            /*
            for (int i = 0; i < docs.size(); ++i) {
                System.out.println(docs.get(i));
            }*/
        }
        return resp_docs;
    }
}
