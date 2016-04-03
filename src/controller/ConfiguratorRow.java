/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import listeners.ConfiguratorRowListener;
import model.Component;
import model.ComponentDescription;

/**
 *
 * @author Rafa
 */
public class ConfiguratorRow implements EventHandler<ActionEvent> {

    private final Node[] nodes;
    private final SplitMenuButton button;
    private final ConfiguratorRowListener listener;
    private final MenuItem remove;
    private Component component;

    public ConfiguratorRow(Component component, ConfiguratorRowListener listener) {
        nodes = new Node[2];
        this.listener = listener;

        Label label = new Label(component.getComponentDescription().getName());

        button = new SplitMenuButton();
        button.setOnAction(this);
        button.setText(component.toString());
        remove = new MenuItem("Eliminar");
        remove.setOnAction(this);
        remove.setDisable(true);
        button.getItems().add(remove);
        button.setMaxWidth(Double.MAX_VALUE);

        nodes[0] = label;
        nodes[1] = button;

        this.component = component;
        
        update();
    }

    public Node[] getNodes() {
        return nodes;
    }

    public SplitMenuButton getButton() {
        return button;
    }

    @Override
    public void handle(ActionEvent event) {
        if (listener != null) {
            if (event.getSource() == button) {
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

    public void update() {
        if (component.hasProduct()) {
            enableRemove();
        }
        else {
            disableRemove();
        }
        button.setText(component.toString());
    }

}
