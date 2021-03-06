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
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.CategoryNames;
import model.PC;

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
    @FXML
    private TextField minPrice;
    @FXML
    private TextField maxPrice;
    @FXML
    private CheckBox inStock;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedProduct = null;

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        categoryColumn.setCellValueFactory(new Callback<CellDataFeatures<Product, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Product, String> p) {
                return new SimpleStringProperty(CategoryNames.getName(p.getValue().getCategory()));
            }
        });

        searchTextfield.textProperty().addListener(this);
        minPrice.textProperty().addListener(this);
        maxPrice.textProperty().addListener(this);

        //Sólo númeos.
        amountTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[0-9]*")) {
                    amountTextfield.setText(oldValue);
                }
            }
        });

        //Para que se seleccione el producto cuando se hace doble clic sobre la fila de la tabla.
        productTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    accept(null);
                }
            });
            return row;
        });

        inStock.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            search();
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
        if (selectedProduct != null) {
            if (selectedProduct.getStock() < getAmount()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setTitle("Aviso");
                alert.setContentText("No hay tantos productos disponibles.");
                alert.showAndWait();
            } else {
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
        search();
    }

    void search() {
        String search = searchTextfield.getText();
        String minPriceString = minPrice.getText();
        String maxPriceString = maxPrice.getText();

        double minValue = 0.f, maxValue = Double.MAX_VALUE;

        if (!minPriceString.isEmpty()) {
            try {
                minValue = Math.max(Double.parseDouble(minPriceString), 0.d);
                minPrice.setStyle("-fx-text-fill: black;");
            } catch (NumberFormatException e) {
                minPrice.setStyle("-fx-text-fill: red;");
            }
        }

        if (!maxPriceString.isEmpty()) {
            try {
                maxValue = Double.parseDouble(maxPriceString);
                maxPrice.setStyle("-fx-text-fill: black;");
            } catch (NumberFormatException e) {
                maxPrice.setStyle("-fx-text-fill: red;");
            }
        }

        List<Product> products = new ArrayList<>();
        for (Product.Category category : visibleCategories) {
            products.addAll(Database.getProductByCategoryDescriptionAndPrice(category, search, minValue, maxValue, inStock.isSelected()));
        }

        updateTable(products);
    }

}
