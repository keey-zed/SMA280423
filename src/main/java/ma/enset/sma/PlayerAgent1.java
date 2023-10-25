package ma.enset.sma;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.gui.*;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
public class PlayerAgent1 extends GuiAgent {
    private PlayerGUI1 playerGUI1;
    @Override
    protected void setup() {
        System.out.println("*** Client : la méthode setup *****");
        playerGUI1=(PlayerGUI1) getArguments()[0];
        playerGUI1.setPlayerAgent1(this);
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receivedMSG = receive();
                if (receivedMSG!=null){
                    playerGUI1.showMessage("<<== "+receivedMSG.getContent());
                    System.out.println(receivedMSG.getContent());
                    System.out.println(receivedMSG.getSender().getName());
                }else {
                    block();
                }
            }}
        );
    }
    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        String parameter =(String) guiEvent.getParameter(0);
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.addReceiver(new AID("serverAgent ", AID.ISGUID));
        message.setContent(parameter);
        send(message);
    }
}