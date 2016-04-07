/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.ComponentButton.ComponentButton;
import controller.ComponentButton.SplitMenuComponentButton;
import static controller.ConfiguratorController.openProductSelectorWindow;
import es.upv.inf.Product;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Component;
import model.ComponentDescription;
import model.PC;
import util.Pair;
import util.SceneTransition;
import listeners.ComponentButtonListener;
import model.Price;

/**
 * FXML Controller class
 *
 * @author Rafa
 */
public class StepByStepController implements Initializable, ComponentButtonListener {

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
    @FXML
    private Label price;

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

        int i = 0;
        for (; i < essentialComponents.size() - 1; i++) {
            if (!essentialComponents.get(i).hasProduct()) {
                break;
            }
        }

        setup(i);
        updatePrice();
    }

    public void setup(int componentIndex) {
        this.currentComponentIndex = componentIndex;

        Component curr = essentialComponents.get(componentIndex);

        progressBar.setProgress((double) (currentComponentIndex + 1) / essentialComponents.size());
        componentName.setText(curr.getComponentDescription().getName());

        SplitMenuComponentButton btn = new SplitMenuComponentButton(curr, this);

        btn.getStyleClass().add("big-button");

        whatIs.setText(curr.getComponentDescription().getDescription());

        leftColumn.getChildren().clear();
        leftColumn.getChildren().add(btn);

        updateNextButton();

    }

    @FXML
    private void back(ActionEvent event) {
        if (currentComponentIndex == 0) {
            SceneTransition.<MainScreenController>showView(stage, "/view/MainScreen.fxml").init(stage);
        } else {
            setup(--currentComponentIndex);
        }
    }

    @FXML
    private void next(ActionEvent event) {
        if (currentComponentIndex == essentialComponents.size() - 1) {

            boolean open = true;
            if (!pc.isComplete()) {

                ButtonType yes = new ButtonType("Sí", ButtonBar.ButtonData.YES);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(yes, no);
                alert.setTitle("¿Seguro?");
                alert.setContentText("No has seleccionado todos los componentes esenciales. ¿Seguro que quieres continuar?");
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent()) {
                    if (res.get() != yes) {
                        open = false;
                    }
                }

            }
            if (open) {
                SceneTransition.<NonEssentialComponentsChooserController>showView(stage, "/view/NonEssentialComponentsChooser.fxml").init(pc, stage);
            }

            
        } else {
            setup(++currentComponentIndex);
        }
    }

    @Override
    public void setProductFor(ComponentDescription component, ComponentButton btn) {
        Pair<Product, Integer> productAmount
                = openProductSelectorWindow(essentialComponents.get(currentComponentIndex).getComponentDescription().getCategories());
        if (productAmount.first != null) {

            pc.addProduct(productAmount.first, productAmount.second);
            btn.update();
            System.out.println(pc.toString());

            updateNextButton();
            updatePrice();
        }
    }

    @Override
    public void removeProductFor(ComponentDescription component, ComponentButton btn) {
        Component c = pc.getComponentByComponentDescription(essentialComponents.get(currentComponentIndex).getComponentDescription());
        c.setProduct(null);
        btn.update();
        updateNextButton();
        updatePrice();
    }

    void updatePrice() {
        this.price.setText(pc.getPrice().getTotalPrice() + "" + Price.SYMBOL);
    }

    public void updateNextButton() {
        if (essentialComponents.get(currentComponentIndex).hasProduct()) {
            nextButton.getStyleClass().add("primary");
        } else {
            nextButton.getStyleClass().removeAll("primary");
        }
    }

}
