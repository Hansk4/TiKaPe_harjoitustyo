
package com.mycompany.antinkarvainentakamus;

import java.util.*;
import java.sql.*;

public class Keskustelu {
    private int id;
    private String nimi;
    private List<Viesti> viestit;
    private Alue alue;
    
    public Keskustelu(int id, String nimi, Alue alue) {
        this.id = id;
        this.nimi = nimi;
        this.alue = alue;
        this.viestit = new ArrayList<>();
    }

    public int getId() {
        return id;
    }
    
    public String getNimi() {
        return nimi;
    }

    public List<Viesti> getViestit() {
        return viestit;
    }
    
    public Alue getAlue() {
        return alue;
    }

    public void lisaaViesti(Viesti viesti) {
        viestit.add(viesti);
    }
    
    public Timestamp viimeisin() {
        Timestamp nolla = Timestamp.valueOf("1050-1-1 11:11:11");
        Timestamp vika = nolla;
        
        if(viestit.size() == 0) {
            return Timestamp.valueOf("1400-1-1 11:11:11");
        }
        
        for (Viesti vv : viestit) {
            if(vv.getAika() == null) {
                continue;
            }
            
            if(vv.getAika().after(vika)) {
                vika = vv.getAika();
            }
        }
        
//        return viestit.get(viestit.size()-1).getAika();
          return vika;
    }
}
