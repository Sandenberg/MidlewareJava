/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.erp.dao.TransptERPDAO;
import br.com.atsinformatica.erp.entity.TransptERPBean;
import br.com.atsinformatica.prestashop.controller.CarrierController;
import org.apache.log4j.Logger;

/**
 *
 * @author kennedimalheiros
 */
public class TransptERPController extends SincERPController<TransptERPBean> {

    CarrierController carrierController = new CarrierController();

    @Override
    public void post(TransptERPBean obj) {
        try {
            //faz o post das Transportadoras
            carrierController = new CarrierController();
            int codEcom = carrierController.insertCarrier(obj);
            if (codEcom != 0) {
                obj.setIdEcom(String.valueOf(codEcom));
                TransptERPDAO dao = new TransptERPDAO();
                //salvando código Ecom da Transportadora cadastrada
                dao.alterar(obj);
            }
            Logger.getLogger(CategoriaERPController.class).info("Transportadora sincronizada na loja virtual com sucesso.");
        } catch (Exception e) {
            Logger.getLogger(CategoriaERPController.class).error("Erro ao sincronizar transportadora na loja virtual: " + e);
        }
    }

    @Override
    public int update(TransptERPBean obj) {
        return carrierController.update(obj);
    }

    @Override
    public int delete(String id) {
        return carrierController.deleteCarrier(id);

    }

}
