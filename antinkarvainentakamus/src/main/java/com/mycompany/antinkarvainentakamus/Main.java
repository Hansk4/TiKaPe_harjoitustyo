package com.mycompany.antinkarvainentakamus;

import java.sql.SQLException;
import java.util.*;
import spark.*;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Database bb = new Database("jdbc:sqlite:foorumi.db");

        AlueDao aDao = new AlueDao(bb);
        KeskusteluDao kDao = new KeskusteluDao(bb, aDao);
        ViestiDao vDao = new ViestiDao(bb, kDao);

        List<Alue> alueet = aDao.findAll();

        List<Keskustelu> keskustelut = kDao.findAll();

        List<Viesti> viestit = vDao.findAll();

        for (Alue aaa : alueet) {
            for (Keskustelu kkk : keskustelut) {
                if (aaa.getId() == kkk.getAlue().getId()) {
                    aaa.lisaaKeskustelu(kkk);
                }
            }
        }
        
        for (Keskustelu kk : keskustelut) {
            for (Viesti vv : viestit) {
                if (kk.getId() == vv.getKeskustelu().getId()) {
                    kk.lisaaViesti(vv);
                }
            }
        }

        get("/foorumi", (req, res) -> {
            HashMap<String, List> map = new HashMap();
            map.put("alueet", alueet);

            return new ModelAndView(map, "foorumi");
        }, new ThymeleafTemplateEngine());

        get("/alue/:alue_id", (req, res) -> {
            int alue_id = Integer.parseInt(req.params(":alue_id"));
            Alue haluttu = aDao.findOne(alue_id);

            HashMap<String, Alue> map = new HashMap<>();
            map.put("alue", haluttu);

            return new ModelAndView(map, "keskustelut");
        }, new ThymeleafTemplateEngine());

        get("/keskustelu/:keskustelu_id", (req, res) -> {
            int keskustelu_id = Integer.parseInt(req.params(":keskustelu_id"));
            Keskustelu haluttu = kDao.findOne(keskustelu_id);

            HashMap<String, Keskustelu> map = new HashMap<>();
            map.put("keskustelu", haluttu);

            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());

        post("/foorumi", (Request req, Response res) -> {
            String nim = req.queryParams("nimi");

            Object nimi = nim;
            bb.update("INSERT INTO Alue(nimi) VALUES(?)", nimi);

            List<Alue> PaivitetytAlueet = paivitaA(aDao, kDao);

            HashMap<String, List> map = new HashMap();
            map.put("alueet", PaivitetytAlueet);

            return new ModelAndView(map, "foorumi");
        }, new ThymeleafTemplateEngine());

        post("/alue/:alue_id", (Request req, Response res) -> {
            int alue_id = Integer.parseInt(req.params(":alue_id"));
            
            String ots = req.queryParams("otsikko");

            Object otsikko = ots;
            Object alueID = alue_id;
            bb.update("INSERT INTO Keskustelu(nimi, alue_id) VALUES(?,?)", otsikko, alueID);

            List<Keskustelu> PaivitetytKeskustelut =paivitaK(kDao, vDao);

            Alue haluttu = aDao.findOne(alue_id);

            HashMap<String, Alue> map = new HashMap<>();
            map.put("alue", haluttu);

            return new ModelAndView(map, "keskustelut");
        }, new ThymeleafTemplateEngine());
        
        post("/keskustelu/:keskustelu_id", (Request req, Response res) -> {
            int keskustelu_id = Integer.parseInt(req.params(":keskustelu_id"));
            
            String tekst = req.queryParams("teksti");
            String lah = req.queryParams("lahettaja");

            Object teksti = tekst;
            Object lahettaja = lah;
            Object keskusteluID = keskustelu_id;
            bb.update("INSERT INTO Viesti(teksti, lahettaja, keskustelu_id) VALUES(?,?,?)", teksti, lahettaja, keskusteluID);

            List<Viesti> PaivitetytViestit =paivitaV(vDao);

            Keskustelu haluttu = kDao.findOne(keskustelu_id);

            HashMap<String, Keskustelu> map = new HashMap<>();
            map.put("keskustelu", haluttu);

            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());
    }

    public static List<Alue> paivitaA(AlueDao aDao, KeskusteluDao kDao) throws SQLException {

        List<Alue> alueet = aDao.findAll();
        List<Keskustelu> keskustelut = kDao.findAll();

        for (Alue aaa : alueet) {
            for (Keskustelu kkk : keskustelut) {
                if (aaa.getId() == kkk.getAlue().getId()) {
                    aaa.lisaaKeskustelu(kkk);
                }
            }
        }

        return alueet;
    }

    public static List<Keskustelu> paivitaK(KeskusteluDao kDao, ViestiDao vDao) throws SQLException {

        List<Keskustelu> keskustelut = kDao.findAll();
        List<Viesti> viestit = vDao.findAll();

        for (Keskustelu kk : keskustelut) {
            for (Viesti vv : viestit) {
                if (kk.getId() == vv.getKeskustelu().getId()) {
                    kk.lisaaViesti(vv);
                }
            }
        }

        return keskustelut;
    }
    
    public static List<Viesti> paivitaV(ViestiDao vDao) throws SQLException {
        
        List<Viesti> viestit = vDao.findAll();
        return viestit;
    }
}
