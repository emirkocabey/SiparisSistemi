package com.example.emir.siparisistemi.Varlikar;

public class Mekan {
    private int Id;
    private String Adi;
    private String Adresi;
    private Double Enlem;
    private Double Boylam;
    private String MekanKulAdi;
    private String Sifre;
    private String Telefon;

    public Mekan(){}

    public Mekan(String Adi,String Adresi,Double Enlem,Double Boylam,String MekanKulAdi,String Sifre,String Telefon){
        this.Adi=Adi;
        this.Adresi=Adresi;
        this.Enlem=Enlem;
        this.Boylam=Boylam;
        this.MekanKulAdi=MekanKulAdi;
        this.Sifre=Sifre;
        this.Telefon=Telefon;
    }
    public int getId(){return Id;}

    public void setId(int id){Id=id;}

    public String getAdi() {return Adi;}

    public void setAdi(String adi) {
        Adi = adi;
    }

    public String getAdresi() {
        return Adresi;
    }

    public void setAdresi(String adresi) {
        Adresi = adresi;
    }

    public Double getEnlem() {
        return Enlem;
    }

    public void setEnlem(Double enlem) {
        Enlem = enlem;
    }

    public Double getBoylam() {
        return Boylam;
    }

    public void setBoylam(Double boylam) {
        Boylam = boylam;
    }

    public String getMekanKulAdi() {
        return MekanKulAdi;
    }

    public void setMekanKulAdi(String mekanKulAdi) {
        MekanKulAdi = mekanKulAdi;
    }

    public String getSifre() {
        return Sifre;
    }

    public void setSifre(String sifre) {
        Sifre = sifre;
    }

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String telefon) {
        Telefon = telefon;
    }
}
