package utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	 FileInputStream file;
	 DocumentBuilderFactory dbFactory;
	 DocumentBuilder dBuilder;
	 Document doc;
	
	public  boolean initialize() {
		try {
			fXmlFile = new File("network/e3.add.xml");
			file = new FileInputStream(fXmlFile);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro de leitura: " + fXmlFile);
		}
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(file);
			return true;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
//			e.printStackTrace();
		}
	}
	
	public  List<String> getIds(String id) {
		List<String> ids = new ArrayList<>();

//		Lista com todos os dados gerados pelos detectores
		NodeList detectors = doc.getElementsByTagName("e3Detector");
		
		for (int i = 0; i < detectors.getLength(); i++) {
			Node detectorNode = detectors.item(i);
//			Verifica se o elemento é valido		
			if (detectorNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) detectorNode;
					if(element.getAttribute("id").split("_")[1].equals(id))
						ids.add(element.getAttribute("id"));
			}
		}
		return ids;
	}
}
