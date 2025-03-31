package org.framework.Utility;

// src/test/java/utils/RandomDataUtil.java


import org.apache.commons.lang3.RandomStringUtils;

public class RandomDataUtil {
    public static String getRandomString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static String getRandomEmail() {
        return getRandomString(8).toLowerCase() + "@example.com";
    }
}
