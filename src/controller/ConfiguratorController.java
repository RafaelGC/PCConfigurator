/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.DialogController.Response;
import es.upv.inf.Product;
import java.io.File;
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
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import listeners.ConfiguratorRowListener;
import model.Component;
import model.PC;
import model.ComponentDescription;
import model.Price;
import util.Pair;

/**
 * FXML Controller class
 *
 * @author Rafa
 */
public class ConfiguratorController implements Initializable, ConfiguratorRowListener {

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void initStage(Stage stage) {
        initStage(stage, PC.load("computer.txt"));
    }

    public void initStage(Stage stage, PC pc) {
        this.currentPC = pc;
        this.stage = stage;

        int rowCount = 0;
        Iterator<Component> it = currentPC.getEssentialComponents().iterator();
        while (it.hasNext()) {
            ConfiguratorRow row = new ConfiguratorRow(it.next(), this);
            essentialComponentsLayout.addRow(rowCount, row.getNodes());
            rowCount++;
        }

        rowCount = 0;
        it = currentPC.getNonEssentialComponents().iterator();
        while (it.hasNext()) {
            ConfiguratorRow row = new ConfiguratorRow(it.next(), this);
            nonEssentialComponentsLayout.addRow(rowCount, row.getNodes());
            rowCount++;
        }

        updatePrice();

    }

    @Override
    public void setProductFor(ComponentDescription description, ConfiguratorRow row) {

        Pair<Product, Integer> productAmount = openProductSelectorWindow(description.getCategories());
        if (productAmount.first != null) {

            currentPC.addProduct(productAmount.first, productAmount.second);
            row.update();
            System.out.println(currentPC.toString());

            updatePrice();

        }

    }

    @Override
    public void removeProductFor(ComponentDescription description, ConfiguratorRow row) {
        Component c = currentPC.getComponentByComponentDescription(description);
        c.setProduct(null);
        row.update();
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

    @FXML
    private void save(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar ordenador");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Archivo XML (*.xml)", "*.xml"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            if (currentPC.saveToFile(file)) {
                DialogController.open("Guardado", "La configuración ha sido guardada con éxito.", DialogController.DialogType.Ok);
            } else {
                DialogController.open("Error", "La configuración no ha podido ser guardada.", DialogController.DialogType.Error);
            }
        }
    }

    @FXML
    private void next(ActionEvent event) {
        try {
            boolean open = true;

            if (!currentPC.isComplete()) {
                DialogController.Response res = DialogController.open(
                        "¿Seguro?",
                        "No has seleccionado todos los componentes esenciales. ¿Seguro que has terminado de configurar tu PC?",
                        DialogController.DialogType.Warning,
                        DialogController.Buttons.Yes_No);

                if (res != DialogController.Response.Yes) {

                    open = false;

                }
            }

            if (open) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Budget.fxml"));
                Parent root = (Parent) loader.load();
                BudgetController controller = loader.<BudgetController>getController();
                controller.init(currentPC, this.stage);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(ConfiguratorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        try {

            Response res = DialogController.open("¿Seguro?",
                    "Si abandonas el configurador perderás la configuración "
                    + "que no hayas guardado. ¿Quieres abandonar?", DialogController.DialogType.Warning, DialogController.Buttons.Yes_No);
            if (res == Response.Yes) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
                Parent root = (Parent) loader.load();
                MainScreenController controller = loader.<MainScreenController>getController();
                controller.init(stage);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(ConfiguratorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
