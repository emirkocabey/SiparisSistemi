package com.example.emir.siparisistemi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

public class KullaniciGiris extends AppCompatActivity {

    Button btn;
    EditText ed1,ed2;
    Switch swKullaniciGirisHatirla;
    TextView tvDurum,tvKullaniciGirisKayitOl,tvKullaniciGirisMEkanOlarakGirisYap;
    Intent i1,i2,i3;

    String kullaniciAdi,sifre;

    VeriErisim dbYardimcisi;

    byte sayac=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kullanici_giris);
        getSupportActionBar().hide();

        init();

        dbYardimcisi=new VeriErisim(this);
        dbYardimcisi.veritabaninaBaglan();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDurum.setText("");

                kullaniciAdi=ed1.getText().toString();
                sifre=ed2.getText().toString();

                new wsKullaniciGiris().execute();
            }
        });
        tvKullaniciGirisKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i1);
            }
        });

        tvKullaniciGirisMEkanOlarakGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i3);
                finish();
            }
        });}

    @Override
    public void onBackPressed(){
     sayac++;
     if(sayac==1){
         Toast.makeText(getApplicationContext(),"Çıkmak için bir kez daha geriye basın",Toast.LENGTH_LONG).show();
     }  else if(sayac==2){
         finish();
     }
    }

    private void init(){
        btn=(Button)findViewById(R.id.btnKullaniciGirisYap);

        ed1=(EditText)findViewById(R.id.edtKullaniciAdi);
        ed2=(EditText)findViewById(R.id.edtKullaniciSifre);

        tvDurum=(TextView)findViewById(R.id.tvDurum);
        tvKullaniciGirisKayitOl=(TextView)findViewById(R.id.tvKullaniciGirisKayitOl);
        tvKullaniciGirisMEkanOlarakGirisYap=(TextView)findViewById(R.id.tvKullaniciGirisMekanOlarakGirisYap);

        Depo.diyalogOlustur(this);

        swKullaniciGirisHatirla=(Switch)findViewById(R.id.swKullaniciGirisHatirla);

        i1=new Intent(this,KullaniciKayit.class);
        i2=new Intent(this,KullaniciSayfasi.class);
        i3=new Intent(this,MekanGiris.class);
    }

//**************************************************************************************************
private class wsKullaniciGiris extends AsyncTask<Void,Void,Void> {

    private final String _Namspace = "http://tempuri.org/";
    private final String _MethodName = "Giris";
    private final String _Action = _Namspace+_MethodName;
    private String _ResultValue;


    protected void onPreExecute() {
        Depo.dialog.show();
    }

    protected Void doInBackground(Void... voids) {

        SoapObject request = new SoapObject(_Namspace, _MethodName);

        request.addProperty("tablo","Kullanici");
        request.addProperty("kulAdi",kullaniciAdi);
        request.addProperty("sifre",sifre);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        AndroidHttpTransport aht = new AndroidHttpTransport(Depo._Url);
        aht.debug=true;

        try {
            aht.call(_Action,envelope);
            SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
            _ResultValue=response.toString();
        }catch (Exception e){
            _ResultValue=e.toString();}
        return null;
    }

    @Override
    protected void onPostExecute(Void voids) {
        Depo.dialog.dismiss();
            try {
                if (Integer.parseInt(_ResultValue) > 0) {
                    Depo.OnaylananKullaniciID = Integer.parseInt(_ResultValue);
                    if(swKullaniciGirisHatirla.isChecked()) {
                        dbYardimcisi.veritabaniniBosalt();
                        dbYardimcisi.KullaniciEkle(1, kullaniciAdi, sifre);
                    }
                        startActivity(i2);
                    dbYardimcisi.veritabaniniKapat();
                        finish();
                     }else {
                    tvDurum.setText("Kullanıcı adı veya şifre hatalı");
                    ed2.setText(null);}
            }catch (Exception e){
                    tvDurum.setText("Server' a bağlanılamıyor !");
                    ed2.setText(null);
                    }
         }
    }
}
