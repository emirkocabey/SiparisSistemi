package com.example.emir.siparisistemi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

public class KullaniciKayit extends AppCompatActivity {
    Button btn;
    EditText ed1, ed2, ed3, ed4;
    private String _ResultValue;

    boolean onay = false;

    Intent i1;

    TextView tvKullaniciKayitUyari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kullanici_kayit);
        getSupportActionBar().hide();
        init();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Depo.KullaniciKayitDurum.equals("GuncellemeIcinGelindi")){
                    //kullanıcı güncelleme asynctask ı
                    if(tvKullaniciKayitUyari.getText().equals(null)){
                    }else Toast.makeText(KullaniciKayit.this,"Lütfen hatalı yerleri düzeltip tekrar deneyin !",Toast.LENGTH_LONG).show();
                }
                if(Depo.KullaniciKayitDurum.equals("Boş")){
                    HerYerDoluMu();
                    if(onay){
                        new wsKullaniciKayit().execute();
                    }else Toast.makeText(KullaniciKayit.this,"Lütfen heryeri düzgünce doldurun ve tekrar deneyin!",Toast.LENGTH_LONG).show();
                        }
                }
        });

        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                new wsKullaniciAdiVarmi().execute();
            }
        });
    }

    void HerYerDoluMu() {
        if (ed1.getText().length() > 0 && ed2.getText().length() > 0 && ed3.getText().length() > 0 && ed4.getText().length() > 0 && (!(tvKullaniciKayitUyari.getText().equals("Bu kullanıcı adı kullanılıyor !")))) {
            onay = true;
        } else onay = false;
    }

    private void init() {
        btn = (Button) findViewById(R.id.btnKayitKullaniciKayit);
        ed1 = (EditText) findViewById(R.id.edtKayitKullaniciAd);
        ed2 = (EditText) findViewById(R.id.edtKayitKullaniciSoyad);
        ed3 = (EditText) findViewById(R.id.edtKayitKullaniciKulAdi);
        ed4 = (EditText) findViewById(R.id.edtKayitKullaniciSifresi);

        i1= new Intent(this,KullaniciSayfasi.class);

        tvKullaniciKayitUyari = (TextView) findViewById(R.id.tvKullaniciKayitUyari);

        Depo.diyalogOlustur(this);

    }
        //**************************************************************************************************
        private class wsKullaniciAdiVarmi extends AsyncTask<Void, Void, Void> {

            private final String _Namspace = "http://tempuri.org/";
            private final String _MethodName = "KullaniciVarMi";
            private final String _Action = _Namspace + _MethodName;


            protected void onPreExecute() {

            }

            protected Void doInBackground(Void... voids) {

                SoapObject request = new SoapObject(_Namspace, _MethodName);

                request.addProperty("tablo", "Kullanici");
                request.addProperty("kulAdi", ed3.getText().toString());

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
                if (_ResultValue.equals("Var")) {
                    ed3.setTextColor(Color.RED);
                    tvKullaniciKayitUyari.setText("Bu kullanıcı adı kullanılıyor !");
                } else {
                    ed3.setTextColor(Color.WHITE);
                    tvKullaniciKayitUyari.setText(null);
                }
            }
        }
//**************************************************************************************************
        private class wsKullaniciKayit extends AsyncTask<Void, Void, Void> {
            private final String _Namspace = "http://tempuri.org/";
            private final String _MethodName = "KullaniciKayit";
            private final String _Action = _Namspace + _MethodName;


            protected void onPreExecute() {
                Depo.dialog.show();
            }

            protected Void doInBackground(Void... voids) {

                SoapObject request = new SoapObject(_Namspace, _MethodName);

                request.addProperty("adi", ed1.getText().toString());
                request.addProperty("soyadi", ed2.getText().toString());
                request.addProperty("kullaniciAdi", ed3.getText().toString());
                request.addProperty("sifre", ed4.getText().toString());

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
                int aa=Integer.parseInt(_ResultValue);
                if(aa>0){
                    Toast.makeText(getApplicationContext(),"Kullanıcı Başarı İle Oluşturuldu",Toast.LENGTH_LONG).show();
                    Depo.OnaylananKullaniciID=aa;
                    startActivity(i1);
                }else{Toast.makeText(getApplicationContext(),"Bir terslik oluştu",Toast.LENGTH_LONG).show();}
            }
        }
    }
