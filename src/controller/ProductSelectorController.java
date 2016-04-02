/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import es.upv.inf.Database;
import es.upv.inf.Product;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.CategoryNames;
import model.Component;

/**
 * FXML Controller class
 *
 * @author Rafa
 */
public class ProductSelectorController implements Initializable, EventHandler<WindowEvent>, ChangeListener<String> {

    @FXML
    private Insets x1;
    @FXML
    private TextField searchTextfield;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> descriptionColumn;
    @FXML
    private TableColumn<Product, String> priceColumn;
    @FXML
    private TableColumn<Product, String> stockColumn;
    @FXML
    private TableColumn<Product, String> categoryColumn;
    @FXML
    private TextField amountTextfield;

    Product selectedProduct;
    Stage stage;
    List<Product.Category> visibleCategories;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedProduct = null;

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("stock"));

        categoryColumn.setCellValueFactory(new Callback<CellDataFeatures<Product, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Product, String> p) {
                return new SimpleStringProperty(CategoryNames.getName(p.getValue().getCategory()));
            }
        });

        searchTextfield.textProperty().addListener(this);

        //Sólo númeos.
        amountTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches("\\d*")) {
                    int value = Integer.parseInt(newValue);
                } else {
                    amountTextfield.setText(oldValue);
                }
            }
        });
    }

    private void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(this);
    }

    public void initStage(Stage stage, List<Product.Category> categories) {
        setStage(stage);
        this.visibleCategories = categories;
        List<Product> products = new ArrayList<>();
        for (Product.Category category : categories) {
            products.addAll(Database.getProductByCategory(category));
        }

        updateTable(products);
    }

    private void updateTable(List<Product> products) {
        productTable.setItems(FXCollections.observableList(products));
    }

    public Product getProduct() {
        return selectedProduct;
    }
    
    public int getAmount() {
        return Integer.parseInt(amountTextfield.getText());
    }

    @FXML
    private void reject(ActionEvent event) {
        selectedProduct = null;
        stage.close();
    }

    @FXML
    private void accept(ActionEvent event) {

        selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct != null && !amountTextfield.getText().isEmpty()) {
            int amount = Integer.parseInt(amountTextfield.getText());
            if (amount > selectedProduct.getStock()) {
                DialogController.open("Aviso", "No tenemos tantos componentes en stock.", DialogController.DialogType.Warning);
            }
            else {
                stage.close();
            }
        }
        else {
            stage.close();
        }
    }

    @Override
    public void handle(WindowEvent event) {
        if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
            selectedProduct = null;
            stage.close();
        }
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        List<Product> products = new ArrayList<>();
        for (Product.Category category : visibleCategories) {
            products.addAll(Database.getProductByCategoryAndDescription(category, newValue, true));
        }

        updateTable(products);
    }

}
