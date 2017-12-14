package com.example.emir.siparisistemi;

import android.app.Dialog;
import android.content.Context;

import com.example.emir.siparisistemi.Varlikar.Mekan;
import com.google.android.gms.maps.model.LatLng;

public class Depo {
    //Bağlantı URL si
    static String _Url="http://192.168.43.207/NNM/WSNNM.asmx";

    //Kullanıcı Verileri
    static int OnaylananKullaniciID;
    static String KullaniciKayitDurum="Boş";

    static String Kullanici_Adi;
    static String Kullanici_Soyadi;
    static String Kullanici_KulAdi;
    static String Kullanici_FavMekanId;

    static  String Kullanici_FavMekanAdi;
    static  String Kullanici_FavMekanAdresi;


    //Mekan Verileri
    static  int    suAnKiMekanID;
    static  String suAnKiMekanADI;
    static  String suAnKiMekanADRESI;
    static  String suAnKiMekanTELEFONU;
    static  Double suAnKiMekanENLEM= Double.valueOf(0);
    static  Double suAnKiMekanBOYLAM= Double.valueOf(0);
    static  String suAnKiMekanKULADI;
    static  String suAnKiMekanSIFRE;
    static  String MekanKayit_Durum="Boş";


    //Yemek Verileri
    static String yemekCekilenFotoStringi;


    //Yorum Verileri


    //Konum Verileri
    static LatLng konum = new LatLng(0,0);
    static LatLng kullanicininKonumu=new LatLng(0,0);

    //Diyalog Penceresi
    static Dialog dialog;

    static int sehirKodu;
    static  byte acilis=0;

    static void diyalogOlustur(Context context){
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.ivirzivir);
    }


}
