/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.clientDAO;

import br.com.atsinformatica.prestashop.model.root.Cart;
import br.com.atsinformatica.prestashop.model.root.Prestashop;
import com.sun.jersey.api.client.ClientResponse;
import java.util.List;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author AlexsanderPimenta
 */
public class CartPrestashopDAO extends GenericPrestashopDAO<Cart> implements IGenericPrestashopDAO<Cart> {

    @Override
    public void post(String path, Cart t) {
        Prestashop ps = new Prestashop();
        ps.setCart(t);
        String xml = createTOXML(ps);
        ClientResponse cliRes = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(ClientResponse.class,xml);
    }
    
    public Cart postCart(String path, Cart t){
        Prestashop ps = new Prestashop();
        ps.setCart(t);
        String xml = createTOXML(ps);
        Prestashop p = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(Prestashop.class,xml);        
        return p.getCart();
    }

    @Override
    public int put(String path, Cart t) {
        Prestashop ps = new Prestashop();
        ps.setCart(t);
        String xml = createTOXML(ps);
        ClientResponse cliRes = getWebResource().path(Cart.URLCART).type(MediaType.APPLICATION_XML).post(ClientResponse.class,xml);
        return cliRes.getStatus();
    }

    @Override
    public List<Cart> get(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Cart getId(String path, int key) {
        Prestashop prestashop = getWebResource().path(path).path(String.valueOf(key)).type(MediaType.APPLICATION_XML).get(Prestashop.class);
        return prestashop.getCart();
    }

    @Override
    public int delete(String path, String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
