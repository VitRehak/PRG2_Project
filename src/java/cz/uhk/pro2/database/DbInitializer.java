package cz.uhk.pro2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbInitializer {
    private final String driver;
    private final String url;

    public DbInitializer(String driver, String url) {
        this.driver = driver;
        this.url = url;
    }

    public void init() {
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            String sqlDropMessagesTable ="DROP TABLE ChatMessages";
            statement.executeUpdate(sqlDropMessagesTable);
            String sqlCreateMessengers =
                    "CREATE TABLE ChatMessages"
                    +"("
                        +"id INT NOT NULL GENERATED ALWAYS AS IDENTITY "
                    +"CONSTRAINT ChatMessages_PK PRIMARY KEY,"
                    +"author VARCHAR(50),"
                    +"text VARCHAR(1000),"
                    +"created timestamp"
                    +")";
            statement.executeUpdate(sqlCreateMessengers);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
