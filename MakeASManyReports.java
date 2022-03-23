package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class MakeASManyReports {
        Connection connection = null;
        Statement statement;
        ResultSet resultSet;
        Document doc=null;


    /**
     * @return null
     * method that prints all the users in the database.
     * TODO: get all users, count how many they are, make a report. easy enough. hopefully
     * */
    public void makeJobReport(){
        try{
            final String DOCUMENTNAMEACTIVITY = "Job.pdf";
            Document doc = new Document(PageSize.A4);

            PdfWriter.getInstance(doc, new FileOutputStream(DOCUMENTNAMEACTIVITY));
            doc.open();

            PdfPTable jobTable = new PdfPTable(2);

            //TODO:  create a phrases for populating the heading address table
            Phrase jobName = new Phrase("Name");
            Phrase jobDescription = new Phrase("Description");

            //TODO: add them
            jobTable.addCell(new PdfPCell(jobName));
            jobTable.addCell(new PdfPCell(jobDescription));


            //TODO: open database and collect all the available records in activity table and user table
            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj", "root", "");
                statement = connection.createStatement();

                String selectQuery = "SELECT * FROM `job` ORDER BY `UserBoss_BUser_id` ASC;";
                resultSet = statement.executeQuery(selectQuery);
            }
            //TODO: get all the values from the activity ResultSet and populate them into the table
            //TODO: get the total count
            int count=0;
            while(resultSet.next()){
                Phrase jobNm = new Phrase(resultSet.getString("JobName"));
                Phrase jobDn = new Phrase(resultSet.getString("JobDescription"));

                jobTable.addCell(new PdfPCell(jobNm));
                jobTable.addCell( new PdfPCell(jobDn));
                count++;
            }

            //TODO: generate an actual report for this particular table;

            Font headingFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD,BaseColor.GREEN);
            Font logoFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLUE);
            Font paragraphFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

            Paragraph headingParagraph = new Paragraph("THE ODD JOB", headingFont);
            headingParagraph.setAlignment(2);
            doc.add(headingParagraph);
            Paragraph metaData = new Paragraph("make some quick cash", headingFont);
            metaData.setAlignment(2);
            doc.add(metaData);

            doc.add(jobTable);


            Paragraph subheadingParagraph = new Paragraph("LIST OF ALL THE AVAILABLE JOBS \n\n", logoFont);
            subheadingParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(subheadingParagraph);

            doc.add(jobDescription);

            String lastStatement = "We have had a total of: "+count+" jobs.";
            Paragraph last = new Paragraph(lastStatement);
            doc.add(last);

            String signingOut = "prepared by:\nMATSIKO BRUNO \n codemuno";
            Paragraph signature = new Paragraph(signingOut);
            signature.setAlignment(Paragraph.ALIGN_RIGHT);
            doc.add(signature);

            doc.close();

            if(Desktop.isDesktopSupported()){
                File activityFile = new File("Job.pdf");
                Desktop.getDesktop().open(activityFile);
            }
        }
        catch (SQLException | FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(doc != null){
                doc.close();}
        }
    }

        public void makeUserReport(){
            try{
                final String DOCUMENTNAMEUSERS = "Users.pdf";
                Document doc = new Document(PageSize.A4);

                PdfWriter.getInstance(doc, new FileOutputStream(DOCUMENTNAMEUSERS));
                doc.open();

                PdfPTable addressTable = new PdfPTable(2);

                //TODO:  create a phrases for populating the heading of users table
                Phrase addressName = new Phrase("First Name");
                Phrase addressDescription = new Phrase("Last Name");

                //TODO: open database and collect all the available records in ascending order
                if (connection == null) {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj", "root", "");
                    statement = connection.createStatement();

                    String selectQuery = "SELECT * FROM `userboss` ORDER BY `userboss`.`BFName` ASC;";
                    resultSet = statement.executeQuery(selectQuery);
                }
                //TODO: get all the values from the users resultSet and populate them into the table
                //TODO: get the total count
                int count=0;
                while(resultSet.next()){
                    Phrase name = new Phrase(resultSet.getString("BFName"));
                    Phrase description = new Phrase(resultSet.getString("BLName"));

                    addressTable.addCell(new PdfPCell(name));
                    addressTable.addCell( new PdfPCell(description));
                    count++;
                }

                //TODO: generate an actual report for this particular table;

                Font headingFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD,BaseColor.GREEN);
                Font logoFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLUE);
                Font paragraphFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

                Paragraph headingParagraph = new Paragraph("THE ODD JOB", headingFont);
                headingParagraph.setAlignment(2);
                doc.add(headingParagraph);
                Paragraph metaData = new Paragraph("make some quick cash", headingFont);
                metaData.setAlignment(2);
                doc.add(metaData);


                Paragraph subheadingParagraph = new Paragraph("LIST OF ALL THE PEOPLE EARNING FROM THE ODD JOB\n\n", logoFont);
                subheadingParagraph.setAlignment(Paragraph.ALIGN_CENTER);
                doc.add(subheadingParagraph);

                doc.add(addressTable);

                String lastStatement = "We have a total of: "+count+" active Users.";
                Paragraph last = new Paragraph(lastStatement);
                doc.add(last);

                String signingOut = "prepared by:\nMATSIKO BRUNO \n codemuno";
                Paragraph signature = new Paragraph(signingOut);
                signature.setAlignment(Paragraph.ALIGN_RIGHT);
                doc.add(signature);

                doc.close();

                if(Desktop.isDesktopSupported()){
                    File activityFile = new File("Users.pdf");
                    Desktop.getDesktop().open(activityFile);



                }





            }
            catch (SQLException | FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(doc != null){
                    doc.close();}
            }
        }
        /**
         * @return null
         * method that prints all the users in the database.
         * TODO: get all users, count how many they are, make a report. easy enough. hopefully
         * */
        public void makeAddressReport(){
            try{
                final String DOCUMENTNAMEADDRESSES = "Addresses.pdf";
                Document doc = new Document(PageSize.A4);

                PdfWriter.getInstance(doc, new FileOutputStream(DOCUMENTNAMEADDRESSES));
                doc.open();

                PdfPTable addressTable = new PdfPTable(2);

                //TODO:  create a phrases for populating the heading address table
                Phrase addressName = new Phrase("Name");
                Phrase addressDescription = new Phrase("Description");

                //TODO: open database and collect all the available records in ascending order
                if (connection == null) {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj", "root", "");
                    statement = connection.createStatement();

                    String selectQuery = "SELECT * FROM `address` ORDER BY `AddressName` ASC;";
                    resultSet = statement.executeQuery(selectQuery);
                }
                //TODO: get all the values form the activity set and populate them into the table
                //TODO: get the total count
                int count=0;
                while(resultSet.next()){
                    Phrase name = new Phrase(resultSet.getString("AddressName"));
                    Phrase description = new Phrase(resultSet.getString("AddressDescription"));

                    addressTable.addCell(new PdfPCell(name));
                    addressTable.addCell( new PdfPCell(description));
                    count++;
                }

                //TODO: generate an actual report for this particular table;

                Font headingFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD,BaseColor.GREEN);
                Font logoFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLUE);
                Font paragraphFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

                Paragraph headingParagraph = new Paragraph("THE ODD JOB", headingFont);
                headingParagraph.setAlignment(2);
                doc.add(headingParagraph);
                Paragraph metaData = new Paragraph("make some quick cash", headingFont);
                metaData.setAlignment(2);
                doc.add(metaData);


                Paragraph subheadingParagraph = new Paragraph("LIST OF ALL THE LOCATIONS AFFECTED BY THE ODD JOB\n\n", logoFont);
                subheadingParagraph.setAlignment(Paragraph.ALIGN_CENTER);
                doc.add(subheadingParagraph);

                doc.add(addressTable);

                String lastStatement = "We have a total of: "+count+" active locations.";
                Paragraph last = new Paragraph(lastStatement);
                doc.add(last);

                String signingOut = "prepared by:\nMATSIKO BRUNO \n codemuno";
                Paragraph signature = new Paragraph(signingOut);
                signature.setAlignment(Paragraph.ALIGN_RIGHT);
                doc.add(signature);

                doc.close();

                if(Desktop.isDesktopSupported()){
                    File activityFile = new File("Addresses.pdf");
                    Desktop.getDesktop().open(activityFile);



                }





            }
            catch (SQLException | FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(doc != null){
                    doc.close();}
            }
        }
    /**
     * @return null
     * method that prints all the users in the database.
     * TODO: get all users, count how many they are, make a report. easy enough. hopefully
     * */
    public void makeActivityReport(){
        try{
            final String DOCUMENTNAMEACTIVITY = "Activity.pdf";
            Document doc = new Document(PageSize.A4);

            PdfWriter.getInstance(doc, new FileOutputStream(DOCUMENTNAMEACTIVITY));
            doc.open();

            PdfPTable activityTable = new PdfPTable(3);

            //TODO:  create a phrases for populating the heading address table
            Phrase userName = new Phrase("Name");
            Phrase activityDate = new Phrase("Date");
            Phrase activityTime = new Phrase("Time");

            //TODO: add them
            activityTable.addCell(new PdfPCell(userName));
            activityTable.addCell(new PdfPCell(activityDate));
            activityTable.addCell(new PdfPCell(activityTime));


            //TODO: open database and collect all the available records in activity table and user table
            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oddj", "root", "");
                statement = connection.createStatement();

                String selectQuery = "SELECT * FROM activity , login WHERE `logIn_logIn_id`=`logIn_id`;";
                resultSet = statement.executeQuery(selectQuery);
            }
            //TODO: get all the values from the activity Resultset and populate them into the table
            //TODO: get the total count
            int count=0;
            while(resultSet.next()){
                Phrase userNm = new Phrase(resultSet.getString("AddressName"));
                Phrase activityDt = new Phrase(resultSet.getString("AddressDescription"));
                Phrase activityTm = new Phrase(resultSet.getString("AddressDescription"));

                activityTable.addCell(new PdfPCell(userNm));
                activityTable.addCell( new PdfPCell(activityDt));
                activityTable.addCell( new PdfPCell(activityTm));
                count++;
            }

            //TODO: generate an actual report for this particular table;

            Font headingFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD,BaseColor.GREEN);
            Font logoFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLUE);
            Font paragraphFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

            Paragraph headingParagraph = new Paragraph("THE ODD JOB", headingFont);
            headingParagraph.setAlignment(2);
            doc.add(headingParagraph);
            Paragraph metaData = new Paragraph("make some quick cash", headingFont);
            metaData.setAlignment(2);
            doc.add(metaData);


            Paragraph subheadingParagraph = new Paragraph("LIST OF ALL THE USERS AND THEIR USAGE PATTERNS \n\n", logoFont);
            subheadingParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(subheadingParagraph);

            doc.add(activityTable);

            String lastStatement = "We have had a total of: "+count+" logins.";
            Paragraph last = new Paragraph(lastStatement);
            doc.add(last);

            String signingOut = "prepared by:\nMATSIKO BRUNO \n codemuno";
            Paragraph signature = new Paragraph(signingOut);
            signature.setAlignment(Paragraph.ALIGN_RIGHT);
            doc.add(signature);

            doc.close();

            if(Desktop.isDesktopSupported()){
                File activityFile = new File("Activity.pdf");
                Desktop.getDesktop().open(activityFile);



            }





        }
        catch (SQLException | FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(doc != null){
                doc.close();}
        }
    }
        /**
         * TODO: create a graphical report
         * @return null
         *simply obtain the scatter image create when we visit the activity scene.
         * */
        public void makeScatterReport(){
            try{
                final String DOCUMENTNAMESCATTER = "Scatter.pdf";
                Document doc = new Document(PageSize.A4);

                PdfWriter.getInstance(doc, new FileOutputStream(DOCUMENTNAMESCATTER));
                doc.open();



                //TODO: generate an actual report for this particular diagram(scatter);

                Font headingFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD,BaseColor.GREEN);
                Font logoFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLUE);
                Font paragraphFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

                Paragraph headingParagraph = new Paragraph("THE ODD JOB", headingFont);
                headingParagraph.setAlignment(2);
                doc.add(headingParagraph);
                Paragraph metaData = new Paragraph("make some quick cash", headingFont);
                metaData.setAlignment(2);
                doc.add(metaData);


                Paragraph subheadingParagraph = new Paragraph("COMPREHENSIVE LOOK AT HOW THE APP IS USED FOR EVERY 24 HOURS\n\n", logoFont);
                subheadingParagraph.setAlignment(Paragraph.ALIGN_CENTER);
                doc.add(subheadingParagraph);

                Image img = Image.getInstance("C:\\Users\\Muno\\Desktop\\javaWork\\scatter.png");
                doc.add(img);


                String signingOut = "prepared by:\nMATSIKO BRUNO \n codemuno";
                Paragraph signature = new Paragraph(signingOut);
                signature.setAlignment(Paragraph.ALIGN_RIGHT);
                doc.add(signature);

                doc.close();

                if(Desktop.isDesktopSupported()){
                    File activityFile = new File("Scatter.pdf");
                    Desktop.getDesktop().open(activityFile);
                }
            }
            catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(doc != null){
                    doc.close();}
            }
        }
    public void makeBarReport(){
        try{
            final String DOCUMENTNAMEBAR = "Bar.pdf";
            Document doc = new Document(PageSize.A4);

            PdfWriter.getInstance(doc, new FileOutputStream(DOCUMENTNAMEBAR));
            doc.open();



            //TODO: generate an actual report for this particular diagram(scatter);

            Font headingFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD,BaseColor.GREEN);
            Font logoFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLUE);
            Font paragraphFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

            Paragraph headingParagraph = new Paragraph("THE ODD JOB", headingFont);
            headingParagraph.setAlignment(2);
            doc.add(headingParagraph);
            Paragraph metaData = new Paragraph("make some quick cash", headingFont);
            metaData.setAlignment(2);
            doc.add(metaData);


            Paragraph subheadingParagraph = new Paragraph("COMPREHENSIVE LOOK AT HOW THE APP IS USED A MONTHLY BASIS\n\n", logoFont);
            subheadingParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(subheadingParagraph);

            Image img = Image.getInstance("C:\\Users\\Muno\\Desktop\\javaWork\\bar.png");
            doc.add(img);


            String signingOut = "prepared by:\nMATSIKO BRUNO \n codemuno";
            Paragraph signature = new Paragraph(signingOut);
            signature.setAlignment(Paragraph.ALIGN_RIGHT);
            doc.add(signature);

            doc.close();

            if(Desktop.isDesktopSupported()){
                File activityFile = new File("bar.pdf");
                Desktop.getDesktop().open(activityFile);
            }
        }
        catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(doc != null){
                doc.close();}
        }
    }


        /**
         * i need these for my next trick
         *
         *             Image img2 = Image.getInstance("C:\\Users\\Muno\\Desktop\\javaWork\\bar.png");
         *
         * */
    }

