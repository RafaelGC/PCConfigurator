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
 * @brief Define los elementos que componen un ordenador, tanto los obligatorios como los opcionales.
 * @author Rafa
 */
public class PCStructure {
    private static PCStructure uniqueInstance = null;
    
    private ArrayList<PCStructureComponent> components;
    
    private PCStructure() {
        components = new ArrayList<PCStructureComponent>();
        
        components.add(new PCStructureComponent(Product.Category.CASE, true));
        components.add(new PCStructureComponent(Product.Category.MOTHERBOARD, true));
        components.add(new PCStructureComponent(Product.Category.CPU, true));
        components.add(new PCStructureComponent(Product.Category.RAM, true));
        components.add(new PCStructureComponent(Product.Category.GPU, true));
        
        /*
        components.add(new PCStructureComponent(Product.Category.HDD, true));
        components.add(new PCStructureComponent(Product.Category.HDD_SSD, true));*/
        
        Product.Category[] hddCats = {Product.Category.HDD, Product.Category.HDD_SSD};
        components.add(new PCStructureComponent(new ArrayList<>(Arrays.asList(hddCats)), CategoryNames.getName(Product.Category.HDD), true));
        
        components.add(new PCStructureComponent(Product.Category.KEYBOARD, false));
        components.add(new PCStructureComponent(Product.Category.MOUSE, false));
        components.add(new PCStructureComponent(Product.Category.DVD_WRITER, false));
        components.add(new PCStructureComponent(Product.Category.FAN, false));
        components.add(new PCStructureComponent(Product.Category.MULTIREADER, false));
        components.add(new PCStructureComponent(Product.Category.POWER_SUPPLY, false));
        components.add(new PCStructureComponent(Product.Category.SCREEN, false));
        components.add(new PCStructureComponent(Product.Category.SPEAKER, false));
        
    }
    
    public String nameOfCategory(Product.Category category) {
        Iterator<PCStructureComponent> it = components.iterator();
        while (it.hasNext()) {
            PCStructureComponent cmp = it.next();
            if (cmp.is(category)) {
                return cmp.getName();
            }
        }
        return null;
    }
    
    public static PCStructure instance() {
        if (uniqueInstance == null) {
            uniqueInstance = new PCStructure();
        }
        return uniqueInstance;
    }
    
    public Iterator<PCStructureComponent> iterator() {
        return components.iterator();
    }
    
    public ArrayList<PCStructureComponent> getEssentialComponents() {
        ArrayList<PCStructureComponent> result = new ArrayList<PCStructureComponent>();
        
        Iterator<PCStructureComponent> it = components.iterator();
        while (it.hasNext()) {
            PCStructureComponent cmp = it.next();
            if (cmp.isEssential()) {
                result.add(cmp);
            }
        }
        return result;
    }
    
    public ArrayList<PCStructureComponent> getNonEssentialComponents() {
        ArrayList<PCStructureComponent> result = new ArrayList<PCStructureComponent>();
        
        Iterator<PCStructureComponent> it = components.iterator();
        while (it.hasNext()) {
            PCStructureComponent cmp = it.next();
            if (!cmp.isEssential()) {
                result.add(cmp);
            }
        }
        return result;
    }
    
}
