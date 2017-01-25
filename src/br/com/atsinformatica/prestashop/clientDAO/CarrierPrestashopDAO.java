/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.clientDAO;

import br.com.atsinformatica.prestashop.model.list.prestashop.AccessXMLAttribute;
import br.com.atsinformatica.prestashop.model.list.prestashop.PrestashopItens;
import br.com.atsinformatica.prestashop.model.root.Carrier;
import br.com.atsinformatica.prestashop.model.root.Prestashop;
import com.sun.jersey.api.client.ClientResponse;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author kennedimalheiros
 */
public class CarrierPrestashopDAO extends GenericPrestashopDAO<Carrier> implements IGenericPrestashopDAO<Carrier> {

    @Override
    public void post(String path, Carrier t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Carrier postRetorno(String path, Carrier t) {
        Prestashop p = new Prestashop();
        p.setCarrier(t);
        String xml = createTOXML(p);
        Prestashop response = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(Prestashop.class, p);
        return response.getCarrier();
    }

    @Override
    public int put(String path, Carrier t) {
        Prestashop prestashopCarrier = new Prestashop();
        prestashopCarrier.setCarrier(t);
        String xml = createTOXML(prestashopCarrier);
        ClientResponse response = getWebResource().path(path).type(MediaType.APPLICATION_XML).put(ClientResponse.class, xml);
        return response.getStatus();
    }

    @Override
    public List<Carrier> get(String path) {
        PrestashopItens getListItens = getWebResource().path(path).type(MediaType.APPLICATION_XML).get(PrestashopItens.class);
        List<Carrier> listCarrier = new ArrayList<>();
        for (AccessXMLAttribute accessXMLAttribute : getListItens.getCarriers().getCarrier()) {
            Prestashop prestashop = getWebResource().path(path).path(accessXMLAttribute.getId()).type(MediaType.APPLICATION_XML).get(Prestashop.class);
            listCarrier.add(prestashop.getCarrier());
        }
        return listCarrier;
    }

    @Override
    public Carrier getId(String path, int key) {
        Prestashop prestashop = getWebResource().path(path).path(String.valueOf(key)).type(MediaType.APPLICATION_XML).get(Prestashop.class);
        return prestashop.getCarrier();
    }

    @Override
    public int delete(String path, String id) {
        ClientResponse response = getWebResource().path(path).path(id).type(MediaType.APPLICATION_XML).delete(ClientResponse.class);
        return response.getStatus();
    }

}
