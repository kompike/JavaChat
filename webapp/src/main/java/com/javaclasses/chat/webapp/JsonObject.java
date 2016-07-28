package com.javaclasses.chat.webapp;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Entity for creation and generation of string
 * with necessary data in JSON format
 */
public class JsonObject {

    private int responseStatusCode;

    private final Map<String, String> jsonObject = new LinkedHashMap<>();

    public void add(String key, String value) {
        jsonObject.put(key, value);
    }

    public int getResponseStatusCode() {

        if (responseStatusCode == 0) {
            return 404;
        }

        return responseStatusCode;
    }

    public void setResponseStatusCode(int responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
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