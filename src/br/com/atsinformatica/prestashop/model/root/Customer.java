/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.root;

import br.com.atsinformatica.prestashop.clientDAO.CustomerPrestashopDAO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author kennedimalheiros
 */
@XmlRootElement(name = "customer")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customer", propOrder = {
    "id",
    "lastname",
    "firstname",
    "email",
    "birthday",
    "tipo_pessoa",
    "cpf",
    "rg",
    "company",
    "nome_fantasia",
    "siret",
    "ape"
})

public class Customer {

    public static String URLCUSTOMER = "customers/";

    @XmlElement(name = "id", required = true)
    private String id;
    @XmlElement(name = "lastname")
    private String lastname;
    @XmlElement(name = "firstname")
    private String firstname;
    @XmlElement(name = "email")
    private String email;
    @XmlElement(name = "birthday")
    private String birthday;
    @XmlElement(name = "tipo_pessoa")
    private String tipo_pessoa;
    @XmlElement(name = "cpf")
    private String cpf;
    @XmlElement(name = "rg")
    private String rg;

    @XmlElement(name = "company")
    private String company;
    @XmlElement(name = "nome_fantasia")
    private String nome_fantasia;
    @XmlElement(name = "siret")
    private String siret;
    @XmlElement(name = "ape")
    private String ape;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {

        Date dtTemp;
        String dtformatada = null;
        try {

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");//formato do mySQL
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");//formato para mostrar
            dtTemp = sdf1.parse(birthday);
            dtformatada = sdf2.format(dtTemp);
        } catch (ParseException ex) {
            Logger.getLogger(CustomerPrestashopDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dtformatada;
    }

    public void setBirthday(String birthday) {

        this.birthday = birthday;

    }

    public String getTipo_pessoa() {
        return tipo_pessoa;
    }

    public void setTipo_pessoa(String tipo_pessoa) {
        this.tipo_pessoa = tipo_pessoa;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg_ie) {
        this.rg = rg;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNome_fantasia() {
        return nome_fantasia;
    }

    public void setNome_fantasia(String nome_fantasia) {
        this.nome_fantasia = nome_fantasia;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getApe() {
        return ape;
    }

    public void setApe(String ape) {
        this.ape = ape;
    }

}
