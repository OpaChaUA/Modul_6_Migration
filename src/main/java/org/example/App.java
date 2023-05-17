package org.example;

import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        Connection connection = Database.getInstance().getConnection();
        new FlayWayConfiguration().initDb();

      ClientSevice clientService = new ClientSevice();
        System.out.println(clientService.create("New_Client"));
        System.out.println(clientService.getById(3));
        clientService.deleteById(4);
        System.out.println(clientService.listAll());

    }
}