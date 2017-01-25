/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.clientDAO;

import br.com.atsinformatica.prestashop.model.root.OrderSlip;
import br.com.atsinformatica.prestashop.model.root.Prestashop;
import com.sun.jersey.api.client.ClientResponse;
import java.util.List;
import javax.ws.rs.core.MediaType;

/**
 * DAO de devolução do pedido e creditos
 * @author AlexsanderPimenta
 */
public class OrderSlipDAO extends GenericPrestashopDAO<OrderSlip> implements IGenericPrestashopDAO<OrderSlip> {

    @Override
    public void post(String path, OrderSlip t) {
       Prestashop ps = new Prestashop();
       ps.setOrderSlip(t);
       String xml = createTOXML(ps);
       ClientResponse response = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(ClientResponse.class, xml);
    }
    
    public OrderSlip postOrderSlip(String path, OrderSlip t){
        Prestashop ps = new Prestashop();
        ps.setOrderSlip(t);
        String xml = createTOXML(ps);
        Prestashop p = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(Prestashop.class,xml);        
        return p.getOrderSlip();
    }

    @Override
    public int put(String path, OrderSlip t) {
       Prestashop ps = new Prestashop();
       ps.setOrderSlip(t);
       String xml = createTOXML(ps);
       return getWebResource().path(path).type(MediaType.APPLICATION_XML).post(ClientResponse.class, xml).getStatus();
    }

    @Override
    public List<OrderSlip> get(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OrderSlip getId(String path, int key) {
        return getWebResource().path(path).path(String.valueOf(key)).type(MediaType.APPLICATION_XML).get(OrderSlip.class);
    }

    @Override
    public int delete(String path, String id) {
        return getWebResource().path(path).path(id).type(MediaType.APPLICATION_XML).delete(ClientResponse.class).getStatus();
    }
    
}
