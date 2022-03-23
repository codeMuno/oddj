package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Comment {
    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnhome;

    @FXML
    private Button btnreset;

    public void home(){
        FXMLLoader loginloader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent loginroot = null;
        try {
            loginroot = (Parent) loginloader.load();
            Stage oldStage = (Stage) btnhome.getScene().getWindow();
            oldStage.close();
        } catch (IOException e) {
            e.printStackTrace();//will have to remove this eventually

        }

        Stage homeStage = new Stage();
        homeStage.setTitle("OddJ");
        Scene loginscene = new Scene(loginroot,650,500);
        homeStage.setScene(loginscene);
        homeStage.show();
    }
}
