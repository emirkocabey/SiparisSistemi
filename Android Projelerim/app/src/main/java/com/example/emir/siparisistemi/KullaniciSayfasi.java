package com.example.emir.siparisistemi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

public class KullaniciSayfasi extends AppCompatActivity {
    TextView tvKullaniciSayfasiProfil_AdSoyad,tvKullaniciSayfasi_Profil_KulAdi,tvKullaniciSayfasi_Profil_MekanAdi,tvKullaniciSayfasi_Profil_MekanAdresi;
    Button btnKullaniciSayfasi_Profil_CikisYap,btnKullaniciSayfasi_Profil_HesabiSil,btnKullaniciSayfasiSiparisler,btnKullaniciSayfasiEnler,btnKullaniciSayfasiProfil;

    LinearLayout lytKullaniciSayfasiProfil_LinearLayout,lytKullaniciSayfasi_Liste_LinearLayout;
    LinearLayout listeLayout;

    VeriErisim dbYardimcisi;

    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kullanici_sayfasi);
        getSupportActionBar().hide();
        init();

        dbYardimcisi=new VeriErisim(this);
        dbYardimcisi.veritabaninaBaglan();

        //Kullanıcı verilerini çek, favorimekan ID ile de mekanın adını ve adresini çek bunları tvlere ata

        //Temel butonlar
        btnKullaniciSayfasiSiparisler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytKullaniciSayfasiProfil_LinearLayout.setVisibility(View.GONE);
                lytKullaniciSayfasi_Liste_LinearLayout.setVisibility(View.VISIBLE);

                //Siparişler tablosundan kullanıcı ıd si bu kullanıcı olanları zamana göre sırala en yeni en üstte

            }
        });
        btnKullaniciSayfasiEnler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytKullaniciSayfasiProfil_LinearLayout.setVisibility(View.GONE);
                lytKullaniciSayfasi_Liste_LinearLayout.setVisibility(View.VISIBLE);

                //Servisten istekte bulunulacak
                //İlk önce KULLANICININ KONUMU İLE TÜM ŞEHİRLER KARŞILAŞTIRILACAK GERİYE KULLANICININ
                // EN YAKIN OLDUĞU ŞEHİR PLAKA KODU DÖNDÜRELECEK

                //SONRA O PLAKA KODUNDAKİ MEKANLARIN KONUMLARI İLE KULLANICIN O AN Kİ KONUMU
                // KIYASLANIP UZAKLIĞINA GÖRE YENİ BİR TABLO YAPILACAK

                //sonra artan sırada çağırılıp for ile sırayla tasarıma yansıtılacak


                //Programatik şekilde satir_mekanlar layoutunu bu linearlayoutta liste şeklinde göster

            }
        });
        btnKullaniciSayfasiProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new wsKullaniciBilgileriniCek().execute();

                lytKullaniciSayfasi_Liste_LinearLayout.setVisibility(View.GONE);
                lytKullaniciSayfasiProfil_LinearLayout.setVisibility(View.VISIBLE);
            }
        });
        //Profil sayfası butonları
        btnKullaniciSayfasi_Profil_CikisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(KullaniciSayfasi.this);
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
        btnKullaniciSayfasi_Profil_HesabiSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kullanıcı silme servisini çalıştırıp sonrasında bu sayfayı yok edip kullanıcı giriş sayfasına git
                AlertDialog.Builder builder = new AlertDialog.Builder(KullaniciSayfasi.this);
                builder.setTitle("Hesap Silme");
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

    @Override
    protected void onResume(){
        super.onResume();
        btnKullaniciSayfasiProfil.performClick();
    }

    void init(){
        tvKullaniciSayfasiProfil_AdSoyad= (TextView) findViewById(R.id.tvKullaniciSayfasiProfil_AdSoyad);
        tvKullaniciSayfasi_Profil_KulAdi= (TextView) findViewById(R.id.tvKullaniciSayfasi_Profil_KulAdi);
        tvKullaniciSayfasi_Profil_MekanAdi= (TextView) findViewById(R.id.tvKullaniciSayfasi_Profil_MekanAdi);
        tvKullaniciSayfasi_Profil_MekanAdresi= (TextView) findViewById(R.id.tvKullaniciSayfasi_Profil_MekanAdresi);

        btnKullaniciSayfasi_Profil_CikisYap= (Button) findViewById(R.id.btnKullaniciSayfasi_Profil_CikisYap);
        btnKullaniciSayfasi_Profil_HesabiSil= (Button) findViewById(R.id.btnKullaniciSayfasi_Profil_HesabiSil);
        btnKullaniciSayfasiProfil= (Button) findViewById(R.id.btnKullaniciSayfasiProfil);
        btnKullaniciSayfasiEnler= (Button) findViewById(R.id.btnKullaniciSayfasiEnler);
        btnKullaniciSayfasiSiparisler= (Button) findViewById(R.id.btnKullaniciSayfasiSiparisler);


        lytKullaniciSayfasiProfil_LinearLayout= (LinearLayout) findViewById(R.id.lytKullaniciSayfasiProfil_LinearLayout);
        lytKullaniciSayfasi_Liste_LinearLayout= (LinearLayout) findViewById(R.id.lytKullaniciSayfasi_Liste_LinearLayout);

        listeLayout=(LinearLayout) findViewById(R.id.lytKullaniciSayfasi_Liste_LinearLayout);

        Depo.diyalogOlustur(this);

        i=new Intent(this,KullaniciGiris.class);
    }
    //**********************************************************************************************
    private class wsKullaniciBilgileriniCek extends AsyncTask<Void,Void,Void> {

        private final String _Namspace = "http://tempuri.org/";
        private final String _MethodName = "KullaniciBilgileriniCek";
        private final String _Action = _Namspace+_MethodName;
        private String _ResultValue;


        protected void onPreExecute() {
            Depo.dialog.show();
        }
        protected Void doInBackground(Void... voids) {

            SoapObject request = new SoapObject(_Namspace, _MethodName);

            request.addProperty("id",Depo.OnaylananKullaniciID);

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
            try {
                JSONArray json = new JSONArray(_ResultValue);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject eee = json.getJSONObject(i);

                    Depo.Kullanici_Adi = eee.getString("Adi");
                    Depo.Kullanici_KulAdi= eee.getString("KullaniciAdi");
                    Depo.Kullanici_Soyadi= eee.getString("Soyadi");
                    Depo.Kullanici_FavMekanId = eee.getString("FavMekanId");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            try {
                if (Depo.Kullanici_FavMekanId != null) {
                    new wsFavoriMekanBilgileriniCek().execute();
                }else{
                    tvKullaniciSayfasi_Profil_MekanAdi.setText("Favori Mekanın Yok !");
                    tvKullaniciSayfasi_Profil_MekanAdresi.setVisibility(View.INVISIBLE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //**********************************************************************************************
    private class wsKullaniciSil extends AsyncTask<Void,Void,Void> {

    private final String _Namspace = "http://tempuri.org/";
    private final String _MethodName = "KullaniciSil";
    private final String _Action = _Namspace+_MethodName;
    private String _ResultValue;


    protected void onPreExecute() {
        Depo.dialog.show();
    }
    protected Void doInBackground(Void... voids) {

        SoapObject request = new SoapObject(_Namspace, _MethodName);

        request.addProperty("tablo","Kullanici");
        request.addProperty("id",Depo.OnaylananKullaniciID);

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
            if (_ResultValue.equals("Silindi")) {
                dbYardimcisi.veritabaniniBosalt();
                Depo.OnaylananKullaniciID=0;
                Toast.makeText(getApplicationContext(),"Kullanıcı Başarıyla SİLİNDİ",Toast.LENGTH_LONG).show();
                finish();
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(),"Bir Problem Oluştu",Toast.LENGTH_LONG).show();
            }
    }
}
    //**********************************************************************************************
    private class wsFavoriMekanBilgileriniCek extends AsyncTask<Void,Void,Void> {

        private final String _Namspace = "http://tempuri.org/";
        private final String _MethodName = "MekanProfilBilgileriniCek";
        private final String _Action = _Namspace+_MethodName;
        private String _ResultValue;


        protected void onPreExecute() {
        }
        protected Void doInBackground(Void... voids) {

            SoapObject request = new SoapObject(_Namspace, _MethodName);

            request.addProperty("mekanID",Depo.Kullanici_FavMekanId);

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
            try {
                JSONArray json = new JSONArray(_ResultValue);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject eee = json.getJSONObject(i);

                    Depo.Kullanici_FavMekanAdi = eee.getString("Adi1");
                    Depo.Kullanici_FavMekanAdresi= eee.getString("Adresi1");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            GelenKullaniciVerileriniDoldur();
            Depo.dialog.dismiss();
        }
    }
    //**********************************************************************************************
    private void GelenKullaniciVerileriniDoldur() {
        tvKullaniciSayfasiProfil_AdSoyad.setText(Depo.Kullanici_Adi+" "+Depo.Kullanici_Soyadi);
        tvKullaniciSayfasi_Profil_KulAdi.setText(Depo.Kullanici_KulAdi);

        tvKullaniciSayfasi_Profil_MekanAdi.setText(Depo.Kullanici_FavMekanAdi);
        tvKullaniciSayfasi_Profil_MekanAdresi.setText(Depo.Kullanici_FavMekanAdresi);
    }
}
