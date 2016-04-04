/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
    private GridPane productsLayout;
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
    @FXML
    private Label budgetLabel;
    @FXML
    private Font x4;

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
        this.budgetLabel.setText(budgetLabel.getText() + " '" + pc.getName() + "'");
        int y = 1;
        for (Iterator<Component> it = pc.iterator(); it.hasNext();) {
            Component c = it.next();
            if (c.hasProduct()) {
                Label tmp = new Label(c.getProduct().getDescription());
                productsLayout.getChildren().add(tmp);
                GridPane.setConstraints(tmp, 0, y);

                tmp = new Label(c.getProduct().getPrice() + "" + Price.SYMBOL);
                productsLayout.getChildren().add(tmp);
                GridPane.setConstraints(tmp, 1, y);

                tmp = new Label("x" + c.getAmount());
                productsLayout.getChildren().add(tmp);
                GridPane.setConstraints(tmp, 2, y);

                tmp = new Label(c.getTotalPrice() + "" + Price.SYMBOL);
                productsLayout.getChildren().add(tmp);
                GridPane.setConstraints(tmp, 3, y);

                y++;
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BudgetPrint.fxml"));
            Parent node = (Parent) loader.load();
            loader.<BudgetPrintController>getController().init(pc);
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {
                PageLayout layout = job.getPrinter().createPageLayout(Paper.A4, PageOrientation.PORTRAIT, 0, 0, 0, 0);
                boolean success = job.printPage(layout, node);
                if (success) {
                    job.endJob();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(BudgetController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void finish(ActionEvent event) {
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

    @FXML
    private void save(ActionEvent event) {
        ConfiguratorController.savePCDialog(this.pc, stage);
    }

}
