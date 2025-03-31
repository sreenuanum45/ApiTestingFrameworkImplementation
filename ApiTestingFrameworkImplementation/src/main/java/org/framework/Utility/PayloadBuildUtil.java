package org.framework.Utility;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;

public class PayloadBuildUtil {
    @Getter
    private static String DPSPayload;
    private  static String DPSPayload1;
    private static String DpsPayload2;

    public static void setAllPayloads() throws IOException {
        DPSPayload = setPayload("createuser.json");
        DPSPayload1=setPayload("CreateUserForPetstore.json");
        DpsPayload2=setPayload("ListOfUsers.json");

    }
    private static String setPayload(String filename) throws IOException {
        String directory = "payloadTemplates/";
        String payload = "";
        ClassLoader classLoader = PayloadBuildUtil.class.getClassLoader();
        URL resource = classLoader.getResource(directory + filename);
        if (resource != null) {
            try (FileReader reader = new FileReader(resource.getFile().replace("%20"," "));
                 BufferedReader br = new BufferedReader(reader)) {
                payload = br.lines().collect(Collectors.joining());
            }
        } else {
            throw new IllegalArgumentException("File not found in resources! - " + directory + filename);
        }
        return payload;
    }
    public static String getDPSPayload() {
        return DPSPayload;
    }
    public static String getDPSPayload1() {
        return DPSPayload1;
    }
    public static String getDpsPayload2() {
        return DpsPayload2;
    }

}
