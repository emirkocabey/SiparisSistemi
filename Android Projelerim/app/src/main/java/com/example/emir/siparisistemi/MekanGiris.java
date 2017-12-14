package com.example.emir.siparisistemi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

public class MekanGiris extends AppCompatActivity {
    Button btn;
    EditText ed1, ed2;
    TextView tv, tvMekanKayitOl, tvMekanGiris_KullaniciGiris;
    Intent i, i2, i3;

    Switch swMekanGirisHatirla;

    String kullaniciAdi, sifre;

    private VeriErisim dbYardimcisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mekan_giris);
        getSupportActionBar().hide();
        init();

        dbYardimcisi = new VeriErisim(this);
        dbYardimcisi.veritabaninaBaglan();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kullaniciAdi = ed1.getText().toString();
                sifre = ed2.getText().toString();

                new wsMekanGiris().execute();
            }
        });
        tvMekanKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i2);
            }
        });
        tvMekanGiris_KullaniciGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i3);
                finish();
            }
        });
    }

    private void init() {
        btn = (Button) findViewById(R.id.btnMekanGirisYap);

        ed1 = (EditText) findViewById(R.id.edtMekanKullaniciAdi);
        ed2 = (EditText) findViewById(R.id.edtMekanSifre);

        tv = (TextView) findViewById(R.id.tvDurum2);
        tvMekanKayitOl = (TextView) findViewById(R.id.tvMekanGirisMekanKayitOl);
        tvMekanGiris_KullaniciGiris = (TextView) findViewById(R.id.tvMekanGiris_KullaniciGiris);

        swMekanGirisHatirla = (Switch) findViewById(R.id.swMekanGirisHatirla);

        i = new Intent(this, MekanSayfasi.class);
        i2 = new Intent(this, MekanKayit.class);
        i3 = new Intent(this, KullaniciGiris.class);

        Depo.diyalogOlustur(this);
    }

    //**************************************************************************************************
    private class wsMekanGiris extends AsyncTask<Void, Void, Void> {

        private final String _Namspace = "http://tempuri.org/";
        private final String _MethodName = "Giris";
        private final String _Action = _Namspace + _MethodName;
        private String _ResultValue;

        protected void onPreExecute() {
            Depo.dialog.show();
        }

        protected Void doInBackground(Void... voids) {

            SoapObject request = new SoapObject(_Namspace, _MethodName);

            request.addProperty("tablo", "Mekan");
            request.addProperty("kulAdi", kullaniciAdi);
            request.addProperty("sifre", sifre);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            AndroidHttpTransport aht = new AndroidHttpTransport(Depo._Url);
            aht.debug = true;

            try {
                aht.call(_Action, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                _ResultValue = response.toString();
            } catch (Exception e) {
                _ResultValue = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            Depo.dialog.dismiss();
            try {
                int id = Integer.parseInt(_ResultValue);
                if (id > 0) {
                    Depo.suAnKiMekanID = id;
                    if (swMekanGirisHatirla.isChecked()) {
                        dbYardimcisi.veritabaniniBosalt();
                        dbYardimcisi.KullaniciEkle(2, kullaniciAdi, sifre);
                    }
                    startActivity(i);
                    dbYardimcisi.veritabaniniKapat();
                    finish();
                } else {
                        tv.setText("Kullanıcı Adı veya Şifresi Yanlış !");
                        ed2.setText(null);
                    }
                }catch(Exception e){
                    tv.setText("Server' a bağlanılamıyor !");
                    ed2.setText(null);
                }
            }

    }
}


