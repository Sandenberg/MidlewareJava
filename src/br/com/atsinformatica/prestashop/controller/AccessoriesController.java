/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.erp.entity.SugestVendasERPBean;
import br.com.atsinformatica.prestashop.clientDAO.ProductPrestashopDAO;
import br.com.atsinformatica.prestashop.model.node.Accessories;
import br.com.atsinformatica.prestashop.model.node.ProductNode;
import br.com.atsinformatica.prestashop.model.root.Product;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author AlexsanderPimenta
 */
public class AccessoriesController {
    
    
    public void insert(SugestVendasERPBean sugestVendas){
        Accessories accessories = new Accessories();
        ProductController prodController = new ProductController();
        Product product =  prodController.getProductById(sugestVendas.getIdProdEcom1());
        ProductPrestashopDAO prodDao = new ProductPrestashopDAO();
        accessories = product.getAssociations().getAccessories();        
        ProductNode prodNode = new ProductNode();
        prodNode.setId(String.valueOf(sugestVendas.getIdProdEcom2()));
        if(accessories.getProduct() != null && !accessories.getProduct().isEmpty() )
           accessories.getProduct().add(prodNode);
        else{
            List<ProductNode> listProdNode = new ArrayList<>();
            listProdNode.add(prodNode);
            accessories.setProduct(listProdNode);          
        }       
        product.getAssociations().setAccessories(accessories);
        prodDao.put(Product.URLPRODUCTS, product);                      
    }
    
    
    public int delete(String idAccessorie){
        Accessories accessories = new Accessories();
        String str[] = idAccessorie.split("-");
        ProductController prodController = new ProductController();
        Product product =  prodController.getProductById(Integer.valueOf(str[0]));
        ProductPrestashopDAO prodDao = new ProductPrestashopDAO();
        accessories = product.getAssociations().getAccessories();        
        for(ProductNode prodNodeAux : accessories.getProduct()){
            if(prodNodeAux.getId().equals(str[1])){
                accessories.getProduct().remove(prodNodeAux);
                break;
            }
        }
        //seta accessórios finais a associação
        product.getAssociations().setAccessories(accessories);
        //atualiza cadastro de produto
        return prodDao.put(Product.URLPRODUCTS, product);                      
    }
    
    
    
    
}
