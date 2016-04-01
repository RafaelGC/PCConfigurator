/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import listeners.ConfiguratorRowListener;
import model.Component;
import model.PC;
import model.PCStructure;
import model.PCStructureComponent;
import view.ConfiguratorRow;

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
    private Button save;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentPC = new PC(PCStructure.instance());
    }

    public void initStage(Stage stage) {
        this.stage = stage;

        int rowCount = 0;
        PCStructure structure = PCStructure.instance();
        Iterator<PCStructureComponent> it = structure.getEssentialComponents().iterator();
        while (it.hasNext()) {
            ConfiguratorRow row = new ConfiguratorRow(it.next(), this);
            essentialComponentsLayout.addRow(rowCount, row.getNodes());
            rowCount++;
        }

        rowCount = 0;
        it = structure.getNonEssentialComponents().iterator();
        while (it.hasNext()) {
            ConfiguratorRow row = new ConfiguratorRow(it.next(), this);
            nonEssentialComponentsLayout.addRow(rowCount, row.getNodes());
            rowCount++;
        }

    }

    @Override
    public void setProductFor(PCStructureComponent structureComponent, ConfiguratorRow row) {

        Component component = openProductSelectorWindow(structureComponent.getCategories());
        if (component != null) {
            row.getButton().setText(component.toString());
            
            currentPC.addComponent(component);
            
            System.out.println(currentPC.toString());
            
        }

    }
    
    public static Component openProductSelectorWindow(List<Product.Category> categories) {
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
            
            return controller.getComponent();
            
        } catch (IOException ex) {
            Logger.getLogger(ConfiguratorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @FXML
    private void save(ActionEvent event) {
        if (currentPC.saveToFile("computer.txt")) {
            DialogController.open("Guardado", "La configuración ha sido guardada con éxito.", DialogController.DialogType.Ok);
        }
        else {
            DialogController.open("Error", "La configuración no ha podido ser guardada.", DialogController.DialogType.Error);
        }
    }

}
