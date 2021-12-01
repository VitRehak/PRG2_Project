package cz.uhk.pro2.gui;

import cz.uhk.pro2.chatClient.ChatClient;

import javax.swing.table.AbstractTableModel;

public class LoggedUserTableModel extends AbstractTableModel {
    ChatClient chatClient;

    public LoggedUserTableModel(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public int getRowCount() {
        return chatClient.getLoggedUsers().size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return chatClient.getLoggedUsers().get(rowIndex);
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0:
                return "User name";
            default:
                return null;
        }
    }
}
