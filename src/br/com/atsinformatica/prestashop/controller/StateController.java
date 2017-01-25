/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.erp.entity.EstadoERPBean;
import br.com.atsinformatica.prestashop.clientDAO.StatesPrestashopDAO;
import br.com.atsinformatica.prestashop.model.root.State;
import org.apache.log4j.Logger;

/**
 *
 * @author kennedimalheiros
 */
public class StateController {
    private static final Logger logger = Logger.getLogger(StateController.class);
    private StatesPrestashopDAO statesDAO;
    
    public EstadoERPBean buscaEstadoPrestashop(String codEndereco, String codEnderecoCobranca) {
        try {
            State state = this.getStatesDAO().getId(State.URLSTATE, Integer.valueOf(codEndereco));

            EstadoERPBean bean = new EstadoERPBean();
            bean.setId(state.getId());
            bean.setSigla(state.getIso_code());
            bean.setDescricao(state.getName());

            State stateCobranca = this.getStatesDAO().getId(State.URLSTATE, Integer.valueOf(codEnderecoCobranca));
            bean.setSiglaCobracao(state.getIso_code());
            bean.setDescricaoCobracao(state.getName());

            return bean; 
        } catch (Exception e) {
            logger.error("Erro ao buscar estado no Prestashop: ", e);
            throw new RuntimeException(e);
        }
    }

    public StatesPrestashopDAO getStatesDAO() {
        if (this.statesDAO == null) {
            this.statesDAO = new StatesPrestashopDAO();
        }
        return statesDAO;
    }
}
