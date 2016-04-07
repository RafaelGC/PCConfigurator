/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.NonEssentialComponentsChooserController;
import controller.StepByStepController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Rafa
 */
public class SceneTransition {

    public static <E> E showView(Stage stage, String file) {
        try {
            
            FXMLLoader loader = new FXMLLoader(SceneTransition.class.getResource(file));
            Parent root = (Parent) loader.load();
            E controller = loader.<E>getController();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            return controller;
        } catch (IOException ex) {
        }
        return null;
    }
}
