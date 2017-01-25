/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.node;

import br.com.atsinformatica.prestashop.model.list.CartRows;
import br.com.atsinformatica.prestashop.model.list.Combinations;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author AlexsanderPimenta
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "categories",
    "combinations",
    "productOptionValues",    
    "stockAvailables",
    "images",
    "accessories",
    "cartRows",
})
@XmlRootElement(name="associations")
public class AssociationsNode {
    
    @XmlElement(name = "categories")    
    private CategoriesNode categories;
    @XmlElement(name = "combinations")
    private CombinationsNode combinations;
    @XmlElement(name = "product_option_values")
    private ProductOptionValuesNode productOptionValues;
    @XmlElement(name = "images")
    private ImagesNode images;
    @XmlElement(name = "stock_availables")
    private StockAvailablesNode stockAvailables;
    @XmlElement(name = "accessories")
    private Accessories accessories;
    @XmlElement(name = "cart_rows")
    private CartRows cartRows;

   
    
    
    /**
     * @return the categories
     */
    public CategoriesNode getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(CategoriesNode categories) {
        this.categories = categories;
    }

    /**
     * @return the stockAvailables
     */
    public StockAvailablesNode getStockAvailables() {
        return stockAvailables;
    }

    /**
     * @param stockAvailables the stockAvailables to set
     */
    public void setStockAvailables(StockAvailablesNode stockAvailables) {
        this.stockAvailables = stockAvailables;
    }

    /**
     * @return the combinations
     */
    public CombinationsNode getCombinations() {
        return combinations;
    }

    /**
     * @param combinations the combinations to set
     */
    public void setCombinations(CombinationsNode combinations) {
        this.combinations = combinations;
    }

    /**
     * @return the productOptionValues
     */
    public ProductOptionValuesNode getProductOptionValues() {
        return productOptionValues;
    }

    /**
     * @param productOptionValues the productOptionValues to set
     */
    public void setProductOptionValues(ProductOptionValuesNode productOptionValues) {
        this.productOptionValues = productOptionValues;
    }

    /**
     * @return the images
     */
    public ImagesNode getImages() {
        return images;
    }

    /**
     * @param images the images to set
     */
    public void setImages(ImagesNode images) {
        this.images = images;
    }

    /**
     * @return the accessories
     */
    public Accessories getAccessories() {
        return accessories;
    }

    /**
     * @param accessories the accessories to set
     */
    public void setAccessories(Accessories accessories) {
        this.accessories = accessories;
    }

    /**
     * @return the cartRows
     */
    public CartRows getCartRows() {
        return cartRows;
    }

    /**
     * @param cartRows the cartRows to set
     */
    public void setCartRows(CartRows cartRows) {
        this.cartRows = cartRows;
    }
    
   
    
}
