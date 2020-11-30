package solr_io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
                while (!(aux = sc.nextLine()).isEmpty()) {
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

}


//TODO: arreglar espascios de mas al final de los string
