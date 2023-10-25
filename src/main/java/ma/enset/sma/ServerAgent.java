package ma.enset.sma;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
public class ServerAgent extends GuiAgent {
    private ServerGUI agentServerGui;
    private int expectedNumber;
    @Override
    protected void setup() {
        System.out.println("***  la méthode setup *****");
        agentServerGui=(ServerGUI)getArguments()[0];
        agentServerGui.setServerAgent(this);
        expectedNumber = (int) getArguments()[1];
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receivedMSG = receive();
                if (receivedMSG != null){
                    int receivedNumber = Integer.parseInt(receivedMSG.getContent());
                    if (receivedNumber == expectedNumber) {
                        agentServerGui.showMessage("<<== "+receivedNumber+" (correct)");
                        ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                        reply.addReceiver(new AID("client ",AID.ISLOCALNAME));
                        reply.setContent("Correct! You guessed the right number.");
                        send(reply);
                        doDelete();
                    } else if (receivedNumber < expectedNumber) {
                        agentServerGui.showMessage("<<== "+receivedNumber+" (too low)");
                        ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                        reply.addReceiver(new AID("client ",AID.ISLOCALNAME));
                        reply.setContent("Too low. Try again.");
                        send(reply);
                    } else {
                        agentServerGui.showMessage("<<== "+receivedNumber+" (too high)");
                        ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                        reply.addReceiver(new AID("client",AID.ISLOCALNAME));
                        reply.setContent("Too high. Try again.");
                        send(reply);
                    }
                } else {
                    block();
                }
            }}
        );

    }
    @Override
    protected void beforeMove() {
        System.out.println("***  la méthode beforeMove *****");
    }
    @Override
    protected void afterMove() {
        System.out.println("***  la méthode afterMove *****");
    }
    @Override
    protected void takeDown() {
        System.out.println("***  la méthode takeDown *****");
    }
    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        String parameter = (String) guiEvent.getParameter(0);
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.addReceiver(new AID("client ", AID.ISLOCALNAME));
        message.setContent(parameter);
        send(message);
    }
}