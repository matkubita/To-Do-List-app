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
    ImageButton buttonBack2;
    private int imageNumber = 0;
    TextView TextViewDataRealizacji, textViewWykorzystaneTechnologie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_the_app);

        ImageView imageView = findViewById(R.id.images_technology);
        imageView.setOnClickListener(this::updateImageANDText);

        TextViewDataRealizacji = findViewById(R.id.textviewDatarezlizacji);
        updateTextViewDataRealizacji(TextViewDataRealizacji);

        textViewWykorzystaneTechnologie = findViewById(R.id.textViewWykorzystaneTechnologie);
        updateTextViewWykorzystaneTechnologie(textViewWykorzystaneTechnologie);

        setBackButtonListener();


    }

    public void setBackButtonListener(){
        buttonBack2 = findViewById(R.id.buttonBack2);
        buttonBack2.setOnClickListener(this::goBack);

    }

    private void goBack(View view) {
        onBackPressed();
    }

    public void updateTextViewWykorzystaneTechnologie(TextView textView){
        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        textView.setTypeface(customfont);
    }

    public void updateTextViewDataRealizacji(TextView textView){
        textView.setLines(2);
        String sourceString = "<b>" + "Data realizacji aplikacji: " + "</b> " + "wrzesień 2022 "  +
                 "<b>" + "Autor: " + "</b>" + "Mateusz Kubita";
        textView.setText(Html.fromHtml(sourceString));

        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        textView.setTypeface(customfont);

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
        ImageView imageView = findViewById(R.id.images_technology);

        imageView.setImageResource(images[imageNumber % 2]);
    }

    public void updateText(){
        TextView textTechnology = findViewById(R.id.textTechnology);
        textTechnology.setText(textImages[imageNumber % 2]);
    }



}