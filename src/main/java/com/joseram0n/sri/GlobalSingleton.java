/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joseram0n.sri;

/**
 *
 * @author Joseram0n
 */
public class GlobalSingleton {
    
    private Boolean isUnix;

    private String dir_solr;

    private Boolean encendido;
    
    private static GlobalSingleton gbs;

    private GlobalSingleton() {
        this.isUnix = false;
        this.dir_solr = new String();
        this.encendido = false;
    }
    
    public static GlobalSingleton getInstance() {
        if(gbs == null){
            gbs = new GlobalSingleton();
        }
        return gbs;
    }

    public Boolean getIsUnix() {
        return isUnix;
    }

    public void setIsUnix(Boolean isUnix) {
        this.isUnix = isUnix;
    }

    public String getDir_solr() {
        return dir_solr;
    }

    public void setDir_solr(String dir_solr) {
        this.dir_solr = dir_solr;
    }

    public Boolean getEncendido() {
        return encendido;
    }

    public void setEncendido(Boolean encendido) {
        this.encendido = encendido;
    }  
}
