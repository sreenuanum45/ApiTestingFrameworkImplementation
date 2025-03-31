package org.framework.Utility;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;

public class XmlUtils {

    // Create a single instance of XmlMapper (it’s thread-safe after configuration).
    private static final XmlMapper xmlMapper = new XmlMapper();

    /**
     * Deserialize XML from a String into a POJO.
     *
     * @param xml   the XML content as String
     * @param clazz the target class type
     * @param <T>   the type parameter
     * @return an instance of type T populated with data from the XML
     * @throws Exception if an error occurs during deserialization
     */
    public static <T> T fromXml(String xml, Class<T> clazz) throws Exception {
        return xmlMapper.readValue(xml, clazz);
    }

    /**
     * Deserialize XML from a File into a POJO.
     *
     * @param file  the XML file
     * @param clazz the target class type
     * @param <T>   the type parameter
     * @return an instance of type T populated with data from the XML
     * @throws Exception if an error occurs during deserialization
     */
    public static <T> T fromXml(File file, Class<T> clazz) throws Exception {
        return xmlMapper.readValue(file, clazz);
    }

    /**
     * Serialize a POJO into an XML String.
     *
     * @param object the POJO to be serialized
     * @return a String containing the XML representation of the POJO
     * @throws Exception if an error occurs during serialization
     */
    public static String toXml(Object object) throws Exception {
        return xmlMapper.writeValueAsString(object);
    }

}

