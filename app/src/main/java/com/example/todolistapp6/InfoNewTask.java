package com.example.todolistapp6;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.divider.MaterialDivider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class InfoNewTask extends AppCompatActivity {
    TextView btn_nazwa, btn_data, btn_text_category;
    EditText comment_edittext;
    LinearLayout layout_comments;
    LinkedHashMap<String, Task> tasks;
    CheckBox checkBoxpriority;
    ImageButton buttonback;
    Project_fabryka project_fabryka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info_new_task);

        update_task_info(savedInstanceState);

        comment_edittext = findViewById(R.id.text_comment);
        set_edittext_settings(comment_edittext);

        buttonback = findViewById(R.id.buttonBack);
        setBackButtonListener(buttonback);

        setFontTextViewTask();


    }

    public Project_fabryka get_fabryka_from_SP(){
        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("fabryka_projekty", "");
        Type type = new TypeToken< Project_fabryka >() {}.getType();
        project_fabryka = gson.fromJson(json, type);

        return project_fabryka;
    }

    public void showPopUpMenuForCategory(View v){
        PopupMenu popupMenu = new PopupMenu(this, v, Gravity.CENTER);

        project_fabryka = get_fabryka_from_SP();

        for (Project_fabryka.Project_single proj: project_fabryka.getLista_projektow()){
            popupMenu.getMenu().add(proj.getName());
        }

        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String nazwa_projektu = menuItem.getTitle().toString();

                Toast.makeText(v.getContext(), nazwa_projektu, Toast.LENGTH_SHORT).show();
                changeProject_Task_assignment(nazwa_projektu);
                switch (menuItem.getItemId()){
                    case R.id.cat1:
//                        Toast.makeText(v.getContext(), "cat1", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.cat2:
//                        Toast.makeText(v.getContext(), "cat2", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
//                        Toast.makeText(v.getContext(), "default nic", Toast.LENGTH_SHORT).show();
                        return false;
                }
            }
        });
    }

    public void changeProject_Task_assignment(String nazwa_projektu){
        Task task = get_Task();
        task.setCategory(nazwa_projektu);
        save_to_Shared_Pref(task);
        update_text_category(task);

    }


    public void setlottieTaskDoneSettings(Task task ){
        LottieAnimationView lottieTaskDone = findViewById(R.id.lottieTaskDone);

        if (task.isDone){
            lottieTaskDone.setSpeed(3);
            lottieTaskDone.playAnimation();
            lottieTaskDone.setEnabled(false);
        }
        else {
            lottieTaskDone.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    lottieTaskDone.setSpeed(3);
                    lottieTaskDone.playAnimation();
                    lottieTaskDone.setEnabled(false);
                    task.isDone = true;
                    save_to_Shared_Pref(task);
                }
            });
            lottieTaskDone.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }

    }

    public void setFontTextViewTask(){
        btn_nazwa = findViewById(R.id.button_nazwa);
        Typeface customfont = Typeface.createFromAsset(getAssets(), "fonts/dubai_regular.ttf");
        btn_nazwa.setTypeface(customfont);

    }

    public void setBackButtonListener(ImageButton imageButton){
        imageButton.setOnClickListener(this::goBack);

    }

    private void goBack(View view) {
        onBackPressed();
    }

    private void SetCheckBoxPrioritySettings(CheckBox checkBoxpriority, Task task) {

        checkBoxpriority.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                task.setPriority(b);
                save_to_Shared_Pref(task);
            }
        });
    }

    public void set_edittext_settings(EditText editText){
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String s = editText.getText().toString().trim();

                if (i == EditorInfo.IME_ACTION_SEARCH
                        || i == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){


                    if (!s.equals("")){
                        Comment comment = create_new_comment(s);
                        add_comment_to_layout(comment);
                        editText.getText().clear();

                    }

                }

                return false;
            }
        });


    }

    public void add_comment_to_layout(Comment comment){
        layout_comments = findViewById(R.id.linear_layout_2);

        LinearLayout lin_new = new LinearLayout(this);
        lin_new.setOrientation(LinearLayout.VERTICAL);
        lin_new.setBackgroundResource(R.drawable.comment_bg);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams. MATCH_PARENT ,
                LinearLayout.LayoutParams. WRAP_CONTENT ) ;
        layoutParams.setMargins( 0 , 0 , 0 , 24 ) ;
        lin_new.setLayoutParams(layoutParams);




        TextView tv = new TextView(this);
        tv.setText(comment.getText());
        tv.setTextColor(Color.parseColor("#272727"));
        tv.setTextSize(17f);

        TextView tv_date = new TextView(this);
        tv_date.setText(comment.getDate().toString());
        tv_date.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

        lin_new.addView(tv);
        lin_new.addView(tv_date);

        layout_comments.addView(lin_new);

    }

    public Comment create_new_comment(String text){
        Task task = get_Task();
        Comment comment = new Comment(text);
        task.addComment(comment);

        //WE need to again store the Task in shared preferences with additional comment
        save_to_Shared_Pref(task);

        return comment;

    }

    public void save_to_Shared_Pref(Task task){

        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();

        //get and update
        tasks = get_all_task_from_SP();
        tasks.put(task.getTitle(),task);
        String json = gson.toJson(tasks);
        prefsEditor.putString("tasks", json);
        prefsEditor.apply();


    }

    public LinkedHashMap<String,Task> get_all_task_from_SP(){
        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("tasks", "");
        Type type = new TypeToken< LinkedHashMap<String,Task> >() {}.getType();
        tasks = gson.fromJson(json, type);

        return tasks;
    }

    public Task get_Task(){
        btn_nazwa = findViewById(R.id.button_nazwa);
        return get_task_from_SP(btn_nazwa.getText().toString());

    }


    public void update_task_info(Bundle savedInstanceState){

        String title;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                title= null;
            } else {
                title= extras.getString("title_task");
            }
        } else {
            title= (String) savedInstanceState.getSerializable("title_task");

        }

        Task task = get_task_from_SP(title);

        btn_data = findViewById(R.id.button_data);
        btn_nazwa = findViewById(R.id.button_nazwa);

        btn_data.setText(task.get_formatted_date());
        btn_nazwa.setText(task.getTitle());

        //we need to add all comments to layout in order
        ArrayList<Comment> comments = task.getComments();

        for (Comment comm: comments){
            add_comment_to_layout(comm);
        }

        //set checkBoxPriority listener
        checkBoxpriority = findViewById(R.id.checkboxpriority);
        SetCheckBoxPrioritySettings(checkBoxpriority, task);

//        we need to update CheckBoxPriority
        if (task.isPriority()){
            checkBoxpriority.setChecked(true);
        }

        setlottieTaskDoneSettings(task);

        update_text_category(task);

    }

    public void update_text_category(Task task){
        btn_text_category = findViewById(R.id.btn_text_category);
        btn_text_category.setText(task.getCategory());
        btn_text_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpMenuForCategory(view);
            }
        });
    }



    public Task get_task_from_SP(String task_name){
        SharedPreferences mPrefs = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("tasks", "");
        Type type = new TypeToken< LinkedHashMap<String,Task> >() {}.getType();
        LinkedHashMap<String, Task> tasks = gson.fromJson(json, type);

        return tasks.get(task_name);
    }

}