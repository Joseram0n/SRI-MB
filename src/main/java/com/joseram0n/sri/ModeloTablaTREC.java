/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joseram0n.sri;

import java.util.Objects;

/**
 *
 * @author Joseram0n
 */
public class ModeloTablaTREC {
    
    String medida;
    
    String valor;

    public ModeloTablaTREC(String medida, String valor) {
        this.medida = medida;
        this.valor = valor;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.medida);
        hash = 79 * hash + Objects.hashCode(this.valor);
        return hash;
    }

    @Override
    public String toString() {
        return "ModeloTablaTREC{" + "medida=" + medida + ", valor=" + valor + '}';
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
        final ModeloTablaTREC other = (ModeloTablaTREC) obj;
        if (!Objects.equals(this.medida, other.medida)) {
            return false;
        }
        if (!Objects.equals(this.valor, other.valor)) {
            return false;
        }
        return true;
    }
    
}
