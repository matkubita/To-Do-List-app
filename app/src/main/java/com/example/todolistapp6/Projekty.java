package com.example.todolistapp6;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Projekty extends AppCompatActivity {
    private ImageButton buttonTopLeft2, btn_back_nav_draw2, button_back2, button_add2, buttonInfo2;
    private Button button_anuluj, button_utworz;
    private EditText edit_text_nazwa_proj;
    private Project_fabryka project_fabryka;
    private LinearLayout layout_projekty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projekty);

        initWidgets4();

        //set on click listeners
        setNavDrawer();

        button_back2.setOnClickListener(this::goBack);
        button_add2.setOnClickListener(this::showBottomSheetDialog);
        buttonInfo2.setOnClickListener(this::openAboutTheAppActivity);

        initFabryka();

    }

    public void initFabryka(){
        //load fabrka from SP
        if (does_SP_contain_fabryka()){
            create_layouts_with_projects();
        }
        else {
            create_empty_fabryka_SP();
        }
    }

    public void initWidgets4(){
        button_back2 = findViewById(R.id.button_back2);
        buttonInfo2 = findViewById(R.id.buttonInfo2);
        button_add2 = findViewById(R.id.button_add2);
        buttonTopLeft2 = findViewById(R.id.buttonTopLeft22);
        project_fabryka = get_fabryka_from_SP();
    }

    public void create_layouts_with_projects() {

        ArrayList<Project_fabryka.Project_single> lista = project_fabryka.getLista_projektow();

        for (Project_fabryka.Project_single proj: lista){
            create_projekt_layout(proj.getName());
        }
    }

    private void goBack(View view) {
        onBackPressed();
    }

    public void showBottomSheetDialog(View v){

        Dialog bottomSheetDialog = new Dialog(this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.bottomsheetlayout);


        button_utworz = bottomSheetDialog.findViewById(R.id.button_utworz);
        button_utworz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_text_nazwa_proj = bottomSheetDialog.findViewById(R.id.edit_text_nazwa_proj);
                if (!edit_text_nazwa_proj.getText().toString().equals("")){
                    String nazwa_projekt = edit_text_nazwa_proj.getText().toString().trim();
                    Toast.makeText(view.getContext(), nazwa_projekt + " zostal utworzony", Toast.LENGTH_SHORT).show();
                    utworzProjekt(nazwa_projekt);
                    bottomSheetDialog.hide();
                }
                else {
                    Toast.makeText(view.getContext(), "nic tam nie bylo", Toast.LENGTH_SHORT).show();
                }

            }
        });

        button_anuluj = bottomSheetDialog.findViewById(R.id.button_anuluj);
        button_anuluj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.hide();
            }
        });



        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialoAnim;
    }

    public void openAboutTheAppActivity(View v){
        Intent intent = new Intent(this, AboutTheApp.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void utworzProjekt(String project_name){
        save_to_SP(project_name);
        create_projekt_layout(project_name);
    }

    public void create_projekt_layout(String project_name) {
        layout_projekty = findViewById(R.id.layout_projekty);

        LinearLayout layout_projekt = new LinearLayout(this);
        setLayout_proj_settings(layout_projekt);

        TextView textView_nazwa_projektu = new TextView(this);
        textView_nazwa_projektu.setText(project_name);
        set_text_view_settings(textView_nazwa_projektu);

        LinearLayout layout_projekt_inner = new LinearLayout(this);
        setLayout_projekt_inner_settings(layout_projekt_inner);

        ImageView image_kolor = new ImageView(this);
        set_image_kolor_settings(image_kolor);

        layout_projekt_inner.addView(textView_nazwa_projektu);
        layout_projekt_inner.addView(image_kolor);

        layout_projekt.addView(layout_projekt_inner);
        layout_projekty.addView(layout_projekt);
    }

    public void set_text_view_settings(TextView textView){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                0 ,
                LinearLayout.LayoutParams. WRAP_CONTENT, 7.0f) ;
        layoutParams.setMargins( 18 , 0 , 0 , 0 ) ;
        textView.setLayoutParams(layoutParams);

    }

    public void set_image_kolor_settings(ImageView image_kolor){
        image_kolor.setBackgroundResource(R.drawable.circle_colour);
        Drawable background = image_kolor.getBackground();
        setColor(background, R.color.purple_200);
        image_kolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //POP UP WINDOW (NOT MENU!!!) opens with available colours to change
//                showPopUpMenuColors(image_kolor);
                showPopUPWindowColors(image_kolor);
            }
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                0 ,
                LinearLayout.LayoutParams. WRAP_CONTENT, 1.0f) ;
        layoutParams.setMargins( 0 , 0 , 18 , 0 ) ;
        image_kolor.setLayoutParams(layoutParams);

    }

    public void showPopUPWindowColors(View v ){
        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
        final View contactPopUpView = getLayoutInflater().inflate(R.layout.popup, null);
        dialog_builder.setView(contactPopUpView);

        dialog_builder.create().show();
    }

    public void setColor(Drawable background, int kolorek){

        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(ContextCompat.getColor(this,kolorek));
        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(ContextCompat.getColor(this,kolorek));
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(ContextCompat.getColor(this,kolorek));
        }
    }

    public void setLayout_projekt_inner_settings(LinearLayout layout){
        layout.setBackgroundResource(R.drawable.layout_bg);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams. MATCH_PARENT ,
                LinearLayout.LayoutParams. WRAP_CONTENT ) ;
        layoutParams.setMargins( 8 , 8 , 8 , 8 ) ;
        layout.setLayoutParams(layoutParams);
//        layout.setWeightSum(10.0f);

        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopUpMeuDelete(view);
                return false;
            }
        });
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.HORIZONTAL);
    }

    @TargetApi(Build.VERSION_CODES.S_V2)
    private void showPopUpMeuDelete(View view) {
        PopupMenu popup = new PopupMenu(this, view, Gravity.CENTER);
        popup.setForceShowIcon(true);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.usun:

                        LinearLayout lin_lay = (LinearLayout) view;
                        TextView textView_project = (TextView) lin_lay.getChildAt(0);
                        String project_name = textView_project.getText().toString();
                        removeProject(project_name);
                        Toast.makeText(view.getContext(), project_name + " has been deleted", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.popupmenu);
        popup.show();
    }

    public void removeProject(String project_name){
        //1. Remove layout
        //2. Remove from SP

        removeProjectFromSP(project_name);
        removeLayoutProject(project_name);
    }

    public void removeLayoutProject(String project_name){

        LinearLayout linearLayout = findViewById(R.id.layout_projekty);

        for (int i = 0 ; i< linearLayout.getChildCount(); i++){
            LinearLayout layout_projekt = (LinearLayout) linearLayout.getChildAt(i);
            LinearLayout layout_inner = (LinearLayout) layout_projekt.getChildAt(0);
            TextView textView_nazwa = (TextView) layout_inner.getChildAt(0);
            if (textView_nazwa.getText().toString().equals(project_name)){
                linearLayout.removeView(layout_projekt);
            }

        }

    }

    public void removeProjectFromSP(String project_name){
        project_fabryka = get_fabryka_from_SP();
        project_fabryka.usun_projekt(project_name);

        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(project_fabryka);
        prefsEditor.putString("fabryka_projekty", json);
        prefsEditor.apply();

    }

    public void setLayout_proj_settings(LinearLayout layout){
        layout.setBackgroundResource(R.drawable.layout_task_bg);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams. MATCH_PARENT ,
                LinearLayout.LayoutParams. WRAP_CONTENT ) ;
        layoutParams.setMargins( 0 , 0 , 0 , 16 ) ;
        layout.setLayoutParams(layoutParams);
        layout.setGravity(Gravity.CENTER);

    }

    public void save_to_SP(String name_projekt){
        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();

        project_fabryka = get_fabryka_from_SP();
        project_fabryka.dodaj_projekt(name_projekt,Color.BLUE);
        String json = gson.toJson(project_fabryka);
        prefsEditor.putString("fabryka_projekty", json);
        prefsEditor.apply();

    }

    public boolean does_SP_contain_fabryka(){
        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        return mPrefs.contains("fabryka_projekty");
    }

    public Project_fabryka get_fabryka_from_SP(){
        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("fabryka_projekty", "");
        Type type = new TypeToken< Project_fabryka >() {}.getType();
        project_fabryka = gson.fromJson(json, type);

        return project_fabryka;
    }

    public void create_empty_fabryka_SP(){
        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(new Project_fabryka());
        prefsEditor.putString("fabryka_projekty", json);
        prefsEditor.apply();

    }

    public void setNavDrawer(){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout222);
        NavigationView navigationView = findViewById(R.id.navigation_view2);
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