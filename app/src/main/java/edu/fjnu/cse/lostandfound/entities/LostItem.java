package edu.fjnu.cse.lostandfound.entities;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * lostitem 实体
 * Created by zspmh on 2017-6-10.
 */

public class LostItem {
    private int uid;
    @JSONField(name="user")
    private String User;
    @JSONField(name="place")
    private String Place;
    @JSONField(name="detail")
    private String Detail;
    @JSONField(name="label")
    private String Label;
    private String Time;
    @JSONField(name="CreateTime")
    private String CreateTime;
    @JSONField(name="pic")
    private PicItem[] Pic;
    private int type;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public PicItem[] getPic() {
        if(Pic==null){
            return new PicItem[0];
        }else {
            return Pic;
        }
    }

    public void setPic(PicItem[] pic) {
        Pic = pic;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUser(String user) {
        User = user;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getUid() {
        return uid;
    }

    public String getUser() {
        return User;
    }

    public String getPlace() {
        return Place;
    }

    public String getDetail() {
        return Detail;
    }

    public String getLabel() {
        return Label;
    }

    public String getCreateTime() {
        return CreateTime;
    }
}
