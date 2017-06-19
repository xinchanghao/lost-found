package edu.fjnu.cse.lostandfound.entities;

/**
 * API found接口
 *Created by zspmh on 2017-6-10.
 */

public class API_ChangeStatus {
    private int uid;
    private int status;

    public API_ChangeStatus(int uid, int status) {
        this.uid = uid;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
