package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Login {
    //if i made this the main class, then i could work. lets try it
    //------------------------------------
    Stage oldStage = null;

    //----------------------------------------

    //*---------------------------FXML Variables--------------------------------------------------------------------*//
    @FXML
    private Button btnSignIn;
    @FXML
    private Button btnSignUp;
    @FXML
    private TextField lblName;
    @FXML
    private TextField lblPassword;

    @FXML
    AnchorPane anchorPane;

    //*-----------------------------LogIn method--------------------------------------------------------------------*//

    public void loggingGood() throws SQLException {
        //open/make connection to the database
        ConnectDataBase con = new ConnectDataBase();
        con.connectDB();
        //returns true if the username and password are available in the database
        boolean open = con.loggingIn(lblName.getText(), lblPassword.getText());
        //for simplicity in production
        //boolean open = con.loggingIn("codemuno","@45");
        if (open) {
            /*open the home page....
             * close this window....
             * how do i achieve this at the same time. threads?? look this up
             * */

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
            con.makeLog();


        }

    }//--------------------------------------------------------------------------------------------
    public void closeOldWindow(){
        oldStage = (Stage) btnSignIn.getScene().getWindow();
        oldStage.close();
    }//----------------------------------------------------------------------------------------------
    public void temp(){
        System.out.println("will come back");
    }//-----------------------------------------------------------------------------------------------
    public void btnSignUpClicked() throws IOException {
        FXMLLoader loginloader = new FXMLLoader(getClass().getResource("signups.fxml"));
        Parent loginroot = null;
        try {
            loginroot = (Parent) loginloader.load();
            closeOldWindow();
            //new signups().letsBeging();
        } catch (IOException e) {
            e.printStackTrace();//will have to remove this eventually

        }

        Stage homeStage = new Stage();
        homeStage.setTitle("Sign Up");
        Scene loginscene = new Scene(loginroot,650,500);
        //loginscene.setUserAgentStylesheet(STYLESHEET_CASPIAN);
        homeStage.setScene(loginscene);
        homeStage.show();


    }


}
class Logclient implements Serializable{
    String userName;
    String password;
    boolean accountAvailable;

    Logclient(){
        userName = "";
        password = "";
        accountAvailable=false;
    }
}
class LogInClient{
    private ObjectInputStream serverObjectStreamReader;
    private ObjectOutputStream serverObjectStreamWriter;

    /**
     * Since we already have a working program, we are going to create a new class that will simply
     * take the values from the frame(scene) and send them to the server
     * it will have a method that has two parameters ie userName and passWord
     * we can comment out the original code and simply pass this method and the we are good to go
     *
     * */

    public Logclient connectClientToServer(Logclient logclient){
        Logclient outCome = null;
        /**
         * TODO 1.0: create a socket
         * TODO 1.1: prepare the streams
         * TODO 2: make a request to the server(send serialized object )
         * TODO 3: list for responce
         *
         * */
        try {
            Socket socket = new Socket("localhost", 4545);

            serverObjectStreamReader = new ObjectInputStream(socket.getInputStream());
            serverObjectStreamWriter = new ObjectOutputStream(socket.getOutputStream());

            serverObjectStreamWriter.writeObject(logclient);
             outCome = (Logclient) serverObjectStreamReader.readObject();


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return outCome;
    }
}
