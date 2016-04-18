/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.ComponentButton.ComponentButton;
import controller.ComponentButton.SplitMenuComponentButton;
import es.upv.inf.Product;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Component;
import model.PC;
import model.ComponentDescription;
import model.Price;
import util.Pair;
import listeners.ComponentButtonListener;
import util.SceneTransition;

/**
 * FXML Controller class
 *
 * @author Rafa
 */
public class ConfiguratorController implements Initializable, ComponentButtonListener {

    Stage stage;
    @FXML
    private GridPane essentialComponentsLayout;
    @FXML
    private GridPane nonEssentialComponentsLayout;

    private PC currentPC;
    @FXML
    private Insets x1;
    @FXML
    private Font x2;
    @FXML
    private Label price;
    @FXML
    private TextField name;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentPC.setName(newValue);
            }

        });
    }

    public void initStage(Stage stage, PC pc) {
        this.currentPC = pc;
        this.stage = stage;

        int rowCount = 0;
        Iterator<Component> it = currentPC.getEssentialComponents().iterator();
        while (it.hasNext()) {
            Component c = it.next();
            SplitMenuComponentButton btn = new SplitMenuComponentButton(c, this);
            essentialComponentsLayout.addRow(rowCount, new Label(c.getComponentDescription().getName()),
                                                       btn);
            rowCount++;
        }

        rowCount = 0;
        it = currentPC.getNonEssentialComponents().iterator();
        while (it.hasNext()) {
            Component c = it.next();
            SplitMenuComponentButton btn = new SplitMenuComponentButton(c, this);
            nonEssentialComponentsLayout.addRow(rowCount, new Label(c.getComponentDescription().getName()),
                                                       btn);
            rowCount++;
        }

        name.setText(pc.getName());

        updatePrice();

    }

    @Override
    public void setProductFor(ComponentDescription description, ComponentButton btn) {

        Pair<Product, Integer> productAmount = 
                openProductSelectorWindow(description.getCategories());
        if (productAmount.first != null) {

            currentPC.addProduct(productAmount.first, productAmount.second);
            btn.update();
            System.out.println(currentPC.toString());

            updatePrice();

        }

    }

    @Override
    public void removeProductFor(ComponentDescription description, ComponentButton btn) {
        Component c = currentPC.getComponentByComponentDescription(description);
        c.setProduct(null);
        btn.update();
        updatePrice();
    }

    private void updatePrice() {
        price.setText(currentPC.getPrice().getTotalPrice() + "" + Price.SYMBOL);
    }

    public static Pair<Product, Integer> openProductSelectorWindow(List<Product.Category> categories) {
        try {
            Stage productSelectionWindow = new Stage();

            FXMLLoader loader = new FXMLLoader(ConfiguratorController.class.getResource("/view/ProductSelector.fxml"));
            Parent root = (Parent) loader.load();
            loader.<ProductSelectorController>getController().initStage(productSelectionWindow, categories);
            Scene scene = new Scene(root);

            productSelectionWindow.setScene(scene);
            productSelectionWindow.initModality(Modality.APPLICATION_MODAL);
            productSelectionWindow.showAndWait();

            ProductSelectorController controller = loader.<ProductSelectorController>getController();

            return new Pair(controller.getProduct(), controller.getAmount());

        } catch (IOException ex) {
            Logger.getLogger(ConfiguratorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void savePCDialog(PC pc, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar ordenador");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Archivo XML (*.xml)", "*.xml"));
        fileChooser.setInitialFileName(pc.getName());
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            if (pc.saveToFile(file)) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Guardado");
                alert.setContentText("La configuración ha sido guardada con éxito.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("La configuración no pudo ser guardada.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void save(ActionEvent event) {
        savePCDialog(currentPC, stage);
    }

    @FXML
    private void next(ActionEvent event) {
        boolean open = true;
        if (!currentPC.isComplete()) {
            
            ButtonType yes = new ButtonType("Sí", ButtonData.YES);
            ButtonType no = new ButtonType("No", ButtonData.NO);
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(yes, no);
            alert.setTitle("¿Seguro?");
            alert.setContentText("No has seleccionado todos los componentes esenciales. ¿Seguro que has terminado de configurar tu PC?");
            Optional<ButtonType> res = alert.showAndWait();
            if (res.isPresent()) {
                if (res.get() != yes) {
                    open = false;
                }
            }
            
        }
        if (open) {
            SceneTransition.<BudgetController>showView(stage, "/view/Budget.fxml").init(currentPC, stage);
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
            Parent root = (Parent) loader.load();
            MainScreenController controller = loader.<MainScreenController>getController();
            controller.init(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ConfiguratorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

}
