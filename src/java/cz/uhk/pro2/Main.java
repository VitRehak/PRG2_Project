package cz.uhk.pro2;

import cz.uhk.pro2.gui.MainFrame;
import cz.uhk.pro2.models.ChatClient;
import cz.uhk.pro2.models.InMemoryChatClient;
import cz.uhk.pro2.models.ToFileChatClient;
import cz.uhk.pro2.models.chatFileOperations.ChatFileOperations;
import cz.uhk.pro2.models.chatFileOperations.JsonChatFileOperations;

public class Main {

    public static void main(String[] args) {
        ChatFileOperations chatFileOperations = new JsonChatFileOperations();
        ChatClient chatClient = new ToFileChatClient(chatFileOperations);

        MainFrame mainFrame = new MainFrame(800, 600,chatClient);
        mainFrame.setVisible(true);
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
