import agents.sumo.AgentsManager;
import jade.BootProfileImpl;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import trasmapi.genAPI.TraSMAPI;
import trasmapi.genAPI.exceptions.TimeoutException;
import trasmapi.genAPI.exceptions.UnimplementedMethod;
import trasmapi.sumo.Sumo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static boolean JADE_GUI = true;
    private static ProfileImpl profile;
    private static ContainerController mainContainer;

    public static void main(String[] args) throws UnimplementedMethod, InterruptedException, IOException,
            TimeoutException {

        if (JADE_GUI) {
            List<String> params = new ArrayList<String>();
            params.add("-gui");
            profile = new BootProfileImpl(params.toArray(new String[0]));
        } else {
            profile = new ProfileImpl();
        }

        jade.core.Runtime rt = Runtime.instance();
        mainContainer = rt.createMainContainer(profile);

        // Iniciar TraSMAPI framework
        TraSMAPI api = new TraSMAPI();
        //Criar SUMO
        Sumo sumo = new Sumo("guisim");
        List<String> params = new ArrayList<String>();

        params.add("--device.emissions.probability=1.0");
//        params.add("--tripinfo-output=network/logs/trip.xml");
        params.add("-c=network/MAS.sumocfg");
        params.add("--tripinfo-output=output1.xml");
        sumo.addParameters(params);
        sumo.addConnections("127.0.0.1", 8813);

        //Add SUMO para TraSMAPI
        api.addSimulator(sumo);

        //Executa e conecta todos os simuladores adicionado
        api.launch();

        api.connect();
        //Criação dos agentes
        AgentsManager manager = new AgentsManager(sumo, mainContainer);
        manager.startupAgents(mainContainer);

        api.start();

        while (true) {
        	if (!api.simulationStep(0) || sumo.getCurrentSimStep() / 1000 >= 5000)
                break;
        	if(sumo.getCurrentSimStep() / 1000 > 90 && sumo.getCurrentSimStep() / 1000 % 90 == 0)
        		Thread.sleep(1000);
        }
        
        api.close();
    }
}
