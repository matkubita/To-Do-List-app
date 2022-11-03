package com.example.todolistapp6;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity  {
    private ImageButton button_add, btn_back_nav_draw, buttonTopLeft, buttonInfo;
    private LinearLayout layout_mid, LayoutTaskDone, LayoutForEditText;
    private EditText editText;
    private LinkedHashMap<String, Task> tasks;
    private TextView textViewPriorytety, textView_pozostalo, textViewDone;
    private DrawerLayout drawer_layout;
    private NavigationView navigation_view;

    boolean creatingtask = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgetsMain();

        button_add.setOnClickListener(this::StartStopTextEditor);
        buttonInfo.setOnClickListener(this::openAboutTheAppActivity);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        initTasks();

        update_text_view_pozostalo();

        updateTextViewDone();
        setTextViewPriorytety();
        setTextViewPozostale();

        setNavDrawer();



    }

    public void initWidgetsMain(){
        button_add = findViewById(R.id.button_add);
        buttonInfo = findViewById(R.id.buttonInfo);
        textView_pozostalo = findViewById(R.id.textView_pozostalo);
        buttonTopLeft = findViewById(R.id.buttonTopLeft);
        textViewPriorytety = findViewById(R.id.textViewPriorytety);
        textViewDone = findViewById(R.id.textViewDone);

        LayoutTaskDone = findViewById(R.id.LayoutTaskDone);
        layout_mid = findViewById(R.id.layout_mid);
        LayoutForEditText = findViewById(R.id.LayoutForEditText);
        drawer_layout = findViewById(R.id.drawer_layout);
        btn_back_nav_draw = findViewById(R.id.btn_back_nav_draw2);
        navigation_view = findViewById(R.id.navigation_view);
    }

    public void initTasks(){

        if (does_SP_contain_list()){
            load_all_tasks();
        }
        else {
            create_empty_task_list_SP();
        }
    }

    public void openProjektActivity(){
        Intent intent = new Intent(this, Projekty.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void openCalendarActivity(){
        Intent intent = new Intent(this, Calendar_act.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }



    public void setNavDrawer(){

        buttonTopLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(GravityCompat.START);

            }
        });
        navigation_view.setCheckedItem(R.id.Priorytety);
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                drawer_layout.closeDrawer(GravityCompat.START);
                switch (id)
                {

                    case R.id.Priorytety:
                        Toast.makeText(MainActivity.this, "Priorytety is Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.Skrzynka_spraw:
                        Toast.makeText(MainActivity.this, "Skrzynka_spraw is Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.Projekty:
                        Toast.makeText(MainActivity.this, "Projekty is Clicked", Toast.LENGTH_SHORT).show();
                        openProjektActivity();
                        break;
                    case R.id.Ustawienia:
                        Toast.makeText(MainActivity.this, "Ustawienia is Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.kalendarz:
                        Toast.makeText(MainActivity.this, "kalendarz is Clicked", Toast.LENGTH_SHORT).show();
                        openCalendarActivity();
                        break;
                    default:
                        return true;

                }
                return true;
            }
        });


        btn_back_nav_draw.setOnClickListener(this::hideNavDrawer);

    }

    @TargetApi(Build.VERSION_CODES.S_V2)
    public void showPopUpMenuProjects(View v){
        PopupMenu popup = new PopupMenu(this,v, Gravity.CENTER);
        popup.setForceShowIcon(true);

        popup.inflate(R.menu.popupmenu);
        popup.show();

    }

    public void hideNavDrawer(View v){
        drawer_layout.closeDrawer(GravityCompat.START);
    }

    public void setTextViewPozostale(){
        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        textView_pozostalo.setTypeface(customfont);
    }

    public void setTextViewPriorytety(){
        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        textViewPriorytety.setTypeface(customfont);

    }

    public void updateTextViewDone(){
        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        textViewDone.setTypeface(customfont);
    }

    public void openAboutTheAppActivity(View v){
        Intent intent = new Intent(this, AboutTheApp.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void update_text_view_pozostalo(){
        tasks = get_all_task_from_SP();

        int count = 0;
        for (Task task: tasks.values()){
            if (!task.isDone){
                count++;
            }
        }
        textView_pozostalo.setText("Pozostało: " + count);

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


        setTaskDoneAndSave(task);

        removeTaskFromLayout(task, layout_mid);

        AddLayoutWithButtons(task, LayoutTaskDone);



        //upgrade pozostałe
        update_text_view_pozostalo();


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
                MakeLayoutFadeOutFROMTask(task, LayoutTaskDone);

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

        setTaskNOTDoneAndSave(task);

        removeTaskFromLayout(task, LayoutTaskDone);

        AddLayoutWithButtons(task, layout_mid);



        //upgrade pozostałe
        update_text_view_pozostalo();
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
            removeTaskFromLayout(task, LayoutTaskDone);
        }
        else {
            removeTaskFromLayout(task, layout_mid);

        }


        remove_from_SP(task);

        update_text_view_pozostalo();



    }


    public void MakeLayoutFadeInLayoutTaskDone(Task task){

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

        updateNavDrawer();

    }

    public void updateNavDrawer(){
        navigation_view.getMenu().getItem(0).setChecked(true);

    }

    public void updateTasksLayout(){

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

        for (int i = 0 ; i< LayoutTaskDone.getChildCount(); i++){
            LinearLayout LayoutTask = (LinearLayout) LayoutTaskDone.getChildAt(i);
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
        layout_mid.removeAllViews();
        creatingtask = false;
        create_empty_task_list_SP();
        update_text_view_pozostalo();
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


    public void add_task(Task task){


        if (task.isDone){
            AddLayoutWithButtons(task, LayoutTaskDone);
        }
        else {
            AddLayoutWithButtons(task, layout_mid);
        }

    }

    private void create_new_task(String task_name) {

        Task task = make_class_task(task_name);
        save_to_Shared_Pref(task);
        AddLayoutWithButtons(task, layout_mid);
        update_text_view_pozostalo();

    }

    public Task make_class_task(String task_name){
        tasks = get_all_task_from_SP();
        return new Task(task_name);
    }

    public void close_text_editor(){
        editText = findViewById(R.id.text_editor);
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
                LayoutForEditText.removeView(editText);
            }
            creatingtask = false;
            return;
        }
        editText = new EditText(this);
        editText.setId(R.id.text_editor);

        creatingtask = true;

        LayoutForEditText.addView(editText);

        setEditTextSettingsANDListener(editText);


        showKeyboard(editText);


    }

    public void setEditTextSettingsANDListener(EditText editText){

        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        editText.setTypeface(customfont);
//        editText.setCompoundDrawables(getResources().getDrawable(R.drawable.cross), null, null, null);
//        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cross, 0,0,0);
//        editText.setCompoundDrawables(AppCompatResources.getDrawable(this, R.drawable.cross),null,null,null);
//        editText.setCompoundDrawablesRelative(getResources().getDrawable(R.drawable.cross),null,null,null);

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