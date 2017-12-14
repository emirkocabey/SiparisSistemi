package com.example.emir.siparisistemi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class MenuGor extends AppCompatActivity {
    Spinner spnMenuGorMenuKriteri;
    TextView tvMenuGorMekanAdi;
    ListView lstMMenuGorMenu;

    private String[] arraySpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_gor);
        getSupportActionBar().hide();

        this.arraySpinner = new String[] {"En Yüksek Puan", "En Düşük Puan", "Alfabetik"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        spnMenuGorMenuKriteri.setAdapter(adapter);

    }
    void init(){
        spnMenuGorMenuKriteri=(Spinner)findViewById(R.id.spnMenuGorMenuKriteri);
        tvMenuGorMekanAdi=(TextView)findViewById(R.id.tvMenuGorMekanAdi);
        lstMMenuGorMenu=(ListView)findViewById(R.id.lstMenuGorYemekler);
        Depo.diyalogOlustur(this);
    }
}
