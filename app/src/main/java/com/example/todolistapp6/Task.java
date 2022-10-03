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

    public String get_formated_date(){
        String date = getDate().toString();
        return "a";
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
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

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
