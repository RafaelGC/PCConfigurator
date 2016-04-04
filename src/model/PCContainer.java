/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Rafa
 */
public class PCContainer {
    private static PCContainer uniqueInstance = null;
    private List<PC> computers;
    
    private PCContainer() {
        computers = new ArrayList<>();
    }
    
    public PC addPC(PC computer) {
        computers.add(computer);
        return computer;
    }
    
    public Iterator<PC> iterator() {
        return computers.iterator();
    }
    
    public static PCContainer instance() {
        if (uniqueInstance == null) {
            uniqueInstance = new PCContainer();
        }
        return uniqueInstance;
    }
}
