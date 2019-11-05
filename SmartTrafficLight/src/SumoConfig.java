import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents sumo configurations file.
 */
public class SumoConfig {
    
    /** The path of the configuration file. */
    private String file;
    
    /** The local port. */
    private int localPort;
    
    /**
     * Creates a SumoConfig instance.
     * 
     * @param file The file path.
     */
    private SumoConfig(String file) {
        this.file = file;
    }
    
    /**
     * Returns the file path.
     * 
     * @return The file path.
     */
    public String getFile() {
        return file;
    }

    /**
     * Returns the local port.
     * 
     * @return The local port.
     */
    public int getLocalPort() {
        return localPort;
    }
    
    /**
     * Sets the file path.
     * 
     * @param file The new file path.
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * Sets the local port.
     * 
     * @param localPort The new local port.
     */
    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    /**
     * Loads a sumo configuration file and returns it.
     * 
     * @param file The file path.
     * @return The expected configurations object. 
     * @throws Exception
     */
    public static SumoConfig load(String file) throws Exception { 
        File                   confFile = new File(file);
        DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
        DocumentBuilder        builder  = factory.newDocumentBuilder();
        Document               document = builder.parse(confFile);
        Element                root     = document.getDocumentElement();
        SumoConfig             conf     = new SumoConfig(file);
        
        root.normalize();
        NodeList server = root.getElementsByTagName("traci_server");        
        NodeList serverProperties = server.item(0).getChildNodes();
        
        for (int i = 0; i < serverProperties.getLength(); i++) {
            Node node = serverProperties.item(i); 
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                conf.setLocalPort(Integer.valueOf(
                        ((Element) node).getAttribute("value")));
            }
        }
                
        return conf;
    }

}