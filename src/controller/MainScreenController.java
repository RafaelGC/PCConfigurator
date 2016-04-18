/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.PC;
import model.PCContainer;

/**
 * FXML Controller class
 *
 * @author Rafa
 */
public class MainScreenController implements Initializable {

    @FXML
    private Font x1;

    Stage stage;
    @FXML
    private VBox mainLayout;
    @FXML
    private VBox userConfigurationsLayout;
    @FXML
    private Button computer1Button;
    @FXML
    private Button computer2Button;
    @FXML
    private Button computer3Button;

    ArrayList<PC> preconfigured;
    @FXML
    private Label noPC;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainLayout.getStylesheets().add(getClass().getResource("/css/CustomButtons.css").toExternalForm());
        mainLayout.getStylesheets().add(getClass().getResource("/css/ScrollPane.css").toExternalForm());

        preconfigured = new ArrayList<>();
        preconfigured.add(PC.loadFromStream(getClass().getResourceAsStream("/preconfiguredpc/computer1.xml")));
        preconfigured.add(PC.loadFromStream(getClass().getResourceAsStream("/preconfiguredpc/computer2.xml")));
        preconfigured.add(PC.loadFromStream(getClass().getResourceAsStream("/preconfiguredpc/computer3.xml")));

    }

    public void init(Stage stage) {
        this.stage = stage;
        stage.setTitle("PCConfigurator");

        for (Iterator<PC> it = PCContainer.instance().iterator(); it.hasNext();) {
            addPCToUI(it.next());
        }

    }

    @FXML
    private void stepByStepConfigurator(ActionEvent event) {
        PC pc = new PC();
        pc.setAdvancedConfigurator(false);
        
        goToStepByStepConfigurator(pc);
    }

    private void goToStepByStepConfigurator(PC pc) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StepByStep.fxml"));
            Parent root = (Parent) loader.load();
            StepByStepController controller = loader.<StepByStepController>getController();

            controller.init(PCContainer.instance().addPC(pc), stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void advancedConfigurator(ActionEvent event) {
        goToAdvancedConfigurator(PCContainer.instance().addPC(new PC()));
    }

    private void goToAdvancedConfigurator(PC pc) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Configurator.fxml"));
            Parent root = (Parent) loader.load();
            ConfiguratorController controller = loader.<ConfiguratorController>getController();
            controller.initStage(stage, pc);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void goToBudget(PC pc) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Budget.fxml"));
            Parent root = (Parent) loader.load();
            BudgetController controller = loader.<BudgetController>getController();
            controller.init(pc, stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadConfiguration(ActionEvent event) {

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Selecciona la configuración");
        chooser.getExtensionFilters().add(new ExtensionFilter("Archivo XML (*.xml)", "*.xml"));
        File file = chooser.showOpenDialog(stage);
        if (file != null && file.canRead()) {

            PC pc = PC.loadFromFile(file);
            PCContainer.instance().addPC(pc);
            addPCToUI(pc);
        }

    }

    private void addPCToUI(PC pc) {
        noPC.setManaged(false);
        
        

        String name = pc.getName().isEmpty() ? "Sin nombre" : pc.getName();
        Button button = new Button(name);
        button.setUserData(pc);
        button.getStyleClass().add("big-button");
        button.setPrefWidth(Double.MAX_VALUE);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pc.isComplete()) {
                    goToBudget(pc);
                } else if (pc.isAdvancedConfigurator()) {
                    goToAdvancedConfigurator(pc);
                } else {
                    goToStepByStepConfigurator(pc);
                }
            }
        });
        userConfigurationsLayout.getChildren().add(button);
    }

    @FXML
    private void preconfiguredPC(ActionEvent event) {
        Object btn = event.getSource();
        if (btn == computer1Button) {
            goToBudget(PCContainer.instance().addPC(preconfigured.get(0)));
        } else if (btn == computer2Button) {
            goToBudget(PCContainer.instance().addPC(preconfigured.get(1)));
        } else if (btn == computer3Button) {
            goToBudget(PCContainer.instance().addPC(preconfigured.get(2)));
        }
    }

    @FXML
    private void about(ActionEvent event) {
        Alert aboutDialog = new Alert(Alert.AlertType.INFORMATION);
        aboutDialog.setHeaderText("PCConfigurator");
        aboutDialog.setTitle("PCConfigurator");
        aboutDialog.setContentText("Desarrollado por Rafael González Carrizo. Abril, 2016.");
        aboutDialog.showAndWait();
    }

}
