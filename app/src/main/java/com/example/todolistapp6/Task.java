package com.example.todolistapp6;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Task implements Serializable {
    public String title;
    public String category;
    public Date date;
    public ArrayList<Comment> comments;
    public boolean isDone;
    public boolean isPriority;

    public Task(String title) {
        this.title = title;
        this.category = "Skrzynka Spraw";
        this.date = new Date();
        comments = new ArrayList<Comment>();
        this.isDone = false;
        this.isPriority = false;
    }

    public String get_formatted_date(){
        String[] splited = date.toString().split("\\s+");
        String data_new = splited[0] +" " + splited[1] +" " + splited[2];
        return data_new;

    }

    public boolean isPriority() {
        return isPriority;
    }

    public void setPriority(boolean priority) {
        isPriority = priority;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
