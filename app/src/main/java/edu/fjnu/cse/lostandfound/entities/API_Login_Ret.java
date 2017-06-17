package edu.fjnu.cse.lostandfound.entities;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * api login ret接口
 * Created by zspmh on 2017-6-10.
 */

public class API_Login_Ret {
    @JSONField(name="ret")
    private int ret;
    @JSONField(name="academy")
    private String academy;
    @JSONField(name="email")
    private String email;
    @JSONField(name="name")
    private String name;
    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
