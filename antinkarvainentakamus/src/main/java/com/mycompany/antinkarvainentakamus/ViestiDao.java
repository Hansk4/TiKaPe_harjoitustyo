package com.mycompany.antinkarvainentakamus;

import java.sql.*;
import java.util.*;

public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;
    private KeskusteluDao kDao;

    public ViestiDao(Database database, KeskusteluDao k) {
        this.database = database;
        this.kDao = k;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Viesti WHERE id = ?");
        statement.setObject(1, key);

        ResultSet rs = statement.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String teksti = rs.getString("teksti");
        String lahettaja = rs.getString("lahettaja");
        Timestamp aika = rs.getTimestamp("lahetyshetki");
        int keskustelu = rs.getInt("keskustelu_id");

        Keskustelu viestinKeskustelu = kDao.findOne(keskustelu);
        Viesti viesti = new Viesti(id, teksti, lahettaja, aika, viestinKeskustelu);

        rs.close();
        statement.close();
        connection.close();

        return viesti;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT id, teksti, lahettaja, strftime('%s', 'lahetyshetki') as hetki, keskustelu_id FROM Viesti");

        ResultSet rs = statement.executeQuery();
        List<Viesti> viestit = new ArrayList<>();

        while (rs.next()) {
            Integer id = rs.getInt("id");
            String teksti = rs.getString("teksti");
            String lahettaja = rs.getString("lahettaja");
            Timestamp aika = rs.getTimestamp("hetki");
            int keskustelu = rs.getInt("keskustelu_id");

            Keskustelu viestinKeskustelu = kDao.findOne(keskustelu);
            Viesti viesti = new Viesti(id, teksti, lahettaja, aika, viestinKeskustelu);

            viestit.add(viesti);
        }

        rs.close();
        statement.close();
        connection.close();

        return viestit;
    }

    @Override
    public void delete(Integer key) throws SQLException {

    }

    @Override
    public List<Viesti> findAllIn(Collection<Integer> avaimet) throws SQLException {
        if (avaimet.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder muuttujat = new StringBuilder("?");
        for (int i = 1; i < avaimet.size(); i++) {
            muuttujat.append(", ?");
        }

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE id IN (" + muuttujat + ")");

        int laskuri = 1;
        for (Integer key : avaimet) {
            stmt.setObject(laskuri, key);
            laskuri++;
        }

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String teksti = rs.getString("teksti");
            String lahettaja = rs.getString("lahettaja");
            Timestamp aika = rs.getTimestamp("lahetyshetki");
            int keskustelu = rs.getInt("keskustelu_id");

            Keskustelu viestinKeskustelu = kDao.findOne(keskustelu);
            Viesti viesti = new Viesti(id, teksti, lahettaja, aika, viestinKeskustelu);

            viestit.add(viesti);
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

}
