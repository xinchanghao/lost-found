package edu.fjnu.cse.lostandfound.entities;

/**
 * API found接口
 *Created by zspmh on 2017-6-10.
 */

public class API_GetMy {
    private int page;
    private String user;

    public int getPage() {
        return page;
    }

    public API_GetMy(int page, String user) {
        this.page = page;
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}
