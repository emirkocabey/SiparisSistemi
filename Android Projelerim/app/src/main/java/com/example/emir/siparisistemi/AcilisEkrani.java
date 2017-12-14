package com.example.emir.siparisistemi;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.emir.siparisistemi.Varlikar.Mekan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

public class AcilisEkrani extends AppCompatActivity {

    private VeriErisim dbYardimcisi;
    Cursor cs;

    Intent i,i2,i3;

    String durum;
    String kullaniciAdi,sifre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acilis_ekrani);
        getSupportActionBar().hide();

        Depo.diyalogOlustur(this);

        i=new Intent(this,KullaniciSayfasi.class);
        i2=new Intent(this,MekanSayfasi.class);
        i3=new Intent(this,KullaniciGiris.class);


        dbYardimcisi=new VeriErisim(this);
        dbYardimcisi.veritabaninaBaglan();

        SehirlerDoluMu();

        if(durum.equals("Dolu")){
            OncedenKayitliBiriVarMı();
        }else{
            new wsSehirleriCek().execute();
        }


    }

    private void SehirlerDoluMu() {
        cs=dbYardimcisi.SehirleriCek();
        startManagingCursor(cs);
        try {
            cs.moveToLast();
            int id=cs.getInt(0);
            //dolu ama bozuk
            if(id!=81){dbYardimcisi.SehirleriBosalt();durum= "Boş";}
            //dolu
            else if(id==81){durum= "Dolu";}

        }catch (Exception e){
            //tüm boş ki move last yapamamış
            durum= "Boş";}
        finally {
            cs.close();
        }
    }

    private void OncedenKayitliBiriVarMı() {
        cs = dbYardimcisi.KullaniciyiCek();
        startManagingCursor(cs);
        try {
            cs.moveToFirst();
            int id = cs.getInt(0);

            kullaniciAdi = cs.getString(1);
            sifre = cs.getString(2);

            if (id > 0 && id < 2) {
                new wsKullaniciGiris().execute();
            } else if (id == 2) {
                new wsMekanGiris().execute();
            }

            } catch (Exception e) {
            e.printStackTrace();
            startActivity(i3);
            cs.close();
            finish();
            dbYardimcisi.veritabaniniKapat();
            }

    }

    //**************************************************************************************************
    private class wsSehirleriCek extends AsyncTask<Void,Void,Void> {

        private final String _Namspace = "http://tempuri.org/";
        private final String _MethodName = "SehirleriCek";
        private final String _Action = _Namspace + _MethodName;
        private String _ResultValue;


        protected void onPreExecute() {
            Depo.dialog.show();
        }

        protected Void doInBackground(Void... voids) {

            SoapObject request = new SoapObject(_Namspace, _MethodName);

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

                    JSONObject eee = json.getJSONObject(i);

                    String EnlemStr = eee.getString("Enlem");
                    String BoylamStr = eee.getString("Boylam");

                    EnlemStr.replace('.', ',');
                    BoylamStr.replace('.', ',');

                    double ee = Double.parseDouble(EnlemStr);
                    double bb = Double.parseDouble(BoylamStr);

                    dbYardimcisi.SehirEkle(eee.getInt("Plaka"), eee.getString("SehirAdi"), ee, bb);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            Depo.dialog.dismiss();
            OncedenKayitliBiriVarMı();
        }
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
            request.addProperty("sifre", sifre);

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

            int id = Integer.parseInt(_ResultValue);

                if (id > 0) {
                    Depo.OnaylananKullaniciID = id;
                    startActivity(i);

                    cs.close();
                    finish();
                    dbYardimcisi.veritabaniniKapat();
                }else{
                    startActivity(i3);

                    cs.close();
                    finish();
                    dbYardimcisi.veritabaniniKapat();
                    Toast.makeText(getApplicationContext(),"Server a bağlantı yok",Toast.LENGTH_LONG).show();
                }

            }
        }
    //**************************************************************************************************
    private class wsMekanGiris extends AsyncTask<Void,Void,Void> {

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
            try{int id = Integer.parseInt(_ResultValue);
            if (id > 0) {
                Depo.suAnKiMekanID = id;
                startActivity(i2);

                cs.close();
                finish();
                dbYardimcisi.veritabaniniKapat();
            } else {
                startActivity(i3);

                cs.close();
                finish();
                dbYardimcisi.veritabaniniKapat();
            }}
            catch(Exception e){
                Toast.makeText(getApplicationContext(),"Server a bağlantı yok",Toast.LENGTH_LONG).show();
                cs.close();
                finish();
                dbYardimcisi.veritabaniniKapat();
            }
        }
    }
}
