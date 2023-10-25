package ma.enset.sma;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
public class PlayerGUI2 extends Application {
    private PlayerAgent2 agent;
    private ListView<String> messageList;
    private TextField numberField;
    public void setAgent(PlayerAgent2 agent) {
        this.agent = agent;
    }
    public ListView<String> getMessageList() {
        return messageList;
    }
    public TextField getNumberField() {
        return numberField;
    }
    public void start(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();
        messageList = new ListView<>();
        VBox.setVgrow(messageList, Priority.ALWAYS);
        messageList.setPrefHeight(300);
        Label numberLabel = new Label("Enter a number between 0 and 100 : ");
        numberField = new TextField();
        Button guessButton = new Button("Guess");
        guessButton.setOnAction(e -> {
            try {
                int guess = Integer.parseInt(numberField.getText());
                if (guess < 0 || guess > 100) {
                    messageList.getItems().add("Number must be between 0 and 100.");
                } else {
                    agent.sendGuess(guess);
                    numberField.clear();
                }
            } catch (NumberFormatException ex) {
                messageList.getItems().add("Invalid number format.");
            }
        });
        VBox inputBox = new VBox(10, numberLabel, numberField, guessButton);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(messageList);
        borderPane.setBottom(inputBox);
        Scene scene = new Scene(borderPane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Player 2");
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }
    public void display() {
        Platform.runLater(() -> start(new Stage()));
    }
}