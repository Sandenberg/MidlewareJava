/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.clientDAO;

import br.com.atsinformatica.prestashop.model.root.CPFModuleData;
import br.com.atsinformatica.prestashop.model.root.Prestashop;
import java.util.List;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author kennedimalheiros
 */
public class CPFModuleDataPrestashopDAO extends GenericPrestashopDAO<CPFModuleData> implements IGenericPrestashopDAO<CPFModuleData> {

    @Override
    public void post(String path, CPFModuleData t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int put(String path, CPFModuleData t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CPFModuleData> get(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CPFModuleData getId(String path, int key) {

        Prestashop prestashop = getWebResource().path(path).path(String.valueOf(key)).type(MediaType.APPLICATION_XML).get(Prestashop.class);
        return prestashop.getCpfmodule_data();

    }

    @Override
    public int delete(String path, String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
