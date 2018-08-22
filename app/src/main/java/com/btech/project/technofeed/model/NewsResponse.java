package com.btech.project.technofeed.model;

import java.util.ArrayList;

public class NewsResponse {
    private int totalResults;
    private ArrayList<ArticleStructure> articles;

    public int getTotalResults() {
        return totalResults;
    }

    public ArrayList<ArticleStructure> getArticles() {
        return articles;
    }
}