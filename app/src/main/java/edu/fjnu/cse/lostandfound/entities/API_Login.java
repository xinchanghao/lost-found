package edu.fjnu.cse.lostandfound.entities;

/**
 * api login 接口
 * Created by qinghua on 2016/11/29.
 */

public class API_Login {
    private String username;//用户名
    private String password;//密码
    private boolean hasCaptcha;
    private String captcha;

    public API_Login(String username, String password, boolean hasCaptcha, String captcha) {
        this.username = username;
        this.password = password;
        this.hasCaptcha = hasCaptcha;
        this.captcha = captcha;
    }

    public boolean isHasCaptcha() {
        return hasCaptcha;
    }

    public void setHasCaptcha(boolean hasCaptcha) {
        this.hasCaptcha = hasCaptcha;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
