package org.example;

import mod.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientSevice {
    private static final String ID_EXCEPTION_MESSAGE = "ID should be greater than 0";
    private static final String NAME_LENGTH_AND_ID_EXCEPTION_MESSAGE = "Name length should be in range between 2 and 1000 chars and ID should be greater than 0";
    private static final String CREATE_CLIENT = "INSERT INTO client (name) VALUES (?)";
    private static final String CLIENT_BY_ID = "SELECT name FROM client WHERE id = ?";
    private static final String UPDATE_CLIENTS_NAME = "UPDATE client SET name = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM client WHERE id = ?";
    private static final String SELECT_FROM_CLIENT = "SELECT * FROM client";
    private static final String NAME = "name";
    private static final String ID = "id";
    private final Connection connection = Database.getInstance().getConnection();

    public long create(String name) {
        long id = 0;
        try (PreparedStatement createSt = connection.prepareStatement(CREATE_CLIENT, Statement.RETURN_GENERATED_KEYS)) {
            createSt.setString(1, name);
            createSt.executeUpdate();
            ResultSet generatedKeys = createSt.getGeneratedKeys();
            generatedKeys.next();
            id = generatedKeys.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public String getById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(ID_EXCEPTION_MESSAGE);
        }
        String name = null;
        try (PreparedStatement getByIdSt = connection.prepareStatement(CLIENT_BY_ID)) {
            getByIdSt.setLong(1, id);
            ResultSet resultSet = getByIdSt.executeQuery();
            resultSet.next();
            name = resultSet.getString(NAME);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public void setName(long id, String name) {
        if (name.length() < 2 || name.length() > 1000 || id <= 0) {
            throw new IllegalArgumentException(NAME_LENGTH_AND_ID_EXCEPTION_MESSAGE);
        }
        try (PreparedStatement setNameSt = connection.prepareStatement(UPDATE_CLIENTS_NAME)) {
            setNameSt.setString(1, name);
            setNameSt.setLong(2, id);
            setNameSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(ID_EXCEPTION_MESSAGE);
        }
        try (PreparedStatement deleteByIdSt = connection.prepareStatement(DELETE)) {
            deleteByIdSt.setLong(1, id);
            deleteByIdSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Client> listAll() {
        List<Client> clientList = new ArrayList<>();
        try (PreparedStatement listAllSt = connection.prepareStatement(SELECT_FROM_CLIENT)) {
            ResultSet resultSet = listAllSt.executeQuery();
            while (resultSet.next()) {
                Client client = new Client();
                client.setId((resultSet.getLong(ID)));
                client.setName(resultSet.getString(NAME));
                clientList.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientList;
    }

}

