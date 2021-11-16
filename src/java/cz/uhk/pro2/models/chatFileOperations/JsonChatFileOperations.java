package cz.uhk.pro2.models.chatFileOperations;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import cz.uhk.pro2.convertor.LocalDateTimeConverter;
import cz.uhk.pro2.models.Message;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonChatFileOperations implements ChatFileOperations {
    private static final String MESSAGES_FILE = "./messages.json";
    private final Gson gson;

    public JsonChatFileOperations() {
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter()).setPrettyPrinting().create();
    }

    @Override
    public List<Message> loadMessages() {
        List<Message> messages = new ArrayList<>();
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
            messages = gson.fromJson(json.toString(), targetType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public void writeMessagesToFile(List<Message> messages) {
        Type targetType = new TypeToken<ArrayList<Message>>() {
        }.getType();
        String json = gson.toJson(messages, targetType);
        try {
            FileWriter writer = new FileWriter(MESSAGES_FILE);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
