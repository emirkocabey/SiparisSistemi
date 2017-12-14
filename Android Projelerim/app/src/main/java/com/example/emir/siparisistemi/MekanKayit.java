package com.example.emir.siparisistemi;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

public class MekanKayit extends AppCompatActivity {
    EditText edtMekanAdi, edtMekanAdresi, edtMekanKulAdi, edtMekanSifre, edt90, edt500;
    TextView tvKonumDurumu, tvMekanKayitUyari;
    Button btnKonumEkrani, btnMekanEkle;
    Intent i1, i2;
    private String _ResultValue;

    boolean onay = false;

    VeriErisim dbYardimcisi;
    Cursor csSehir, tmpSehirler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mekan_kayit);
        getSupportActionBar().hide();

        init();

        dbYardimcisi = new VeriErisim(this);
        dbYardimcisi.veritabaninaBaglan();

        if (Depo.MekanKayit_Durum.equals("GuncellemeİcinGelindi")) {
            edtMekanAdi.setText(Depo.suAnKiMekanADI);
            edtMekanKulAdi.setText(Depo.suAnKiMekanKULADI);
            edtMekanAdresi.setText(Depo.suAnKiMekanADRESI);
            btnMekanEkle.setText("Bilgileri Güncelle");
            tvKonumDurumu.setText("Konum Seçildi");
            btnKonumEkrani.setText("Seçili Konumu Güncelle");
        }
        btnKonumEkrani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i1);
            }
        });

        if (Depo.konum.latitude > 0) {
            tvKonumDurumu.setText("Konum Seçildi");
        }

        edtMekanKulAdi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                new wsMekanKullaniciVarMi().execute();
            }
        });

        btnMekanEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Depo.MekanKayit_Durum.equals("GuncellemeİcinGelindi")) {
                    HerYerDoluMu();
                    //heryer dolu ama şifre doğru mu diye bakıyoruz
                    if ((Depo.suAnKiMekanSIFRE.equals(edtMekanSifre.getText().toString()))) {
                    } else {
                        tvMekanKayitUyari.setText("Şifrenizi yanlış girdiniz !");
                        onay = false;
                    }
                    if (onay) new wsMekanGuncelle().execute();
                    else
                        Toast.makeText(MekanKayit.this, "Lütfen hatalı yerleri düzeltip tekrar deneyin !", Toast.LENGTH_LONG).show();
                } else if (Depo.MekanKayit_Durum.equals("Boş")) {
                    HerYerDoluMu();
                    if (onay) {
                        Depo.dialog.show();
                        Double a;
                        for (int i = 0; i < 81; i++) {
                            csSehir = dbYardimcisi.SehiriGetir(i + 1);
                            startManagingCursor(csSehir);
                            csSehir.moveToFirst();
                        /*a = ((Depo.konum.latitude - csSehir.getDouble(2))*(Depo.konum.latitude - csSehir.getDouble(2)))+((Depo.konum.longitude-csSehir.getDouble(3))*(Depo.konum.longitude-csSehir.getDouble(3)));
                        */
                            a = (Math.pow((csSehir.getDouble(2) - Depo.konum.latitude), 2)) + Math.pow((csSehir.getDouble(3) - Depo.konum.longitude), 2);
                            a = Math.sqrt(a);
                            dbYardimcisi.Temp_SehirEkle(i + 1, csSehir.getString(1), a);
                        }
                        csSehir.close();

                        //en yakına karar verip döndürme
                        tmpSehirler = dbYardimcisi.TempSehirleriGetir();
                        startManagingCursor(tmpSehirler);
                        tmpSehirler.moveToFirst();
                        Depo.sehirKodu = tmpSehirler.getInt(0);
                        tmpSehirler.close();
                        dbYardimcisi.TempSehirleriBosalt();

                        new wsMekanKullaniciKayit().execute();
                    } else {
                        Toast.makeText(MekanKayit.this, "Lütfen heryeri düzgünce doldurun ve tekrar deneyin!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    void HerYerDoluMu() {
        if (edtMekanAdi.getText().length() > 0 && edtMekanKulAdi.getText().length() > 0 && edt500.getText().length() > 0 && edtMekanAdresi.getText().length() > 0 && edtMekanSifre.getText().length() > 0 && tvKonumDurumu.getText().toString().equals("Konum Seçildi") && (!(tvMekanKayitUyari.getText().equals("Bu kullanıcı adı kullanılıyor !")))) {
            onay = true;
        }else onay=false;
    }

    @Override
    public void onBackPressed(){
        Depo.konum = new LatLng(0,0);
        this.finish();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if((Depo.konum.latitude > 0 && Depo.konum.longitude> 0))
        {tvKonumDurumu.setText("Konum Seçildi");}
            else if( (Depo.suAnKiMekanENLEM>0 && Depo.suAnKiMekanBOYLAM>0))
        {
            tvKonumDurumu.setText("Konum Seçildi");
        }else{
            tvKonumDurumu.setText("Konum Seçilmedi");
        }
    }
    private void init() {
        edtMekanAdi=(EditText)findViewById(R.id.edtKayitMekanAdi);
        edtMekanAdresi=(EditText)findViewById(R.id.edtKayitMekanAdresi);
        edtMekanKulAdi=(EditText)findViewById(R.id.edtKayitMekanKullaniciAdi);
        edtMekanSifre=(EditText)findViewById(R.id.edtKayitMekanSifresi);
        edt500=(EditText)findViewById(R.id.edt500);

        tvKonumDurumu=(TextView)findViewById(R.id.tvKonumDurumu);
        tvMekanKayitUyari=(TextView)findViewById(R.id.tvMekanKayitUyari);

        btnKonumEkrani=(Button)findViewById(R.id.btnKonumAl);
        btnMekanEkle=(Button)findViewById(R.id.btnKayitMekanEkle);

        i1=new Intent(this,MapsActivity.class);
        i2=new Intent(this,MekanSayfasi.class);

        Depo.diyalogOlustur(this);
    }

    //**********************************************************************************************
    private class wsMekanKullaniciVarMi extends AsyncTask<Void,Void,Void> {

        private final String _Namspace = "http://tempuri.org/";
        private final String _MethodName = "KullaniciVarMi";
        private final String _Action = _Namspace+_MethodName;



        protected void onPreExecute() {

        }
        protected Void doInBackground(Void... voids) {

            SoapObject request = new SoapObject(_Namspace, _MethodName);

            request.addProperty("tablo","Mekan");
            request.addProperty("kulAdi",edtMekanKulAdi.getText().toString());

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
                if (_ResultValue.equals("Var")) {
                    edtMekanKulAdi.setTextColor(Color.RED);
                    tvMekanKayitUyari.setText("Bu kullanıcı adı kullanılıyor !");
                }else{
                    edtMekanKulAdi.setTextColor(Color.WHITE);
                    tvMekanKayitUyari.setText(null);}
        }
    }
    //**********************************************************************************************
    private class wsMekanKullaniciKayit extends AsyncTask<Void,Void,Void> {
        private final String _Namspace = "http://tempuri.org/";
        private final String _MethodName = "MekanKayit";
        private final String _Action = _Namspace+_MethodName;


        protected void onPreExecute() {
        }
        protected Void doInBackground(Void... voids) {

            SoapObject request = new SoapObject(_Namspace, _MethodName);

            request.addProperty("Adi",edtMekanAdi.getText().toString());
            request.addProperty("Adresi",edtMekanAdresi.getText().toString());
            request.addProperty("kulAdi",edtMekanKulAdi.getText().toString());
            request.addProperty("sifre",edtMekanSifre.getText().toString());
            request.addProperty("tel","+90"+edt500.getText().toString());
            request.addProperty("kodu",Integer.parseInt(String.valueOf(Depo.sehirKodu)));
            request.addProperty("enlem",String.valueOf(Depo.konum.latitude));
            request.addProperty("boylam",String.valueOf(Depo.konum.longitude));


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
            int aa=Integer.parseInt(_ResultValue);
            if(aa>0){
            Toast.makeText(getApplicationContext(),"Kullanıcı Başarı İle Oluşturuldu",Toast.LENGTH_LONG).show();
            Depo.suAnKiMekanID=aa;
            startActivity(i2);
            }else{Toast.makeText(getApplicationContext(),"Bir terslik oluştu",Toast.LENGTH_LONG).show();}
        }
    }
    //**********************************************************************************************
    private class wsMekanGuncelle extends AsyncTask<Void,Void,Void> {
        private final String _Namspace = "http://tempuri.org/";
        private final String _MethodName = "MekanGuncelle";
        private final String _Action = _Namspace+_MethodName;


        protected void onPreExecute() {
        }
        protected Void doInBackground(Void... voids) {

            SoapObject request = new SoapObject(_Namspace, _MethodName);

            request.addProperty("Adi",edtMekanAdi.getText().toString());
            request.addProperty("Adresi",edtMekanAdresi.getText().toString());
            request.addProperty("kulAdi",edtMekanKulAdi.getText().toString());
            request.addProperty("sifre",edtMekanSifre.getText().toString());
            request.addProperty("tel","+90"+edt500.getText().toString());
            request.addProperty("kodu",Integer.parseInt(String.valueOf(Depo.sehirKodu)));
            request.addProperty("enlem",String.valueOf(Depo.konum.latitude));
            request.addProperty("boylam",String.valueOf(Depo.konum.longitude));
            request.addProperty("id",String.valueOf(Depo.suAnKiMekanID));


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
            int aa=Integer.parseInt(_ResultValue);
            if(aa>0){
                Toast.makeText(getApplicationContext(),"Kullanıcı Başarı İle GÜNCELLENDİ",Toast.LENGTH_LONG).show();
                Depo.suAnKiMekanID=aa;
                startActivity(i2);
            }else{Toast.makeText(getApplicationContext(),"Bir terslik oluştu",Toast.LENGTH_LONG).show();}
        }
    }
}