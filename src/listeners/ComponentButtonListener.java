/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import controller.ComponentButton.ComponentButton;
import model.ComponentDescription;

/**
 *
 * @author Rafa
 */
public interface ComponentButtonListener {
    public void setProductFor(ComponentDescription component, ComponentButton btn);
    public void removeProductFor(ComponentDescription component, ComponentButton btn);
}
