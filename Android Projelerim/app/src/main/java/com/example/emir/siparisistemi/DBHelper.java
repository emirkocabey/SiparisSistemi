package com.example.emir.siparisistemi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static String veritabani_adi="SiparisSistemi.db";
    public static int veritabani_versiyonu= 1 ;

    public DBHelper(Context context) {
        super(context,veritabani_adi,null,veritabani_versiyonu);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Kullanici(_id INTEGER NOT NULL PRIMARY KEY,kulAdi TEXT,sifre TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE Sehirler(_id INTEGER NOT NULL PRIMARY KEY,adi TEXT,enlem REAL,boylam REAL);");
        sqLiteDatabase.execSQL("CREATE TABLE Temp_Sehirler(_id INTEGER NOT NULL PRIMARY KEY,adi TEXT,uzaklik REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP Table If Exists Kullanici");
        sqLiteDatabase.execSQL("DROP Table If Exists Sehirler");
        sqLiteDatabase.execSQL("DROP Table If Exists Temp_Sehirler");
        this.onCreate(sqLiteDatabase);
    }
}
