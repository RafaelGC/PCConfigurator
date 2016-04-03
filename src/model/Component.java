/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import es.upv.inf.Product;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Rafa
 */
@XmlRootElement(name = "component")
@XmlAccessorType(XmlAccessType.FIELD)
public class Component {

    @XmlElement(name = "product")
    private model.Product product;
    @XmlElement(name = "amount")
    private int amount;
    @XmlElement(name = "component-description")
    private ComponentDescription componentDescription;
    //@XmlElement(name = "product-description")
    //private String productDescription; //Como la clase Product no se puede guardar en XML, se guarda sólo la descripción.

    public Component() {
        product = null;
        amount = 0;
        componentDescription = null;
    }

    public Component(ComponentDescription componentDescription) {
        this.componentDescription = componentDescription;
        amount = 0;
        product = null;
    }

    public ComponentDescription getComponentDescription() {
        return this.componentDescription;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        if (product == null) {
            this.product = null;
        } else {
            this.product = new model.Product(product);
            //this.productDescription = product.getDescription();
        }
    }

    public boolean hasProduct() {
        return product != null;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTotalPrice() {
        return getProduct().getPrice() * getAmount();
    }

    @Override
    public String toString() {
        if (product != null) {
            return product.getDescription() + "\t" + " x" + getAmount();
        } else {
            return "Sin seleccionar";
        }
    }

    public String toString(boolean addPrice) {
        if (product != null) {
            if (!addPrice) {
                return toString();
            }
            return product.getDescription() + "\t" + getProduct().getPrice() + Price.SYMBOL + " x" + getAmount() + " = " + getTotalPrice() + Price.SYMBOL;
        } else {
            return "Sin seleccionar";
        }
    }

}
