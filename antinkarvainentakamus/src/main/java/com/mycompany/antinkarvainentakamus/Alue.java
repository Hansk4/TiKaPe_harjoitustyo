
package com.mycompany.antinkarvainentakamus;

import java.util.*;
import java.sql.*;

public class Alue {
    private int id;
    private String nimi;
    private List<Keskustelu> keskustelut;
    
    public Alue(int id, String nimi) {
        this.id = id;
        this.nimi = nimi;
        this.keskustelut = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    
    public String getNimi() {
        return nimi;
    }
    
    public List<Keskustelu> getKeskustelut() {
        return keskustelut;
    }
    
    public void lisaaKeskustelu(Keskustelu kk) {
        keskustelut.add(kk);
    }
    
    public int viestejaAlueella() {
        int viesteja = 0;
        for(Keskustelu kk : keskustelut) {
            viesteja += kk.getViestit().size();
        }
        
        return viesteja;
    }
    
    public Timestamp viimeisin() {
        Timestamp nolla = Timestamp.valueOf("1000-1-1 11:11:11");
        
        if(keskustelut == null) {
            return Timestamp.valueOf("1500-1-1 11:11:11");
        }
        
        Timestamp vika = nolla;
        
        for(Keskustelu kk : keskustelut) {
            if (kk.viimeisin().equals(nolla)) {
                continue;
            }
            if (kk.viimeisin().after(vika)) {
                vika = kk.viimeisin();
            }
        }
        
        return vika;
    }
    
    
}
