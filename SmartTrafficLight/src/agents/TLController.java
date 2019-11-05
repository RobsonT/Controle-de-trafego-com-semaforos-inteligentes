package agents;

import trasmapi.genAPI.exceptions.UnimplementedMethod;
import trasmapi.sumo.Sumo;
import trasmapi.sumo.SumoLane;
import trasmapi.sumo.SumoTrafficLight;
//import utils.Logger;

import java.util.ArrayList;
import java.util.Random;

public class TLController implements Runnable {

    private String name;
    private Sumo sumo;
    private ArrayList<String> neighbours;
    private TrafficLightAgent parentAgent;
    private int emergencyIndex = -1;

    public TLController(TrafficLightAgent parent, Sumo sumo, String name, ArrayList<String> neighbours) {
        parentAgent = parent;
        this.name = name;
        this.sumo = sumo;
        this.neighbours = new ArrayList<>(neighbours);
    }

    @Override
    public void run() {
        int nrIntersections = neighbours.size();
        SumoTrafficLight light = new SumoTrafficLight(name);
        System.out.println(name);
        int previousIndex = -1;
        while (true) {
            for (int i = 0; i < nrIntersections; i++) {
                
                String state = light.getState();
                light.setState(state);
            	

            }
        }
    }
}