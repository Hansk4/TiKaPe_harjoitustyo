
package com.mycompany.antinkarvainentakamus;

import java.sql.Timestamp;

public class Viesti {
    private int id;
    private String teksti;
    private String lahettaja;
    private Timestamp aika;
    private Keskustelu keskustelu;
    
    
    public Viesti(int id, String teksti, String lahettaja, Timestamp aika, Keskustelu keskustelu) {
        this.id = id;
        this.teksti = teksti;
        this.lahettaja = lahettaja;
        this.aika = aika;
        this.keskustelu = keskustelu;
    }
    
    public int getId() {
        return id;
    }
    
    public String getTeksti() {
        return teksti;
    }
    
    public String getLahettaja() {
        return lahettaja;
    }
    
    public Timestamp getAika() {
        return aika;
    }
    
    public Keskustelu getKeskustelu() {
        return keskustelu;
    }
}
