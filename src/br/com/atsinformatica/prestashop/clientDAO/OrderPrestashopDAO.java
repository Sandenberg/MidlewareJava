/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.clientDAO;

import br.com.atsinformatica.prestashop.model.list.prestashop.AccessXMLAttributeOrder;
import br.com.atsinformatica.prestashop.model.list.prestashop.PrestashopItens;
import br.com.atsinformatica.prestashop.model.root.Order;
import br.com.atsinformatica.prestashop.model.root.Prestashop;
import com.sun.jersey.api.client.ClientResponse;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author kennedimalheiros
 */
public class OrderPrestashopDAO extends GenericPrestashopDAO<Order> implements IGenericPrestashopDAO<Order> {

    
    @Override
    public void post(String path, Order t) {
        Prestashop prestaShopOrder = new Prestashop();
        prestaShopOrder.setOrder(t);
        String xml = createTOXML(prestaShopOrder);
        ClientResponse response = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(ClientResponse.class, xml);
    }
    
    public Order postOrder(String path, Order t) {
        Prestashop prestaShopOrder = new Prestashop();
        prestaShopOrder.setOrder(t);
        String xml = createTOXML(prestaShopOrder);
        Prestashop p = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(Prestashop.class, xml);
        return p.getOrder();
    }

    @Override
    public int put(String path, Order t) {
        Prestashop prestaShopOrder = new Prestashop();
        prestaShopOrder.setOrder(t);
        String xml = createTOXML(prestaShopOrder);
        ClientResponse response = getWebResource().path(path).type(MediaType.APPLICATION_XML).put(ClientResponse.class, xml);
        return response.getStatus();

    }
    
    @Override
    public List<Order> get(String path) {
        List<Order> listOrder = new ArrayList<>();
        try {
            PrestashopItens getListItens = getWebResource().path(path).type(MediaType.APPLICATION_XML).get(PrestashopItens.class);            
            for (AccessXMLAttributeOrder accessXMLAttributeOrder : getListItens.getOrders().getOrder()) {
                Prestashop prestashop = getWebResource().path(Order.URLORDER).path(accessXMLAttributeOrder.getId()).type(MediaType.APPLICATION_XML).get(Prestashop.class);
                listOrder.add(prestashop.getOrder());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return listOrder;
    }

    @Override
    public Order getId(String path, int key) {
        Prestashop prestashop = getWebResource().path(path).path(String.valueOf(key)).type(MediaType.APPLICATION_XML).get(Prestashop.class);
        return prestashop.getOrder();

    }

    @Override
    public int delete(String path, String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
