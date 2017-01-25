package br.com.atsinformatica.prestashop.model.root;

import br.com.atsinformatica.prestashop.model.node.*;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "product", propOrder = {
    "id",
    "idCategoryDefault",
    "idManufacturer",
    "width",
    "height",
    "weight",
    "ean13",
    "name",
    "price",
    "shortDescription",
    "description",
    "associations",
    
})

@XmlRootElement(name = "product")
public class ProductBrinkel {
    public static String URLPRODUCTS = "products/";    
    
    @XmlElement(name = "id", required = true)
    private Id id;
    @XmlElement(name = "id_category_default")
    private Integer idCategoryDefault;
    @XmlElement(name = "id_manufacturer")
    private Integer idManufacturer;
    @XmlElement(name = "width")
    private String width;
    @XmlElement(name = "height")
    private String height;
    @XmlElement(name = "weight")
    private String weight;
    @XmlElement(name = "ean13")
    private String ean13;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "price")
    private Price price;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name= "short_description")
    private String shortDescription;
    @XmlElement(name="associations")
    private AssociationNodeBrinkel associations;
    
    public ProductBrinkel() {
        idCategoryDefault = 2;
        width = "0.000000";
        height = "0.000000";
        weight = "0.000000";
        ean13 = "0";        
    }

    /**
     * @return the id
     */
    public Id getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Id id) {
        this.id = id;
    }

    /**
     * @return the idCategoryDefault
     */
    public Integer getIdCategoryDefault() {
        return idCategoryDefault;
    }

    /**
     * @param idCategoryDefault the idCategoryDefault to set
     */
    public void setIdCategoryDefault(Integer idCategoryDefault) {
        this.idCategoryDefault = idCategoryDefault;
    }

    /**
     * @return the idManufacturer
     */
    public Integer getIdManufacturer() {
        return idManufacturer;
    }

    /**
     * @param idManufacturer the idManufacturer to set
     */
    public void setIdManufacturer(Integer idManufacturer) {
        this.idManufacturer = idManufacturer;
    }

    /**
     * @return the width
     */
    public String getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public String getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * @return the weight
     */
    public String getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(String weight) {
        this.weight = weight;
    }

    /**
     * @return the ean13
     */
    public String getEan13() {
        return ean13;
    }

    /**
     * @param ean13 the ean13 to set
     */
    public void setEan13(String ean13) {
        this.ean13 = ean13;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public Price getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Price price) {
        this.price = price;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the shortDescription
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * @param shortDescription the shortDescription to set
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * @return the associations
     */
    public AssociationNodeBrinkel getAssociations() {
        return associations;
    }

    /**
     * @param associations the associations to set
     */
    public void setAssociations(AssociationNodeBrinkel associations) {
        this.associations = associations;
    }

  
}
