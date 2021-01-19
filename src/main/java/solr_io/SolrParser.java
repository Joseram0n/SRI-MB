package solr_io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.solr.common.SolrDocumentList;
import org.xml.sax.SAXException;

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
                //if (!doc.id.startsWith("Document")) {
                if (!doc.id.contains("Document")) {
                    doc.id = sc.nextLine();
                }

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

                doc.setId(doc.getId().replaceAll("[^0-9]", ""));
                docs.add(doc);
            }

        } catch (Exception e) {
            System.out.println("Error leyendo la movida");
            e.printStackTrace();
        }

        return docs;
    }

    /**
     * Parsea las preguntas en un ArrayList de Pregunta
     * @param doc_path
     * @return 
     */
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

    /**
     * Crea el fichero de trec top file
     * @param resp_docs
     * @param rutaArchivo
     * @return
     * @throws IOException 
     */
    public boolean crear_trec_file(ArrayList<SolrDocumentList> resp_docs, String rutaArchivo) throws IOException {
        try {
            File f = new File(rutaArchivo);
            PrintWriter pw = new PrintWriter(f);
            for (int i = 0; i < resp_docs.size(); i++) {
                SolrDocumentList sdl = resp_docs.get(i);
                for (int d = 0; d < sdl.size(); d++) {
                    System.out.println("Indx_ArrayList " + i + " Indx_SolrDocList " + d);
                    //Consulta Q0 documento ranking score EQUIPO
                    pw.println(i + 1 + " Q0 " + sdl.get(d).getFieldValue("id") + " " + d + " " + sdl.get(d).getFieldValue("score") + " Joseram0n");
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
            LinkedHashMap<Integer, ArrayList<String>> qrel = new LinkedHashMap<>(); // Map con en numero de la query y las lista de id's

            File f = new File(rutaArchivo); // Abrimos archivo con File
            Scanner sc = new Scanner(f); // Creamos un Scanner con el File anterior

            while (sc.hasNext()) {

                docsIdRel = new ArrayList<>();

                int nq = sc.nextInt(); // Obtener numero de la query

                int nid = sc.nextInt(); // Obtener numero de doc relevantes

                for (int i = 0; i < nid; i++) { //Add los docs relecantes
                    docsIdRel.add(sc.next());
                }

                qrel.put(nq, docsIdRel);
            }

            sc.close();

            System.out.println("QREL: " + qrel);

            /*Escribir TREC_REL_FILE*/
            f = new File(f.getParent() + "/trec_rel_file.test"); // Crear nuevo fichero en el mismo directorio
            PrintWriter pw = new PrintWriter(f, "UTF-8"); // Print Writer para escribir en el archivo

            int qmax = Collections.max(qrel.keySet()); // Obtener la ultima consulta

            int docmax = 6004; // maximo de documentos

            for (int i = 1; i <= qmax; i++) { // recorremos desde 1 - ultima consulta

                ArrayList<String> aux = qrel.get(i); // Obtener la lista de id's de los docuementos
                if (aux == null) {
                    aux = new ArrayList<>();
                }

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

    /**
     * Procesa los Documentos normales a documentos de GATE
     * @param docs
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public ArrayList<DocumentoGATE> procesar_doc_gate(ArrayList<Documento> docs) throws ParserConfigurationException, SAXException, IOException {

        Pattern org = Pattern.compile("<Organization>(.*?)<\\/Organization>");
        Pattern loc = Pattern.compile("<Location>(.*?)<\\/Location>");
        ArrayList<DocumentoGATE> dgate = new ArrayList<>();

        try {

            for (Documento doc : docs) {
                String organizacion = new String();
                String localizacion = new String();
                String titulo = doc.getTitulo();
                String texto = doc.getTexto();

                //titulo
                Matcher titleMatchOrg = org.matcher(titulo);
                Matcher titleMatchLoc = loc.matcher(titulo);

                while (titleMatchOrg.find()) {
                    organizacion += titleMatchOrg.group(1) + " ";
                    titulo = titulo.replace(titleMatchOrg.group(0), titleMatchOrg.group(1));
                }

                while (titleMatchLoc.find()) {
                    localizacion += titleMatchLoc.group(1) + " ";
                    titulo = titulo.replace(titleMatchLoc.group(0), titleMatchLoc.group(1));
                }

                //texto
                Matcher textoMatchOrg = org.matcher(texto);
                Matcher textoMatchLoc = loc.matcher(texto);

                while (textoMatchOrg.find()) {
                    organizacion += textoMatchOrg.group(1) + " ";
                    texto = texto.replace(textoMatchOrg.group(0), textoMatchOrg.group(1));
                }

                while (textoMatchLoc.find()) {
                    localizacion += textoMatchLoc.group(1) + " ";
                    texto = texto.replace(textoMatchLoc.group(0), textoMatchLoc.group(1));
                }

                dgate.add(new DocumentoGATE(doc.getId().replaceAll("[<Organization><\\/Organization>]", ""), titulo, texto, organizacion, localizacion));
            }

        } catch (Exception e) {
            System.out.println("Error leyendo la movida");
            e.printStackTrace();
        }

        //System.out.println(dgate.get(1));
        return dgate;
    }

    /**
     * Procesa los preguntas normales a documentos de GATE
     * @param prg
     * @return 
     */
    public ArrayList<PreguntaGATE> procesar_preguntas_gate(ArrayList<Pregunta> prg) {

        Pattern org = Pattern.compile("<Organization>(.*?)<\\/Organization>");
        Pattern loc = Pattern.compile("<Location>(.*?)<\\/Location>");
        ArrayList<PreguntaGATE> pgate = new ArrayList<>();

        try {

            for (Pregunta p : prg) {
                String organizacion = new String();
                String localizacion = new String();
                String id = p.getId();
                String texto = p.getTexto();

                //texto
                Matcher textoMatchOrg = org.matcher(texto);
                Matcher textoMatchLoc = loc.matcher(texto);

                while (textoMatchOrg.find()) {
                    organizacion += textoMatchOrg.group(1) + " ";
                    texto = texto.replace(textoMatchOrg.group(0), textoMatchOrg.group(1));
                }

                while (textoMatchLoc.find()) {
                    localizacion += textoMatchLoc.group(1) + " ";
                    texto = texto.replace(textoMatchLoc.group(0), textoMatchLoc.group(1));
                }

                pgate.add(new PreguntaGATE(id, texto, organizacion, localizacion));
            }

        } catch (Exception e) {
            System.out.println("Error leyendo la movida");
            e.printStackTrace();
        }

        System.out.println(pgate.get(1));
        return pgate;
    }

}