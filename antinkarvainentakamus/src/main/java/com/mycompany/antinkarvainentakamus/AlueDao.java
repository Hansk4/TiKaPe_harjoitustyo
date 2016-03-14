package com.mycompany.antinkarvainentakamus;

import java.sql.*;
import java.util.*;

public class AlueDao implements Dao<Alue, Integer> {

    private Database database;

    public AlueDao(Database bb) {
        this.database = bb;
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Alue WHERE id = ?");
        statement.setObject(1, key);

        ResultSet rs = statement.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Alue aaa = new Alue(id, nimi);

        rs.close();
        statement.close();
        connection.close();

        return aaa;
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Alue");

        ResultSet rs = statement.executeQuery();
        List<Alue> alueet = new ArrayList<>();

        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            Alue aaa = new Alue(id, nimi);
            alueet.add(aaa);
        }
        rs.close();
        statement.close();
        connection.close();
        return alueet;
    }

    @Override
    public void delete(Integer key) throws SQLException {

    }

    @Override
    public List<Alue> findAllIn(Collection<Integer> avaimet) throws SQLException {
        if (avaimet.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder muuttujat = new StringBuilder("?");
        for (int i = 1; i < avaimet.size(); i++) {
            muuttujat.append(", ?");
        }

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE id IN (" + muuttujat + ")");

        int laskuri = 1;
        for (Integer key : avaimet) {
            stmt.setObject(laskuri, key);
            laskuri++;
        }

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            Alue aaa = new Alue(id, nimi);
            alueet.add(aaa);
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }

}
