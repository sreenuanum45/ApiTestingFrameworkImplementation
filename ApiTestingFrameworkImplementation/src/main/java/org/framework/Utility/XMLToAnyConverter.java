package org.framework.Utility;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.inst2xsd.Inst2Xsd;
import org.apache.xmlbeans.impl.inst2xsd.Inst2XsdOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A utility class that converts XML into various Java types.
 * <p>
 * It provides methods for converting:
 * <ul>
 *   <li>Large XML files to a String</li>
 *   <li>XML content to a Map</li>
 *   <li>XML content to a JSON String</li>
 *   <li>XML content to a List (useful for repeated elements)</li>
 *   <li>XML content to a POJO (custom Java object)</li>
 * </ul>
 * <p>
 * This class uses Jackson’s XmlMapper for dynamic parsing and conversion.
 */
public class XMLToAnyConverter {

    // Create reusable mapper instances
    private static final XmlMapper xmlMapper = new XmlMapper();
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    static {
        // Configure the XML mapper:
        // - Treat empty XML elements as null
        // - Accept single values as arrays when needed
        // - Do not fail on unknown properties when mapping to POJOs
        xmlMapper.configure(FromXmlParser.Feature.EMPTY_ELEMENT_AS_NULL, true);
        xmlMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // You can add more configurations here to handle namespaces if needed.
    }

    /**
     * Reads a large XML file from disk and converts it to a String.
     *
     * @param filePath the path to the XML file
     * @return the XML content as a String
     * @throws IOException if reading the file fails
     */
    public static String largeXMLToString(String filePath) throws IOException {
        // Note: For extremely large files, consider using a streaming approach.
        return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
    }

    /**
     * Converts an XML string into a Map representation.
     *
     * @param xml the XML content as a String
     * @return a Map representing the XML structure
     * @throws IOException if the XML cannot be parsed
     */
    public static Map<String, Object> xmlToMap(String xml) throws IOException {
        return xmlMapper.readValue(xml, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * Reads a large XML file from disk and converts it directly into a Map.
     *
     * @param filePath the path to the XML file
     * @return a Map representing the XML structure
     * @throws IOException if reading or parsing the file fails
     */
    public static Map<String, Object> largeXMLFileToMap(String filePath) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filePath))) {
            return xmlMapper.readValue(is, new TypeReference<Map<String, Object>>() {});
        }
    }

    /**
     * Converts an XML string into a JSON string.
     * <p>
     * This method first converts XML into a Map and then uses the JSON mapper to produce
     * a pretty-printed JSON string.
     *
     * @param xml the XML content as a String
     * @return a JSON string representation of the XML
     * @throws IOException if the XML cannot be parsed
     */
    public static String xmlToJson(String xml) throws IOException {
        Map<String, Object> map = xmlToMap(xml);
        return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
    }

    /**
     * Converts XML content into a List of Maps based on a specified node name.
     * <p>
     * For example, if your XML has the following structure:
     *
     * <pre>
     *   &lt;items&gt;
     *     &lt;item id="1"&gt;...&lt;/item&gt;
     *     &lt;item id="2"&gt;...&lt;/item&gt;
     *   &lt;/items&gt;
     * </pre>
     *
     * You would call xmlToList(xml, "item") to extract the list of items.
     *
     * @param xml             the XML content as a String
     * @param listElementName the XML element name that should be treated as a list item
     * @return a List of Maps representing each occurrence of the specified element
     * @throws IOException if the XML cannot be parsed
     */
    public static List<Map<String, Object>> xmlToList(String xml, String listElementName) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        // Parse the XML into a JsonNode tree
        JsonNode root = xmlMapper.readTree(xml);

        // Find all nodes with the given element name
        Iterator<JsonNode> nodes = root.findValues(listElementName).iterator();
        while (nodes.hasNext()) {
            JsonNode node = nodes.next();
            // If the node is an array, iterate through its items
            if (node.isArray()) {
                for (JsonNode arrayItem : node) {
                    Map<String, Object> map = jsonMapper.convertValue(arrayItem, new TypeReference<Map<String, Object>>() {});
                    list.add(map);
                }
            } else {
                Map<String, Object> map = jsonMapper.convertValue(node, new TypeReference<Map<String, Object>>() {});
                list.add(map);
            }
        }
        return list;
    }

    /**
     * Converts an XML string into a POJO of the specified type.
     *
     * @param xml   the XML content as a String
     * @param clazz the Class of the desired POJO
     * @param <T>   the type parameter for the POJO
     * @return an instance of T populated with data from the XML
     * @throws IOException if the XML cannot be parsed into the POJO
     */
    public static <T> T xmlToPOJO(String xml, Class<T> clazz) throws IOException {
        return xmlMapper.readValue(xml, clazz);
    }

    /**
     * Reads a large XML file from disk and converts it directly into a POJO.
     *
     * @param filePath the path to the XML file
     * @param clazz    the Class of the desired POJO
     * @param <T>      the type parameter for the POJO
     * @return an instance of T populated with data from the XML file
     * @throws IOException if reading or parsing the file fails
     */
    public static <T> T xmlFileToPOJO(String filePath, Class<T> clazz) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filePath))) {
            return xmlMapper.readValue(is, clazz);
        }
    }

    /**
     * Reads a large XML file from disk and converts it to a String.
     *
     * @param filePath the path to the XML file
     * @return the XML content as a String
     * @throws IOException if file reading fails
     */
    public static String largeXMLToStringConverter(String filePath) throws IOException {
        // For very large files, consider a streaming approach.
        return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
    }
    /**
     * Converts an XML String into a Map representation.
     *
     * @param xml the XML content as a String
     * @return a Map representing the XML structure
     * @throws IOException if XML parsing fails
     */
    public static Map<String, Object> xmlToMapConverter(String xml) throws IOException {
        return xmlMapper.readValue(xml, new TypeReference<Map<String, Object>>() {});
    }
    /**
     * Reads a large XML file and converts it directly into a Map.
     *
     * @param filePath the path to the XML file
     * @return a Map representing the XML structure
     * @throws IOException if file reading or parsing fails
     */
    public static Map<String, Object> largeXMLFileToMapConverter(String filePath) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filePath))) {
            return xmlMapper.readValue(is, new TypeReference<Map<String, Object>>() {});
        }
    }
    /**
     * Converts an XML String into a JSON String.
     * <p>
     * This method converts XML to a Map and then uses the JSON mapper to generate a pretty-printed JSON.
     *
     * @param xml the XML content as a String
     * @return a JSON String representation of the XML
     * @throws IOException if XML parsing fails
     */
    public static String xmlToJsonConverter(String xml) throws IOException {
        Map<String, Object> map = xmlToMap(xml);
        return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
    }
    /**
     * Generates an XSD from an XML String using Apache XMLBeans Inst2Xsd.
     * <p>
     * This method parses the XML into an XmlObject, applies Inst2Xsd conversion with
     * default options (using the "Russian Doll" design), and returns the generated XSD as a String.
     *
     * @param xml the XML content as a String
     * @return a String representation of the generated XSD
     * @throws XmlException if XMLBeans cannot parse the XML or generate the XSD
     */
    public static String xmlToXSD(String xml) throws XmlException {
        // Parse XML into an XmlObject.
        XmlObject xmlObject = XmlObject.Factory.parse(xml);
        // Set up Inst2Xsd options.
        Inst2XsdOptions options = new Inst2XsdOptions();
        options.setDesign(Inst2XsdOptions.DESIGN_RUSSIAN_DOLL);
        // Generate the schema documents from the XML instance.
        SchemaDocument[] schemaDocs = Inst2Xsd.inst2xsd(new XmlObject[] { xmlObject }, options);
        if (schemaDocs != null && schemaDocs.length > 0) {
            // For simplicity, return the first schema generated.
            return schemaDocs[0].xmlText();
        } else {
            throw new XmlException("Unable to generate XSD from the provided XML.");
        }
    }

    /**
     * Reads a large XML file and generates an XSD from its contents.
     *
     * @param filePath the path to the XML file
     * @return a String representation of the generated XSD
     * @throws IOException  if file reading fails
     * @throws XmlException if XMLBeans cannot parse the XML or generate the XSD
     */
    public static String xmlFileToXSD(String filePath) throws IOException, XmlException {
        String xmlContent = largeXMLToString(filePath);
        return xmlToXSD(xmlContent);
    }


}

