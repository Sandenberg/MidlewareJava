/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.clientDAO;

import br.com.atsinformatica.midler.ui.PanelHistorico;
import br.com.atsinformatica.prestashop.model.list.prestashop.AccessXMLAttribute;
import br.com.atsinformatica.prestashop.model.list.prestashop.PrestashopItens;
import br.com.atsinformatica.prestashop.model.list.prestashop.WSItens;
import br.com.atsinformatica.prestashop.model.root.Prestashop;
import br.com.atsinformatica.prestashop.model.root.Category;
import br.com.atsinformatica.prestashop.model.root.WsItem;
import com.sun.jersey.api.client.ClientResponse;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author ricardosilva
 */
public class CategoryPrestashopDAO extends GenericPrestashopDAO<Category> implements IGenericPrestashopDAO<Category> {
    
    /**
     * Adiciona um item Categoria
     *
     * @param path
     * @param t
     */
    @Override
    public void post(String path, Category t) {
        Prestashop prestashopCategory = new Prestashop();
        prestashopCategory.setCategory(t);
        String xml = createTOXML(prestashopCategory);
        ClientResponse response = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(ClientResponse.class, xml);
        System.out.println(response.getStatus());
    }
    
    @Override
    public int delete(String path, String idCategorie) {        
        ClientResponse response = getWebResource().path(path).path(idCategorie).type(MediaType.APPLICATION_XML).delete(ClientResponse.class);
        return response.getStatus();
    }

    /**
     * Adiciona uma categoria e retorna o objeto salvo
     * @param path
     * @param t
     * @return
     */
    public int postCategory(String path, Category t) {
        WsItem prestashopCategory = new WsItem();
        prestashopCategory.setCategory(t);
        String xml = createTOXML(prestashopCategory);
        String xmlResponse = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(String.class, xml);
        //WsItem post = getWebResource().path(path).type(MediaType.APPLICATION_XML).post(WsItem.class, xml);        
        return Integer.parseInt(unmarshallContext_(xmlResponse).getCategory().getId());
    }
    
    /**
     * Atualiza um item específico de Categoria.
     *
     * @param path
     * @param t
     */
    @Override
    public int put(String path, Category t) {
        Prestashop prestashopCategory = new Prestashop();
        prestashopCategory.setCategory(t);
        String xml = createTOXML(prestashopCategory);        
        ClientResponse response = getWebResource().path(path).type(MediaType.APPLICATION_XML).put(ClientResponse.class, xml);        
        return response.getStatus();
    }
    
    public int putCategory(String path, Category t){
        Prestashop prestashopCategory = new Prestashop();
        prestashopCategory.setCategory(t);
        String xml = createTOXML(prestashopCategory);        
        ClientResponse response = getWebResource().path(path).type(MediaType.APPLICATION_XML).put(ClientResponse.class, xml);        
        return response.getStatus();
    }

    /**
     * retorna todos itens de categoria
     *
     * @param path
     * @return
     */ 
    @Override
    public List<Category> get(String path) {
        WSItens getListItens = getWebResource().path(path).type(MediaType.APPLICATION_XML).get(WSItens.class);
        List<Category> listCategory = new ArrayList<>();
        for (AccessXMLAttribute accessXMLAttribute : getListItens.getCategories().getCategory()) {
            if(!accessXMLAttribute.getId().equals("0")){
               //WsItem item = getWebResource().path(path).path(accessXMLAttribute.getId()).type(MediaType.APPLICATION_XML).get(WsItem.class);
               String xml = getWebResource().path(path).path(accessXMLAttribute.getId()).type(MediaType.APPLICATION_XML).get(String.class);
               WsItem p = unmarshallContext_(xml);               
               listCategory.add(p.getCategory()); 
               PanelHistorico.textArea.append("Retornando categoria: "+ p.getCategory().getName().getTextName() + "\n");
            }            
        }
        return listCategory;
    }

    /**
     * Retorna um item em específico da Categoria
     * @param path
     * @param key
     * @return
     */
    @Override
    public Category getId(String path, int key) {    
        String xml = getWebResource().path(path).path(String.valueOf(key)).type(MediaType.APPLICATION_XML).get(String.class);
        WsItem item = unmarshallContext_(xml);
        return item.getCategory();
    }

}
