/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.model.root;

import br.com.atsinformatica.prestashop.model.node.AssociationsCartOrder;
import br.com.atsinformatica.prestashop.model.node.AssociationsNode;
import java.util.Date;
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
@XmlType(name = "cart", propOrder = {
    "id",
    "idAddressDelivery",
    "idAddressInvoice",
    "idCurrency",
    "idCustomer",
    "idGuest",
    "idLang",
    "idShopGroup",
    "idShop",
    "idCarrier",
    "recyclable",
    "secureKey",
    "gift",
    "giftMessage",
    "mobileTheme",
    "deliveryOption",
    "allowSeperatedPackage",
    "dateAdd",
    "dateUpd",
    "associations",
})

@XmlRootElement(name = "cart")
public class Cart {
    public static String URLCART = "carts/";
    
    @XmlElement(name = "id")
    private String id;
    @XmlElement(name = "id_address_delivery")
    private int idAddressDelivery;
    @XmlElement(name = "id_address_invoice")
    private int idAddressInvoice;
    @XmlElement(name = "id_currency")
    private int idCurrency;
    @XmlElement(name = "id_customer")
    private int idCustomer;
    @XmlElement(name = "id_guest")
    private int idGuest;
    @XmlElement(name = "id_lang")
    private int idLang;
    @XmlElement(name = "id_shop_group")
    private int idShopGroup;
    @XmlElement(name = "id_shop")
    private int idShop;
    @XmlElement(name = "id_carrier")
    private int idCarrier;
    @XmlElement(name = "recyclable")
    private String recyclable;
    @XmlElement(name = "secure_key")
    private String secureKey;
    @XmlElement(name = "gift")
    private String gift;
    @XmlElement(name = "gift_message")
    private String giftMessage;
    @XmlElement(name = "mobile_theme")
    private String mobileTheme;
    @XmlElement(name = "delivery_option")
    private String deliveryOption;
    @XmlElement(name = "allow_seperated_package")
    private String allowSeperatedPackage;
    @XmlElement(name = "date_add")
    private Date dateAdd;
    @XmlElement(name = "date_upd")
    private Date dateUpd;
    @XmlElement(name = "associations")
    private AssociationsCartOrder associations;

    /**
     * @return the idAddressDelivery
     */
    public int getIdAddressDelivery() {
        return idAddressDelivery;
    }

    /**
     * @param idAddressDelivery the idAddressDelivery to set
     */
    public void setIdAddressDelivery(int idAddressDelivery) {
        this.idAddressDelivery = idAddressDelivery;
    }

    /**
     * @return the idCurrency
     */
    public int getIdCurrency() {
        return idCurrency;
    }

    /**
     * @param idCurrency the idCurrency to set
     */
    public void setIdCurrency(int idCurrency) {
        this.idCurrency = idCurrency;
    }

    /**
     * @return the idCustomer
     */
    public int getIdCustomer() {
        return idCustomer;
    }

    /**
     * @param idCustomer the idCustomer to set
     */
    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    /**
     * @return the idGuest
     */
    public int getIdGuest() {
        return idGuest;
    }

    /**
     * @param idGuest the idGuest to set
     */
    public void setIdGuest(int idGuest) {
        this.idGuest = idGuest;
    }

    /**
     * @return the idLan
     */
    public int getIdLang() {
        return idLang;
    }

    /**
     * @param idLan the idLan to set
     */
    public void setIdLang(int idLang) {
        this.idLang = idLang;
    }

    /**
     * @return the idShop
     */
    public int getIdShop() {
        return idShop;
    }

    /**
     * @param idShop the idShop to set
     */
    public void setIdShop(int idShop) {
        this.idShop = idShop;
    }

    /**
     * @return the idCarrier
     */
    public int getIdCarrier() {
        return idCarrier;
    }

    /**
     * @param idCarrier the idCarrier to set
     */
    public void setIdCarrier(int idCarrier) {
        this.idCarrier = idCarrier;
    }

    /**
     * @return the recyclable
     */
    public String getRecyclable() {
        return recyclable;
    }

    /**
     * @param recyclable the recyclable to set
     */
    public void setRecyclable(String recyclable) {
        this.recyclable = recyclable;
    }

    /**
     * @return the gift
     */
    public String getGift() {
        return gift;
    }

    /**
     * @param gift the gift to set
     */
    public void setGift(String gift) {
        this.gift = gift;
    }

    /**
     * @return the giftMessage
     */
    public String getGiftMessage() {
        return giftMessage;
    }

    /**
     * @param giftMessage the giftMessage to set
     */
    public void setGiftMessage(String giftMessage) {
        this.giftMessage = giftMessage;
    }

    /**
     * @return the mobileTheme
     */
    public String getMobileTheme() {
        return mobileTheme;
    }

    /**
     * @param mobileTheme the mobileTheme to set
     */
    public void setMobileTheme(String mobileTheme) {
        this.mobileTheme = mobileTheme;
    }

    /**
     * @return the deliveryOption
     */
    public String getDeliveryOption() {
        return deliveryOption;
    }

    /**
     * @param deliveryOption the deliveryOption to set
     */
    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    /**
     * @return the allowSeperatedPackage
     */
    public String getAllowSeperatedPackage() {
        return allowSeperatedPackage;
    }

    /**
     * @param allowSeperatedPackage the allowSeperatedPackage to set
     */
    public void setAllowSeperatedPackage(String allowSeperatedPackage) {
        this.allowSeperatedPackage = allowSeperatedPackage;
    }

    /**
     * @return the dateAdd
     */
    public Date getDateAdd() {
        return dateAdd;
    }

    /**
     * @param dateAdd the dateAdd to set
     */
    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    /**
     * @return the dateUpd
     */
    public Date getDateUpd() {
        return dateUpd;
    }

    /**
     * @param dateUpd the dateUpd to set
     */
    public void setDateUpd(Date dateUpd) {
        this.dateUpd = dateUpd;
    }


    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the idAddressInvoice
     */
    public int getIdAddressInvoice() {
        return idAddressInvoice;
    }

    /**
     * @param idAddressInvoice the idAddressInvoice to set
     */
    public void setIdAddressInvoice(int idAddressInvoice) {
        this.idAddressInvoice = idAddressInvoice;
    }

    /**
     * @return the secureKey
     */
    public String getSecureKey() {
        return secureKey;
    }

    /**
     * @param secureKey the secureKey to set
     */
    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    /**
     * @return the idShopGroup
     */
    public int getIdShopGroup() {
        return idShopGroup;
    }

    /**
     * @param idShopGroup the idShopGroup to set
     */
    public void setIdShopGroup(int idShopGroup) {
        this.idShopGroup = idShopGroup;
    }

    /**
     * @return the associations
     */
    public AssociationsCartOrder getAssociations() {
        return associations;
    }

    /**
     * @param associations the associations to set
     */
    public void setAssociations(AssociationsCartOrder associations) {
        this.associations = associations;
    }
    
    
    
}
