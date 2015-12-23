package com.smartbracelet.sunny.utils;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sunny on 2015/12/23.
 * 简单的Json格式数据解析工具类：
 * json格式如下：
 * {"errorCode":"0000",
 * "warnCode":"0000",
 * "result":""}
 * 注：如果是一个有经验的后台设计者，这个返回的格式最外层基本是一至，便于封装。
 * 即，errorCode,warnCode,result等这些基本的key是固定的，
 * 由于目前这套接口完全没有任何的封装，故就此说明。
 */
public class SimpleJsonParseUtils {

    /**
     * @param json      后台返回的json数据
     * @param reslutKey jsono数据中值对应的key,由于不确定性，所以这个key必须由外面传进来
     * @return
     */
    public static String getResult(String json, String reslutKey) {

        String result = "";

        if (TextUtils.isEmpty(json)) {
            throw new NullPointerException("the result json data may not be null");
        }

        if (TextUtils.isEmpty(reslutKey)) {
            throw new IllegalArgumentException("Attention that you must pass into the result key!!");
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(reslutKey)) {
                result = jsonObject.getString(reslutKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;

    }
}
