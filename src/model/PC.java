/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import es.upv.inf.Product;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @brief Define los elementos que componen un ordenador, tanto los obligatorios
 * como los opcionales.
 * @author Rafa
 */
@XmlRootElement(name = "pc")
@XmlAccessorType(XmlAccessType.FIELD)
public class PC {

    @XmlElement(name = "component")
    private ArrayList<Component> components;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "advanced-configurator")
    private boolean advancedConfigurator;

    public PC() {
        name = "Sin nombre";
        advancedConfigurator = true;
        
        //Definición de los componentes que forman un ordenador.
        
        components = new ArrayList<>();
        
        components.add(new Component(new ComponentDescription(
                                            Product.Category.CASE,
                                            ComponentDescription.ESSENTIAL,
                                            ComponentTextDescriptions.CASE)));
        
        components.add(new Component(new ComponentDescription(
                                            Product.Category.MOTHERBOARD,
                                            ComponentDescription.ESSENTIAL,
                                            ComponentTextDescriptions.MOTHERBOARD)));
        
        components.add(new Component(new ComponentDescription(
                                            Product.Category.CPU,
                                            ComponentDescription.ESSENTIAL,
                                            ComponentTextDescriptions.CPU)));
        
        components.add(new Component(new ComponentDescription(
                                            Product.Category.RAM,
                                            ComponentDescription.ESSENTIAL,
                                            ComponentTextDescriptions.RAM)));
        
        components.add(new Component(new ComponentDescription(
                                            Product.Category.GPU,
                                            ComponentDescription.ESSENTIAL,
                                            ComponentTextDescriptions.GPU)));

        /*
         components.add(new PCStructureComponent(Product.Category.HDD, true));
         components.add(new PCStructureComponent(Product.Category.HDD_SSD, true));*/
        Product.Category[] hddCats = {Product.Category.HDD, Product.Category.HDD_SSD};
        components.add(new Component(new ComponentDescription(
                new ArrayList<>(Arrays.asList(hddCats)),
                CategoryNames.getName(Product.Category.HDD),
                ComponentDescription.ESSENTIAL,
                ComponentTextDescriptions.HDD)));

        components.add(new Component(new ComponentDescription(Product.Category.KEYBOARD, ComponentDescription.NON_ESSENTIAL, "")));
        components.add(new Component(new ComponentDescription(Product.Category.MOUSE, ComponentDescription.NON_ESSENTIAL, "")));
        components.add(new Component(new ComponentDescription(Product.Category.DVD_WRITER, ComponentDescription.NON_ESSENTIAL, "")));
        components.add(new Component(new ComponentDescription(Product.Category.FAN, ComponentDescription.NON_ESSENTIAL, "")));
        components.add(new Component(new ComponentDescription(Product.Category.MULTIREADER, ComponentDescription.NON_ESSENTIAL, "")));
        components.add(new Component(new ComponentDescription(Product.Category.POWER_SUPPLY, ComponentDescription.NON_ESSENTIAL, "")));
        components.add(new Component(new ComponentDescription(Product.Category.SCREEN, ComponentDescription.NON_ESSENTIAL, "")));
        components.add(new Component(new ComponentDescription(Product.Category.SPEAKER, ComponentDescription.NON_ESSENTIAL, "")));

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
        ArrayList<Component> result = new ArrayList<>();

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
        ArrayList<Component> result = new ArrayList<>();

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
     *
     * @param product Producto que se añade.
     * @param amount Cantidad de productos. Debe ser mayor que cero.
     * @return Devuelve el componente añadido o null si no se ha podido añadir
     * porque el ordenador no acepta ese tipo de componente.
     */
    public Component addProduct(Product product, int amount) {
        if (amount <= 0) {
            return null;
        }
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
     * @return True si todos los compotentes necesarios tienen un producto
     * vinculado.
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append("\n");
        sb.append("=================\n");

        for (Component component : components) {
            if (component.hasProduct()) {
                sb.append(component).append("\n");
            }
        }
        sb.append("=================\n").append(getPrice());
        return sb.toString();
    }

    public boolean saveToFile(File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PC.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(this, file);
            return true;
        } catch (JAXBException ex) {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static PC loadFromFile(File file) {
        try {
            return loadFromStream(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static PC loadFromStream(InputStream stream) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PC.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            
            return (PC) jaxbUnmarshaller.unmarshal(stream);
        } catch (JAXBException ex) {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof PC) {
            PC c = (PC) other;
            
            return  this.name.compareTo(c.name) == 0 &&
                    this.components.equals(c.components);
            
        }
        
        return false;
    }

    /**
     * @return the advancedConfigurator
     */
    public boolean isAdvancedConfigurator() {
        return advancedConfigurator;
    }

    /**
     * @param advancedConfigurator the advancedConfigurator to set
     */
    public void setAdvancedConfigurator(boolean advancedConfigurator) {
        this.advancedConfigurator = advancedConfigurator;
    }

}
