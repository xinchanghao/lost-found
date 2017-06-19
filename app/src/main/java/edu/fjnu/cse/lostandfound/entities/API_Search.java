package edu.fjnu.cse.lostandfound.entities;

/**
 * API found接口
 * Created by zspmh on 2017-6-10.
 */

public class API_Search {
    private String text;
    private int page;

    public API_Search(int page, String text) {
        this.text = text;
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
