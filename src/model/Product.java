/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Rafa
 */
@XmlRootElement(name = "product")
@XmlType(namespace = "pcconfigurator")
public class Product extends es.upv.inf.Product {

    @XmlElement(name = "description")
    private final String description;
    @XmlElement(name = "price")
    private final double price;
    @XmlElement(name = "stock")
    private final int stock;
    @XmlElement(name = "category")
    private final Category category;

    public Product() {
        super(null, 0.d, 0, null);
        description = null;
        price = 0.d;
        stock = 0;
        category = null;
    }

    public Product(es.upv.inf.Product product) {
        this(product.getDescription(), product.getPrice(), product.getStock(), product.getCategory());
    }

    public Product(String description, double price, int stock, Category category) {
        super(description, price, stock, category);

        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;

    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Product) {
            Product p = (Product) other;

            return p.category.equals(this.category)
                    && p.description.compareTo(description) == 0
                    && p.price == this.price
                    && p.stock == this.stock;

        }
        return false;
    }


}
