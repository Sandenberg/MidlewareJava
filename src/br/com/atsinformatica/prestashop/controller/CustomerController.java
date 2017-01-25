/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.erp.entity.ClienteERPBean;
import br.com.atsinformatica.erp.entity.TipoPessoa;
import br.com.atsinformatica.prestashop.clientDAO.CustomerPrestashopDAO;
import br.com.atsinformatica.prestashop.model.root.Customer;
import br.com.atsinformatica.utils.Funcoes;
import br.com.atsinformatica.utils.StringUtil;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

/**
 *
 * @author kennedimalheiros
 */
public class CustomerController {

    private static Logger logger = Logger.getLogger(CustomerController.class);

    private CustomerPrestashopDAO customerDAO;

    public ClienteERPBean buscaClientePrestashop(String codido) {
        try {
            Customer customer = this.getCustomerDAO().getId(Customer.URLCUSTOMER, Integer.valueOf(codido));

            ClienteERPBean bean = new ClienteERPBean();
            bean.setId(customer.getId());
            bean.setEmail(customer.getEmail());
            bean.setAniversario(new SimpleDateFormat("dd/MM/yyyy").parse(customer.getBirthday()));
            
            switch (customer.getTipo_pessoa().toUpperCase()) {
                case "FISICA":
                    bean.setTipoPessoa(TipoPessoa.F);
                    bean.setCpfCnpj(customer.getCpf());
                    bean.setRgIe(customer.getRg());
                    
                    String nome = new String();
                    if (customer.getFirstname() != null)
                        nome += customer.getFirstname();
                    if (nome.length() > 0)
                        nome += " ";
                    if (customer.getLastname() != null)
                        nome += customer.getLastname();
                    
                    bean.setNome(nome);
                    break;
                    
                case "JURIDICA":
                    bean.setTipoPessoa(TipoPessoa.J);
                    bean.setCpfCnpj(customer.getSiret());
                    bean.setRgIe(customer.getApe());
                    bean.setNome(customer.getCompany());
                    bean.setNomeFantasia(customer.getNome_fantasia());
                    break;
                    
                default:
                    throw new UnsupportedOperationException("Tipo de pessoa desconhecido.");
            }
            
            if (bean.getNome() != null) {
                bean.setNome(Funcoes.ReplaceAcento(bean.getNome()).toUpperCase());
            }
                        
            return bean;
        } catch (Exception e) {
            logger.error("Erro ao buscar cliente no Prestashop: ", e);
            throw new RuntimeException(e);
        }
    }

    /* ************ */
    public CustomerPrestashopDAO getCustomerDAO() {
        if (this.customerDAO == null) {
            this.customerDAO = new CustomerPrestashopDAO();
        }
        return customerDAO;
    }
}
