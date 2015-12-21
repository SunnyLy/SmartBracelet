package com.smartbracelet.sunny.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.het.common.utils.GsonUtil;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.model.TimeBloodPressure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.logging.MemoryHandler;

/**
 * Created by sunny on 2015/11/23.
 * 把Json转化为Model工具类
 */
public class Json2Model {

    public Json2Model() {

    }


    public static <T> T parseJson(String json, Class<T> tClass) {
        if (TextUtils.isEmpty(json))
            throw new IllegalArgumentException("the json may not be null");

        T t = GsonUtil.getGsonInstance().fromJson(json, tClass);

        return t;
    }

    /**
     * 解析List
     *
     * @param json
     * @param key    服务器返回的json中，list的key
     * @param tClass Model
     * @param <T>
     * @return 这个对象的集合
     */
    public static <T> List<T> parseJsonToList(String json, String key, Class<T> tClass) {
        List<T> tList = new ArrayList<>();

        if (TextUtils.isEmpty(json)) {
            throw new NullPointerException("the json can not be null");
        }

        if (tClass == null) {
            throw new NullPointerException("the Model can not be null");
        }

        Field[] fields = tClass.getDeclaredFields();
        Method[] methods = tClass.getDeclaredMethods();
        Method modelMethod;

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = null;
            if (!jsonObject.has(key)) {
                throw new IllegalArgumentException("the key is not exist,please to sure ");
            }
            jsonArray = jsonObject.getJSONArray(key);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                    T model = tClass.newInstance();
                    for (Field field : fields) {
                        String modelKey = field.getName();
                        StringBuilder nameBuilder = new StringBuilder(modelKey);
                        nameBuilder.setCharAt(0, (nameBuilder.charAt(0) + "").toUpperCase().charAt(0));
                        modelMethod = tClass.getMethod("set" + nameBuilder, String.class);
                        String modelValue = jsonObject1.getString(modelKey);
                        //再把这个值 set进去
                        modelMethod.invoke(model, modelValue);
                    }

                    tList.add(model);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return tList;
    }
}
