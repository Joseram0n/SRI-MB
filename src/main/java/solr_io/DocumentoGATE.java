/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solr_io;

import java.util.Objects;

/**
 * Simple clase que contiene la estructura de un documento con GATE
 * @author Joseram0n
 */
public class DocumentoGATE extends Documento{
    
    String Organization;
    String Location;

    public DocumentoGATE(String id, String titulo, String texto, String Organization, String Location) {
        super(id, titulo, texto);
        this.Organization = Organization;
        this.Location = Location;
    }

    public String getOrganization() {
        return Organization;
    }

    public void setOrganization(String Organization) {
        this.Organization = Organization;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitulo() {
        return titulo;
    }

    @Override
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String getTexto() {
        return texto;
    }

    @Override
    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.Organization);
        hash = 47 * hash + Objects.hashCode(this.Location);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DocumentoGATE other = (DocumentoGATE) obj;
        if (!Objects.equals(this.Organization, other.Organization)) {
            return false;
        }
        if (!Objects.equals(this.Location, other.Location)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DocumentoGATE{" + "id=" + super.getId() + " ,Organization=" + Organization + ", Location=" + Location + " ,Titulo=" + super.getTitulo() + " ,Texto=" + super.getTexto() + '}';
    }
}
