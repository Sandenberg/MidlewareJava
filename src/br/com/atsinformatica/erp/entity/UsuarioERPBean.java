/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.entity;

/**
 * Tela de login
 * @author kennedimalheiros
 */
public class UsuarioERPBean {
    private static UsuarioERPBean INSTANCE;
    private static String usuario;
    private String senha;
    private boolean mestre = false;
    
    public static UsuarioERPBean getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UsuarioERPBean();
        }
        return INSTANCE;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        UsuarioERPBean.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isMestre() {
        return mestre;
    }

    public void setMestre(boolean mestre) {
        this.mestre = mestre;
    }
    

}
