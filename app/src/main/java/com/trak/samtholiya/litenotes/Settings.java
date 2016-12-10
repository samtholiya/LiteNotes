package com.trak.samtholiya.litenotes;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class Settings extends AppCompatActivity {

    public static int TitleFont=R.style.liteCasual;
    public static int DetailFont=R.style.liteCasual;
    Spinner titleFontSelect,bodyFontSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        titleFontSelect =(Spinner)findViewById(R.id.spinner_title_font);
        bodyFontSelect=(Spinner)findViewById(R.id.spinner_body_font);
        titleFontSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] fontArray=getResources().getStringArray(R.array.font_array);
                TitleFont=getTypeFace(fontArray[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bodyFontSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] fontArray=getResources().getStringArray(R.array.font_array);
                DetailFont=getTypeFace(fontArray[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public int getTypeFace(String name){
        switch (name) {
            case "monospace":
                return R.style.liteMonospace;
            case "sans-serif":
                return R.style.liteSansSerif;
            case "serif-monospace":
                return R.style.liteSerifMonospace;
            case "cursive":
                return R.style.liteCursive;
            case "serif":
                return R.style.liteSerif;
            case "sans-serif-condensed":
                return R.style.liteSansSerifCondensed;
            case "sans-serif-smallcaps":
                return R.style.liteSansSerifSmallCaps;
            default:
                return R.style.liteCasual;
        }
    }
}
