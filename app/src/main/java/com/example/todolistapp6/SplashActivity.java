package com.example.todolistapp6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {
    private ConstraintLayout layoutSplash;
    private LottieAnimationView lot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initWidgets3();

        lot.setOnClickListener(this::breakAnimation);
        layoutSplash.setOnClickListener(this::breakAnimation);
        lot.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in,
                        R.anim.fade_out);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    public void initWidgets3(){
        lot = findViewById(R.id.lot);
        layoutSplash = findViewById(R.id.layoutsplash);
    }

    public void breakAnimation(View v){
        LottieAnimationView lot = findViewById(R.id.lot);
        lot.setSpeed(10.0f);
    }
}