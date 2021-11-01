package cz.uhk.pro2.gui;

import cz.uhk.pro2.models.ChatClient;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTextField txtInputName, txtInputMessage;
    private JButton btnLogin, btnSend;
    private JTextArea txtAreaChat;
    private JTable tblLoggedUsers;

    private LoggedUserTableModel loggedUserTableModel;
    private ChatClient chatClient;

    public MainFrame(int width, int height, ChatClient chatClient) {
        super("PRO2 Chat client 2021");
        this.chatClient = chatClient;
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initGui();
    }

    private void initGui() {
        JPanel panelMain = new JPanel(new BorderLayout());
        JPanel panelLogin = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelChat = new JPanel();
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelLoggedUser = new JPanel();

        initLoginPanel(panelLogin);
        initChatPanel(panelChat);
        initFooterPanel(panelFooter);
        initLoggedUsersPanel(panelLoggedUser);


        panelMain.add(panelLogin, BorderLayout.NORTH);
        panelMain.add(panelChat, BorderLayout.CENTER);
        panelMain.add(panelFooter, BorderLayout.SOUTH);
        panelMain.add(panelLoggedUser, BorderLayout.EAST);
        add(panelMain);
    }

    private void initLoginPanel(JPanel panelLogin) {
        panelLogin.add(new JLabel("Jméno"));
        txtInputName = new JTextField("", 30);
        panelLogin.add(txtInputName);
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> {
            if (chatClient.isAuthenticated()) {
                chatClient.logout();
                txtInputName.setEditable(true);
                btnLogin.setText("Login");
                txtAreaChat.setEnabled(false);

            } else {
                if (txtInputName.getText().length() > 0) {
                    chatClient.login(txtInputName.getText());
                    txtInputName.setEditable(false);
                    btnLogin.setText("Logout");
                    txtAreaChat.setEnabled(true);

                } else {
                    JOptionPane.showMessageDialog(null, "Enter your user name", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            //refreshMessages();
        });
        panelLogin.add(btnLogin);
    }

    private void initChatPanel(JPanel panelChat) {
        panelChat.setLayout(new BoxLayout(panelChat, BoxLayout.X_AXIS));
        txtAreaChat = new JTextArea();
        txtAreaChat.setEditable(false);
        txtAreaChat.setAutoscrolls(true);
        txtAreaChat.setEnabled(false);
        chatClient.addActionListenerMessageAdded(e -> refreshMessages());
        JScrollPane scrollPane = new JScrollPane(txtAreaChat);
        panelChat.add(scrollPane);
    }

    private void initFooterPanel(JPanel panelFooter) {
        txtInputMessage = new JTextField("", 50);
        panelFooter.add(txtInputMessage);
        btnSend = new JButton("Send");
        btnSend.addActionListener(e -> {
            String text = txtInputMessage.getText();
            if (text.length() == 0)
                return;
            if (!chatClient.isAuthenticated())
                return;
            chatClient.sendMessage(text);
            txtInputMessage.setText("");
            //refreshMessages();


        });
        panelFooter.add(btnSend);
    }

    private void initLoggedUsersPanel(JPanel panelLoggedUser) {
        /*Object[][]data= new Object[][]{
                {"1:1","1:2"},
                {"2:1","2:2"},
                {"3:1","3:2"}
        };
        String[]colNames=new String[]{"Column 1", "Column 2"};
        tblLoggedUsers = new JTable(data,colNames);*/
        loggedUserTableModel = new LoggedUserTableModel(chatClient);
        tblLoggedUsers = new JTable();
        tblLoggedUsers.setModel(loggedUserTableModel);

        chatClient.addActionListenerLoggedUserChanged(e -> loggedUserTableModel.fireTableDataChanged());
        JScrollPane scrollPane = new JScrollPane(tblLoggedUsers);
        scrollPane.setPreferredSize(new Dimension(250, 500));
        panelLoggedUser.add(scrollPane);
    }

    private void refreshMessages() {
        txtAreaChat.setText("");
        chatClient.getMessages().forEach(message -> {
            txtAreaChat.append(message.toString());
            txtAreaChat.append("\n");
        });
    }
}
