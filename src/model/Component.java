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
public class Component implements Serializable{

    private Product product;
    private int amount;

    public Component(Product product, int amount) {
        this.product = product;
        this.amount = amount;
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
    
    public String toString() {
        return product.getDescription() + "\t" + " x" + amount;
    }
    
    public String toString(boolean addPrice) {
        if (!addPrice) return toString();
        return product.getDescription() + "\t" + product.getPrice() + Price.SYMBOL + " x" + amount + " = " + getTotalPrice() + Price.SYMBOL;
    }
    
    public double getTotalPrice() {
        return product.getPrice() * amount;
    }

}
