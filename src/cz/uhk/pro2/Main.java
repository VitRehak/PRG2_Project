package cz.uhk.pro2;

import cz.uhk.pro2.gui.MainFrame;
import cz.uhk.pro2.models.ChatClient;
import cz.uhk.pro2.models.InMemoryChatClient;

public class Main {

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(800,600);
        mainFrame.setVisible(true);
    }






    public static void testChat() {
        ChatClient chatClient = new InMemoryChatClient();

        System.out.println("Loging in");
        chatClient.login("Vít Řehák");
        System.out.println("user is logged:" + chatClient.isAuthenticated());

        System.out.println("Currently logged in:");
        chatClient.getLoggedUsers().forEach(u -> System.out.println(u));
        System.out.println("Sending message 1");
        chatClient.sendMessage("Hello world");

        System.out.println("Sending message 2");
        chatClient.sendMessage("hello World 2 ");

        chatClient.getMessages().forEach(m -> System.out.println(m));

        System.out.println("loging out");
        chatClient.logout();
        System.out.println("user is logged:" + chatClient.isAuthenticated());

    }
}
