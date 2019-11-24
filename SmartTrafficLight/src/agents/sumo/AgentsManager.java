package agents.sumo;

import agents.TrafficLightAgent;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import trasmapi.genAPI.TraSMAPI;
import trasmapi.sumo.Sumo;
import trasmapi.sumo.SumoTrafficLight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class AgentsManager {
    ArrayList<TrafficLightAgent> agents = new ArrayList<>();

    public AgentsManager(Sumo sumo, ContainerController mainContainer) {
        ArrayList<String> tlsIds = SumoTrafficLight.getIdList();

        for (String tlId : tlsIds) {
            SumoTrafficLight tl = new SumoTrafficLight(tlId);

            TrafficLightAgent agent;

            try {
                agent = new TrafficLightAgent(sumo, tlId);

                agents.add(agent);
                mainContainer.acceptNewAgent("TrafficLight-" + tlId, agent);

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }


    public void startupAgents(ContainerController mainContainer) {
        try {
            for (TrafficLightAgent agent : agents) {
                mainContainer.getAgent(agent.getLocalName()).start();
            }
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}
