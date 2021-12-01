package cz.uhk.pro2.chatClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cz.uhk.pro2.convertor.WebLocalDateTimeConverter;
import cz.uhk.pro2.models.Message;
import cz.uhk.pro2.models.api.MessageRequest;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebChatClient implements ChatClient {

    //http://fimuhkpro22021.aspifyhost.cz/swagger/index.html

    private final String BASE_URL = "http://fimuhkpro22021.aspifyhost.cz";

    private String loggedUser;
    private String token;

    private List<String> loggedUsers;
    private List<Message> messages;

    private final Gson gson;

    private final List<ActionListener> listenersLoggedUserChanged = new ArrayList<>();
    private final List<ActionListener> listenerMessageAdded = new ArrayList<>();

    public WebChatClient() {
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new WebLocalDateTimeConverter()).create();
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();

        new Thread(() -> {
            Thread.currentThread().setName("refreshLoggedUsersThread");
            while (true) {
                if (isAuthenticated())
                    refreshLoggedUsers();
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            Thread.currentThread().setName("refreshMessagesThread");
            while (true) {
                if (isAuthenticated())
                    refreshMessages();
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public Boolean isAuthenticated() {
        return token != null;
    }

    @Override
    public void login(String userName) {
        try {
            String url = BASE_URL + "/api/chat/login";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity("\"" + userName + "\"", "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == 200) {
                token = EntityUtils.toString(response.getEntity());
                token = token.replaceAll("\"", "");
                loggedUser = userName;
                addMessage(new Message(Message.USER_LOGGED_IN, loggedUser));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logout() {
        try {
            String url = BASE_URL + "/api/chat/logout";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity("\"" + token + "\"", "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == 204) {
                addMessage(new Message(Message.USER_LOGGED_OUT, loggedUser));
                token = null;
                loggedUser = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String text) {
        addMessage(new Message(loggedUser, text));
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void addActionListenerLoggedUserChanged(ActionListener toAdd) {
        listenersLoggedUserChanged.add(toAdd);
    }

    @Override
    public void addActionListenerMessageAdded(ActionListener toAdd) {
        listenerMessageAdded.add(toAdd);
    }

    private void raiseEventListenerUsersChanged() {
        listenersLoggedUserChanged.forEach(l -> l.actionPerformed(new ActionEvent(this, 1, "listenerLoggedUsersChanged")));
    }

    private void raiseEventListenerMessageAdded() {
        listenerMessageAdded.forEach(m -> m.actionPerformed(new ActionEvent(this, 1, "listenerMessageAdded")));
    }

    private void refreshLoggedUsers() {
        try {
            System.out.println("Refreshing users");
            String url = BASE_URL + "/api/chat/getLoggedUsers";
            HttpGet get = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String jsonResult = EntityUtils.toString(entity);
                loggedUsers = gson.fromJson(jsonResult, new TypeToken<ArrayList<String>>() {
                }.getType());

                raiseEventListenerUsersChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMessage(Message message) {
        try {
            MessageRequest messageRequest = new MessageRequest(token, message);
            String url = BASE_URL + "/api/chat/sendMessage";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity(gson.toJson(messageRequest), "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == 204) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshMessages() {
        try {
            System.out.println("Refreshing messages");
            String url = BASE_URL + "/api/chat/getMessages";
            HttpGet get = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String jsonResult = EntityUtils.toString(entity);
                messages = gson.fromJson(jsonResult, new TypeToken<ArrayList<Message>>() {
                }.getType());
                raiseEventListenerMessageAdded();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
