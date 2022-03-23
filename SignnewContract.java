package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.sql.SQLException;
import java.util.Optional;

public class SignnewContract {

    @FXML
    private HBox AccountBox;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnImage;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private ImageView imgView;

    @FXML
    private PasswordField psqTwo;

    @FXML
    private PasswordField pswOne;

    @FXML
    private RadioButton rdBusiness;

    @FXML
    private RadioButton rdOther;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtfirstname;

    @FXML
    private TextField txtlastname;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextArea txaAddress;

    String accountType=null;
    String imageName=null;
    Image img = null;
    Stage oldStage;

    public void saveBtnClicked() throws SQLException {
        ConnectDataBase con = new ConnectDataBase();
        con.connectDB();
        boolean duplicate = false;
        accountType = "other";
        //field validation
        if(txtfirstname.getText().equals("")) {txtfirstname.setPromptText("Field must be filled");}
        else if(txtlastname.getText().equals("")) {txtlastname.setPromptText("Field must be filled");}
        else if(txtEmail.getText().equals("")) {txtEmail.setPromptText("Field must be filled");}
        else if(txtUserName.getText().equals("")) {txtUserName.setPromptText("Field must be filled");}
        else if(pswOne.getText().equals("")) {pswOne.setPromptText("Field must be filled");}
        else if(psqTwo.getText().equals("")) {psqTwo.setPromptText("Field must be filled");}
        else if(txtPhone.getText().equals("")){txtPhone.setPromptText("Field must be filled");}
        else if(txtAddress.getText().equals("")){txtAddress.setPromptText("Field must be filled");}
        else if(txaAddress.getText().equals("")){txaAddress.setPromptText("Field must be filled");}
        else if(!pswOne.getText().equals(psqTwo.getText())){
            pswOne.setPromptText("miss matching password");
            psqTwo.setPromptText("try again");
        }
        else{
            //must returns false if this combination of both is found
            duplicate = con.saveUserAccount(txtUserName.getText(),psqTwo.getText());
            System.out.println(duplicate);//debugging
        }
        if(duplicate) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Create New Profile");
            alert.setHeaderText(txtUserName.getText());
            alert.setContentText("Confirm User");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                boolean saved = con.saveuserDetails(txtfirstname.getText(), txtlastname.getText(), txtEmail.getText(), txtPhone.getText(), accountType, txtAddress.getText(), txaAddress.getText(), pswOne.getText(), txtUserName.getText(), imageName);
                //save all the files obtained from here into the database
                //String fName, String lName, String email, String phone, String accountType, String address, String AdDescription, String password, String userName
            //-----------------------------------------------------
                FXMLLoader loginloader = new FXMLLoader(getClass().getResource("home.fxml"));
                Parent loginroot = null;
                try {
                    loginroot = (Parent) loginloader.load();
                    closeOldWindow();
                } catch (IOException e) {
                    e.printStackTrace();//will have to remove this eventually

                }

                Stage homeStage = new Stage();
                homeStage.setTitle("OddJ");
                Scene loginscene = new Scene(loginroot,650,500);
                homeStage.setScene(loginscene);
                homeStage.show();
            //======================================================

            }
        }
            else {
            txtUserName.setPromptText("please a different user name");
        }

    }
    public void resetButtonClicked(){
        txtUserName.setText("");
        txaAddress.setText("");
        txtAddress.setText("");
    }
    public void businessButtonClicked(){
        accountType="business";
        rdOther.setSelected(false);
    }
    public void otherButtonClicked(){
        accountType="other";
        rdBusiness.setSelected(false);
    }
    public void imageButtonClicked() throws FileNotFoundException {
        final JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        imageName = file.getAbsolutePath();
        //imageName = imageName.replace("\\","\\\\");
        InputStream stream = new FileInputStream(file);
        img = new Image(stream);
        imgView.setImage(img);
    }
    public void closeOldWindow(){
        oldStage = (Stage) btnSave.getScene().getWindow();
        oldStage.close();

}}
