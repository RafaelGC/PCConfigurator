/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Component;
import model.PC;
import model.Price;

/**
 * FXML Controller class
 *
 * @author Rafa
 */
public class BudgetController implements Initializable {

    @FXML
    private VBox productsLayout;
    @FXML
    private Font x1;
    @FXML
    private Font x2;
    @FXML
    private Insets x3;
    @FXML
    private VBox root;
    private PC pc;
    @FXML
    private Label price;
    @FXML
    private Label totalPrice;
    @FXML
    private Label tax;
    @FXML
    private Label date;
    Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void init(PC pc, Stage stage) {
        this.pc = pc;
        this.stage = stage;

        for (Iterator<Component> it = pc.iterator(); it.hasNext();) {
            Component c = it.next();
            if (c.hasProduct()) {
                productsLayout.getChildren().add(new Label(c.toString(true)));
            }
        }

        Price p = pc.getPrice();
        price.setText(String.valueOf(p.getPrice()) + Price.SYMBOL);
        tax.setText(String.valueOf(p.getTax()) + Price.SYMBOL + " (" + Price.TAX + "%)");
        totalPrice.setText(String.valueOf(p.getTotalPrice()) + Price.SYMBOL);

        date.setText((new SimpleDateFormat("dd/MM/yyyy")).format(new Date()));
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Configurator.fxml"));
            Parent root = (Parent) loader.load();
            ConfiguratorController controller = loader.<ConfiguratorController>getController();
            controller.initStage(stage, pc);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(BudgetController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void print(ActionEvent event) {
        Node node = root;
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean success = job.printPage(node);
            if (success) {
                job.endJob();
            }
        }

    }

    @FXML
    private void finish(ActionEvent event) {
    }

    @FXML
    private void save(ActionEvent event) {
    }

}
