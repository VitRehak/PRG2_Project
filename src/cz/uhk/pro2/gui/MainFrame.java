package cz.uhk.pro2.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTextField txtInputName, txtInputMessage;
    private JButton btnLogin, btnSend;
    private JTextArea txtAreaChat;

    public MainFrame(int width, int height) {
        super("PRO2 Chat client 2021");
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initGui();
    }

    private void initGui(){
        JPanel panelMain = new JPanel(new BorderLayout());

        JPanel panelLogin = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelChat  = new JPanel();
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.LEFT));



        //login
        panelLogin.add(new JLabel("Jméno"));
        txtInputName = new JTextField("",30);
        panelLogin.add(txtInputName);
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> {
            System.out.println("Btn login click - "+txtInputName.getText());
        });
        panelLogin.add(btnLogin);
        //konec loginu



        //panel chat
        panelChat.setLayout(new BoxLayout(panelChat,BoxLayout.X_AXIS));
        txtAreaChat = new JTextArea();
        txtAreaChat.setEditable(false);
        txtAreaChat.setAutoscrolls(true);
        JScrollPane scrollPane = new JScrollPane(txtAreaChat);
        panelChat.add(scrollPane);
        for (int i = 0; i < 50; i++) {
            txtAreaChat.append("text:"+i+"\n");
        }
        //konec panel chat



        //patička
        txtInputMessage = new JTextField("",50);
        panelFooter.add(txtInputMessage);
        btnSend = new JButton("Send");
        btnSend.addActionListener(e -> {
            System.out.println("BtnSend - "+ txtInputMessage.getText());
        });
        panelFooter.add(btnSend);
        //konec patičky



        panelMain.add(panelLogin,BorderLayout.NORTH);
        panelMain.add(panelChat,BorderLayout.CENTER);
        panelMain.add(panelFooter,BorderLayout.SOUTH);
        add(panelMain);
    }
}
