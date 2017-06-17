package hk.path.lf.entities;

/**
 * api prelogin 接口
 * Created by zspmh on 2016-12-23.
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
