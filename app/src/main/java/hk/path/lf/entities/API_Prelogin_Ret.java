package hk.path.lf.entities;

import android.graphics.Bitmap;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * api prelogin ret 接口
 * Created by zspmh on 2016-12-23.
 */

public class API_Prelogin_Ret {
    @JSONField(name="ret")
    private int ret;
    @JSONField(name="captcha")
    private String captcha;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
