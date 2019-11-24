package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import trasmapi.genAPI.Lane;
import trasmapi.genAPI.LanePosition;
import trasmapi.genAPI.TrafficLight;
import trasmapi.genAPI.exceptions.UnimplementedMethod;
import trasmapi.sumo.ControlledLinks;
//import learning.QLearning;
import trasmapi.sumo.Sumo;
import trasmapi.sumo.SumoE3Detector;
import trasmapi.sumo.SumoEdge;
import trasmapi.sumo.SumoLane;
import trasmapi.sumo.SumoTrafficLight;
import trasmapi.sumo.SumoTrafficLightProgram;
import trasmapi.sumo.SumoTrafficLightProgram.Phase;
//import utils.Logger;
import utils.E3Detector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TrafficLightAgent extends Agent {

    private String name;
    private double[] iLO, iNS;
    double gLO;
	double gNS, mLO = 0, mNS = 0; 
    int position, quantity;
    Sumo sumo;

    public TrafficLightAgent(Sumo sumo, String name) throws Exception {
        super();
        this.name = "TrafficLight-" + name;
        this.sumo = sumo;
        this.position = this.quantity = 0;
        iLO = new double[3];
        iNS = new double[3];
//        tlController = new TLController(this, sumo, name, (ArrayList<String>) neighbours.clone());
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
//        new Thread(tlController).start();
        changeTrafficLigthStateBehaviour changeTrafficLigthStateBehaviour = new changeTrafficLigthStateBehaviour(this);
        addBehaviour(changeTrafficLigthStateBehaviour);
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

    private class changeTrafficLigthStateBehaviour extends CyclicBehaviour{
    	public changeTrafficLigthStateBehaviour(Agent a) {
            super(a);
        }

		@Override
		public void action() {
			double efficiencylo = 0;
			double efficiencyns = 0;
			double aux;
			E3Detector e3Detector = new E3Detector();
			if(name.split("-")[1].equals("428667950")) {
				if(sumo.getCurrentSimStep() / 1000 > 90 && sumo.getCurrentSimStep() / 1000 % 90 == 0) {
					e3Detector.initialize();

					SumoTrafficLight tl = new SumoTrafficLight(name.split("-")[1]);
					List<String> e3Detectors = e3Detector.getIds(name.split("-")[1]);
					for (String d : e3Detectors) {
						SumoE3Detector detector= new SumoE3Detector(d);
						SumoLane lane = new SumoLane(d.split("_")[2] + "_0");
						double minTime = lane.getLength() / lane.getMaxSpeed();
						double meanSpeed = detector.getMenSpeed();
						double meanTime;
						meanTime = (meanSpeed == -1)? minTime : (lane.getLength()/meanSpeed);
						aux = meanTime / minTime;
						if(d.split("_")[3].equals("lo")) {
							if(efficiencylo == 0 || efficiencylo < aux) {
								efficiencylo = aux; 
							}
						}else {
							if(efficiencyns == 0 || efficiencyns < aux) {
								efficiencyns = aux; 
							}
						}
					}
	//	//			Calcular Glo e Gns
					gLO = efficiencylo;
					gNS = efficiencyns;
	//	//			preencher Ilo e Ins
					iLO[position] = gLO;
					iNS[position] = gNS;
					position = ++position % 3;
					if(quantity < 3)
						quantity++;
		//			preencher Mlo e Mns
					if(quantity == 3) {
						mLO = (iLO[0] + iLO[1] + iLO[2]) / 3;
						mNS = (iNS[0] + iNS[1] + iNS[2]) / 3;
					}
		//			Alteração de plano semáforico
						if(Math.abs(mLO - mNS) > 0.1) {
							SumoTrafficLightProgram tlProgram = tl.getProgram();
							SumoTrafficLightProgram newProgram = new SumoTrafficLightProgram(name + (sumo.getCurrentSimStep() / 1000) / 90);
							for (Phase p : tlProgram.getPhases()) {
								newProgram.addPhase(p.getDuration() - 1, p.getState());
								String newState = "";
								for (char s : p.getState().toCharArray()) {
									if(Character.toLowerCase(s) == 'g') 
										newState += 'r';
									else if(Character.toLowerCase(s) == 'r')
										newState += 'g';
									else
										newState += 'y';
								}
								newProgram.addPhase(1, newState);
							}
							tl.setProgram(newProgram);
						}
				}
			}
		}
    }
}
