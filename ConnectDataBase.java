package sample;
import com.mysql.cj.Query;
import com.mysql.cj.jdbc.exceptions.MySQLTimeoutException;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.*;

public class ConnectDataBase{
    Connection connection;
    Statement statement;
    public  ResultSet resultSet, activitySet;
    String selectAllQuery, deleteQuery, updateQuery, insertQuery, loginQuery, newUserQuery;
    int LoginFullID;
    int currentPerson;
    ConnectDataBase(){
        connection = null;
        statement = null;
        resultSet = null;


    }
    public void connectDB(){
    if(connection==null){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj","root","");
            statement = connection.createStatement();
        }
        catch (SQLException e){
            //this is for developer mode will have to remove eventually...
            System.out.println("connection issue");
            e.printStackTrace();
        }
    }

    }//end of connectDB()---------------------------------------------------------------------------------------
    public void setSelectAll(String tableName){
        if (statement!=null){
            selectAllQuery = "SELECT * FROM "+tableName+";";
            try{

                statement.executeUpdate(selectAllQuery);//missing swl statement. figure out the best to not repeat yourself
            }
            catch (SQLException e){//this is for dev mode...
                e.printStackTrace();
            }
        }
    }//end of select all------------------------------------------------------------------------------------------------
    public boolean saveUserAccount(String userName, String passWord){
        //returns true if the username and password can be retrieved from the database is found by the
        if (statement!=null){
            insertQuery = "INSERT INTO login (`logInPsw`, `logInUserName`) VALUES ('"+passWord+"', '"+userName+"');";
            try{
                statement.executeUpdate(insertQuery);//missing swl statement. figure out the best to not repeat yourself
            }
            catch (SQLException e){//this is for dev mode...
                e.printStackTrace();
            }
        }
        return loggingIn(userName, passWord);
    }//end of saveUserAccount
    public boolean loggingIn(String userName, String passWord){
        boolean userAvailable = false;
        int count = 0;
        String name = null;
        String psw = null;
        int id = 0;
        loginQuery = "SELECT * FROM `login` WHERE logInUserName ='"+userName+"' and logInPsw = '"+passWord+"';";
        if(statement!=null){
            try{
                resultSet = statement.executeQuery(loginQuery);
                while (resultSet.next()){
                   if(resultSet.getString("logInUserName").equals(userName)&(resultSet.getString("logInPsw").equals(passWord))){
                         userAvailable =true;
                         count++;
                     }
                    name = resultSet.getString("logInUserName");
                    psw = resultSet.getString("logInPsw");
                    LoginFullID = resultSet.getInt("logIn_id");
                    currentPerson = resultSet.getInt("logIn_id");
                }
            }
            catch (SQLException e){
                e.printStackTrace(); //for debugging only. will need to remove this eventually
            }
        }
        //System.out.println(name+ " "+ psw);
        return userAvailable;
    }
    /*
    * method that closes window
    * will be used before opening a new window.
    * */
    public void closeWindow(Stage window){
        window.close();
    }

    public boolean saveuserDetails(String fName, String lName, String email, String phone, String accountType, String address, String AdDescription, String password, String userName, String imageName) throws SQLException {
        //put in all the respective tables first.
        String tableName;
        if(accountType.equals("business")){
            tableName = "UserBoss";
        }
        else
        {
            tableName = "UserBoss";
        }
        int ContactID = createContact(phone);//working
        System.out.println("contact "+ContactID);// working
        int EmailID = createEmail(email);
        System.out.println("email "+EmailID);//working
        int ImageId = createImage("imageName", imageName); //why wont you work?
        System.out.println("contact "+ImageId);
        int AddressID = createAddress(address,AdDescription);//working
        System.out.println("contact "+AddressID);
        int LoginID = createLogin(userName, password);//this one works
        System.out.println("contact "+LoginID);
        if(ContactID!=0 & EmailID !=0 ){
            System.out.println("this is the table we are inserting into: "+tableName);
            //INSERT INTO `userboss` (`BUser_id`, `BFName`, `BLName`, `Email_Email_id`, `Contact_Contact_id`, `Address_Address_id`, `logIn_logIn_id`, `Image_Image_id`) VALUES (NULL, 'matsiko', 'bruno', '1', '1', '1', '1', '1');
            String tempQuery = "INSERT INTO `"+tableName+"` (`BUser_id`, `BFName`, `BLName`, `Email_Email_id`, `Contact_Contact_id`, `Address_Address_id`, `logIn_logIn_id`, `Image_Image_id`) VALUES (NULL, '"+fName+"', '"+lName+"', '"+EmailID+"', '"+ContactID+"', '"+AddressID+"', '"+LoginID+"', '"+ImageId+"');";
            statement.executeUpdate(tempQuery);
        }
        else {
            System.out.println("sorry friend database not working yet");//remember to have this part inform the user of the faults
        }

        newUserQuery = "insert ";

        boolean success = false;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    return success;}

    private int createLogin(String userName, String password) throws SQLException {
        int loginID = 0;
        //loginQuery = "SELECT * FROM `login` WHERE logInUserName ='"+userName+"' and logInPsw = '"+passWord+"';";
        String tempQuery = "INSERT INTO `login` (`logIn_id`, `logInPsw`, `logInUserName`) VALUES (NULL, '"+userName+"', '"+password+"');";
        statement.executeUpdate(tempQuery);
        //"SELECT * FROM `login` WHERE logInUserName ='"+userName+"' and logInPsw = '"+passWord+"';";
        String anOtherQuery = "SELECT * FROM `login` WHERE logInUserName ='"+userName+"' and logInPsw = '"+password+"';";
        ResultSet savedfile = statement.executeQuery(anOtherQuery);
        while(savedfile.next()){
            loginID = savedfile.getInt("logIn_id");}
        return loginID;
    }

    private int createImage(String s, String t) throws SQLException {
        if(connection==null){
            try{
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj","root","");
                statement = connection.createStatement();
            }
            catch (SQLException e){
                //this is for developer mode will have to remove eventually...
                System.out.println("connection issue");
                e.printStackTrace();
            } }
        //-------------------------------------------------------------------------------------------------------------------------------------
        //problem of the day...
        int imageID= 0;
        //INSERT INTO `image` (`Image_id`, `Imagename`, `Imageitem`) VALUES (NULL, 'muno', 'muno');
        System.out.println("right before inserting image");
        System.out.println(s + t);
        //String tempQuery = "INSERT INTO `image` (`Image_id`, `Imagename`, `Imageitem`) VALUES (NULL, '"+s+"' null);";
        String tempQuery = "INSERT INTO `image` (`Image_id`, `Imagename`, `Imageitem`) VALUES (NULL, '"+s+"', '"+t+"');";
        statement.executeUpdate(tempQuery);
        System.out.println("this is after inserting image");
        //"SELECT * FROM `login` WHERE logInUserName ='"+userName+"' and logInPsw = '"+passWord+"';";
        String anOtherQuery = "SELECT * FROM `image` WHERE `Imagename` = '"+s+"'; ";
        ResultSet savedfile = statement.executeQuery(anOtherQuery);
        while(savedfile.next()){
            imageID = savedfile.getInt("Image_id");}
        return imageID;
    }

    private int createEmail(String email) throws SQLException {
        int falseEmail = 0;
        String tempQuery = "INSERT INTO `email` (`Email_id`, `EmailAddress`) VALUES (NULL, '"+email+"');";
        System.out.println("we made it to email");
        statement.executeUpdate(tempQuery);
        System.out.println("we executed email query");
        //"SELECT * FROM `login` WHERE logInUserName ='"+userName+"' and logInPsw = '"+passWord+"';";
        String anOtherQuery = "SELECT * FROM `email` WHERE `EmailAddress` = '"+email+"'; ";
        ResultSet savedfile = statement.executeQuery(anOtherQuery);
        while(savedfile.next()){
            falseEmail = savedfile.getInt("Email_id");}

        return falseEmail;
    }

    public int createContact(String contact) throws SQLException {
        int contactID = 0;
        String tempQuery = "INSERT INTO `contact` (`Contact_id`, `ContactPhone`) VALUES (NULL, '"+contact+"');";
        statement.executeUpdate(tempQuery);
        //"SELECT * FROM `login` WHERE logInUserName ='"+userName+"' and logInPsw = '"+passWord+"';";
        String anOtherQuery = "SELECT * FROM `contact` WHERE ContactPhone = '"+contact+"'; ";
        ResultSet savedfile = statement.executeQuery(anOtherQuery);
        while(savedfile.next()){
        contactID = savedfile.getInt("Contact_id");}
        return contactID;
    }
    public int createAddress(String address, String desc) throws SQLException {
        int addressID = 0;
        String tempQuery = "INSERT INTO `address` (`Address_id`, `AddressName`, `AddressDescription`) VALUES (NULL, '"+address+"', '"+desc+"');";
        //System.out.println("we made to address");
        statement.executeUpdate(tempQuery);
        //System.out.println("we made after address");
        //"SELECT * FROM `login` WHERE logInUserName ='"+userName+"' and logInPsw = '"+passWord+"';";
        String anOtherQuery = "SELECT * FROM `address` WHERE AddressName = '"+address+"'; ";
        ResultSet savedfile = statement.executeQuery(anOtherQuery);
        while(savedfile.next()){
            addressID = savedfile.getInt("Address_id");}
        //System.out.println("Address"+addressID);
        return addressID;
    }

    public void makeLog() throws SQLException {
        if(connection==null|statement==null){
            connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj","root","");
            statement = connection.createStatement();
        }
        //get current date
        java.util.Date date=new java.util.Date();
        java.sql.Date sqlDate=new java.sql.Date(date.getTime());
        //get current time
        java.sql.Timestamp sqlTime=new java.sql.Timestamp(date.getTime());
        String aquery = "INSERT INTO `activity` (`Activity_id`, `ActivityDate`, `ActivityStartTime`, `ActivityEndTime`, `logIn_logIn_id`) VALUES (NULL, '"+sqlDate +"', '"+sqlTime +"', '"+sqlTime+"', '"+LoginFullID+"');";
        try {
            statement.executeUpdate(aquery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void makeLogOutLog(){
        java.util.Date date=new java.util.Date();
        java.sql.Date sqlDate=new java.sql.Date(date.getTime());
        //get current time
        java.sql.Timestamp sqlTime=new java.sql.Timestamp(date.getTime());
        String aquery = "UPDATE `activity` SET `ActivityEndTime` = '17:00:35' WHERE `activity`.`logIn_logIn_id` = `"+LoginFullID+"` ;";
        try {
            statement.executeUpdate(aquery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean getAllDates() {
        boolean rst = false;
        try {
            if(connection==null){
                connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj","root","");
                statement = connection.createStatement();
            }
            String selectQuery =  "SELECT * FROM `activity` ;";
            activitySet = statement.executeQuery(selectQuery);
            while(activitySet.next()){
                rst = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rst;
    }

    public void saveJob(String tittle, String description, String jobImageName) throws SQLException {
        //insert into job table
        //*******************************************************************************************
        int currentUser = getUserId();
        System.out.println("what we have here\n "+jobImageName);
        int ImageId = createImage("Job Image",jobImageName);
        String jobQuery = "INSERT INTO `job` (`Job_id`, `JobName`, `JobDescription`, `UserBoss_BUser_id`, `Image_Image_id`) VALUES (NULL, '"+tittle+"', '"+description+"', '"+currentUser+"','"+ImageId+"');";
        statement.executeUpdate(jobQuery);
    }

    private int getUserId() {
        int id=1;
        try {
            if(connection==null){
                connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj","root","");
                statement = connection.createStatement();
            }
            String selectQuery =  "SELECT * FROM `activity`, userboss WHERE activity.logIn_logIn_id = userboss.logIn_logIn_id";
            ResultSet userSet = statement.executeQuery(selectQuery);
            userSet.last();
            userSet.getInt("BUser_id");



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}




class OpenFiles{
    File file;
    OpenFiles(){
        file = null;
    }

}


