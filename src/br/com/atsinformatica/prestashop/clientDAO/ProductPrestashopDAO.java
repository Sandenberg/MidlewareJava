/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.clientDAO;

import br.com.atsinformatica.midler.ui.PanelHistorico;
import br.com.atsinformatica.prestashop.model.list.prestashop.AccessXMLAttribute;
import br.com.atsinformatica.prestashop.model.list.prestashop.WSItens;
import br.com.atsinformatica.prestashop.model.root.Prestashop;
import br.com.atsinformatica.prestashop.model.root.Product;
import br.com.atsinformatica.prestashop.model.root.WsItem;

import com.sun.jersey.api.client.ClientResponse;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

/**
 *
 * @author ricardosilva
 */
public class ProductPrestashopDAO extends GenericPrestashopDAO<Product> implements IGenericPrestashopDAO<Product> {

    /**
     * Adiciona um item Produto
     *
     * @param path
     * @param t
     * @return boolean
     */
    public int postWithVerification(String path, Product t) {
        Prestashop prestashop = new Prestashop();
        prestashop.setProduct(t);
        String xml = createTOXML(prestashop);
        Prestashop post = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(Prestashop.class, xml);
        return Integer.parseInt(post.getProduct().getId().getContent());
    }

    /**
     * Adiciona um item e retorna o objeto salvo
     *
     * @param path
     * @param t
     * @return
     */
    public Product postProduct(String path, Product t) {
        WsItem ws = new WsItem();
        ws.setProduct(t);
        String xml = createTOXML(ws);
        // WsItem wsItem = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(WsItem.class, xml);
        String xmlResponse = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(String.class, xml);
        Product prod = unmarshallContext_(xmlResponse).getProduct();
        return prod;
    }

    /**
     * Atualiza um item específico de Produto.
     *
     * @param path
     * @param t
     */
    @Override
    public int put(String path, Product t) {
        WsItem ws = new WsItem();
        ws.setProduct(t);
        String xml = createTOXML(ws);
        ClientResponse response = getWebResource().path(path).type(MediaType.APPLICATION_XML).put(ClientResponse.class, xml);
        return response.getStatus();
    }

    /**
     * retorna todos itens de Produto
     *
     * @param path
     * @return
     */
    @Override
    public List<Product> get(String path) {
        int i = 0;
        try {
            WSItens getListItens = getWebResource().path(path).type(MediaType.APPLICATION_XML).get(WSItens.class);
            List<Product> listProdFeature = new ArrayList<>();
            for (AccessXMLAttribute attribute : getListItens.getProducts().getProduct()) {
                String xml = getWebResource().path(path).path(attribute.getId()).type(MediaType.APPLICATION_XML).get(String.class);
                if (xml != null || !xml.equals("")) {
                    System.out.println(attribute.getId());
                    WsItem p = unmarshallContext_(xml);
                    listProdFeature.add(p.getProduct());
                    PanelHistorico.textArea.append("Retornando produto: " + p.getProduct().getName().getTextName() + "\n");
                }

            }
            return listProdFeature;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

    /**
     * Retorna produto pelo id do atributo do mesmo na loja virtual
     * @param path 
     * @param id
     * @return 
     */
    public Product getProductByAttributeId(String path, String id) {
        String xml = getWebResource().path(path).path(id).type(MediaType.APPLICATION_XML).get(String.class);
        Product prod = null;
        if (xml != null || !xml.equals("")) {
            WsItem p = unmarshallContext_(xml);
            prod = p.getProduct();            
        }
        return prod;
    }

    public WSItens getProductItem(String path) {
        try {
            WSItens getListItens = getWebResource().path(path).type(MediaType.APPLICATION_XML).get(WSItens.class);
            return getListItens;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retorna um item em específico da Produto
     *
     * @param path
     * @param key
     * @return
     */
    @Override
    public Product getId(String path, int key) {
        String xml = getWebResource().path(path).path(String.valueOf(key)).type(MediaType.APPLICATION_XML).get(String.class);
        Product p = unmarshallContext_(xml).getProduct();
        return p;
    }

    public Product getById(String path, int key) throws JAXBException {
        String xml = getWebResource().path(path).path(String.valueOf(key)).type(MediaType.APPLICATION_XML).get(String.class);;
        Product p1 = unmarshallContext_(xml).getProduct();
        return p1;
    }

    public Product getId(String path, String key) {
        String xml = getWebResource().path(path).path(String.valueOf(key)).type(MediaType.APPLICATION_XML).get(String.class);
        Product p = unmarshallContext_(xml).getProduct();
        return p;
    }

    @Override
    public void post(String path, Product t) {
        Prestashop prestashop = new Prestashop();
        prestashop.setProduct(t);
        String xml = createTOXML(prestashop);
        ClientResponse clientResponse = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(ClientResponse.class, xml);
    }

    @Override
    public int delete(String path, String id) {
        ClientResponse response = getWebResource().path(path).path(id).type(MediaType.APPLICATION_XML).delete(ClientResponse.class);
        return response.getStatus();
    }
}
