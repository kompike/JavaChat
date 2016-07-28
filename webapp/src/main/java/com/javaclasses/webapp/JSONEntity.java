package com.javaclasses.webapp;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Entity for creation and generation of string
 * with necessary data in JSON format
 */
public class JsonEntity {

    private final Map<String, String> jsonObject = new LinkedHashMap<>();

    public void add(String key, String value) {
        jsonObject.put(key, value);
    }

    public int getResponseStatus() {
        final String responseStatus = jsonObject.get("responseStatus");

        if (responseStatus == null) {
            return 404;
        }

        return Integer.valueOf(responseStatus);
    }

    /**
     * Generates string in JSON format
     * @return String in JSON format
     */
    public String generateJson() {

        final StringBuilder builder = new StringBuilder();
        builder.append("{");

        for (Map.Entry<String, String> entry : jsonObject.entrySet()) {
            builder.append("\n");
            builder.append("'").append(entry.getKey()).append("':'").append(entry.getValue()).append("',");
        }

        if (builder.length() > 1) {
            builder.setLength(builder.length() - 1);
        }

        builder.append("\n").append("}");

        return builder.toString();
    }
}
