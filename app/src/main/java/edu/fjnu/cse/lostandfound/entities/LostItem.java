package edu.fjnu.cse.lostandfound.entities;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * lostitem 实体
 * Created by zspmh on 2017-6-10.
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


}
