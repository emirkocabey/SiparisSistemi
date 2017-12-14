package com.example.emir.siparisistemi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.renderscript.Double2;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emir.siparisistemi.Varlikar.Kullanici;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MekanSayfasi extends AppCompatActivity {
    TabHost thost;
    TabHost.TabSpec tspec1, tspec2, tspec3;
    ListView lstSiparisler,lstEnler;
    TextView tvMekanProfilAdi,tvMekanProfilAdresi,tvMekanProfilKulAdi,tvMEkanProfilTelefon;
    Button btnMekanProfilMenuGor,btnMekanProfilYemekEkle,btnMekanProfilGuncelle,btnMekanSayfasi_HesabiSil,btnMekanSayfasi_CikisYap;
    ImageView imgHarita;

    Intent i,i1,i2;

    VeriErisim dbYardimcisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mekan_sayfasi);
        getSupportActionBar().hide();
        init();

        dbYardimcisi=new VeriErisim(this);
        dbYardimcisi.veritabaninaBaglan();

        new wsMekanSayfasiProfil().execute();

        //gelenVeriyiDoldur();

        btnMekanProfilYemekEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i1);
            }
        });
        btnMekanProfilMenuGor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnMekanProfilGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mekan Kayıt sayfasına yönlendirmeden Depodaki değişkenleri şimdiden doldurup mekan kayıt açılırken onu o şekilde dolu görüntüle
                Depo.MekanKayit_Durum="GuncellemeİcinGelindi";
                startActivity(i2);
            }
        });
        btnMekanSayfasi_CikisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MekanSayfasi.this);
                builder.setTitle("Çıkış");
                builder.setMessage("Çıkmak istediğinize emin misiniz?");
                builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) { }
                });
                builder.setPositiveButton("ÇIKIŞ YAP",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        dbYardimcisi.veritabaniniBosalt();
                        finish();
                        startActivity(i);
                    }
                });
                builder.show();
            }
        });
        btnMekanSayfasi_HesabiSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MekanSayfasi.this);
                builder.setTitle("Hesap Silme İşlemi");
                builder.setMessage("Hesabınızı SİLMEK istediğinize emin misiniz?");
                builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) { }
                });
                builder.setPositiveButton("Hesabı Sil",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        new wsKullaniciSil().execute();
                    }
                });
                builder.show();
            }
        });

    }

    private void gelenVeriyiDoldur() {
        tvMekanProfilAdi.setText(Depo.suAnKiMekanADI);
        tvMekanProfilKulAdi.setText(Depo.suAnKiMekanKULADI);
        tvMekanProfilAdresi.setText(Depo.suAnKiMekanADRESI);
        tvMEkanProfilTelefon.setText(Depo.suAnKiMekanTELEFONU);



        /*btnMekanProfilMenuGor.setText(String.valueOf(Depo.suAnKiMekanENLEM));
        btnMekanProfilYemekEkle.setText(String.valueOf(Depo.suAnKiMekanBOYLAM));*/

        imgHarita=(ImageView)findViewById(R.id.imgHarita);

        //pb=(ProgressBar)findViewById(R.id.progressBar2);

        //HaritayiBas();
    }

    void HaritayiBas(){
        URL url= null;
        try {
            url = new URL("http://maps.google.com/maps/api/staticmap?center="+ Depo.suAnKiMekanENLEM+","+ Depo.suAnKiMekanBOYLAM+"&zoom=12&size=385x200&sensor=false");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgHarita.setImageBitmap(bmp);
        //hata vaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar sonra bakmayı unutma
    }

    void init(){
        thost=(TabHost)findViewById(R.id.tab_host);
        thost.setup();

        tspec1=thost.newTabSpec("siparişler");
        tspec1.setIndicator("Siparişler");
        tspec1.setContent(R.id.tab1);
        thost.addTab(tspec1);

        tspec2=thost.newTabSpec("en'ler");
        tspec2.setIndicator("En'ler");
        tspec2.setContent(R.id.tab2);
        thost.addTab(tspec2);

        tspec3=thost.newTabSpec("profil");
        tspec3.setIndicator("Profil");
        tspec3.setContent(R.id.tab3);
        thost.addTab(tspec3);

        lstSiparisler= (ListView) findViewById(R.id.lstsiparisler);
        lstEnler= (ListView) findViewById(R.id.lstenler);

        tvMekanProfilAdi= (TextView) findViewById(R.id.tvMekanProfilAdi);
        tvMekanProfilAdresi= (TextView) findViewById(R.id.tvMekanProfilAdresi);
        tvMEkanProfilTelefon= (TextView) findViewById(R.id.tvMekanProfilTelefon);
        tvMekanProfilKulAdi= (TextView) findViewById(R.id.tvMekanProfilKulAdi);

        btnMekanProfilMenuGor= (Button) findViewById(R.id.btnMekanProfilMenuyuGor);
        btnMekanProfilYemekEkle= (Button) findViewById(R.id.btnMekanProfilYemekEkle);
        btnMekanProfilGuncelle=(Button) findViewById(R.id.btnMekanProfilBilgiGuncelle);
        btnMekanSayfasi_CikisYap=(Button)findViewById(R.id.btnMekanSayfasi_CikisYap);
        btnMekanSayfasi_HesabiSil=(Button)findViewById(R.id.btnMekanSayfasi_HesabiSil);

        i1=new Intent(this,YemekEkle.class);
        i2=new Intent(this,MekanKayit.class);
        i= new Intent(this,MekanGiris.class);
        Depo.diyalogOlustur(this);
    }
    //**********************************************************************************************
    private class wsKullaniciSil extends AsyncTask<Void,Void,Void> {

        private final String _Namspace = "http://tempuri.org/";
        private final String _MethodName = "KullaniciSil";
        private final String _Action = _Namspace + _MethodName;
        private String _ResultValue;


        protected void onPreExecute() {
            Depo.dialog.show();
        }

        protected Void doInBackground(Void... voids) {

            SoapObject request = new SoapObject(_Namspace, _MethodName);

            request.addProperty("tablo", "Mekan");
            request.addProperty("id", Depo.suAnKiMekanID);

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
            if (_ResultValue.equals("Silindi")) {
                dbYardimcisi.veritabaniniBosalt();
                Depo.OnaylananKullaniciID = 0;
                Toast.makeText(getApplicationContext(), "Kullanıcı Başarıyla SİLİNDİ", Toast.LENGTH_LONG).show();
                finish();
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Bir Problem Oluştu", Toast.LENGTH_LONG).show();
            }
        }
    }
    //***************************************************************************************
    private class wsMekanSayfasiProfil extends AsyncTask<Void, Void, Void> {

            private final String _Namspace = "http://tempuri.org/";
            private final String _MethodName = "MekanProfilBilgileriniCek";
            private final String _Action = _Namspace + _MethodName;
            private String _ResultValue;

            protected void onPreExecute() {
                Depo.dialog.show();
            }

            protected Void doInBackground(Void... voids) {

                SoapObject request = new SoapObject(_Namspace, _MethodName);

                request.addProperty("mekanID", Depo.suAnKiMekanID);

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

                try {
                    JSONArray json = new JSONArray(_ResultValue);

                    for (int i = 0; i < json.length(); i++) {
                        JSONObject e = json.getJSONObject(i);

                        String EnlemStr = e.getString("Enlem1");
                        String BoylamStr = e.getString("Boylam1");

                        EnlemStr.replace('.', ',');
                        BoylamStr.replace('.', ',');

                        double ee = Double.parseDouble(EnlemStr);
                        double bb = Double.parseDouble(BoylamStr);

                    /*double ee=e.getDouble("Enlem1");
                    double bb=e.getDouble("Boylam1");*/

                        Depo.suAnKiMekanADI = e.getString("Adi1");
                        Depo.suAnKiMekanADRESI = e.getString("Adresi1");
                        Depo.suAnKiMekanENLEM = ee;
                        Depo.suAnKiMekanBOYLAM = bb;
                        Depo.suAnKiMekanKULADI = e.getString("KullaniciAdi1");
                        Depo.suAnKiMekanSIFRE = e.getString("Sifre1");
                        Depo.suAnKiMekanTELEFONU = e.getString("Telefon1");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void voids) {
                Depo.dialog.dismiss();
                gelenVeriyiDoldur();
            }
        }
//Ana sınıfın bitişi
    }
