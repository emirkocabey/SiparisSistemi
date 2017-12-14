package com.example.emir.siparisistemi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MenuEkrani extends AppCompatActivity {
    LinearLayout lyt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_ekrani);
        getSupportActionBar().hide();
        init();
        Depo.diyalogOlustur(this);


    }

    private void init() {

    }
}
