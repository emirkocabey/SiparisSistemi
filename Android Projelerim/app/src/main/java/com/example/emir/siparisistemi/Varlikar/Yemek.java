package com.example.emir.siparisistemi.Varlikar;

public class Yemek {
    private String Adi;
    private String Resmi;
    private Double Fiyati;
    private int MekanId;
    private Double ToplamPuan;
    private int PuanSayisi;

    public Yemek(){}

    public Yemek(String Adi,String Resmi,Double Fiyati,int MekanId,Double ToplamPuan,int PuanSayisi){
        this.Adi=Adi;
        this.Resmi=Resmi;
        this.Fiyati=Fiyati;
        this.MekanId=MekanId;
        this.ToplamPuan=ToplamPuan;
        this.PuanSayisi=PuanSayisi;
    }

    public String getAdi() {
        return Adi;
    }

    public void setAdi(String adi) {
        Adi = adi;
    }

    public String getResmi() {
        return Resmi;
    }

    public void setResmi(String resmi) {
        Resmi = resmi;
    }

    public Double getFiyati() {
        return Fiyati;
    }

    public void setFiyati(Double fiyati) {
        Fiyati = fiyati;
    }

    public int getMekanId() {
        return MekanId;
    }

    public void setMekanId(int mekanId) {
        MekanId = mekanId;
    }

    public Double getToplamPuan() {
        return ToplamPuan;
    }

    public void setToplamPuan(Double toplamPuan) {
        ToplamPuan = toplamPuan;
    }

    public int getPuanSayisi() {
        return PuanSayisi;
    }

    public void setPuanSayisi(int puanSayisi) {
        PuanSayisi = puanSayisi;
    }
}
