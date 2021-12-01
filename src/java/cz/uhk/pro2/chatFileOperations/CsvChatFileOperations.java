package cz.uhk.pro2.chatFileOperations;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import cz.uhk.pro2.models.Message;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class CsvChatFileOperations implements ChatFileOperations {
    private static final String MESSAGES_FILE = "./messages.csv";

    @Override
    public List<Message> loadMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(MESSAGES_FILE), '\t');
            List<String[]> tmp = reader.readAll();
            for (String[] s : tmp) {
                LocalDateTime localDateTime = LocalDateTime.of(
                        Integer.parseInt(s[2]),
                        Month.valueOf(s[3]),
                        Integer.parseInt(s[4]),
                        Integer.parseInt(s[5]),
                        Integer.parseInt(s[6]),
                        Integer.parseInt(s[7]),
                        Integer.parseInt(s[8]));
                messages.add(new Message(s[0], s[1], localDateTime));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public void writeMessagesToFile(List<Message> messages) {

        try {
            CSVWriter writer = new CSVWriter(new FileWriter(MESSAGES_FILE), '\t');
            for (Message m : messages) {
                String[] entries = new String[]{
                        m.getAuthor(),
                        m.getText(),
                        String.valueOf(m.getCreated().getYear()),
                        String.valueOf(m.getCreated().getMonth()),
                        String.valueOf(m.getCreated().getDayOfMonth()),
                        String.valueOf(m.getCreated().getHour()),
                        String.valueOf(m.getCreated().getMinute()),
                        String.valueOf(m.getCreated().getSecond()),
                        String.valueOf(m.getCreated().getNano())
                };
                writer.writeNext(entries);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
