/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafa
 */
public class PC {
    private ArrayList<StructureComponent> components;
    private String name;
    private PCStructure structure;
    
    public PC(PCStructure structure) {
        components = new ArrayList<StructureComponent>();
        for (Iterator<PCStructureComponent> it = structure.iterator(); it.hasNext();) {
            components.add(new StructureComponent(it.next(), null));
        }
        name = "Sin nombre";
    }
    
    public static PC loadFromFile(String fileName) {
        PC pc = new PC(PCStructure.instance());
        
        for (Iterator<StructureComponent> it = pc.components.iterator(); it.hasNext();) {
            //
        }
        
        return pc;
    }
    
    public boolean saveToFile(String fileName) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
            os.writeObject(getComponents());
            
            return true;
            
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {
        }
        return false;
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
    
    public void addComponent(Component component) {
        Iterator<StructureComponent> it = components.iterator();
        while (it.hasNext()) {
            StructureComponent strComponent = it.next();
            
            if (strComponent.structureComponent.is(component.getProduct().getCategory())) {
                strComponent.component = component;
            }
            
        }
    }
    
    public boolean isComplete() {
        for (Iterator<StructureComponent> it = components.iterator(); it.hasNext();) {
            StructureComponent sc = it.next();
            if (sc.structureComponent.isEssential() && sc.component == null) {
                return false;
            }
        }
        return true;
    }
    
    public List<Component> getComponents() {
        ArrayList<Component> components = new ArrayList<Component>();
        
        for (Iterator<StructureComponent> it = this.components.iterator(); it.hasNext();) {
            components.add(it.next().component);
        }
        
        return components;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append("=================\n");
        
        for (Iterator<model.StructureComponent> it = components.iterator(); it.hasNext();) {
            model.StructureComponent sc = it.next();
            if (sc.component != null)
            sb.append(sc.component.toString(true)).append("\n");
        }
        sb.append("=================\n").append(getPrice());
        return sb.toString();
    }
    
    public Price getPrice() {
        double sum = 0.d;
        for (Iterator<model.StructureComponent> it = components.iterator(); it.hasNext();) {
            model.StructureComponent sc = it.next();
            if (sc.component != null)
                sum += sc.component.getTotalPrice();
        }
        return new Price(sum);
    }
}

class StructureComponent {
    public PCStructureComponent structureComponent;
    public Component component;
    public StructureComponent(PCStructureComponent a, Component b) {
        structureComponent = a;
        component = b;
    }
}