package com.example.emir.siparisistemi;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class YemekEkle extends AppCompatActivity {
    EditText edtYemekEkleAdi,edtYemekEkleFiyati,edtYemekEkleİcerigi;
    ImageView imgYemekEkleResmi;
    Button btnYemekEkleEkle;

    TextView tv;

    Intent intent;

    private final int ACTIVITY_CHOOSE_PHOTO = 1;
    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);

    byte sayac=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_ekle);
        getSupportActionBar().hide();
        init();
        Depo.diyalogOlustur(this);

        tv.setText(Html.fromHtml("<b>Branch Name: </b>"));

        imgYemekEkleResmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);}});

        btnYemekEkleEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new wsYemekEkle().execute();
                // web servisten yemek ekleme komutu çalıştır.
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgYemekEkleResmi.setImageBitmap(photo);

           Depo.yemekCekilenFotoStringi = bitmapToString(photo);
             }
    }
    public String bitmapToString(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] imageArray = stream.toByteArray();

        String imgString = Base64.encodeToString(imageArray, Base64.NO_WRAP).toString();

        return imgString;
    }
    void init(){
        edtYemekEkleAdi=(EditText)findViewById(R.id.edtYemekEkleAdi);
        edtYemekEkleFiyati= (EditText) findViewById(R.id.edtYemekEkleFiyati);
        edtYemekEkleİcerigi= (EditText) findViewById(R.id.edtYemekEkleİcerigi);

        imgYemekEkleResmi= (ImageView) findViewById(R.id.imgYemekEkleResmi);

        btnYemekEkleEkle=(Button)findViewById(R.id.btnYemekEkleEkle);

        tv=(TextView)findViewById(R.id.textView9);
        Depo.diyalogOlustur(this);
    }
    //**********************************************************************************************
    private class wsYemekEkle extends AsyncTask<Void,Void,Void> {

        private final String _Namspace = "http://tempuri.org/";
        private final String _MethodName = "YemekEkle";
        private final String _Action = _Namspace+_MethodName;
        private String _ResultValue;

        protected void onPreExecute() {
            Depo.dialog.show();
        }
        protected Void doInBackground(Void... voids) {

            SoapObject request = new SoapObject(_Namspace, _MethodName);

            request.addProperty("MekanId",Depo.suAnKiMekanID);

            request.addProperty("Adi",edtYemekEkleAdi.getText().toString());
            request.addProperty("Fiyati",edtYemekEkleFiyati.getText().toString());
            request.addProperty("Icerigi",edtYemekEkleİcerigi.getText().toString());
            request.addProperty("Fotograf",Depo.yemekCekilenFotoStringi);

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
            if(_ResultValue.equals("Başarılı")){
                Toast.makeText(getBaseContext(),"Yemek Eklendi",Toast.LENGTH_SHORT).show();
            }   else
                tv.setText("Başarısız");
        }
    }
}
