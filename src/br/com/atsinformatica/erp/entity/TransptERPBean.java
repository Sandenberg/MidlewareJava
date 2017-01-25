/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

import br.com.atsinformatica.midler.annotation.GenericController;

/**
 *
 * @author kennedimalheiros
 */
@GenericController(classPath = "br.com.atsinformatica.erp.controller.TransptERPController")
public class TransptERPBean {

    private String idERP;
    private String idEcom;
    private String nome;

    public String getIdERP() {
        return idERP;
    }

    public void setIdERP(String idERP) {
        this.idERP = idERP;
    }

    public String getIdEcom() {
        return idEcom;
    }

    public void setIdEcom(String idEcom) {
        this.idEcom = idEcom;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return idERP + " " + nome;
    }
    
    

}
