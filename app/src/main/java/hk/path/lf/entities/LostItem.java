package hk.path.lf.entities;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * lostitem 实体
 * Created by zspmh on 2016-12-21.
 */

public class LostItem {
    @JSONField(name="uid")
    private int ID;
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

    public void setID(int ID) {
        this.ID = ID;
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

    public int getID() {
        return ID;
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
