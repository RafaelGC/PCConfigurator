/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.ConfiguratorController.openProductSelectorWindow;
import es.upv.inf.Product;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import listeners.ConfiguratorRowListener;
import model.Component;
import model.ComponentDescription;
import model.PC;
import util.Pair;

/**
 * FXML Controller class
 *
 * @author Rafa
 */
public class StepByStepController implements Initializable, ConfiguratorRowListener {

    @FXML
    private Label componentName;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private VBox leftColumn;
    @FXML
    private Label whatIs;
    private PC pc;
    private List<Component> essentialComponents;
    private int currentComponentIndex = 0;
    private Stage stage;
    @FXML
    private VBox mainLayout;
    @FXML
    private Button nextButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainLayout.getStylesheets().add(getClass().getResource("/css/CustomButtons.css").toExternalForm());
        
    }

    public void init(PC pc, Stage stage) {
        this.stage = stage;
        this.pc = pc;
        this.essentialComponents = pc.getEssentialComponents();

        setup(0);
    }

    public void setup(int componentIndex) {
        this.currentComponentIndex = componentIndex;

        Component curr = essentialComponents.get(componentIndex);

        progressBar.setProgress((double) (currentComponentIndex + 1) / essentialComponents.size());
        componentName.setText(curr.getComponentDescription().getName());

        ConfiguratorRow row = new ConfiguratorRow(curr, this);

        row.getButton().getStyleClass().add("big-button");
        
        whatIs.setText(curr.getComponentDescription().getDescription());

        leftColumn.getChildren().clear();
        leftColumn.getChildren().add(row.getButton());
        
        updateNextButton();
        
    }

    @FXML
    private void back(ActionEvent event) {
        if (currentComponentIndex == 0) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
                Parent root = (Parent) loader.load();
                MainScreenController controller = loader.<MainScreenController>getController();
                controller.init(stage);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(StepByStepController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            setup(--currentComponentIndex);
        }
    }

    @FXML
    private void next(ActionEvent event) {
        setup(++currentComponentIndex);
    }

    @Override
    public void setProductFor(ComponentDescription component, ConfiguratorRow row) {
        Pair<Product, Integer> productAmount
                = openProductSelectorWindow(essentialComponents.get(currentComponentIndex).getComponentDescription().getCategories());
        if (productAmount.first != null) {

            pc.addProduct(productAmount.first, productAmount.second);
            row.update();
            System.out.println(pc.toString());
            
            updateNextButton();
            //updatePrice();
        }
    }

    @Override
    public void removeProductFor(ComponentDescription component, ConfiguratorRow row) {
        Component c = pc.getComponentByComponentDescription(essentialComponents.get(currentComponentIndex).getComponentDescription());
        c.setProduct(null);
        row.update();
        updateNextButton();
        //updatePrice();
    }
    
    public void updateNextButton() {
        if (essentialComponents.get(currentComponentIndex).hasProduct()) {
            nextButton.getStyleClass().add("primary");
        }
        else {
            nextButton.getStyleClass().removeAll("primary");
        }
    }

}
