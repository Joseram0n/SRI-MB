/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joseram0n.sri;

/**
 *
 * @author joseram0n
 */
public class ModeloTablaSRI {
    
    int n_File;
    String nombreArchivo;
    int n_docs;
    String estado_index;

    public ModeloTablaSRI(int n_File, String nombreArchivo, int n_docs, String estado_index) {
        this.n_File = n_File;
        this.nombreArchivo = nombreArchivo;
        this.n_docs = n_docs;
        this.estado_index = estado_index;
    }

    public int getN_File() {
        return n_File;
    }

    public void setN_File(int n_file) {
        this.n_File = n_file;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public int getN_docs() {
        return n_docs;
    }

    public void setN_docs(int n_docs) {
        this.n_docs = n_docs;
    }

    public String getEstado_index() {
        return estado_index;
    }

    public void setEstado_index(String estado_index) {
        this.estado_index = estado_index;
    }
    
}
