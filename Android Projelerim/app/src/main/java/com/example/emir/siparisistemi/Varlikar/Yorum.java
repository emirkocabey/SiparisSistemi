package com.example.emir.siparisistemi.Varlikar;

public class Yorum {
    private String Icerik;
    private int YemekId;
    private Double ToplamPuan;
    private int PuanSayisi;

    public Yorum(){}

    public Yorum(String Icerik,int YemekId,Double ToplamPuan,int PuanSayisi){
        this.Icerik=Icerik;
        this.YemekId=YemekId;
        this.ToplamPuan=ToplamPuan;
        this.PuanSayisi=PuanSayisi;
    }

    public String getIcerik() {
        return Icerik;
    }

    public void setIcerik(String icerik) {
        Icerik = icerik;
    }

    public int getYemekId() {
        return YemekId;
    }

    public void setYemekId(int yemekId) {
        YemekId = yemekId;
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
