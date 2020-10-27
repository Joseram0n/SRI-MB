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

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

public class SolrAdd {

    public static void main(String[] args) throws IOException, SolrServerException {
        System.out.println("Hola");
        SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/gettingstarted_shard1_replica_n1").build();
        
        System.out.println("Hola2");
        //Create solr document
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("name", "Manuel de la Villa");
        doc.addField("age", 46);
        doc.addField("email", "manuel.villa@dti.uhu.es");
        client.add(doc);
        client.commit();
    }

}
