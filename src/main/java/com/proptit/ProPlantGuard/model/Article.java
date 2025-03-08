package com.proptit.ProPlantGuard.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Article {
    private LocalDate date;
    private String title;
    private String content;

    public Article(LocalDate date, String title, String content) {
        this.date = date;
        this.title = title;
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return date + " - " + title + "\n" + content;
    }
}