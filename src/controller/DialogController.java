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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rafa
 */
public class DialogController implements Initializable {
    @FXML
    private ImageView image;
    @FXML
    private Label messageLabel;

    public enum DialogType {Ok, Error, Warning};
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void setup(String message, DialogType open) {
        this.messageLabel.setText(message);
    }
    
    public static void open(String windowTitle, String message, DialogType type) {
        
        try {
            Stage window = new Stage();
            window.setTitle(windowTitle);
            
            FXMLLoader loader = new FXMLLoader(DialogController.class.getResource("/view/Dialog.fxml"));
            Parent root = (Parent) loader.load();
            loader.<DialogController>getController().setup(message, type);
            Scene scene = new Scene(root);
            
            window.setScene(scene);
            window.initModality(Modality.APPLICATION_MODAL);
            window.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(DialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
