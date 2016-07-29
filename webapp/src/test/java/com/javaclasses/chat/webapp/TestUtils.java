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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.http.HttpHeaders.USER_AGENT;

/**
 * Util methods for web tests
 */
public final class TestUtils {

    private static final String URL = "http://localhost:8080/";
    private static final String REGISTRATION_URL = URL + "register";
    private static final String LOGIN_URL = URL + "login";
    private static final String CHAT_CREATION_URL = URL + "create-chat";
    private static final String JOIN_CHAT_URL = URL + "join-chat";
    private static final String LEAVE_CHAT_URL = URL + "leave-chat";
    private static final String ADD_MESSAGE_URL = URL + "add-message";

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

    public static HttpResponse registerUser(String nickname, String password, String confirmPassword)
            throws IOException {

        final List<NameValuePair> registrationUrlParameters = new ArrayList<>();
        registrationUrlParameters.add(new BasicNameValuePair("nickname", nickname));
        registrationUrlParameters.add(new BasicNameValuePair("password", password));
        registrationUrlParameters.add(new BasicNameValuePair("confirmPassword", confirmPassword));

        return generateResponse(REGISTRATION_URL, registrationUrlParameters);
    }

    public static HttpResponse loginUser(String nickname, String password) throws IOException {

        final List<NameValuePair> loginUrlParameters = new ArrayList<>();
        loginUrlParameters.add(new BasicNameValuePair("nickname", nickname));
        loginUrlParameters.add(new BasicNameValuePair("password", password));

        return generateResponse(LOGIN_URL, loginUrlParameters);
    }

    public static HttpResponse createChat(HttpResponse response, String chatName)
            throws IOException {

        final String tokenId = getParameterFromResponse(response, "tokenId");

        final List<NameValuePair> urlParameters = getChatUrlParameters(chatName, tokenId);

        return generateResponse(CHAT_CREATION_URL, urlParameters);
    }

    public static HttpResponse joinChat(HttpResponse response, String chatName)
            throws IOException {

        final String tokenId = getParameterFromResponse(response, "tokenId");

        final List<NameValuePair> urlParameters = getChatUrlParameters(chatName, tokenId);

        return generateResponse(JOIN_CHAT_URL, urlParameters);
    }

    public static HttpResponse leaveChat(HttpResponse response, String chatName)
            throws IOException {

        final String tokenId = getParameterFromResponse(response, "tokenId");

        final List<NameValuePair> urlParameters = getChatUrlParameters(chatName, tokenId);

        return generateResponse(LEAVE_CHAT_URL, urlParameters);
    }

    public static HttpResponse createMessage(HttpResponse response, String chatName, String message)
            throws IOException {

        final String tokenId = getParameterFromResponse(response, "tokenId");

        final List<NameValuePair> addMessageUrlParameters = new ArrayList<>();
        addMessageUrlParameters.add(new BasicNameValuePair("chatName", chatName));
        addMessageUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));
        addMessageUrlParameters.add(new BasicNameValuePair("message", message));

        return generateResponse(ADD_MESSAGE_URL, addMessageUrlParameters);
    }

    private static List<NameValuePair> getChatUrlParameters(String chatName, String tokenId) {

        final List<NameValuePair> chatUrlParameters = new ArrayList<>();
        chatUrlParameters.add(new BasicNameValuePair("chatName", chatName));
        chatUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        return chatUrlParameters;
    }

    private static String getParameterFromResponse(HttpResponse httpResponse, String parameter)
            throws IOException {
        final String loginResponse = getResponseContent(httpResponse);
        final Pattern pattern = Pattern.compile(String.format("\'%s\':'(\\d+|\\w+)'", parameter));
        final Matcher matcher = pattern.matcher(loginResponse);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }
}
