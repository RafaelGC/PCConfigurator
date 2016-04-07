/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.ComponentButton.ComponentButton;
import controller.ComponentButton.DoubleComponentButton;
import static controller.ConfiguratorController.openProductSelectorWindow;
import es.upv.inf.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import listeners.ComponentButtonListener;
import model.Component;
import model.ComponentDescription;
import model.PC;
import model.Price;
import util.Pair;
import util.SceneTransition;

/**
 * FXML Controller class
 *
 * @author Rafa
 */
public class NonEssentialComponentsChooserController implements Initializable, ComponentButtonListener {

    @FXML
    private GridPane gridLayout;
    private Stage stage;
    private PC pc;
    private List<Component> nonEssentialComponents;
    @FXML
    private Button back;
    @FXML
    private Label price;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void init(PC pc, Stage stage) {
        this.pc = pc;
        this.stage = stage;
        nonEssentialComponents = pc.getNonEssentialComponents();
        int y = 0;
        int x = 0;
        Image img = new Image("/pictures/tick.png");
        for (Iterator<Component> it = nonEssentialComponents.iterator(); it.hasNext();) {

            DoubleComponentButton btn = DoubleComponentButton.createButton(it.next(), this, img);

            gridLayout.getChildren().add(btn);
            GridPane.setConstraints(btn, x, y);
            if (++x > 2) {
                x = 0;
                ++y;
            }
        }
        
        updatePrice();
    }

    @FXML
    private void back(ActionEvent event) {
        SceneTransition.<StepByStepController>showView(stage, "/view/StepByStep.fxml").init(pc, stage);
    }

    @FXML
    private void finish(ActionEvent event) {
        SceneTransition.<BudgetController>showView(stage, "/view/Budget.fxml").init(pc, stage);
    }

    @Override
    public void setProductFor(ComponentDescription component, ComponentButton btn) {
        Pair<Product, Integer> productAmount
                = openProductSelectorWindow(component.getCategories());
        if (productAmount.first != null) {

            pc.addProduct(productAmount.first, productAmount.second);
            btn.update();
            updatePrice();

        }
    }

    @Override
    public void removeProductFor(ComponentDescription component, ComponentButton btn) {
        Component c = pc.getComponentByComponentDescription(component);
        c.setProduct(null);
        btn.update();
        updatePrice();
    }
    
    public void updatePrice() {
        this.price.setText(pc.getPrice().getTotalPrice() + "" + Price.SYMBOL);
    }

}
