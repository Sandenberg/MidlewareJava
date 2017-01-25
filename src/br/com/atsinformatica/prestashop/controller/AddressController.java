/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.erp.entity.EnderecoERPBean;
import br.com.atsinformatica.prestashop.clientDAO.AddressPrestashopDAO;
import br.com.atsinformatica.prestashop.model.root.Address;
import org.apache.log4j.Logger;

/**
 *
 * @author kennedimalheiros
 */
public class AddressController {
    private static final Logger logger = Logger.getLogger(AddressController.class);
    
    private AddressPrestashopDAO addressDAO;

    public EnderecoERPBean buscaEnderecoPrestashop(int codEntrega, int codCobranca) {
        /* Buscando Endereço de Entrega */
        EnderecoERPBean bean = new EnderecoERPBean();
        Address address = null;
        try {
            address = this.getAddressDAO().getId(Address.URLADDRESS, codEntrega);
            
            bean.setId_customer(address.getId_customer());
            bean.setAlias(address.getAlias());
            bean.setLastname(address.getLastname());
            bean.setFirstname(address.getFirstname());
            bean.setAddress1(address.getAddress1());
            bean.setNumero(address.getNumero());
            bean.setComplemento(address.getComplemento());
            bean.setAddress2(address.getAddress2());
            bean.setPostcode(address.getPostcode());
            bean.setCity(address.getCity());
            bean.setCodCity(address.getCity());
            bean.setOther(address.getOther());
            bean.setPhone(address.getPhone());
            bean.setPhone_mobile(address.getPhone_mobile());
            bean.setId_state(address.getId_state());
            
            bean.setEnderecoEnt(address.getAddress1());
            bean.setNumeroEnt(address.getNumero());
            bean.setBairroEnt(address.getAddress2());
            bean.setCepEnt(address.getPostcode());
            bean.setCidadeEnt(address.getCity());
            bean.setCodCidadeEnt(address.getCity());            
            bean.setEstadoEnt(address.getId_state());
            bean.setDataHoraPrevEnt(address.getDeliveryDate());           
        } catch (Exception e) {
            logger.error("Falha ao buscar endereço de entrega", e);
            throw new RuntimeException(e);
        }
        
        try {
            /* Buscando Endereço de cobrança */
            Address addressCobraca = address;
            if (codCobranca != 0){
                addressCobraca = this.getAddressDAO().getId(Address.URLADDRESS, codCobranca);
            }
            
            bean.setEnderecoCob(addressCobraca.getAddress1());
            bean.setNumeroCob(addressCobraca.getNumero());
            bean.setBairroCob(addressCobraca.getAddress2());
            bean.setCepCob(addressCobraca.getPostcode());
            bean.setCidadeCob(addressCobraca.getCity());
            bean.setCodCidadeCob(addressCobraca.getCity());            
            bean.setEstadoCob(addressCobraca.getId_state());
        } catch (Exception e) {
            logger.error("Falha ao buscar endereço de cobrança", e);
            throw new RuntimeException(e);
        }
        
        return bean;
    }

    public AddressPrestashopDAO getAddressDAO() {
        if (this.addressDAO == null) {
            this.addressDAO = new AddressPrestashopDAO();
        }
        return addressDAO;
    }
}
