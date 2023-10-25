package ma.enset.sma;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.gui.*;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
public class PlayerAgent2 extends GuiAgent {
    private ListView<String> messageList;
    private TextField numberField;
    private int magicNumber;
    private PlayerGUI2 gui;
    protected void setup() {
        Platform.runLater(() -> {
            gui = new PlayerGUI2();
            gui.setAgent(PlayerAgent2.this);
            gui.display();
            messageList = gui.getMessageList();
            numberField = gui.getNumberField();
        });
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    ACLMessage reply = msg.createReply();
                    if (content.equals("start")) {
                        magicNumber = Integer.parseInt(msg.getSender().getLocalName());
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent("game started");
                    } else if (content.equals("stop")) {
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent("game over");
                    } else {
                        int guess = Integer.parseInt(content);
                        if (guess == magicNumber) {
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent("You won! The magic number is " + magicNumber);
                        } else if (guess > magicNumber) {
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent("The magic number is lower than " + guess);
                        } else {
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent("The magic number is higher than " + guess);
                        }
                    }
                    send(reply);
                    Platform.runLater(() -> messageList.getItems().add(msg.getContent()));
                } else {
                    block();
                }
            }
        });
    }
    protected void onGuiEvent(GuiEvent ge) {
        if (ge.getType() == 1) {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(getAID("server"));
            String content = numberField.getText();
            msg.setContent(content);
            send(msg);
            Platform.runLater(() -> messageList.getItems().add("You guessed " + content));
        }
    }
    public void sendGuess(int guess) {
        if (guess == magicNumber) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Correct! You win.");
            send(msg);
        } else if (guess < magicNumber) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Your guess is too low.");
            send(msg);
        } else {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Your guess is too high.");
            send(msg);
        }
    }
    public void setGui(PlayerGUI2 gui) {
        this.gui = gui;
    }
}