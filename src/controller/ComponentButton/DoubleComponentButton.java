/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.ComponentButton;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Component;
import listeners.ComponentButtonListener;

/**
 *
 * @author Rafa
 */
public class DoubleComponentButton extends Button implements ComponentButton, EventHandler<ActionEvent> {
    private Button remove;
    private ComponentButtonListener listener;
    private Component component;
    private Image img;
    private ImageView imageView;
    private DoubleComponentButton(Node node, Button remove, Component component, ComponentButtonListener listener, Image img, ImageView imageView) {
        super("", node);
        this.component = component;
        this.listener = listener;
        this.img = img;
        this.imageView = imageView;
        this.remove = remove;
        
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
        
        this.setOnAction(this);
        remove.setOnAction(this);
        
        update();
    }

    @Override
    public void update() {
        if (component.hasProduct()) {
            imageView.setImage(img);
            remove.setDisable(false);
        }
        else {
            imageView.setImage(null);
            remove.setDisable(true);
        }
    }
    
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == remove){
            listener.removeProductFor(component.getComponentDescription(), this);
            event.consume();
        }
        else if (event.getSource() == this) {
            listener.setProductFor(component.getComponentDescription(), this);
        }
    }

    public static DoubleComponentButton createButton(Component component, ComponentButtonListener listener, Image img) {
        VBox btnLayout = new VBox();

        HBox topLayout = new HBox();

        Label name = new Label(component.getComponentDescription().getName());
        name.setFont(Font.font(18));
        topLayout.getChildren().add(name);

        HBox midLayout = new HBox();
        midLayout.setAlignment(Pos.CENTER);
        ImageView image = new ImageView();
        image.setPreserveRatio(true);
        image.setSmooth(true);
        image.setFitWidth(50);
        midLayout.getChildren().add(image);
        VBox.setVgrow(midLayout, Priority.ALWAYS);

        HBox botLayout = new HBox();
        botLayout.setPrefHeight(HBox.USE_COMPUTED_SIZE);
        botLayout.setAlignment(Pos.TOP_RIGHT);
        
        Button b = new Button("Eliminar");
        
        botLayout.getChildren().add(b);

        btnLayout.getChildren().add(topLayout);
        btnLayout.getChildren().add(midLayout);
        btnLayout.getChildren().add(botLayout);

        return new DoubleComponentButton(btnLayout, b, component, listener, img, image);
    }

    
}
