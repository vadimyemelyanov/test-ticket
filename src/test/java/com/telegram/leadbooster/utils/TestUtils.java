package com.telegram.leadbooster.utils;

import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestUtils {


    public static String getFileContent(String path) {
        InputStream resource = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(path);

        String expectedResponse = null;
        try {
            expectedResponse = IOUtils.toString(resource, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return expectedResponse;
    }
}
