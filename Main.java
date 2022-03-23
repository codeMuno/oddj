package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Main extends Application {
    Stage globalStage;
    @FXML
    private TextField lblPassword;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Execution starts from here
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("LogIn");
        primaryStage.setScene(new Scene(root, 650,500));
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);

    }


}
