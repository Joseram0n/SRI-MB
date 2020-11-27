/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solr_io;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.solr.client.solrj.SolrQuery;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

/**
 * test
 * @author joseram0n
 */
public class SolrSearch {

    public static void main(String[] args) throws IOException,
            SolrServerException {

        HttpSolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/corpus").build();

        SolrParser sp = new SolrParser();
        
        sp.leer_preguntas("/media/joseram0n/PEN32/Motores de Busqueda/Practica_1/corpus/LISA.QUE");

        ArrayList<SolrParser.Pregunta> prg = sp.get_preg();

        for (SolrParser.Pregunta p : prg) {
            SolrQuery query = new SolrQuery();
            String[] aux = p.pre.split(" ");
            String preg5 = new String();
            for (int i = 0; i < 5; i++) {
                preg5 = preg5 + " " + aux[i];
            }
            query.setQuery("text:" + preg5);
            System.out.println(preg5);
            query.setFields("id", "title", "text");
            QueryResponse rsp = solr.query(query);
            SolrDocumentList docs = rsp.getResults();
            for (int i = 0; i < docs.size(); ++i) {
                System.out.println(docs.get(i));
            }
        }
        /*
        query.setQuery("*:villa");
        //query.setQuery("Apple");
        //query.addFilterQuery("cat:electronics");
        //query.setFields("id","price","merchant","cat","store");
        QueryResponse rsp = solr.query(query);
        SolrDocumentList docs = rsp.getResults();
        for (int i = 0; i < docs.size(); ++i) {
            System.out.println(docs.get(i));
        }
         */
    }
}
