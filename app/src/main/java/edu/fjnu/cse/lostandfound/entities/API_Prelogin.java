package edu.fjnu.cse.lostandfound.entities;

/**
 * api prelogin 接口
 * Created by zspmh on 2017-6-10.
 */

public class API_Prelogin {
    private String username;//用户名

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public API_Prelogin(String username) {
        this.username = username;
    }
}
