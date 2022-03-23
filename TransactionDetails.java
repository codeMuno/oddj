package sample;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class TransactionDetails {


    @FXML
    private TextArea txaDisplay;
    @FXML
    private TextField txtAmount;
    @FXML
    private TextField txtJob;
    @FXML
    private TextField txtReceipt;

    public void findClicked() throws SQLException {
        Connection connection;
        Statement statement;
        ResultSet rs=null;

        String Query = "SELECT * FROM oddj.job, oddj.userboss WHERE userboss.BUser_id = job.UserBoss_BUser_id;";
        //
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj", "root", "");
            statement = connection.createStatement();
            rs = statement.executeQuery(Query);
            System.out.println("made it to end of query");

        } catch (SQLException e) {
            //this is for developer mode will have to remove eventually...
            System.out.println("connection issue");
            e.printStackTrace();
        }
        if(rs!=null){
            int number = 0;
             rs.next();
                if(txtJob.getText().equalsIgnoreCase(rs.getString("JobName"))){
                    String details="Description: "+rs.getString("JobDescription")+"\nPosted by: "+rs.getString("BFName")+" "+rs.getString("BLName");
                    txaDisplay.setText(details);
            }
                else {
                    txtJob.setPromptText("Job not found");
                }
        }
        else {
            txtJob.setPromptText("Jobs not found");
        }
    }
    public void homebtnClicked(){
        FXMLLoader loginloader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent loginroot = null;
        try {
            loginroot = (Parent) loginloader.load();
           Stage oldStage = (Stage) txtJob.getScene().getWindow();
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
    public void receiptbtnClicked(){
        String DOC="Receipt.pdf";
        if(txtAmount.getText().equals("")){
            txtAmount.setPromptText("No money No Receipt");
        }else if(txtReceipt.getText().equals("")){
            txtReceipt.setPromptText("Without a number its very confusing");
        }else if(txaDisplay.getText().equals("")){
            txaDisplay.setText("Which job exactly are we looking at?");
        }else{

        Document document = new Document(PageSize.A5.rotate());
        try {
            PdfWriter.getInstance(document, new FileOutputStream(DOC));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();
        PdfPTable table = new PdfPTable(2);
//=================================================================================

        Phrase p = new Phrase("Name:");
        PdfPCell cell = new PdfPCell(p);
        table.addCell(cell);

         p = new Phrase(txtJob.getText());
         cell = new PdfPCell(p);
        table.addCell(cell);

        //=================================================
         p = new Phrase("Amount:");
         cell = new PdfPCell(p);
        table.addCell(cell);

        p = new Phrase(txtAmount.getText());
        cell = new PdfPCell(p);
        table.addCell(cell);

        //=================================================
        p = new Phrase("Receipt Number");
        cell = new PdfPCell(p);
        table.addCell(cell);

        p = new Phrase(txtReceipt.getText());
        cell = new PdfPCell(p);
        table.addCell(cell);

        //=================================================
        p = new Phrase("Details");
        cell = new PdfPCell(p);
        table.addCell(cell);

        p = new Phrase(txaDisplay.getText());
        cell = new PdfPCell(p);
        table.addCell(cell);

        //=================================================
        try {
            document.add(table);
        } catch (DocumentException ex) {
            //Logger.getLogger(pdf.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

        if(Desktop.isDesktopSupported()){
            try {
                File myFile = new File("Receipt.pdf");
                Desktop.getDesktop().open(myFile);
            }
            catch (IOException e){
                e.printStackTrace();
            }
            finally {
                document.close();
            }
        }


    }}
   /** public void confirmbtnClicked() {
        if(txtAmount.getText().equalsIgnoreCase("")){
            txtAmount.setPromptText("Must not be empty");
        }
        else if(txtAmount.getText().equalsIgnoreCase("")){
            txtAmount.setPromptText("Dont be cheap. You must pay");
        }
        else {
            try {
                double amount = Double.parseDouble(txtAmount.getText());
                Connection connection = null;
                Statement statement;
                String Query = "INSERT INTO `transaction` (`Transaction_id`, `TransactionAmount`, `TransactionStatus`, `Account_Account_id`, `UseEmpleyee_EUser_id`, `UserBoss_BUser_id`) VALUES (NULL, "+Double.parseDouble(txtAmount.getText())+", "+null+", 1, 1, 1);";
                try {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj", "root", "");
                    statement = connection.createStatement();
                    statement.executeUpdate(Query);
                    homebtnClicked();
                } catch (SQLException e) {
                    //this is for developer mode will have to remove eventually...
                    System.out.println("connection issue");
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                txtAmount.setPromptText("Not compatible");
            }
        }
    }//method ends here
    */
}
