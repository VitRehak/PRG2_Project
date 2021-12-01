package cz.uhk.pro2.chatFileOperations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.uhk.pro2.models.Message;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlChatFileOperations implements ChatFileOperations {

    private static final String MESSAGES_FILE = "./messages.xml";
    ObjectMapper mapper;

    public XmlChatFileOperations() {
        mapper = new XmlMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public void writeMessagesToFile(List<Message> messages) {
        try {
            mapper.writeValue(new File(MESSAGES_FILE),messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> loadMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            InputStream inputStream = new FileInputStream(new File(MESSAGES_FILE));
            messages = mapper.readValue(inputStream, new TypeReference<List<Message>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }
}
