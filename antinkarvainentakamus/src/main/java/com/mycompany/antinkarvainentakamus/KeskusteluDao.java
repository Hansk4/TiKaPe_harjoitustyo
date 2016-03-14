package com.mycompany.antinkarvainentakamus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeskusteluDao implements Dao<Keskustelu, Integer> {

    private Database database;
    private AlueDao aDao;

    public KeskusteluDao(Database database, AlueDao a) {
        this.database = database;
        this.aDao = a;
    }

    @Override
    public Keskustelu findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Keskustelu WHERE id = ?");
        statement.setObject(1, key);

        ResultSet rs = statement.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        int alue = rs.getInt("alue_id");

        Alue keskustelunAlue = aDao.findOne(alue);
        Keskustelu kkk = new Keskustelu(id, nimi, keskustelunAlue);

        rs.close();
        statement.close();
        connection.close();

        return kkk;
    }

    @Override
    public List<Keskustelu> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Keskustelu");

        ResultSet rs = statement.executeQuery();
        List<Keskustelu> keskustelut = new ArrayList<>();

        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            int alue = rs.getInt("alue_id");

            Alue keskustelunAlue = aDao.findOne(alue);
            Keskustelu kkk = new Keskustelu(id, nimi, keskustelunAlue);

            keskustelut.add(kkk);
        }

        rs.close();
        statement.close();
        connection.close();

        return keskustelut;
    }

    @Override
    public void delete(Integer key) throws SQLException {

    }

    @Override
    public List<Keskustelu> findAllIn(Collection<Integer> avaimet) throws SQLException {
        if (avaimet.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder muuttujat = new StringBuilder("?");
        for (int i = 1; i < avaimet.size(); i++) {
            muuttujat.append(", ?");
        }

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu WHERE id IN (" + muuttujat + ")");

        int laskuri = 1;
        for (Integer key : avaimet) {
            stmt.setObject(laskuri, key);
            laskuri++;
        }

        ResultSet rs = stmt.executeQuery();
        List<Keskustelu> keskustelut = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            int alue = rs.getInt("alue_id");

            Alue keskustelunAlue = aDao.findOne(alue);
            Keskustelu kkk = new Keskustelu(id, nimi, keskustelunAlue);

            keskustelut.add(kkk);
        }

        rs.close();
        stmt.close();
        connection.close();
        
        return keskustelut;
    }

}
