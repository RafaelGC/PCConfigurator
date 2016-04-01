/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import listeners.ConfiguratorRowListener;
import model.PCStructureComponent;

/**
 *
 * @author Rafa
 */
public class ConfiguratorRow implements EventHandler<ActionEvent> {
    private Node[] nodes;
    private SplitMenuButton button;
    private ConfiguratorRowListener listener;
    private PCStructureComponent component;
    
    public ConfiguratorRow(PCStructureComponent component, ConfiguratorRowListener listener) {
        nodes = new Node[2];
        this.listener = listener;
        
        Label label = new Label(component.getName());
        
        button = new SplitMenuButton();
        button.setOnAction(this);
        button.setText("Sin seleccionar");
        MenuItem remove = new MenuItem("Eliminar");
        remove.setDisable(true);
        button.getItems().add(remove);
        button.setMaxWidth(Double.MAX_VALUE);
        
        nodes[0] = label;
        nodes[1] = button;
        
        this.component = component;
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
            listener.setProductFor(component, this);
        }
    }
    
}