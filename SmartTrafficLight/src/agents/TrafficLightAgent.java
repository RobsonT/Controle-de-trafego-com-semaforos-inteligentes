package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
//import learning.QLearning;
import trasmapi.sumo.Sumo;
import trasmapi.sumo.SumoTrafficLight;
//import utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;


public class TrafficLightAgent extends Agent {

    private ArrayList<String> neighbours;
    private int nrIntersections;
    private int nrStates;
    private int nrActions;
    private String name;

    private TLController tlController;

    public TrafficLightAgent(Sumo sumo, String name, ArrayList<String> neighbours) throws Exception {
        super();
        this.name = "TrafficLight-" + name;
        this.neighbours = (ArrayList<String>) neighbours.clone();

        nrIntersections = neighbours.size();
//        nrStates = (int) Math.pow(TrafficLightState.NR_STATES_PER_LIGHT, nrIntersections); 
        nrActions = (int) Math.pow(3, nrIntersections);  
        tlController = new TLController(this, sumo, name, (ArrayList<String>) neighbours.clone());
        updateAndCleanNeighboursNames();
    }

    private void updateAndCleanNeighboursNames() {
        ArrayList<String> tlsIds = SumoTrafficLight.getIdList();
        for (int i = 0; i < neighbours.size(); i++) {
            if (Arrays.asList(tlsIds.toArray()).contains(neighbours.get(i))) {
                neighbours.set(i, "TrafficLight-" + neighbours.get(i));
            } else {
                neighbours.remove(i);
                i--;
            }
        }
    }

    @Override
    protected void setup() {
        // Registro DF
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("TrafficLightsAgent");
        sd.setName(getName());
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            doDelete();
        }
        new Thread(tlController).start();
        super.setup();
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        super.takeDown();
    }
}
