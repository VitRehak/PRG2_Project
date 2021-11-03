package cz.uhk.pro2.models.chatFileOperations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cz.uhk.pro2.models.Message;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonChatFileOperations implements ChatFileOperations{
    private static final String MESSAGES_FILE = "./messages.json";
    private final Gson gson;

    public JsonChatFileOperations() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public List<Message> loadMessages() {
        File f = new File(MESSAGES_FILE);
        if (f.exists() && !f.isDirectory())
            try {
                FileReader reader = new FileReader(MESSAGES_FILE);
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    json.append(line);
                }
                Type targetType = new TypeToken<ArrayList<Message>>() {
                }.getType();
                return gson.fromJson(json.toString(), targetType);
            } catch (IOException e) {
                e.printStackTrace();

            }
        return new ArrayList<>();
    }
    @Override
    public void writeMessagesToFile(List<Message> messages) {
        String json = gson.toJson(messages);
        try {
            FileWriter writer = new FileWriter(MESSAGES_FILE);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> loadLoggedUsers() {
        return null;
    }

    @Override
    public void writeLoggedUsers(List<String> users) {

    }
}
