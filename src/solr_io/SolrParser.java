package solr_io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner; 

/**
 * Clase que parsea corpus
 * @author joseram0n
 */
public class SolrParser {
    
    public class Documento{
        String id;
        String titulo;
        String texto;
        
        public Documento(){
            id = new String();
            titulo = new String();
            texto = new String();
        }
    }
    
    ArrayList<Documento> docs;

    public SolrParser(){
        docs = new ArrayList<>();
    }
    
    public void leer_doc(String doc_path){
        try {
            File d = new File(doc_path);
            Scanner sc = new Scanner(d);
               
            while(sc.hasNextLine()){
                Documento doc = new Documento();
                String aux = new String();
                
                doc.id = sc.nextLine();
                while(!(aux = sc.nextLine()).isEmpty()){
                    if(aux != null)
                        doc.titulo += aux + " ";
                }
               
                while(!sc.hasNext("[***]+")){
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
    
    public void print_doc(int n){
        System.out.println("\nID: " + docs.get(n).id + "\nTitulo: " 
                + docs.get(n).titulo + "\nTexto: "
                + docs.get(n).texto);
    }
    
    public ArrayList<Documento> get_docs(){
        return docs;
    }
    
}
