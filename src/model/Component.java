/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import es.upv.inf.Product;
import java.io.Serializable;

/**
 *
 * @author Rafa
 */
public class Component implements Serializable {

    private Product product;
    private int amount;
    private ComponentDescription componentDescription;

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
        this.product = product;
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
        }
        else {
            return "";
        }
    }

}
