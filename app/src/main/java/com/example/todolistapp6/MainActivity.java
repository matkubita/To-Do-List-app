package com.example.todolistapp6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity  {
    Button button_clear_all;
    ImageButton button_add;
    LinearLayout layout_mid, layoutTaskDone, LayoutForEditText;
    EditText editText;
    boolean creatingtask = false;
    LinkedHashMap<String, Task> tasks;
    TextView tv_pozostalo, textViewPriorytety, textView_pozostalo;
    ImageButton buttonTopLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set xml
        setContentView(R.layout.activity_main);

        //set on click listeners
        button_add = findViewById(R.id.button_add);
        button_add.setOnClickListener(this::StartStopTextEditor);
        ImageButton btn_info = findViewById(R.id.buttonInfo);
        btn_info.setOnClickListener(this::openAboutTheAppActivity);

        //keyboard settings
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        //load all the tasks from SP
        if (does_SP_contain_list()){
            load_all_tasks();
        }
        else {
            //let's reset SP and load the list
            clear_shared_pref(button_clear_all);
            create_empty_task_list_SP();
        }


        tv_pozostalo = findViewById(R.id.textView_pozostalo);
        update_text_view_pozostalo(tv_pozostalo);

        updateTextViewPriorytety();
        updateTextViewDone();
        setTextViewPozostaleSettings();

        setNavDrawer();


    }

    public void setNavDrawer(){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        buttonTopLeft = findViewById(R.id.buttonTopLeft);
        buttonTopLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {

                    case R.id.Priorytety:
                        Toast.makeText(MainActivity.this, "Priorytety is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.Skrzynka_spraw:
                        Toast.makeText(MainActivity.this, "Skrzynka_spraw is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.Projekty:
                        Toast.makeText(MainActivity.this, "Projekty is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.Ustawienia:
                        Toast.makeText(MainActivity.this, "Ustawienia is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.kalendarz:
                        Toast.makeText(MainActivity.this, "kalendarz is Clicked", Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;

                }
                return true;
            }
        });

    }

    public void setTextViewPozostaleSettings(){
        textView_pozostalo = findViewById(R.id.textView_pozostalo);
        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        textView_pozostalo.setTypeface(customfont);
    }

    public void updateTextViewPriorytety(){
        textViewPriorytety = findViewById(R.id.textViewPriorytety);
        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        textViewPriorytety.setTypeface(customfont);

    }

    public void updateTextViewDone(){
        TextView textViewDone = findViewById(R.id.textViewDone);
        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        textViewDone.setTypeface(customfont);
    }

    public void openAboutTheAppActivity(View v){
        Intent intent = new Intent(this, AboutTheApp.class);
        startActivity(intent);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void update_text_view_pozostalo(TextView tv_pozostalo){
        tasks = get_all_task_from_SP();

        int count = 0;
        for (Task task: tasks.values()){
            if (!task.isDone){
                count++;
            }
        }
        tv_pozostalo.setText("Pozostało: " + count);

    }

    public void removeTaskFromLayout(Task task, LinearLayout linearLayout){


        for (int i = 0 ; i< linearLayout.getChildCount(); i++){
            LinearLayout layouts = (LinearLayout) linearLayout.getChildAt(i);
            for (int j = 0; j < layouts.getChildCount(); j ++){
                if (layouts.getChildAt(j) instanceof Button){
                    Button btn = (Button) layouts.getChildAt(j);
                    if (btn.getText().toString().equals(task.getTitle())){
                        linearLayout.removeView(layouts);
                    }
                }

            }

        }
    }



    public void SetTaskDoneNew(Task task){
        layoutTaskDone = findViewById(R.id.LayoutTaskDone);
        layout_mid = findViewById(R.id.MID);

        setTaskDoneAndSave(task);

        removeTaskFromLayout(task, layout_mid);

        AddLayoutWithButtons(task, layoutTaskDone);



        //upgrade pozostałe
        tv_pozostalo = findViewById(R.id.textView_pozostalo);
        update_text_view_pozostalo(tv_pozostalo);


    }

    public void setTaskDoneAndSave(Task task){
        task.isDone = true;
        save_to_Shared_Pref(task);

    }

    public void setTaskNOTDoneAndSave(Task task){
        task.isDone = false;
        save_to_Shared_Pref(task);

    }


    public void remove_from_SP(Task task){
        tasks = get_all_task_from_SP();
        tasks.remove(task.getTitle());

        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tasks);
        prefsEditor.putString("tasks", json);
        prefsEditor.apply();
    }

    public void create_empty_task_list_SP(){
        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(new LinkedHashMap<String, Task>());
        prefsEditor.putString("tasks", json);
        prefsEditor.apply();

    }

    public Task get_task_from_SP(String task_name){
        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("tasks", "");
        Type type = new TypeToken< LinkedHashMap<String,Task> >() {}.getType();
        LinkedHashMap<String, Task> tasks = gson.fromJson(json, type);

        return tasks.get(task_name);
    }


    public void AddLayoutWithButtons(Task task, LinearLayout layout){


        LinearLayout layout_task = new LinearLayout(this);
        LottieAnimationView lottie = new LottieAnimationView(this);
        Button ButtonTask = new Button(this);
        CheckBox checkBoxPriority = new CheckBox(this);

        setLayoutTaskSettings(layout_task);

        setButtonTaskSettingsANDListener(ButtonTask, task);
        setCheckBoxPrioritySettingsANDListener(checkBoxPriority, task);

        layout_task.addView(lottie);
        layout_task.addView(ButtonTask);
        layout_task.addView(checkBoxPriority);

        layout.addView(layout_task);


        if (task.isDone){
            layout_task.setAlpha(0.4f);
            setLottieForLayoutTaskDoneLISTENER(lottie, task);
        }
        else {
            setLottieForLayoutMIDLISTENER(lottie, task);
        }
        if (task.isPriority){
            checkBoxPriority.setChecked(true);

        }

    }

    private void setLottieForLayoutMIDLISTENER(LottieAnimationView lottie, Task task) {
        lottie.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        lottie.setAnimation(R.raw.checked_done);


        lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lottie.setSpeed(3);
                lottie.playAnimation();

            }
        });
        lottie.addAnimatorListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationStart(Animator animator) {
                layout_mid = findViewById(R.id.MID);
                MakeLayoutFadeOutFROMTask(task, layout_mid);

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                SetTaskDoneNew(task);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void setLottieForLayoutTaskDoneLISTENER(LottieAnimationView lottie, Task task) {
        lottie.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        lottie.setScaleX(2.5f);
        lottie.setScaleY(2.5f);
        lottie.setAnimation(R.raw.layouttaskdoneanimation);


        lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lottie.setSpeed(3);
                lottie.playAnimation();

            }
        });
        lottie.addAnimatorListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationStart(Animator animator) {
                layoutTaskDone = findViewById(R.id.LayoutTaskDone);
                MakeLayoutFadeOutFROMTask(task, layoutTaskDone);

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                SetTaskNOTDone(task);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    public void SetTaskNOTDone(Task task){
        layoutTaskDone = findViewById(R.id.LayoutTaskDone);
        layout_mid = findViewById(R.id.MID);

        setTaskNOTDoneAndSave(task);

        removeTaskFromLayout(task, layoutTaskDone);

        AddLayoutWithButtons(task, layout_mid);



        //upgrade pozostałe
        tv_pozostalo = findViewById(R.id.textView_pozostalo);
        update_text_view_pozostalo(tv_pozostalo);
    }




    public void setCheckBoxPrioritySettingsANDListener(CheckBox checkBox, Task task ){

        checkBox.setButtonDrawable(R.drawable.checkboxpriority);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        params.gravity = Gravity.CENTER;
        checkBox.setLayoutParams(params);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                task.setPriority(b);
                save_to_Shared_Pref(task);
            }
        });

    }

    public void setLayoutTaskSettings(LinearLayout layout_task){
        layout_task.setBackgroundResource(R.drawable.layout_task_bg);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams. MATCH_PARENT ,
                LinearLayout.LayoutParams. WRAP_CONTENT ) ;
        layoutParams.setMargins( 0 , 0 , 0 , 16 ) ;
        layout_task.setLayoutParams(layoutParams);

        layout_task.setGravity(Gravity.CENTER);
        layout_task.setOrientation(LinearLayout.HORIZONTAL);

    }

    public void setButtonTaskSettingsANDListener(Button button, Task task){

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                0 ,
                LinearLayout.LayoutParams. WRAP_CONTENT, 8.0f ) ;
        layoutParams.setMargins( 16 , 0 , 16 , 0 ) ;


        button.setBackgroundResource(R.drawable.testbuttoncombineshapeandselector);

        button.setLayoutParams(layoutParams);
        button.setTextColor(Color.BLACK);
        button.setTransformationMethod(null);
        button.setText(task.getTitle());
        button.setOnClickListener(this::open_task_info);
        button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        button.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                showPopUpMenu(view);
                return false;
            }
        });

        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        button.setTypeface(customfont);

        button.setTextSize(15f);


    }

    @TargetApi(Build.VERSION_CODES.S_V2)
    public void showPopUpMenu(View v){
        PopupMenu popup = new PopupMenu(this, v, Gravity.CENTER);
        popup.setForceShowIcon(true);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.usun:
                        String task_name = ((Button) v).getText().toString();
                        removeTask(task_name);
                        Toast.makeText(v.getContext(), "Your task has been deleted", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.popupmenu);
        popup.show();

    }

    public void removeTask(String task_name){

        Task task = get_task_from_SP(task_name);

        //remove from layout
        if (task.isDone){
            layoutTaskDone = findViewById(R.id.LayoutTaskDone);
            removeTaskFromLayout(task, layoutTaskDone);
        }
        else {
            layout_mid = findViewById(R.id.MID);
            removeTaskFromLayout(task, layout_mid);

        }


        remove_from_SP(task);

        update_text_view_pozostalo(findViewById(R.id.textView_pozostalo));



    }


    public void MakeLayoutFadeInLayoutTaskDone(Task task){

        layoutTaskDone = findViewById(R.id.LayoutTaskDone);

        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setRepeatMode(Animation.REVERSE);

        for (int i = 0 ; i< layout_mid.getChildCount(); i++){
            LinearLayout layouts = (LinearLayout) layout_mid.getChildAt(i);
            for (int j = 0; j < layouts.getChildCount(); j ++){
                if (layouts.getChildAt(j) instanceof Button){
                    Button btn = (Button) layouts.getChildAt(j);
                    if (btn.getText().toString().equals(task.getTitle())){
                        layouts.startAnimation(anim);
                    }
                }

            }

        }



    }



    public void MakeLayoutFadeOutFROMTask(Task task, LinearLayout linearLayout){


        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(1000);
        anim.setRepeatMode(Animation.REVERSE);

        for (int i = 0 ; i< linearLayout.getChildCount(); i++){
            LinearLayout layouts = (LinearLayout) linearLayout.getChildAt(i);
            for (int j = 0; j < layouts.getChildCount(); j ++){
                if (layouts.getChildAt(j) instanceof Button){
                    Button btn = (Button) layouts.getChildAt(j);
                    if (btn.getText().toString().equals(task.getTitle())){
                        layouts.startAnimation(anim);
                    }
                }

            }

        }



    }





    @Override
    protected void onRestart() {
        super.onRestart();
        //we need to update the CheckBox if it was pressed
        updateCheckBoxesPriority();
        //we need to move some tasks to LayoutTaskDone if it was pressed
        updateTasksLayout();

    }

    public void updateTasksLayout(){

        layout_mid = findViewById(R.id.MID);
        for (int i = 0 ; i< layout_mid.getChildCount(); i++){
            LinearLayout LayoutTask = (LinearLayout) layout_mid.getChildAt(i);
            for (int j = 0; j < LayoutTask.getChildCount(); j ++){
                if (LayoutTask.getChildAt(j) instanceof CheckBox){
                    Button btn = (Button) LayoutTask.getChildAt(j-1);
                    LottieAnimationView lottieAnimationView = (LottieAnimationView) LayoutTask.getChildAt(j-2);
                    Task task = get_task_from_SP(btn.getText().toString());
                    if (task.isDone){
                        //bonzur
                        SetTaskDoneNew(task);
                    }

                }

            }

        }

    }

    public void updateCheckBoxesPriority(){
        layout_mid = findViewById(R.id.MID);
        for (int i = 0 ; i< layout_mid.getChildCount(); i++){
            LinearLayout LayoutTask = (LinearLayout) layout_mid.getChildAt(i);
            for (int j = 0; j < LayoutTask.getChildCount(); j ++){
                if (LayoutTask.getChildAt(j) instanceof CheckBox){
                    Button btn = (Button) LayoutTask.getChildAt(j-1);
                    CheckBox checkBox = (CheckBox) LayoutTask.getChildAt(j);
                    Task task = get_task_from_SP(btn.getText().toString());
                    if (task.isPriority()){
                        checkBox.setChecked(true);
                    }
                    else {
                        checkBox.setChecked(false);
                    }

                }

            }

        }

        layoutTaskDone = findViewById(R.id.LayoutTaskDone);
        for (int i = 0 ; i< layoutTaskDone.getChildCount(); i++){
            LinearLayout LayoutTask = (LinearLayout) layoutTaskDone.getChildAt(i);
            for (int j = 0; j < LayoutTask.getChildCount(); j ++){
                if (LayoutTask.getChildAt(j) instanceof CheckBox){
                    Button btn = (Button) LayoutTask.getChildAt(j-1);
                    CheckBox checkBox = (CheckBox) LayoutTask.getChildAt(j);
                    Task task = get_task_from_SP(btn.getText().toString());
                    if (task.isPriority()){
                        checkBox.setChecked(true);
                    }
                    else {
                        checkBox.setChecked(false);
                    }

                }

            }

        }

    }

    public void load_all_tasks(){
        //load all the task from shared preferences
        LinkedHashMap<String, Task> task_list = get_all_task_from_SP();

        for (String key: task_list.keySet()){
            add_task(task_list.get(key));
        }

    }

    private void delete_all_tasks(View v) {
        layout_mid = findViewById(R.id.MID);
        layout_mid.removeAllViews();
        creatingtask = false;
        clear_shared_pref(v);
        create_empty_task_list_SP();
        update_text_view_pozostalo(findViewById(R.id.textView_pozostalo));
    }

    public void open_task_info(View v){
        Button btn = (Button) v;
        String title_task = btn.getText().toString();
        Intent intent = new Intent(this, InfoNewTask.class);
        intent.putExtra("title_task", title_task);
        startActivity(intent);
    }

    public void save_to_Shared_Pref(Task task){

        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();

        //get and update

        if (does_SP_contain_list()){
            tasks = get_all_task_from_SP();
            tasks.put(task.getTitle(),task);
            String json = gson.toJson(tasks);
            prefsEditor.putString("tasks", json);
            prefsEditor.apply();
        }
        else {
            //we must create the tasks and add the first one
            //let's reset SP and load the list
            clear_shared_pref(button_clear_all);
            create_empty_task_list_SP();
            tasks = get_all_task_from_SP();
            tasks.put(task.getTitle(),task);
            String json = gson.toJson(tasks);
            prefsEditor.putString("tasks", json);
            prefsEditor.apply();
        }

    }

    public boolean does_SP_contain_list(){
        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        return mPrefs.contains("tasks");

    }

    public LinkedHashMap<String,Task> get_all_task_from_SP(){
        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("tasks", "");
        Type type = new TypeToken< LinkedHashMap<String,Task> >() {}.getType();
        tasks = gson.fromJson(json, type);

        return tasks;
    }

    public void clear_shared_pref(View v){
        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.clear();
        prefsEditor.apply();
    }

    public void add_task(Task task){
        button_add = findViewById(R.id.button_add);

        if (task.isDone){
            AddLayoutWithButtons(task, findViewById(R.id.LayoutTaskDone));
        }
        else {
            AddLayoutWithButtons(task, findViewById(R.id.MID));
        }

    }

    private void create_new_task(String task_name) {

        button_add = findViewById(R.id.button_add);
        Task task = make_class_task(task_name);
        save_to_Shared_Pref(task);
        AddLayoutWithButtons(task, findViewById(R.id.MID));
        update_text_view_pozostalo(findViewById(R.id.textView_pozostalo));

    }

    public Task make_class_task(String task_name){
        tasks = get_all_task_from_SP();
        return new Task(task_name);
    }

    public void close_text_editor(){
        editText = findViewById(R.id.text_editor);
        LayoutForEditText = findViewById(R.id.LayoutForEditText);
        LayoutForEditText.removeView(editText);
    }

    public void StartStopTextEditor(View v){


        if (creatingtask){
            String s = editText.getText().toString().trim();
            hideKeyboard(editText);
            if (s.equals("")){
                close_text_editor();
            }
            else {
                create_new_task(s);
                LayoutForEditText = findViewById(R.id.LayoutForEditText);
                LayoutForEditText.removeView(editText);
            }
            creatingtask = false;
            return;
        }
        editText = new EditText(this);
        editText.setId(R.id.text_editor);

        creatingtask = true;

        LayoutForEditText = findViewById(R.id.LayoutForEditText);
        LayoutForEditText.addView(editText);

        setEditTextSettingsANDListener(editText);


        showKeyboard(editText);


    }

    public void setEditTextSettingsANDListener(EditText editText){

        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        editText.setTypeface(customfont);
        editText.setCompoundDrawables(AppCompatResources.getDrawable(this, R.drawable.cross),null,null,null);
        editText.setCompoundDrawablesRelative(getResources().getDrawable(R.drawable.cross),null,null,null);

        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String s = editText.getText().toString().trim();

                if (i == EditorInfo.IME_ACTION_SEARCH
                        || i == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    creatingtask = false;
                    if (!s.equals("")){
                        create_new_task(s);

                        //aa
                    }
                    hideKeyboard(editText);
                    LayoutForEditText = findViewById(R.id.LayoutForEditText);
                    LayoutForEditText.removeView(editText);
                }

                return false;
            }
        });

    }

    private void hideKeyboard(EditText editText) {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showKeyboard(EditText editText){
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.requestFocus();
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


    }





}