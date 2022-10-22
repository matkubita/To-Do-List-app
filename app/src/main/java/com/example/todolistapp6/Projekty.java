package com.example.todolistapp6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Projekty extends AppCompatActivity {
    ImageButton buttonTopLeft2, btn_back_nav_draw2, button_back2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projekty);

        //set on click listeners
        setNavDrawer();

        button_back2 = findViewById(R.id.button_back2);
        button_back2.setOnClickListener(this::goBack);
    }

    private void goBack(View view) {
        onBackPressed();
    }

    public void openMainMenuPriorytety(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public void setNavDrawer(){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout222);
        NavigationView navigationView = findViewById(R.id.navigation_view2);
        buttonTopLeft2 = findViewById(R.id.buttonTopLeft22);
        buttonTopLeft2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        navigationView.setCheckedItem(R.id.Projekty);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {

                    case R.id.Priorytety:
                        Toast.makeText(Projekty.this, "Priorytety is Clicked", Toast.LENGTH_SHORT).show();
                        onBackPressed();
//                        openMainMenuPriorytety();
                        break;
                    case R.id.Skrzynka_spraw:
                        Toast.makeText(Projekty.this, "Skrzynka_spraw is Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.Projekty:
                        Toast.makeText(Projekty.this, "Projekty is Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.Ustawienia:
                        Toast.makeText(Projekty.this, "Ustawienia is Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.kalendarz:
                        Toast.makeText(Projekty.this, "kalendarz is Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;

                }
                return true;
            }
        });

        btn_back_nav_draw2 = findViewById(R.id.btn_back_nav_draw2);
        btn_back_nav_draw2.setOnClickListener(this::hideNavDrawer);

    }

    public void hideNavDrawer(View v){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout222);
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}