package com.javaclasses.chat.webapp;

import org.apache.http.HttpEntity;
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

/*package*/ final class TestUtils {

    // URL constants
    private static final String URL = "http://localhost:8080/api/";
    private static final String REGISTRATION_URL = URL + "register";
    private static final String LOGIN_URL = URL + "login";
    private static final String CHAT_CREATION_URL = URL + "chats";
    private static final String JOIN_CHAT_URL = URL + "membership/join";
    private static final String LEAVE_CHAT_URL = URL + "membership/leave";
    private static final String ADD_MESSAGE_URL = URL + "messages";

    // Parameter constants
    private static final String NICKNAME = "nickname";
    private static final String PASSWORD = "password";
    private static final String CHAT_NAME = "chatName";
    private static final String TOKEN_ID = "tokenId";
    private static final String CONFIRM_PASSWORD = "confirmPassword";
    private static final String MESSAGE = "message";
    private static final String COLOR = "color";


    private TestUtils() {
    }

    /*package*/ static String getResponseContent(HttpEntity httpEntity) throws IOException {

        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(httpEntity.getContent()));

        final StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    /*package*/ static HttpEntity registerUser(String nickname, String password, String confirmPassword)
            throws IOException {

        final List<NameValuePair> registrationUrlParameters = new ArrayList<>();
        registrationUrlParameters.add(new BasicNameValuePair(NICKNAME, nickname));
        registrationUrlParameters.add(new BasicNameValuePair(PASSWORD, password));
        registrationUrlParameters.add(new BasicNameValuePair(CONFIRM_PASSWORD, confirmPassword));

        return generatePostResponse(REGISTRATION_URL, registrationUrlParameters);
    }

    /*package*/ static HttpEntity loginUser(String nickname, String password) throws IOException {

        final List<NameValuePair> loginUrlParameters = new ArrayList<>();
        loginUrlParameters.add(new BasicNameValuePair(NICKNAME, nickname));
        loginUrlParameters.add(new BasicNameValuePair(PASSWORD, password));

        return generatePostResponse(LOGIN_URL, loginUrlParameters);
    }

    /*package*/ static HttpEntity createChat(String tokenId, String chatName)
            throws IOException {

        final List<NameValuePair> urlParameters = getChatUrlParameters(chatName, tokenId);

        return generatePostResponse(CHAT_CREATION_URL, urlParameters);
    }

    /*package*/ static HttpEntity joinChat(String tokenId, String chatName)
            throws IOException {

        final List<NameValuePair> urlParameters = getChatUrlParameters(chatName, tokenId);

        return generatePostResponse(JOIN_CHAT_URL, urlParameters);
    }

    /*package*/ static HttpEntity leaveChat(String tokenId, String chatName)
            throws IOException {

        final List<NameValuePair> urlParameters = getChatUrlParameters(chatName, tokenId);

        return generatePostResponse(LEAVE_CHAT_URL, urlParameters);
    }

    /*package*/ static HttpEntity createMessage(String tokenId, String chatName, String message, String color)
            throws IOException {

        final List<NameValuePair> addMessageUrlParameters = new ArrayList<>();
        addMessageUrlParameters.add(new BasicNameValuePair(CHAT_NAME, chatName));
        addMessageUrlParameters.add(new BasicNameValuePair(TOKEN_ID, tokenId));
        addMessageUrlParameters.add(new BasicNameValuePair(MESSAGE, message));
        addMessageUrlParameters.add(new BasicNameValuePair(COLOR, color));

        return generatePostResponse(ADD_MESSAGE_URL, addMessageUrlParameters);
    }

    /*package*/ static String getParameterFromResponse(HttpEntity httpEntity, String parameter)
            throws IOException {
        final String loginResponse = getResponseContent(httpEntity);
        final Pattern pattern = Pattern.compile(String.format("\'%s\':'(\\d+|\\w+)'", parameter));
        final Matcher matcher = pattern.matcher(loginResponse);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    private static HttpEntity generatePostResponse(String url, List<NameValuePair> urlParameters)
            throws IOException {

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        final HttpResponse response = client.execute(post);
        return response.getEntity();
    }

    private static List<NameValuePair> getChatUrlParameters(String chatName, String tokenId) {

        final List<NameValuePair> chatUrlParameters = new ArrayList<>();
        chatUrlParameters.add(new BasicNameValuePair(CHAT_NAME, chatName));
        chatUrlParameters.add(new BasicNameValuePair(TOKEN_ID, tokenId));

        return chatUrlParameters;
    }
}
