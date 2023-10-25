package ma.enset.sma;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Random;
import javafx.application.Platform;
public class ServerGUI extends Application {
    private ServerAgent serverAgent;
    private Random random;
    private int magicNumber;
    private boolean gameOn;
    ObservableList<String> data = FXCollections.observableArrayList();
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        random = new Random();
        magicNumber = random.nextInt(101);
        gameOn = true;
        startContainer();
        stage.setTitle("Server Agent");
        BorderPane root = new BorderPane();
        Label labelMsg = new Label("Message : ");
        TextField textFieldMsg = new TextField();
        Button buttonSend = new Button("Envoyer");
        HBox hBox = new HBox(labelMsg, textFieldMsg, buttonSend);
        ListView<String> listView = new ListView<>(data);
        root.setBottom(hBox);
        root.setCenter(listView);
        Scene scene = new Scene(root, 400, 200);
        stage.setScene(scene);
        stage.show();
        buttonSend.setOnAction(event -> {
            GuiEvent guiEvent = new GuiEvent(this, 1);
            guiEvent.addParameter(textFieldMsg.getText());
            data.add("==>>"+textFieldMsg.getText());
            textFieldMsg.setText("");
            serverAgent.onGuiEvent(guiEvent);
        });
    }
    private void startContainer() throws StaleProxyException {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        AgentContainer container = runtime.createAgentContainer(profile);
        AgentController serverController = container.createNewAgent("server", "ma.enset.sma.ServerAgent", new Object[]{this});
        serverController.start();
        AgentController player1Controller = container.createNewAgent("player1", "ma.enset.sma.PlayerAgent", new Object[]{this, 1});
        player1Controller.start();
        AgentController player2Controller = container.createNewAgent("player2", "ma.enset.sma.PlayerAgent", new Object[]{this, 2});
        player2Controller.start();
    }
    public void setServerAgent(ServerAgent agentServer) {
        this.serverAgent = agentServer;
    }
    public void showMessage(String message) {
        Platform.runLater(() -> {
            data.add(message);
        });
    }
    public boolean isGameOn() {
        return gameOn;
    }
    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }
    public int getMagicNumber() {
        return magicNumber;
    }
}