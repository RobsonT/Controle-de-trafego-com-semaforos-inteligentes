package utils;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class E3Detector {
	File fXmlFile;
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	static Document doc;
	
	private static E3Detector instance = null;
	
	private E3Detector() {
		fXmlFile = new File("/Users/mkyong/staff.xml");
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static E3Detector getInstance() {
		if(instance == null) 
			instance = new E3Detector();
		return instance;
	}
	
	public static float getMeanSpeed(String id) {
		float meanSpeed = 0;
		int interval = 0;
//		Lista com todos os dados gerados pelos detectores
		NodeList detectors = doc.getElementsByTagName("interval");
//		Percorre cada intervalo dos detectores
		for (int i = 0; i < detectors.getLength(); i++) {

			Node detectorNode = detectors.item(i);
//			Verifica se o elemento é valido		
			if (detectorNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) detectorNode;
				if(element.getAttribute("id").split("_")[1].equals(id))
					if(Integer.parseInt(element.getAttribute("end")) > interval)
//						atualiza o meanSpeed com a velocidade médi do intervalo mais atual
						meanSpeed = Float.parseFloat(element.getAttribute("meanSpeed"));
			}
		}
		return meanSpeed;
	}
	
	public static float getMeanTravelTime(String id) {
		float meanTravelTime = 0;
		int interval = 0;
//		Lista com todos os dados gerados pelos detectores
		NodeList detectors = doc.getElementsByTagName("interval");
//		Percorre cada intervalo dos detectores
		for (int i = 0; i < detectors.getLength(); i++) {

			Node detectorNode = detectors.item(i);
//			Verifica se o elemento é valido		
			if (detectorNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) detectorNode;
				if(element.getAttribute("id").split("_")[1].equals(id))
					if(Integer.parseInt(element.getAttribute("end")) > interval)
//						atualiza o meanTravelTime com o tempo de viagem médio do intervalo mais atual
						meanTravelTime = Float.parseFloat(element.getAttribute("meanTravelTime"));
			}
		}
		return meanTravelTime;
	}
	
	public static int getVehicleCount(String id) {
		int vehicles = 0;
		int interval = 0;
//		Lista com todos os dados gerados pelos detectores
		NodeList detectors = doc.getElementsByTagName("interval");
//		Percorre cada intervalo dos detectores
		for (int i = 0; i < detectors.getLength(); i++) {

			Node detectorNode = detectors.item(i);
//			Verifica se o elemento é valido		
			if (detectorNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) detectorNode;
				if(element.getAttribute("id").split("_")[1].equals(id))
					if(Integer.parseInt(element.getAttribute("end")) > interval)
//						atualiza o número de veículos do intervalo mais atual
						vehicles = Integer.parseInt(element.getAttribute("vehicleSum"));
			}
		}
		return vehicles;
	}
	
}
