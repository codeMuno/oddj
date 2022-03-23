package sample;

import javafx.animation.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.images.PrintingPdfClass;

//import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
//import sample.images.Job;

import java.io.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Home {
    //---------------------------------------------------------------------------------------------------------
    /*all the @fxml variables here*/
    @FXML
    private Button btnActivity;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnFind;
    @FXML
    private Button btnView;
    @FXML
    private BorderPane homeBorderpane;
    @FXML
    private ImageView userImage1;
    @FXML
    private AnchorPane mainRoot;
    @FXML
    private Label lblemail;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lbljobName;
    @FXML
    private TextArea jobDescription;
    //--------------------------------------------------------------
    ResultSet rs = null;
    boolean play = true;

    public Home(String filename) throws IOException {
        openFile(filename);
    }

    public Home() {
        /**
         *  The method movePanes() will have the files in these panes change every after 5 seconds
         * */
        movePanes();
    }

    public void movePanes() {
    }

    public void stopPlaying() {
        play = false;
    }

    public void startPlaying() {
        play = true;
    }

    public void openFile(String fileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        //Stage stage = (Stage) mainRoot.getScene().getWindow();
        Scene scene = mainRoot.getScene();
        root.translateYProperty().set(scene.getHeight());
        mainRoot.getChildren().remove(homeBorderpane);
        mainRoot.getChildren().add(root);

        Timeline time = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);
        time.getKeyFrames().add(keyFrame);
        time.play();
        //mainRoot.getChildren().remove(homeBorderpane);
    }

    //call the method above for different thing scenes
    public void openComments() throws IOException {//two of two
        openFile("comment.fxml");
    }

    public void openAccounts() throws IOException {//missing
        openFile("activity.fxml");
    }

    public void openActivity() throws IOException {//one of two
        openFile("activity.fxml");
    }

    public void openJobs() throws IOException {//one of two
        openFile("createJob.fxml");
    }

    public void openMessages() throws IOException {//one of two
        openFile("theBoss.fxml");
    }

    public void openRating() throws IOException {//missings
        openFile("comment.fxml");
    }

    public void openViews() throws IOException {//one of two
        openFile("profile.fxml");
    }

    public void openFlags() throws IOException {// one of two
        openFile("reportAbuse.fxml");
    }

    public void openDeals() throws IOException {//one of two
        openFile("makeOffer.fxml");
    }

    public void openEdit() throws IOException {//one of two
        openFile("editProfile.fxml");
    }

    public void openHome() throws IOException {
        openFile("home.fxml");
    }

    public void openTransaction() throws IOException {
        openFile("transactionDetails.fxml");
    }
    public void startChatting(){
        //this is the one for chatting
    }

    public void opencreateJob() throws IOException {
        //Parent root = FXMLLoader.load(getClass().getResource("createJob.fxml"));
        //openFile("createJob.fxml");
    }
    public void prevJob() throws IOException, SQLException {
            if (rs!=null){
            rs.previous();
            lblemail.setText(rs.getString("JobName"));
            lblUsername.setText(rs.getString("BFName") + " " + rs.getString("BLName"));
            jobDescription.setText(rs.getString("JobDescription"));
            lbljobName.setText(rs.getString("JobName"));}

    }

    public void nextJob() throws IOException, SQLException {
        btnFind.setVisible(true);
        if (rs!=null){
        rs.next();
        lblemail.setText(rs.getString("JobName"));
        lblUsername.setText(rs.getString("BFName") + " " + rs.getString("BLName"));
        jobDescription.setText(rs.getString("JobDescription"));
        lbljobName.setText(rs.getString("JobName"));

            System.out.println(rs.getString("Imageitem"));
            String string=rs.getString("Imageitem");
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<string.length(); i++) {
                char c = string.charAt(i);
                if(Character.isUpperCase(c)){
                    sb.append('/');
                }
                sb.append(c);
            }
            String newString = sb.toString();
            String complete = "file://"+newString;
            System.out.println(complete);
            //InputStream stream = new FileInputStream(complete);
            userImage1.setImage(new Image(complete));
        }

    }

    public void viewContents() throws SQLException, InterruptedException, IOException {
        //we need 3 images, 3 titles and 3 descriptions

        //String joinQuery = "SELECT * FROM `job` J, `image` I WHERE `Image_Image_id` = `Image_id`";
        //String joinQuery = "SELECT * FROM `job` J, `image` I, `userboss` U";
        String joinQuery= "SELECT * FROM Job, userboss, image WHERE job.UserBoss_BUser_id=userboss.BUser_id AND job.Image_Image_id=image.Image_id";
        //remember to do the user too. outside of here

        ResultSet personRs = null;
        ConnectDataBase con = new ConnectDataBase();
        if (con.connection == null) {
            try {
                con.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj", "root", "");
                con.statement = con.connection.createStatement();
                btnNext.setVisible(true);
            } catch (SQLException e) {
                //this is for developer mode will have to remove eventually...
                System.out.println("connection issue");
                e.printStackTrace();
            }
        }
        try {
            rs = con.statement.executeQuery(joinQuery);
        } catch (SQLException e) {
            e.printStackTrace();

        }

        if (rs.next()) {
            lblemail.setText(rs.getString("JobName"));
            lblUsername.setText(rs.getString("BFName") + " " + rs.getString("BLName"));
            jobDescription.setText(rs.getString("JobDescription"));
            lbljobName.setText(rs.getString("JobName"));
            //------image handling------------------------------

            System.out.println("===============================================================");
            System.out.println(rs.getString("Imageitem"));
                String string=rs.getString("Imageitem");
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<string.length(); i++) {
                char c = string.charAt(i);
                if(Character.isUpperCase(c)){
                    sb.append('/');
                }
                sb.append(c);
            }
            String newString = sb.toString();
            String complete = "file://"+newString;
            System.out.println(complete);
            //InputStream stream = new FileInputStream(complete);
            userImage1.setImage(new Image(complete));


        }

    }
    public void addressReport(){
        MakeASManyReports printOneReport = new MakeASManyReports();
        printOneReport.makeAddressReport();
    }
    public void userReport(){
        MakeASManyReports printOneReport = new MakeASManyReports();
        printOneReport.makeUserReport();
    }
    public void dailyReport(){
        MakeASManyReports printOneReport = new MakeASManyReports();
        printOneReport.makeScatterReport();
    }
    public void monthlyReport(){
        MakeASManyReports printOneReport = new MakeASManyReports();
        printOneReport.makeBarReport();
    }
    public void activityReport(){
        MakeASManyReports printOneReport = new MakeASManyReports();
        printOneReport.makeBarReport();
    }
    public void jobReport(){
        MakeASManyReports printOneReport = new MakeASManyReports();
        printOneReport.makeJobReport();
    }

}
class ClassThread implements Runnable{
    private Thread t;
    Label l1,l2, l3,l4;

    ClassThread(Label l1, Label l2, Label l3,Label l4) {
        this.l1 = l1;
        this.l2=l2;
        this.l3=l3;
        this.l4 =l4;

        t = new Thread(this, "Painter");
    }
    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

    }
}