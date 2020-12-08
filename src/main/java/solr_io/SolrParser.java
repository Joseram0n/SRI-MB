package solr_io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Scanner;
import org.apache.solr.common.SolrDocumentList;

/**
 * Clase que parsea corpus y preguntas
 *
 * @author joseram0n
 */
public class SolrParser {

    public ArrayList<Documento> leer_doc(String doc_path) {

        ArrayList<Documento> docs = new ArrayList<>();

        try {
            File d = new File(doc_path);
            Scanner sc = new Scanner(d);

            while (sc.hasNextLine()) {
                Documento doc = new Documento();
                String aux = new String();

                doc.id = sc.nextLine();
                while (!(aux = sc.nextLine()).isBlank()) {
                    if (aux != null) {
                        doc.titulo += aux + " ";
                    }
                }

                while (!sc.hasNext("[***]+")) {
                    doc.texto += sc.nextLine() + " ";
                }

                //sc.skip("[***]+");
                sc.nextLine();
                
                docs.add(doc);
            }

        } catch (Exception e) {
            System.out.println("Error leyendo la movida");
            e.printStackTrace();
        }

        return docs;
    }

    public ArrayList<Pregunta> leer_preguntas(String doc_path) {

        ArrayList<Pregunta> preg = new ArrayList<>();

        try {
            File d = new File(doc_path);
            Scanner sc = new Scanner(d);

            while (sc.hasNextLine()) {
                Pregunta p = new Pregunta();

                p.id = sc.nextLine();

                while (!sc.hasNext("#")) {
                    p.texto += sc.next() + " ";
                }

                sc.nextLine();

                preg.add(p);
            }

        } catch (Exception e) {
            System.out.println("Error leyendo la movida");
            e.printStackTrace();
        }

        return preg;
    }

    public boolean crear_trec_file(ArrayList<SolrDocumentList> resp_docs, String rutaArchivo) throws IOException {
        try {
            File f = new File(rutaArchivo);
            PrintWriter pw = new PrintWriter(f);
            for (int i = 0; i < resp_docs.size(); i++) {
                SolrDocumentList sdl = resp_docs.get(i);
                for (int d = 0; d < sdl.size(); d++) {
                    System.out.println("Indx_ArrayList " + i + " Indx_SolrDocList " + d);
                    //Consulta Q0 documento ranking score EQUIPO
                    pw.println(i + " Q0 " + sdl.get(d).getFieldValue("id") + " " + d + " " + sdl.get(d).getFieldValue("score") + " Joseram0n");
                }

            }

            pw.close();

        } catch (Exception e) {
            System.out.println("Algo fuel mal al guardar el trec file \n" + e.getMessage());
        }

        return false;
    }

    /**
     * Funcion que procesa el archivo LISARJ.NUM para generar el fichero de
     * consultas relevantes de la herramienta trec_eval.
     *
     * @param rutaArchivo
     * @return TRUE si el archivo fue procesado de forma correcta, FALSE en caso
     * contrario.
     * @throws IOException
     */
    public boolean generar_consultas_relevantes(String rutaArchivo) throws IOException {

        try {

            /*Leer LISARJ.NUM*/
            ArrayList<String> docsIdRel; // ID's de los documentos relevantes
            LinkedHashMap<Integer,ArrayList<String>> qrel = new LinkedHashMap<>(); // Map con en numero de la query y las lista de id's

            File f = new File(rutaArchivo); // Abrimos archivo con File
            Scanner sc = new Scanner(f); // Creamos un Scanner con el File anterior

            while (sc.hasNext()) {
                
                docsIdRel = new ArrayList<>();
                
                int nq = sc.nextInt(); // Obtener numero de la query
                
                int nid = sc.nextInt(); // Obtener numero de doc relevantes
                
                for (int i = 0; i < nid; i++) { //Add los docs relecantes
                    docsIdRel.add(sc.next());
                }

                qrel.put(nq,docsIdRel);
            }
            
            sc.close();
            
            System.out.println("QREL: " + qrel);

            /*Escribir TREC_REL_FILE*/
            
            f = new File(f.getParent()+"/trec_rel_file.test"); // Crear nuevo fichero en el mismo directorio
            PrintWriter pw = new PrintWriter(f,"UTF-8"); // Print Writer para escribir en el archivo
            
            int qmax = Collections.max(qrel.keySet()); // Obtener la ultima consulta
            
            int docmax = 6004; // maximo de documentos
            
            for (int i = 1; i <= qmax; i++) { // recorremos desde 1 - ultima consulta
                
                ArrayList<String> aux = qrel.get(i); // Obtener la lista de id's de los docuementos
                if(aux == null) aux = new ArrayList<>();
                
                for (int j = 1; j <= docmax; j++) { // Para todos los documentos 1 - 6004
                    pw.println(i + " 0 " + j + (aux.contains(String.valueOf(j)) ? " 1" : " 0"));
                }
            }
            
            pw.close();
            
            return true;
            
            
        } catch (Exception e) {
            System.out.println("Algo salio mal generando el qrel");
            e.printStackTrace();
        }

        return false;
    }

}


//TODO: arreglar espacios de mas al final de los string
