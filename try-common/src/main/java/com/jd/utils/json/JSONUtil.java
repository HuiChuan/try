package com.jd.utils.json;

import com.jd.utils.json.impl.JacksonUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 公用json解析类
 */
public class JSONUtil {
    private final static JacksonUtil instance = new JacksonUtil();

    /**
     * 对象转为json字符串
     * @param o
     * @return
     */
    public static String writeToStr(Object o){
        return instance.writeToStr(o);
    }

    /**
     * json字符串转为node
     * @param json
     * @return
     */
    public static Object fromObject(String json){
        return instance.fromObject(json);
    }

    /**
     * 转为Map<String, Object>
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(String json){
        return instance.toMap(json);
    }

    public static Map<String, Object> toMap(Object node){
        return instance.toMap(node);
    }

    /**
     * 转为Map<String, String>
     * @param json
     * @return
     */
    public static Map<String, String> toMapOfString(String json){
        return instance.toMapOfString(json);
    }

    public static Map<String, String> toMapOfString(Object node){
        return instance.toMapOfString(node);
    }

    public static <T> T toBean(String json, Class<T> valueType) {
        return instance.toBean(json, valueType);
    }

    public static <T> T toBean(Object node, Class<T> valueType) {
        return instance.toBean(node, valueType);
    }

    /**
     * 自定义转换bean，Jackson传入jsonDeserializer，jsonLib传入config
     * @param json
     * @param valueType
     * @param jsonConfig
     * @param <T>
     * @return
     */
    public static <T> T toBean(String json, Class<T> valueType, Object jsonConfig) {
        return instance.toBean(json, valueType, jsonConfig);
    }

    public static <T> T toBean(Object node, Class<T> valueType, Object jsonConfig) {
        return instance.toBean(node, valueType, jsonConfig);
    }

    /**
     * 转为List<Bean>
     * @param jsonStr
     * @param elementClasses
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String jsonStr, Class<T> elementClasses){
        return instance.toList(jsonStr, elementClasses);
    }

    public static <T> List<T> toList(Object node, Class<T> elementClasses){
        return instance.toList(node, elementClasses);
    }

    /**
     * 转为List<Bean>, 若为空返回默认值
     * @param jsonStr
     * @param elementClasses
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String jsonStr, Class<T> elementClasses, List<T> defaultValue){
        return instance.toList(jsonStr, elementClasses, defaultValue);
    }

    public static <T> List<T> toList(Object node, Class<T> elementClasses, List<T> defaultValue){
        return instance.toList(node, elementClasses, defaultValue);
    }

    /**
     * 转为List<Bean>,自定义转换bean
     * @param jsonStr
     * @param elementClasses
     * @param jsonConfig
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String jsonStr, Class<T> elementClasses, Object jsonConfig){
        return instance.toList(jsonStr, elementClasses, jsonConfig);
    }

    /**
     * 转为List<Bean>,自定义转换bean，失败则返回默认
     * @param jsonStr
     * @param elementClasses
     * @param jsonConfig
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String jsonStr, Class<T> elementClasses, Object jsonConfig, List<T> defaultValue){
        return instance.toList(jsonStr, elementClasses, jsonConfig, defaultValue);
    }

    public static List<Map<String, Object>> toListOfMapObject(String json){
        return instance.toListOfMapObject(json);
    }

    public static List<Map<String, Object>> toListOfMapObject(Object node){
        return instance.toListOfMapObject(node);
    }

    public static List<Map<String, String>> toListOfMapString(String json){
        return instance.toListOfMapString(json);
    }

    public static List<Map<String, String>> toListOfMapString(Object node){
        return instance.toListOfMapString(node);
    }

    public static Object toNode(Object object){
        return instance.toNode(object);
    }

    /**
     * 获取节点对象，直接前端使用
     * @param json
     * @return
     */
    public static Object toJsonObject(String json){
        return instance.toJsonObject(json);
    }

    public static Object toJsonObject(Object obj){
        return instance.toJsonObject(obj);
    }

    public static Object toJsonArray(Object obj){
        return instance.toJsonArray(obj);
    }

    public static Object toJsonObject(Object node, String fieldName){
        return instance.toJsonObject(node, fieldName);
    }

    public static Object getNode(Object node, String fieldName){
        return instance.getNode(node, fieldName);
    }

    public static Object getSubNode(Object node, String fieldName, String subFieldName){
        return instance.getSubNode(node, fieldName, subFieldName);
    }

    public static Iterator<String> keys(Object node){
        return instance.keys(node);
    }

    public static List getNodeArray(Object node,String fieldName){
        return instance.getNodeArray(node, fieldName);
    }

    public static List getNodeArray(Object node){
        return instance.getNodeArray(node);
    }

    /**
     * 获取节点field，json字符串
     * @param json
     * @param fieldName
     * @return
     */
    public static String getValueAsJson(String json,String fieldName){
        return instance.getValueAsJson(json, fieldName);
    }

    public static String getValueAsJson(Object node,String fieldName){
        return instance.getValueAsJson(node, fieldName);
    }

    public static String getValueAsString(String json,String fieldName){
        return instance.getValueAsString(json, fieldName);
    }

    public static String getValueAsString(Object node,String fieldName){
        return instance.getValueAsString(node, fieldName);
    }

    public static Boolean getValueAsBoolean(String json,String fieldName){
        return instance.getValueAsBoolean(json, fieldName);
    }

    public static Boolean getValueAsBoolean(Object node,String fieldName){
        return instance.getValueAsBoolean(node, fieldName);
    }

    public static Integer getValueAsInt(String json,String fieldName){
        return instance.getValueAsInt(json, fieldName);
    }

    public static Integer getValueAsInt(Object node,String fieldName){
        return instance.getValueAsInt(node, fieldName);
    }

    public static Long getValueAsLong(String json,String fieldName){
        return instance.getValueAsLong(json, fieldName);
    }

    public static Long getValueAsLong(Object node,String fieldName){
        return instance.getValueAsLong(node, fieldName);
    }

    public static Double getValueAsDouble(String json,String fieldName){
        return instance.getValueAsDouble(json, fieldName);
    }

    public static Double getValueAsDouble(Object node,String fieldName){
        return instance.getValueAsDouble(node, fieldName);
    }

    /**
     * 获取子节点对象，直接前端使用
     * @param json
     * @param fieldName
     * @param subField
     * @return
     */
    public static Object toSubJsonObject(String json,String fieldName, String subField){
        return instance.toSubJsonObject(json, fieldName, subField);
    }

    public static String getSubAsJson(String json,String fieldName, String subField){
        return instance.getSubAsJson(json, fieldName, subField);
    }

    public static String getSubValueAsString(Object node,String fieldName, String subField){
        return instance.getSubValueAsString(node, fieldName, subField);
    }

    public static Boolean getSubValueAsBoolean(Object node,String fieldName, String subField){
        return instance.getSubValueAsBoolean(node, fieldName, subField);
    }

    public static Integer getSubValueAsInt(Object node,String fieldName, String subField){
        return instance.getSubValueAsInt(node, fieldName, subField);
    }

    public static Long getSubValueAsLong(Object node,String fieldName, String subField){
        return instance.getSubValueAsLong(node, fieldName, subField);
    }

    /**
     * 是否包含属性
     * @param json
     * @param fieldName
     * @return
     */
    public static boolean has(String json,String fieldName){
        return instance.has(json, fieldName);
    }

    public static boolean has(Object node,String fieldName){
        return instance.has(node, fieldName);
    }

    /**
     * 是否包含子属性
     * @param json
     * @param fieldName
     * @return
     */
    public static boolean has(String json,String fieldName, String subFieldName){
        return instance.has(json, fieldName, subFieldName);
    }

    public static boolean has(Object node, String fieldName, String subFieldName){
        return instance.has(node, fieldName, subFieldName);
    }

    /**
     * 节点是否不为空
     * @param json
     * @return
     */
    public static boolean isNotNull(String json){
        return instance.isNotNull(json);
    }

    public static boolean isNotNull(Object node){
        return instance.isNotNull(node);
    }

    /**
     * 节点是否为node
     * @param node
     * @return
     */
    public static boolean isNode(Object node){
        return instance.isNode(node);
    }

    /**
     * 是否数组
     * @param json
     * @return
     */
    public static boolean isArray(String json){
        return instance.isArray(json);
    }

    public static boolean isArray(Object node){
        return instance.isArray(node);
    }

    public static Integer getArraySize(Object node){
        return instance.getArraySize(node);
    }

    public static void remove(Object node, String fieldName){
        instance.remove(node, fieldName);
    }

    public static void put(Object node, String fieldName, Object value){
        instance.put(node, fieldName, value);
    }

    public static void add(Object node, Object value){
        instance.add(node, value);
    }

    public static void set(Object node, int index, Object value){
        instance.set(node, index, value);
    }
}
