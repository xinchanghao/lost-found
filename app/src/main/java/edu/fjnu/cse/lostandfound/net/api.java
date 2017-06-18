package edu.fjnu.cse.lostandfound.net;

import android.app.Activity;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.fjnu.cse.lostandfound.activity.AppContext;
import edu.fjnu.cse.lostandfound.entities.API_Return;
import edu.fjnu.cse.lostandfound.entities.API_Send;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * API接口实现
 * Created by zspmh on 2016-11-29.
 */
public class api {
    private static final MediaType mediaTypeJson = MediaType.parse("application/json; charset=utf-8");
    private static String url = "http://" + AppContext.mDomain + "/api.php";
    private static final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

    private static Class getActualTypeInterface(Class entity) {
        Class clazz = entity;
        while (clazz != Object.class) {
            Type t = clazz.getGenericInterfaces()[0];
            if (t instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    return (Class) args[0];
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    private static Class getActualTypeClass(Class entity) {
        Class clazz = entity;
        while (clazz != Object.class) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    return (Class) args[0];
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    public static void Request(Object Send, final API_Return ret, final Activity activity) {
        API_Send sendObject = new API_Send(Send.getClass().getSimpleName().substring(4), Send);
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url, cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url);
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
        RequestBody body = RequestBody.create(mediaTypeJson, JSON.toJSONString(sendObject));

        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ret.ret(1, null);
                        }
                    });
                } else {
                    ret.ret(1, null);
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //Class a = getActualTypeClass();
                Class retClass = getActualTypeInterface(ret.getClass());
                if (retClass == null) {
                    ret.ret(1, null);
                    return;
                }
                final Object resultData;
                try {
                    String body = response.body().string();
                    Log.d("api-Body", body);
                    resultData = JSON.parseObject(body, retClass);
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ret.ret(0, resultData);
                            }
                        });
                    } else {
                        ret.ret(0, resultData);
                    }
                } catch (JSONException e) {
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ret.ret(2, null);
                            }
                        });
                    } else {
                        ret.ret(2, null);
                    }
                }
            }

        });
    }


    public static void Request(Object Send, final API_Return ret) {
        API_Send sendObject = new API_Send(Send.getClass().getSimpleName().substring(4), Send);
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url, cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url);
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
        RequestBody body = RequestBody.create(mediaTypeJson, JSON.toJSONString(sendObject));
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ret.ret(1, null);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Class retClass = getActualTypeInterface(ret.getClass());
                if (retClass == null) {
                    ret.ret(1, null);
                    return;
                }
                final Object resultData;
                try {
                    String body = response.body().string();
                    //Log.d("api-Body", body);
                    resultData = JSON.parseObject(body, retClass);
                    ret.ret(0, resultData);
                } catch (JSONException e) {
                    ret.ret(2, null);
                }
            }

        });
    }
}
