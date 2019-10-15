package com.slc.agent.utils;


import com.slc.agent.utils.json.JsonWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化工具类
 * @author zrh
 */
public class JsonUtil {
    public static String toJsonStr(Object obj) {
        Map<String, Object> item = new HashMap<>(2);
        item.put("TYPE", false);
        item.put(JsonWriter.SKIP_NULL_FIELDS, true);
        return JsonWriter.objectToJson(obj, item);
    }

}

