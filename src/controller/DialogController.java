/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rafa
 */
public class DialogController implements Initializable, EventHandler<ActionEvent> {
    public enum DialogType {
        Ok, Error, Warning, Information
    };
    
    public enum Buttons {
        Ok,
        Yes_No
    }
    
    public enum Response {
        Ok, Yes, No, Cancel
    }
    
    @FXML
    private ImageView image;
    @FXML
    private Label messageLabel;
    @FXML
    private HBox buttonsLayout;
    Stage window;
    Response response;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        response = Response.Cancel;
    }
    
    public void setup(Stage window, String message, DialogType type, Buttons buttons) {
        this.window = window;
        this.messageLabel.setText(message);
        this.image.setImage(new Image("/pictures/" + getImageName(type) + ".png"));
        
        if (buttons == Buttons.Ok) {
            Button btn = new Button("Ok");
            btn.setUserData(Response.Ok);
            btn.setOnAction(this);
            buttonsLayout.getChildren().add(btn);
        }
        else if (buttons == Buttons.Yes_No) {
            Button btn = new Button("Sí");
            btn.setUserData(Response.Yes);
            btn.setOnAction(this);
            
            Button btn2 = new Button("No");
            btn2.setUserData(Response.No);
            btn2.setOnAction(this);
            
            buttonsLayout.getChildren().add(btn2);
            buttonsLayout.getChildren().add(btn);
        }

    }
    
    @Override
    public void handle(ActionEvent event) {
        response = (Response)((Node)event.getSource()).getUserData();
        window.close();
    }
    
    public Response getResponse() {
        return response;
    }
    
    public static Response open(String windowTitle, String message, DialogType type) {
        return open(windowTitle, message, type, Buttons.Ok);
    }
    
    public static Response open(String windowTitle, String message, DialogType type, Buttons buttons) {

        try {
            Stage window = new Stage();
            window.setTitle(windowTitle);

            FXMLLoader loader = new FXMLLoader(DialogController.class.getResource("/view/Dialog.fxml"));
            Parent root = (Parent) loader.load();
            DialogController controller = loader.<DialogController>getController();
            controller.setup(window, message, type, buttons);
            Scene scene = new Scene(root);

            window.setScene(scene);
            window.initModality(Modality.APPLICATION_MODAL);
            window.showAndWait();
            
            return controller.getResponse();
        } catch (IOException ex) {
            Logger.getLogger(DialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.Cancel;

    }

    private static String getImageName(DialogType type) {
        if (type == DialogType.Ok) {
            return "ok";
        }
        if (type == DialogType.Information) {
            return "information";
        }
        if (type == DialogType.Warning) {
            return "warning";
        }
        return "error";
    }

}
