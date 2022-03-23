package sample;

/**
 * @author codeMuno
 * @version 1.2.7
 * */
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import sample.images.PrintingPdfClass;

import java.io.IOException;
import java.sql.*;

public class Activity {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane homePane1;

    @FXML
    private Pane homepane2;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnLog;

    @FXML
    private Button btnReport;

    @FXML
    private TableView<Details> table;

    @FXML
    private TableColumn<Details, String> fileDate;

    @FXML
    private TableColumn<Details, String> fileTime;

    @FXML
    private TableColumn<Details, String> fileUserName;

    @FXML
    private TableColumn<Details, String> fileOutTime;


    private Stage oldStage;

    /**
     * method to put database values into table view
     * */
    public void homeBtnClicked() throws IOException {
        FXMLLoader loginloader = new FXMLLoader(getClass().getResource("home.fxml"));
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
        Scene loginscene = new Scene(loginroot, 650, 500);
        //loginscene.setUserAgentStylesheet(STYLESHEET_CASPIAN);
        homeStage.setScene(loginscene);
        homeStage.show();
    }

    public void closeOldWindow() {
        oldStage = (Stage) btnHome.getScene().getWindow();
        oldStage.close();
    }

    /**
     * method that get data from table activity and displays into a table view
     * when the view button is clicked
     * */
    public void viewButtonClicked() throws SQLException {
        ObservableList<Details> fullList = FXCollections.observableArrayList();

        fileDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Details, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Details, String> e) {
                return e.getValue().dateProperty();
            }
        });

        fileUserName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Details, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Details, String> e) {
                return e.getValue().nameProperty();
            }
        });

        fileTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Details, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Details, String> e) {
                return e.getValue().timeInProperty();
            }
        });

        fileOutTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Details, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Details, String> e) {
                return e.getValue().timeOutProperty();
            }
        });
        try {
            Connection connection=null;
            Statement statement = null;
            if(connection==null){
                connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj","root","");
                statement = connection.createStatement();
            }
            String selectQuery =  "SELECT * FROM `activity` ;";
            ResultSet st = statement.executeQuery(selectQuery);
            while(st.next()){
                String name , date, time1, time2;
                name = Integer.toString( st.getInt("logIn_logIn_id"));
                date = st.getString("ActivityDate");
                time1 = st.getString("ActivityStartTime");
                time2 = st.getString("ActivityEndTime");
                System.out.println(date+ " date "+ time1+" time");
                fullList.add(new Details(name, date, time1,time2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setItems(fullList);

    }//end of viewButtonClicked()---------------------------------------------------------------------------------------

    /**
     * method that creates pdf
     * TO DO
     *  open database
     *  get all the values
     *  printing them in a pdf file
     *  will use a while loop to populate the entire pdf.
     *  then i can call it a night
     *  good luck
     * */
    public void printPdfButtonClicked() throws Exception {
        //or you can simply call one method
        PrintingPdfClass printing = new PrintingPdfClass();
                printing.prinntActivity();

    }
    public void logoutButtonclicked() throws IOException {
       //=========================================================
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        //Stage stage = (Stage) mainRoot.getScene().getWindow();
        Scene scene = anchorPane.getScene();
        scene.setUserAgentStylesheet("activity.css");
        //root.translateYProperty().set(scene.getHeight());
        anchorPane.getChildren().remove(homePane1);
        anchorPane.getChildren().remove(homepane2);
        anchorPane.getChildren().add(root);

        Timeline time = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);
        time.getKeyFrames().add(keyFrame);
        time.play();

        //========================================================

    }
    public void chartsButtonClicked() throws IOException {
        //System.out.println("clicked");
        Parent root = FXMLLoader.load(getClass().getResource("reportchart.fxml"));
        //Stage stage = (Stage) mainRoot.getScene().getWindow();
        Scene scene = anchorPane.getScene();
        //scene.setUserAgentStylesheet("activity.css");
        //root.translateYProperty().set(scene.getHeight());
        anchorPane.getChildren().remove(homePane1);
        anchorPane.getChildren().remove(homepane2);
        anchorPane.getChildren().add(root);

        Timeline time = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);
        time.getKeyFrames().add(keyFrame);
        time.play();
        //new Reportchart();
    }

}//end of Activity class-----------------------------------------------------------------------------------------------

/**
 * class for using in the cell factory setting.
 **/
class Details{
    private SimpleStringProperty date;
    private SimpleStringProperty name;
    private SimpleStringProperty timeIn;
    private SimpleStringProperty timeOut;
    //getters and setters

    public String getTimeIn() {
        return timeIn.get();
    }

    public SimpleStringProperty timeInProperty() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn.set(timeIn);
    }

    public String getTimeOut() {
        return timeOut.get();
    }

    public SimpleStringProperty timeOutProperty() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut.set(timeOut);
    }

    public Details(String name, String date, String timeIn, String timeOut) {
        this.date = new SimpleStringProperty(date);
        this.name = new SimpleStringProperty(name);
        this.timeIn = new SimpleStringProperty(timeIn);
        this.timeOut = new SimpleStringProperty(timeOut);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}

