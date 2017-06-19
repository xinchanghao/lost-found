package edu.fjnu.cse.lostandfound.entities;

/**
 * api login 接口
 * Created by zspmh on 2017-6-10.
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


}
