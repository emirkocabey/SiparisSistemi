package com.example.emir.siparisistemi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import static java.lang.String.valueOf;

public class VeriErisim {
    private SQLiteDatabase db;
    private final Context context;
    private final DBHelper dbYardimcisi;

    public VeriErisim(Context c) {
        context=c;
        dbYardimcisi=new DBHelper(context);
    }
    public void veritabaniniKapat(){
        db.close();
    }

    public void veritabaninaBaglan() throws SQLiteException {
        try{
            db= dbYardimcisi.getWritableDatabase();
        }catch (SQLiteException ex){
            db=dbYardimcisi.getReadableDatabase();
        }
    }

    public long KullaniciEkle(int id,String kulAdi,String sifre){
       try{
        ContentValues yeniContentValue = new ContentValues();
        yeniContentValue.put("_id",id);
        yeniContentValue.put("kulAdi",kulAdi);
        yeniContentValue.put("sifre",sifre);

        return db.insert("Kullanici",null,yeniContentValue);
        }catch (SQLiteException ex){
        return -1;
        }
    }
    public long SehirEkle(int id,String adi,Double enlem,Double boylam){
        try{
            ContentValues yeniContentValue = new ContentValues();
            yeniContentValue.put("_id",id);
            yeniContentValue.put("adi",adi);
            yeniContentValue.put("enlem",enlem);
            yeniContentValue.put("boylam",boylam);

            return db.insert("Sehirler",null,yeniContentValue);
        }catch (SQLiteException ex){
            return -1;
        }
    }

    public Cursor SehirleriCek() {
        String sql="SELECT * FROM Sehirler";
        Cursor cursor=db.rawQuery(sql,null);
        return cursor;
    }


    public Cursor KullaniciyiCek() {
        String sql="SELECT * FROM Kullanici";
        Cursor cursor=db.rawQuery(sql,null);
        return cursor;
    }
    public long veritabaniniBosalt() {
        return db.delete("Kullanici",null,null);
    }

    public long SehirleriBosalt() {
        return db.delete("Sehirler",null,null);
    }
    public long TempSehirleriBosalt() {
        return db.delete("Temp_Sehirler",null,null);
    }

    public Cursor SehiriGetir(int i) {
            String sql="SELECT * FROM Sehirler WHERE _id="+i;
            Cursor cursor=db.rawQuery(sql,null);
            return cursor;
    }

    public long Temp_SehirEkle(int i, String string, Double a) {
        try{
                ContentValues yeniContentValue = new ContentValues();
                yeniContentValue.put("_id",i);
                yeniContentValue.put("adi",string);
                yeniContentValue.put("uzaklik",a);

                return db.insert("Temp_Sehirler",null,yeniContentValue);
            }catch (SQLiteException e){
            e.printStackTrace();
                return -1;
            }
    }
    public Cursor TempSehirleriGetir() {
        Cursor cursor = null;
        try {
            String sql = "SELECT _id,adi,uzaklik FROM Temp_Sehirler ORDER BY uzaklik ASC";
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;

    }

    void deneme(){
        //Depo.dialog.show();
        Cursor csSehir = null,tmpSehirler;
        Double a;
        for (int i = 0; i < 81; i++) {
            csSehir = SehiriGetir(i + 1);
            //startManagingCursor(csSehir);
            csSehir.moveToFirst();
                        /*a = ((Depo.konum.latitude - csSehir.getDouble(2))*(Depo.konum.latitude - csSehir.getDouble(2)))+((Depo.konum.longitude-csSehir.getDouble(3))*(Depo.konum.longitude-csSehir.getDouble(3)));
                        */
            a = (Math.pow((csSehir.getDouble(2) - Depo.konum.latitude), 2)) + Math.pow((csSehir.getDouble(3) - Depo.konum.longitude), 2);
            a = Math.sqrt(a);
            Temp_SehirEkle(i + 1, csSehir.getString(1), a);
        }
        csSehir.close();

        //en yakına karar verip döndürme
        tmpSehirler = TempSehirleriGetir();
       //startManagingCursor(tmpSehirler);
        tmpSehirler.moveToFirst();
        Depo.sehirKodu = tmpSehirler.getInt(0);
        tmpSehirler.close();
        TempSehirleriBosalt();
    }
}
