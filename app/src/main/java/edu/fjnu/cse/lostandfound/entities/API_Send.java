package edu.fjnu.cse.lostandfound.entities;

/**
 * api_send 接口
 * Created by zspmh on 2016-12-23.
 */

public class API_Send {
    public String action;
    public Object content;

    public API_Send(String action, Object content) {
        this.action = action;
        this.content = content;
    }
}
