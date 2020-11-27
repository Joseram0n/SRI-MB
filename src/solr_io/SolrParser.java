package solr_io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase que parsea corpus y preguntas
 *
 * @author joseram0n
 */
public class SolrParser {

    /**
     * Subclase documento, contiene la estructura de un documento.
     */
    public class Documento {

        String id;
        String titulo;
        String texto;

        public Documento() {
            id = new String();
            titulo = new String();
            texto = new String();
        }
    }

    /**
     * Subclase pregunta, contiene la estructura de una pregunta.
     */
    public class Pregunta {

        int id;
        String pre;

        public Pregunta() {
            id = 0;
            pre = new String();
        }
    }

    ArrayList<Documento> docs;

    ArrayList<Pregunta> preg;

    public SolrParser() {
        docs = new ArrayList<>();
        preg = new ArrayList<>();
    }

    public void leer_doc(String doc_path) {
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
    }

    public void print_doc(int n) {
        System.out.println("\nID: " + docs.get(n).id + "\nTitulo: "
                + docs.get(n).titulo + "\nTexto: "
                + docs.get(n).texto);
    }

    public ArrayList<Documento> get_docs() {
        return docs;
    }

    public void leer_preguntas(String doc_path) {
        try {
            File d = new File(doc_path);
            Scanner sc = new Scanner(d);

            while (sc.hasNextLine()) {
                Pregunta p = new Pregunta();

                p.id = Integer.parseInt(sc.nextLine());

                while (!sc.hasNext("#")) {
                    p.pre += sc.next() + " ";
                }

                sc.nextLine();

                preg.add(p);
            }

        } catch (Exception e) {
            System.out.println("Error leyendo la movida");
            e.printStackTrace();
        }
    }

    public void print_pregunta(int n) {
        System.out.println("\nID: " + preg.get(n).id + "\nPREGUNTA: "
                + preg.get(n).pre);
    }

    public ArrayList<Pregunta> get_preg() {
        return preg;
    }
}
