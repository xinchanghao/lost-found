package edu.fjnu.cse.lostandfound.entities;

/**
 * API found接口
 * Created by zspmh on 2017-6-10.
 */

public class API_Post {
    private String detail;
    private String user;
    private String label;
    private String time;
    private String place;
    private String[] image;
    private int type;

    public String[] getImage() {
        return image;
    }

    public API_Post(String user, int type, String label, String time, String place, String detail, String[] image) {
        this.detail = detail;
        this.user = user;
        this.type = type;
        this.label = label;
        this.time = time;
        this.place = place;
        this.image = image;
    }


}
