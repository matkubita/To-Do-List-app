package com.example.todolistapp6;

import java.util.Date;

public class Comment {
    public String text;
    public Date date;

    public Comment(String text) {
        this.date = new Date();
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
