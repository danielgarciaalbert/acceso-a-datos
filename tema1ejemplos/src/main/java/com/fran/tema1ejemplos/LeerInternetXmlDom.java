package com.fran.tema1ejemplos;


import java.net.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class LeerInternetXmlDom {
	static URL dir = null;
	public static void main(String[] args) {
        try {
            DocumentBuilderFactory dbFactory 
                = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            // Cogemos de ejemplo las noticias de primera división del Marca
            dir = new URL("https://e00-marca.uecdn.es/rss/futbol/primera-division.xml");
            Document doc = dBuilder.parse(dir.openStream());
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");
            
            System.out.println("Imprimiendo noticias..."); 
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("Noticia: " 
                            + eElement.getElementsByTagName("title")
                            .item(0).getTextContent());                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    		
	}
}
