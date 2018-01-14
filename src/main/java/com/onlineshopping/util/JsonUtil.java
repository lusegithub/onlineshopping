package com.onlineshopping.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jacky on 17-6-9.
 */
public class JsonUtil {

    private static final ObjectMapper objectMapper=new ObjectMapper();

    /**
     * @param json
     * @param cls
     * @return json数据解析后的对象，使用的时候需要对结果进行强制类型转换
     * @throws IOException
     */
    public static Object jsonToObject(String json,Class<?> cls) throws IOException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(fmt);
        Object result=objectMapper.readValue(json,cls);
        return result;
    }

    /**
     * @param object
     * @return json格式数据
     * @throws JsonProcessingException
     */
    public static String objectToJson(Object object) throws JsonProcessingException {
        String result=objectMapper.writeValueAsString(object);
        return result;
    }

    /**
     * @param json
     * @return HashMap对象
     * @throws IOException
     */
    public static Map<String ,Object> jsonToMap(String json) throws IOException {
        Map<String ,Object> map=objectMapper.readValue(json, HashMap.class);
        return map;
    }
}
