/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.ComponentButton;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import model.Component;
import model.ComponentDescription;
import listeners.ComponentButtonListener;

/**
 *
 * @author Rafa
 */
public class SplitMenuComponentButton extends SplitMenuButton implements ComponentButton, EventHandler<ActionEvent> {
    private final MenuItem remove;
    private final ComponentButtonListener listener;
    private final Component component;
    public SplitMenuComponentButton(Component component, ComponentButtonListener listener) {
        this.component = component;
        this.setOnAction(this);
        this.setText(component.toString());
        
        this.listener = listener;
        
        remove = new MenuItem("Eliminar");
        remove.setOnAction(this);
        remove.setDisable(true);
        
        this.getItems().add(remove);
        this.setMaxWidth(Double.MAX_VALUE);
        
        update();
        
    }

    @Override
    public void handle(ActionEvent event) {
        if (listener != null) {
            if (event.getSource() == this) {
                listener.setProductFor(component.getComponentDescription(), this);
            } else if (event.getSource() == remove) {
                listener.removeProductFor(component.getComponentDescription(), this);
            }
        }
    }

    private void enableRemove() {
        remove.setDisable(false);
    }

    private void disableRemove() {
        remove.setDisable(true);
    }

    @Override
    public void update() {
        if (component.hasProduct()) {
            enableRemove();
        }
        else {
            disableRemove();
        }
        setText(component.toString());
    }

}
