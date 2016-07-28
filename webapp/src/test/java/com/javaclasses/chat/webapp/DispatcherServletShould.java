package com.javaclasses.chat.webapp;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpHeaders.USER_AGENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DispatcherServletShould {

    private static final String URL = "http://localhost:8080/";

    @Test
    public void allowNewUserToRegister() throws IOException {

        final String registrationPageUrl = URL + "register";

        final String nickname = "User";
        final String password = "password";

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("nickname", nickname));
        urlParameters.add(new BasicNameValuePair("password", password));
        urlParameters.add(new BasicNameValuePair("confirmPassword", password));

        final HttpResponse response = generateResponse(registrationPageUrl, urlParameters);

        final String responseContent = getResponseContent(response);
        System.out.println(responseContent);

        assertEquals("Response content does not equal expected JSON.",
                "{'message':'User successfully registered'}", responseContent);
    }

    @Test
    public void allowExistingUserToLogin() throws IOException {

        final String registrationPageUrl = URL + "register";

        final String nickname = "NewUser";
        final String password = "password";

        final List<NameValuePair> registrationUrlParameters = new ArrayList<>();
        registrationUrlParameters.add(new BasicNameValuePair("nickname", nickname));
        registrationUrlParameters.add(new BasicNameValuePair("password", password));
        registrationUrlParameters.add(new BasicNameValuePair("confirmPassword", password));

        generateResponse(registrationPageUrl, registrationUrlParameters);

        final String loginPageUrl = URL + "login";

        final List<NameValuePair> loginUrlParameters = new ArrayList<>();
        loginUrlParameters.add(new BasicNameValuePair("nickname", nickname));
        loginUrlParameters.add(new BasicNameValuePair("password", password));

        final HttpResponse response = generateResponse(loginPageUrl, loginUrlParameters);

        final String responseContent = getResponseContent(response);

        assertTrue("Result must contain tokenId field.",
                responseContent.contains("tokenId"));
        assertTrue("Result must contain userName field with '" + nickname + "' value.",
                responseContent.contains(nickname));
        assertTrue("Result must contain message field.",
                responseContent.contains("User successfully logged in"));
    }

    private HttpResponse generateResponse(String url, List<NameValuePair> urlParameters)
            throws IOException {

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        return client.execute(post);
    }

    private String getResponseContent(HttpResponse response) throws IOException {

        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        final StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }
}
