package br.com.atsinformatica.prestashop.clientDAO;
import br.com.atsinformatica.prestashop.model.list.prestashop.WSItens;
import br.com.atsinformatica.prestashop.model.root.OrderState;
import java.util.List;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author kennedimalheiros
 */
public class OrderStatePrestashopDAO extends GenericPrestashopDAO<OrderState> implements IGenericPrestashopDAO<OrderState>{

    public WSItens getAllItens() {
        try {
            WSItens list = getWebResource().path(OrderState.URLORDERSTATE).type(MediaType.APPLICATION_XML).get(WSItens.class);
            return list;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public OrderState getId(String path, int key) {
        String xml = getWebResource().path(path).path(String.valueOf(key)).type(MediaType.APPLICATION_XML).get(String.class);
        OrderState o = unmarshallContext_(xml).getOrderState();
        return o;
    }

    @Override
    public int delete(String path, String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void post(String path, OrderState t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int put(String path, OrderState t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OrderState> get(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
