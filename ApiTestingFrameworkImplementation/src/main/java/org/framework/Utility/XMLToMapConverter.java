package org.framework.Utility;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLToMapConverter {

    /**
     * Converts an XML payload (as a String) into a Map.
     *
     * @param xmlString the XML content to convert
     * @return a Map representing the XML structure
     * @throws Exception if any XML parsing errors occur
     */
    public static Map<String, Object> xmlToMap(String xmlString) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);

        // Security best practice: disable external entities
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
        doc.getDocumentElement().normalize();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(doc.getDocumentElement().getNodeName(), elementToMap(doc.getDocumentElement()));
        return resultMap;
    }

    /**
     * Recursively converts an XML Element into a Map or List.
     *
     * @param element the XML Element to convert
     * @return an Object representing the element's data (Map, String, or List)
     */
    private static Object elementToMap(Element element) {
        Map<String, Object> map = new HashMap<>();
        NodeList children = element.getChildNodes();
        boolean hasElements = false;

        // Check for text content first
        String textContent = element.getTextContent().trim();
        if (!textContent.isEmpty() && children.getLength() == 1) {
            return textContent;
        }

        // Process child nodes
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                hasElements = true;
                Element childElement = (Element) node;
                String nodeName = childElement.getNodeName();
                Object value = elementToMap(childElement);

                handleDuplicateKeys(map, nodeName, value);
            }
        }

        return hasElements ? map : textContent;
    }

    /**
     * Handles duplicate keys by converting them to lists when necessary.
     *
     * @param map   the map being built
     * @param key   the current key
     * @param value the value to store
     */
    private static void handleDuplicateKeys(Map<String, Object> map, String key, Object value) {
        if (map.containsKey(key)) {
            Object existingValue = map.get(key);
            List<Object> values;
            if (existingValue instanceof List) {
                values = (List<Object>) existingValue;
            } else {
                values = new ArrayList<>();
                values.add(existingValue);
            }
            values.add(value);
            map.put(key, values);
        } else {
            map.put(key, value);
        }
    }

    /**
     * Prints the Map structure in a human-readable format.
     *
     * @param map    the Map to print
     * @param indent the current indentation level
     */
    public static void printMap(Map<String, Object> map, int indent) {
        String indentation = "  ".repeat(indent);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.print(indentation + entry.getKey() + ": ");
            Object value = entry.getValue();

            if (value instanceof Map) {
                System.out.println();
                printMap((Map<String, Object>) value, indent + 1);
            } else if (value instanceof List) {
                System.out.println();
                printList((List<Object>) value, indent + 1);
            } else {
                System.out.println(value);
            }
        }
    }

    /**
     * Helper method to print List values.
     *
     * @param list   the List to print
     * @param indent the current indentation level
     */
    private static void printList(List<Object> list, int indent) {
        String indentation = "  ".repeat(indent);
        for (Object item : list) {
            if (item instanceof Map) {
                printMap((Map<String, Object>) item, indent);
            } else {
                System.out.println(indentation + "- " + item);
            }
        }
    }



}
