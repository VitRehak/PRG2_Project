package cz.uhk.pro2;

import cz.uhk.pro2.chatClient.*;
import cz.uhk.pro2.chatFileOperations.XmlChatFileOperations;
import cz.uhk.pro2.gui.MainFrame;
import cz.uhk.pro2.chatFileOperations.ChatFileOperations;
import cz.uhk.pro2.chatFileOperations.CsvChatFileOperations;
import cz.uhk.pro2.chatFileOperations.JsonChatFileOperations;
import cz.uhk.pro2.database.DatabaseOperations;
import cz.uhk.pro2.database.JdbcDatabaseOperations;

public class Main {

    public static void main(String[] args) {

        try {
            String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
            String databaseUrl = "jdbc:derby:ChatClientDb";
            DatabaseOperations databaseOperations = new JdbcDatabaseOperations(databaseDriver, databaseUrl);

            //DbInitializer dbInitializer = new DbInitializer(databaseDriver, databaseUrl);
            //dbInitializer.init();

            ChatFileOperations json = new JsonChatFileOperations();
            ChatFileOperations cSV = new CsvChatFileOperations();
            ChatFileOperations xML = new XmlChatFileOperations();

            ChatClient toFileChatClient = new ToFileChatClient(xML);
            ChatClient databaseChatClient = new DatabaseChatClient(databaseOperations);
            ChatClient webChatClient = new WebChatClient();

            MainFrame mainFrame = new MainFrame(800, 600, toFileChatClient);
            mainFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void testChat() {
        ChatClient chatClient = new InMemoryChatClient();

        System.out.println("Loging in");
        chatClient.login("Vít Řehák");
        System.out.println("user is logged:" + chatClient.isAuthenticated());

        System.out.println("Currently logged in:");
        chatClient.getLoggedUsers().forEach(System.out::println);
        System.out.println("Sending message 1");
        chatClient.sendMessage("Hello world");

        System.out.println("Sending message 2");
        chatClient.sendMessage("hello World 2 ");

        chatClient.getMessages().forEach(System.out::println);

        System.out.println("loging out");
        chatClient.logout();
        System.out.println("user is logged:" + chatClient.isAuthenticated());
    }
}
