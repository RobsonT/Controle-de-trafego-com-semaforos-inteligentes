package trasmapi.sumo;

import java.io.IOException;
import java.util.ArrayList;

import trasmapi.genAPI.exceptions.WrongCommand;
import trasmapi.sumo.protocol.Command;
import trasmapi.sumo.protocol.Constants;
import trasmapi.sumo.protocol.Content;
import trasmapi.sumo.protocol.RequestMessage;
import trasmapi.sumo.protocol.ResponseMessage;

public class SumoE3Detector {

	private String id;
	
	public SumoE3Detector(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	/**
	 * Returns the mean speed of vehicles that have been within in this detector
	 * @return mean speed of vehicles
	 */
	public double getMenSpeed() {
		Command cmd = new Command(Constants.CMD_GET_MULTI_ENTRY_EXIT_DETECTOR_VARIABLE);
        Content cnt = new Content(Constants.LAST_STEP_MEAN_SPEED, id);

        cmd.setContent(cnt);

        RequestMessage reqMsg = new RequestMessage();
        reqMsg.addCommand(cmd);

        try {
            ResponseMessage rspMsg = SumoCom.query(reqMsg);
            Content content = rspMsg.validate(
                    (byte)Constants.CMD_GET_MULTI_ENTRY_EXIT_DETECTOR_VARIABLE,
                    (byte)Constants.RESPONSE_GET_MULTI_ENTRY_EXIT_DETECTOR_VARIABLE,
                    (byte)Constants.LAST_STEP_MEAN_SPEED,
                    (byte)Constants.TYPE_DOUBLE);

            return content.getDouble();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongCommand e) {
            e.printStackTrace();
        }
        return 0;
	}
}
