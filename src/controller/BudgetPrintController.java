/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import model.Component;
import model.PC;
import model.Price;

/**
 * FXML Controller class
 *
 * @author Rafa
 */
public class BudgetPrintController implements Initializable {

    @FXML
    private Label budgetLabel;
    @FXML
    private GridPane productsLayout;
    @FXML
    private Font x4;
    @FXML
    private Font x1;
    @FXML
    private Font x2;
    @FXML
    private Label price;
    @FXML
    private Label tax;
    @FXML
    private Label totalPrice;
    @FXML
    private Insets x3;
    @FXML
    private Label date;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void init(PC pc) {
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

}
