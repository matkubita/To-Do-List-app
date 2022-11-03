package com.example.todolistapp6;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutTheApp extends AppCompatActivity {
    private static int[] images = {R.drawable.java, R.drawable.android_studio};
    private static String[] textImages = {"JAVA", "ANDROID STUDIO"};
    private ImageButton buttonBack2;
    private int imageNumber = 0;
    private TextView textviewDatarezlizacji, textViewWykorzystaneTechnologie, textTechnology;
    private ImageView images_technology;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_the_app);

        initWidgets2();

        images_technology.setOnClickListener(this::updateImageANDText);
        updateTextViewDataRealizacji();
        updateTextViewWykorzystaneTechnologie();
        buttonBack2.setOnClickListener(this::goBack);

    }

    public void initWidgets2(){
        images_technology = findViewById(R.id.images_technology);
        textviewDatarezlizacji = findViewById(R.id.textviewDatarezlizacji);
        textViewWykorzystaneTechnologie = findViewById(R.id.textViewWykorzystaneTechnologie);
        buttonBack2 = findViewById(R.id.buttonBack2);
        textTechnology = findViewById(R.id.textTechnology);
    }

    private void goBack(View view) {
        onBackPressed();
    }

    public void updateTextViewWykorzystaneTechnologie(){
        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        textViewWykorzystaneTechnologie.setTypeface(customfont);
    }

    public void updateTextViewDataRealizacji(){
        textviewDatarezlizacji.setLines(2);
        String sourceString = "<b>" + "Data realizacji aplikacji: " + "</b> " + "wrzesień 2022 "  +
                 "<b>" + "Autor: " + "</b>" + "Mateusz Kubita";
        textviewDatarezlizacji.setText(Html.fromHtml(sourceString));

        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        textviewDatarezlizacji.setTypeface(customfont);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void updateImageANDText(View v){
        updateImage();
        updateText();
        imageNumber++;

    }

    public void updateImage(){
        images_technology.setImageResource(images[imageNumber % 2]);
    }

    public void updateText(){
        textTechnology.setText(textImages[imageNumber % 2]);
    }



}