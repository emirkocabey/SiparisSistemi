package com.example.emir.siparisistemi.Varlikar;


public class Kullanici {
    private String Adi;
    private String Soyadi;
    private String KullaniciAdi;
    private String Sifre;

    public Kullanici(){}

    public Kullanici(String Adi,String Soyadi,String KullaniciAdi,String Sifre){
        this.Adi=Adi;
        this.Soyadi=Soyadi;
        this.KullaniciAdi=KullaniciAdi;
        this.Sifre=Sifre;
    }

    public String getAdi() {
        return Adi;
    }

    public void setAdi(String adi) {
        Adi = adi;
    }

    public String getSoyadi() {
        return Soyadi;
    }

    public void setSoyadi(String soyadi) {
        Soyadi = soyadi;
    }

    public String getKullaniciAdi() {
        return KullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        KullaniciAdi = kullaniciAdi;
    }

    public String getSifre() {
        return Sifre;
    }

    public void setSifre(String sifre) {
        Sifre = sifre;
    }
}
