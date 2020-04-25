/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/**
 *
 * @author 44796
 */
public class LoginFXMain extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {   //used to start the Login Panel
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LoginFXML.fxml"));
        Parent root = loader.load();
        Image image = new Image("/images/Cordis-logo.png");
        stage.getIcons().add(image);
        stage.setTitle("Cordis Application");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
