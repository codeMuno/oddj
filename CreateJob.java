package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.sql.SQLException;

public class CreateJob {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnImage;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btncancel;

    @FXML
    private ImageView jobImage;

    @FXML
    private Pane paneOne;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtTttle;

    @FXML
    private ImageView userImage;

    /**
     * implement job buttons
     */
    String jobImageName, image;
    Image jobImageDisplay;
    ConnectDataBase con = new ConnectDataBase();

    public void btnSaveClicked() throws SQLException {
        if (txtDescription.getText().equals("")) {
            txtDescription.setPromptText("You cannot leave this one Blank");
        /*jobImage;
        btnImage;
        txtDescription;
        txtTttle; */
        } else if (txtTttle.getText().equals("")) {
            txtTttle.setPromptText("Please Enter Job Tittle");
            txtDescription.setPromptText("\t ^ no description provided \n\t |");
        }
        else if(image==null){
            txtDescription.setPromptText("^\n| image not set.");
        }
        else {
            //most of the cool work goes here
            System.out.println("Image has "+image);
            con.saveJob(txtTttle.getText(), txtDescription.getText(), image);
            btnResetClicked();

            //jobImage.setImage(null);
        }
    }

    public void btnResetClicked() {
        txtTttle.setText("");
        txtDescription.setText("");
        //how do i reset image field??
    }

    public void btnImageClicked() throws FileNotFoundException {
        final JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        image = file.getAbsolutePath();
        InputStream stream = new FileInputStream(file);
        jobImageDisplay = new Image(stream);
        jobImage.setImage(jobImageDisplay);
    }

    public void btnCancelClicked() throws SQLException {
        //System.out.println("implement click button");
        //should take us back home
        FXMLLoader loginloader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent loginroot = null;
        try {
            loginroot = (Parent) loginloader.load();
            Stage oldStage = (Stage) btncancel.getScene().getWindow();
            oldStage.close();
        } catch (IOException e) {
            e.printStackTrace();//will have to remove this eventually

        }

        Stage homeStage = new Stage();
        homeStage.setTitle("OddJ");
        Scene loginscene = new Scene(loginroot,650,500);
        homeStage.setScene(loginscene);
        homeStage.show();
        con.makeLog();



    }
}

