/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import es.upv.inf.Product;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @brief Define los elementos que componen un ordenador, tanto los obligatorios
 * como los opcionales.
 * @author Rafa
 */
public class PC {

    private ArrayList<Component> components;
    private String name;

    public PC() {
        name = "Sin nombre";

        components = new ArrayList<Component>();

        components.add(new Component(new ComponentDescription(Product.Category.CASE, ComponentDescription.ESSENTIAL)));
        components.add(new Component(new ComponentDescription(Product.Category.MOTHERBOARD, ComponentDescription.ESSENTIAL)));
        components.add(new Component(new ComponentDescription(Product.Category.CPU, ComponentDescription.ESSENTIAL)));
        components.add(new Component(new ComponentDescription(Product.Category.RAM, ComponentDescription.ESSENTIAL)));
        components.add(new Component(new ComponentDescription(Product.Category.GPU, ComponentDescription.ESSENTIAL)));

        /*
         components.add(new PCStructureComponent(Product.Category.HDD, true));
         components.add(new PCStructureComponent(Product.Category.HDD_SSD, true));*/
        Product.Category[] hddCats = {Product.Category.HDD, Product.Category.HDD_SSD};
        components.add(new Component(new ComponentDescription(
                new ArrayList<>(Arrays.asList(hddCats)),
                CategoryNames.getName(Product.Category.HDD),
                ComponentDescription.ESSENTIAL)));

        components.add(new Component(new ComponentDescription(Product.Category.KEYBOARD, ComponentDescription.NON_ESSENTIAL)));
        components.add(new Component(new ComponentDescription(Product.Category.MOUSE, ComponentDescription.NON_ESSENTIAL)));
        components.add(new Component(new ComponentDescription(Product.Category.DVD_WRITER, ComponentDescription.NON_ESSENTIAL)));
        components.add(new Component(new ComponentDescription(Product.Category.FAN, ComponentDescription.NON_ESSENTIAL)));
        components.add(new Component(new ComponentDescription(Product.Category.MULTIREADER, ComponentDescription.NON_ESSENTIAL)));
        components.add(new Component(new ComponentDescription(Product.Category.POWER_SUPPLY, ComponentDescription.NON_ESSENTIAL)));
        components.add(new Component(new ComponentDescription(Product.Category.SCREEN, ComponentDescription.NON_ESSENTIAL)));
        components.add(new Component(new ComponentDescription(Product.Category.SPEAKER, ComponentDescription.NON_ESSENTIAL)));

    }
    
    public Component getComponentByComponentDescription(ComponentDescription cd) {
        for (Iterator<Component> it = components.iterator(); it.hasNext();) {
            Component cmp = it.next();
            if (cmp.getComponentDescription().equals(cd)) {
                return cmp;
            }
        }
        return null;
    }

    public Iterator<Component> iterator() {
        return components.iterator();
    }

    public ArrayList<Component> getEssentialComponents() {
        ArrayList<Component> result = new ArrayList<Component>();

        Iterator<Component> it = components.iterator();
        while (it.hasNext()) {
            Component cmp = it.next();
            if (cmp.getComponentDescription().isEssential()) {
                result.add(cmp);
            }
        }
        return result;
    }

    public ArrayList<Component> getNonEssentialComponents() {
        ArrayList<Component> result = new ArrayList<Component>();

        Iterator<Component> it = components.iterator();
        while (it.hasNext()) {
            Component cmp = it.next();
            if (!cmp.getComponentDescription().isEssential()) {
                result.add(cmp);
            }
        }
        return result;
    }

    /**
     * Añade un cierta cantidad de productos al ordenador.
     * @param product Producto que se añade.
     * @param amount Cantidad de productos. Debe ser mayor que cero.
     * @return Devuelve el componente añadido o null si no se ha podido añadir porque
     * el ordenador no acepta ese tipo de componente.
     */
    public Component addProduct(Product product, int amount) {
        if (amount <= 0) return null;
        for (Iterator<Component> it = components.iterator(); it.hasNext();) {
            Component cmp = it.next();
            if (cmp.getComponentDescription().is(product.getCategory())) {

                cmp.setProduct(product);
                cmp.setAmount(amount);

                return cmp;
            }
        }
        return null;
    }
    
    /**
     * @return Devuelve el precio del ordenador.
     */
    public Price getPrice() {
        double sum = 0.d;
        for (Iterator<Component> it = components.iterator(); it.hasNext();) {
            Component cmp = it.next();
            if (cmp.hasProduct()) {
                sum += cmp.getTotalPrice();
            }
        }
        return new Price(sum);
    }

    /**
     * 
     * @return True si todos los compotentes necesarios tienen un producto vinculado. 
     */
    public boolean isComplete() {
        for (Iterator<Component> it = components.iterator(); it.hasNext();) {
            Component cmp = it.next();
            if (cmp.getComponentDescription().isEssential() && !cmp.hasProduct()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append("=================\n");

        for (Iterator<Component> it = components.iterator(); it.hasNext();) {
            Component component = it.next();
            if (component.hasProduct()) {
                sb.append(component).append("\n");
            }
        }
        sb.append("=================\n").append(getPrice());
        return sb.toString();
    }

}
