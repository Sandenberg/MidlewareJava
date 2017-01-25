/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.erp.dao.CadFabrERPDAO;
import br.com.atsinformatica.erp.entity.CadFabricERPBean;
import br.com.atsinformatica.prestashop.controller.ManufacturerController;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
public class FabricERPController extends SincERPController<CadFabricERPBean> {
    private ManufacturerController manufController = new ManufacturerController();;
    
    
    @Override
    public void post(CadFabricERPBean obj) {
        try {
            //faz o post das categorias pendentes de sincronização
            manufController = new ManufacturerController();
            int fabricEcom = manufController.createManufacturerPrestashop(obj);
            if (fabricEcom != 0) {
                obj.setIdFabricanteEcom(fabricEcom);
                CadFabrERPDAO dao = new CadFabrERPDAO();
                //salvando código do fabricante cadastrada 
                dao.alterar(obj);
            }
            Logger.getLogger(CategoriaERPController.class).info("Fabricante sincronizado na loja virtual com sucesso.");
        } catch (Exception e) {
            Logger.getLogger(CategoriaERPController.class).error("Erro ao sincronizar fabricante na loja virtual: "+e);
        }
    }

    @Override
    public int update(CadFabricERPBean obj) {            
        return manufController.updateManufacturerPrestashop(obj);                          
    }   
    public int delete(String id) {
        return manufController.deleteManufacturerPrestashop(id);        
    }
    
}
