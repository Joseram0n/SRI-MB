/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solr_io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;

/**
 *
 * @author joseram0n
 */
public class SolrConsulta {

    SolrClient client;

    SolrParser sp;

    SolrClient generalClient;

    public SolrConsulta() {

        client = null;

        sp = new SolrParser();

        generalClient = new HttpSolrClient.Builder("http://localhost:8983/solr").build();

    }

    public boolean indexar(String rutaFichero, String nombreCore) throws SolrServerException, IOException {

        try {
            client = new HttpSolrClient.Builder("http://localhost:8983/solr/" + nombreCore).build();
            //Obtener los documentos del parser
            ArrayList<Documento> rawDocs = sp.leer_doc(rutaFichero);
            //Indexar todos los documentos
            if (nombreCore.contains("mejorado")) {

                System.out.println(rawDocs.get(0));
                ArrayList<DocumentoGATE> dGate = sp.procesar_doc_gate(rawDocs);

                for (DocumentoGATE d : dGate) {
                    SolrInputDocument doc = new SolrInputDocument();
                    doc.addField("id", d.getId());
                    doc.addField("title", d.getTitulo());
                    doc.addField("text", d.getTexto());
                    doc.addField("organization", d.getOrganization());
                    doc.addField("location", d.getLocation());
                    client.add(doc);
                }

            } else {
                for (Documento d : rawDocs) {
                    SolrInputDocument doc = new SolrInputDocument();
                    doc.addField("id", d.id.replaceAll("[^0-9]", ""));
                    doc.addField("title", d.titulo);
                    doc.addField("text", d.texto);
                    client.add(doc);
                }
            }
            //Commit a solr
            client.commit();

            return true;

        } catch (Exception e) {
            System.out.println("Error al indexar. \n" + e.getMessage());
        }

        return false;
    }

    public ArrayList<SolrDocumentList> buscar(String rutaFichero, String nombreCore, int nPalabras, boolean quitarEspeciales) throws SolrServerException, IOException {

        client = new HttpSolrClient.Builder("http://localhost:8983/solr/" + nombreCore).build();

        ArrayList<Pregunta> prg = sp.leer_preguntas(rutaFichero);

        ArrayList<SolrDocumentList> resp_docs = new ArrayList<>();
        
        SolrQuery query = new SolrQuery();

        if (nombreCore.contains("mejorado")) {
            ArrayList<PreguntaGATE> pg = sp.procesar_preguntas_gate(prg);
            for (PreguntaGATE p : pg) {
                StringTokenizer aux = new StringTokenizer(p.getTexto(), "\\s+");
                if (nPalabras != -1) {
                    if (nPalabras > aux.countTokens()) {
                        nPalabras = aux.countTokens();
                    }
                } else {
                    nPalabras = aux.countTokens();
                }

                String preg = new String();
                String org = p.getOrganization();
                String loc = p.getLocation();
                
                for (int i = 0; i < nPalabras; i++) {
                    preg += " " + aux.nextToken();
                }
                
                if (quitarEspeciales) {
                    preg = ClientUtils.escapeQueryChars(preg);
                    org = ClientUtils.escapeQueryChars(org);
                    loc = ClientUtils.escapeQueryChars(loc);
                }

                //System.out.println("pregunta P: " + p);
                //System.out.println("pregunta R2: " + preg);
                
                //query.setQuery("text:" + preg + " OR title:" + preg);
                
               query.setQuery("text:(" + preg + ") OR title:(" + preg + ")"
                        //+ (org.isEmpty() ? "" : " OR organization:(" + org + ")")
                        + (loc.isEmpty() ? "" : " OR location:(" + loc + ")"));
                
                query.setFields("fl", "*,score");

                QueryResponse rsp = client.query(query);
                SolrDocumentList docs = rsp.getResults();
                resp_docs.add(docs);
            }
        } else {
            for (Pregunta p : prg) {
                StringTokenizer aux = new StringTokenizer(p.getTexto(), "\\s+");

                if (nPalabras != -1) {
                    if (nPalabras > aux.countTokens()) {
                        nPalabras = aux.countTokens();
                    }
                } else {
                    nPalabras = aux.countTokens();
                }

                String preg = new String();
                System.out.println("Npalabras: " + nPalabras);
                for (int i = 0; i < nPalabras; i++) {
                    preg += " " + aux.nextToken();
                }

                /*
            Como saltar los caracteres especiales de la query
            https://lucene.apache.org/solr/guide/8_7/the-standard-query-parser.html#escaping-special-characters
                 */
                if (quitarEspeciales) {
                    preg = ClientUtils.escapeQueryChars(preg);
                }

                System.out.println("pregunta P: " + p);
                System.out.println("pregunta R2: " + preg);

                query.setQuery("text:(" + preg + ") OR title:(" + preg + ")");
                query.setFields("fl", "*,score");
                QueryResponse rsp = client.query(query);
                SolrDocumentList docs = rsp.getResults();
                resp_docs.add(docs);
            }
        }

        return resp_docs;
    }

    /*
    private String filtro_caracteres(String randomStr) {
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < randomStr.length(); index++) {
            sb.append(randomStr.charAt(index) == '\'' ? "''" : randomStr.charAt(index));
        }
        return sb.toString();
    }
     */
    public List<String> obtener_nombre_cores() throws SolrServerException, IOException {

        // Request core list
        CoreAdminRequest request = new CoreAdminRequest();
        request.setAction(CoreAdminAction.STATUS);
        CoreAdminResponse cores = request.process(generalClient);

        // List of the cores
        List<String> coreList = new ArrayList<String>();
        for (int i = 0; i < cores.getCoreStatus().size(); i++) {
            coreList.add(cores.getCoreStatus().getName(i));
        }

        return coreList;
    }

    public boolean limpiar(String nombreCore) {
        boolean result = false;
        client = new HttpSolrClient.Builder("http://localhost:8983/solr/" + nombreCore).build();

        try {
            client.deleteByQuery("*:*");
            client.commit();
            result = true;
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException("Failed to delete data in Solr. " + e.getMessage(), e);
        }

        return result;
    }

}
