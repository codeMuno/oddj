package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class TheBoss {

    @FXML
    private Button btnSend;

    @FXML
    private ScrollPane sp_main;

    @FXML
    private TextField txtmsg;

    @FXML
    private VBox vbox_msg;

    Server server;


    public void StartServer() {
        try{
            server = new Server(new ServerSocket(1234));
        }catch (Exception e){
            System.out.println("Error creating Server");
        }

        vbox_msg.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                sp_main.setVvalue((Double) t1);
            }
        });

        server.receiveMessageFromClient(vbox_msg);

        btnSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("clicked");
                String messageToSend = txtmsg.getText();
                if(!messageToSend.isEmpty()){
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5,5,5,10));

                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-color: rgb(239, 242, 255);"+
                            "-fx-background-color: rgb(15, 125, 242);"+
                            "-fx-background-radius: 20px;"
                            );
                    textFlow.setPadding(new Insets(5,10,5,10));
                    text.setFill(Color.color(0.953, 0.945, 0.966));

                    hBox.getChildren().add(textFlow);
                    vbox_msg.getChildren().add(hBox);

                    try {
                        server.sendMessageToClient(messageToSend);
                    } catch (IOException e) {
                        System.out.println("sending message failed");
                        e.printStackTrace();
                    }
                    txtmsg.clear();

                }
            }
        });

    }
    public static void addLabel(String messageFromClient, VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233, 233, 235);"+
                "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }
}
class Server{
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Server(ServerSocket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        try {
            this.socket = serverSocket.accept();
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error creating server");
            e.printStackTrace();
            closeEverThing(socket, bufferedReader, bufferedWriter);
        }
    }
    public void receiveMessageFromClient(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try {
                        String messageFromClient = bufferedReader.readLine();
                        TheBoss.addLabel(messageFromClient, vBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            closeEverThing(socket, bufferedReader, bufferedWriter);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }).start();
    }
    public void sendMessageToClient(String messageToClient) throws IOException {
        try{
            bufferedWriter.write(messageToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
            closeEverThing(socket, bufferedReader, bufferedWriter);
        }
    }
    public void closeEverThing(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) throws IOException {

        if(socket !=null){socket.close();}
        if(bufferedReader!=null){bufferedReader.close();}
        if(bufferedWriter!=null){bufferedWriter.close();}
    }
}
