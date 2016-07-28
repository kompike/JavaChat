package com.javaclasses.chat.webapp;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpHeaders.USER_AGENT;

/**
 * Util methods for web tests
 */
public final class TestUtils {

    private TestUtils() {
    }

    public static HttpResponse generateResponse(String url, List<NameValuePair> urlParameters)
            throws IOException {

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        return client.execute(post);
    }

    public static String getResponseContent(HttpResponse response) throws IOException {

        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        final StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    public static List<NameValuePair> getRegistrationUrlParameters(String nickname, String password, String confirmPassword) {

        final List<NameValuePair> registrationUrlParameters = new ArrayList<>();
        registrationUrlParameters.add(new BasicNameValuePair("nickname", nickname));
        registrationUrlParameters.add(new BasicNameValuePair("password", password));
        registrationUrlParameters.add(new BasicNameValuePair("confirmPassword", confirmPassword));

        return registrationUrlParameters;
    }

    public static List<NameValuePair> getLoginUrlParameters(String nickname, String password) {

        final List<NameValuePair> loginUrlParameters = new ArrayList<>();
        loginUrlParameters.add(new BasicNameValuePair("nickname", nickname));
        loginUrlParameters.add(new BasicNameValuePair("password", password));

        return loginUrlParameters;
    }
}
