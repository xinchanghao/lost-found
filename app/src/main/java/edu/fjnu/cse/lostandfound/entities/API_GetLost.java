package edu.fjnu.cse.lostandfound.entities;

/**
 * api lost 接口
 * Created by zspmh on 2017-6-10.
 */

public class API_GetLost {
    private int page;

    public int getPage() {
        return page;
    }

    public API_GetLost(int page) {
        this.page = page;
    }
}
