package edu.fjnu.cse.lostandfound.entities;

/**
 * api return接口
 * Created by zspmh on 2016-12-23.
 */

public interface API_Return<T> {
    void ret(int Code, T ret);
}
