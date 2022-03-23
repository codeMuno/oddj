package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reportchart {
    @FXML
    private BarChart<?, ?> aBarChart;
    @FXML
    private ScatterChart<String, Double> scatterChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis timeOFDay;
    @FXML
    private NumberAxis activityLevel;

    public void scatterChart(){
        String userName;
        int countJan, countFeb, countMatch;

        countJan =0;
        countFeb =0;
        countMatch =0;

        ConnectDataBase con = null;
        if(con==null){
            try {
                con = new ConnectDataBase();
                con.connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj","root","");
                con.statement = con.connection.createStatement();

                String selectQuery =  "SELECT * FROM `activity` ;";
                ResultSet st = con.statement.executeQuery(selectQuery);
                while(st.next()){
                    String date, time1;
                    date = st.getString("ActivityDate");
                    time1 = st.getString("ActivityStartTime");
                        if(date.substring(5,7).equalsIgnoreCase("01")){
                            countJan++;}
                        else if(date.substring(5,7).equalsIgnoreCase("02")){
                            countFeb++;
                        }
                        else if(date.substring(5,7).equalsIgnoreCase("03")){
                            countMatch++;
                        }


            XYChart.Series<String, Double>  seriesUser = new XYChart.Series<>();
            seriesUser.setName("User Activity");
        //get values form the database
        seriesUser.getData().add(new XYChart.Data<String, Double>("January", (double) countJan));
        seriesUser.getData().add(new XYChart.Data<String, Double>("February", (double) countFeb));
        seriesUser.getData().add(new XYChart.Data<String, Double>("Match", (double) countMatch));

        scatterChart.getData().add(seriesUser);

    }} catch (SQLException e) {
                e.printStackTrace();
            }
    //next thing is to create a stage that displays our charts

        }//end of first if
        //TODO: make image of created chart
        saveScatterAsPng();
    }//method ends
    public void buttonScatterClicked(){
        System.out.println("scatter");
    }
    public void buttonBackClicked() throws IOException {
        FXMLLoader loginloader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent loginroot = null;
        try {
            loginroot = (Parent) loginloader.load();
            Stage oldStage = (Stage) scatterChart.getScene().getWindow();
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
    public void buttonBarClicked() throws SQLException {
        int count00, count04, count08, count12, count16, count20;
        count00=0;
        count04=0;
        count08=0;
        count12=0;
        count16=0;
        count20=0;
        ConnectDataBase con = null;
        if(con==null){
            try {
                con = new ConnectDataBase();
                con.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj", "root", "");
                con.statement = con.connection.createStatement();

                String selectQuery = "SELECT * FROM `activity` ;";
                ResultSet st = con.statement.executeQuery(selectQuery);
                while (st.next()) {
                    String date, time1;
                    date = st.getString("ActivityDate");
                    time1 = st.getString("ActivityStartTime");

                    //check morning hours , noon, evening and night
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    try {
                        Date startime00_00_01 = simpleDateFormat.parse("00:00:01");
                        Date startime04_00_01 = simpleDateFormat.parse("04:00:01");
                        Date startime08_00_01 = simpleDateFormat.parse("08:00:01");
                        Date startime12_00_01 = simpleDateFormat.parse("12:00:01");
                        Date startime16_00_01 = simpleDateFormat.parse("16:00:01");
                        Date startime20_00_01 = simpleDateFormat.parse("20:00:01");

                        Date current_time = simpleDateFormat.parse(time1);
                        //========================================================================
                        if (current_time.after(startime00_00_01) && current_time.before(startime04_00_01)) {
                            count00++;
                        } else if (current_time.after(startime04_00_01) && current_time.before(startime08_00_01)) {
                            count04++;
                        } else if (current_time.after(startime08_00_01) && current_time.before(startime12_00_01)) {
                            count08++;
                        } else if (current_time.after(startime12_00_01) && current_time.before(startime16_00_01)) {
                            count12++;
                        } else if (current_time.after(startime16_00_01) && current_time.before(startime20_00_01)) {
                            count16++;
                        } else if (current_time.after(startime20_00_01) && current_time.before(startime00_00_01)) {
                            count20++;
                        }
                        //========================================================================

                        //========================================================================
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                //graph goes here


            }
            XYChart.Series seriesDay = new XYChart.Series();
            seriesDay.getData().add(new XYChart.Data<String, Double>("00:Hrs", (double) count00));
            seriesDay.getData().add(new XYChart.Data<String, Double>("04:Hrs", (double) count04) );
            seriesDay.getData().add(new XYChart.Data<String, Double>("08:Hrs", (double) count08) );
            seriesDay.getData().add(new XYChart.Data<String, Double>("12:Hrs", (double) count12) );
            seriesDay.getData().add(new XYChart.Data<String, Double>("16:Hrs", (double) count16) );
            seriesDay.getData().add(new XYChart.Data<String, Double>("20:Hrs", (double) count20) );
            aBarChart.getData().add(seriesDay);

        }
        saveScatterAsPng();
        saveBarAsPng();
    }
    public void saveBarAsPng() {
        WritableImage image = aBarChart.snapshot(new SnapshotParameters(), null);

        // TODO: use a file chooser here
        File file = new File("C:\\Users\\Muno\\Desktop\\javaWork\\scatter.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            // TODO: handle exception here
        }
    }
        public void saveScatterAsPng() {
            WritableImage image = scatterChart.snapshot(new SnapshotParameters(), null);

            // TODO: probably use a file chooser here
            File file = new File("C:\\Users\\Muno\\Desktop\\javaWork\\bar.png");

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                // TODO: handle exception here
            }
    }


}//class ends
