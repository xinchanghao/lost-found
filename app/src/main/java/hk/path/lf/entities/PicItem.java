package hk.path.lf.entities;

import com.alibaba.fastjson.annotation.JSONField;

import hk.path.lf.activity.AppContext;

/**
 * picitem实体
 * Created by zspmh on 2016-12-25.
 */

public class PicItem {
    @JSONField(name = "id")
    private String id;
    @JSONField(name = "path")
    private String path;
    @JSONField(name = "type")
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSrcUrl() {
        return "http://" + AppContext.mDomain + "/data/images/" + path;
    }

    public String getThumbnailUrl() {
        return "http://" + AppContext.mDomain + "/data/thumbnail/" + path;
    }
}
