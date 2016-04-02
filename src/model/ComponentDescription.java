/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import es.upv.inf.Product;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafa
 */
public class ComponentDescription {
    public static final boolean ESSENTIAL = true, NON_ESSENTIAL = false;
    
    private ArrayList<Product.Category> categories;
    private String name;
    private boolean essential;
    
    ComponentDescription(Product.Category category, boolean essential) {
        this.categories = new ArrayList<Product.Category>();
        categories.add(category);
        this.name = CategoryNames.getName(category);
        this.essential = essential;
    }
    
    ComponentDescription(ArrayList<Product.Category> categories, String name, boolean essential) {
        this.categories = categories;
        this.name = name;
        this.essential = essential;
    }
    
    public boolean is(Product.Category cat) {
        return categories.contains(cat);
    }
    
    public List<Product.Category> getCategories() {
        return categories;
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
     * @return the essential
     */
    public boolean isEssential() {
        return essential;
    }

    /**
     * @param essential the essential to set
     */
    public void setEssential(boolean essential) {
        this.essential = essential;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof ComponentDescription) {
            ComponentDescription cd = (ComponentDescription) other;
            return cd.essential = this.essential && cd.name.compareTo(this.name) == 0 && cd.categories.equals(cd.categories);
        }
        return false;
    }
    
    
    
}
