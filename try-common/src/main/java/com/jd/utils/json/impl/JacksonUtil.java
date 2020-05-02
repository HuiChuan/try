package com.jd.utils.json.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jd.utils.string.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Jackson公用解析类
 */
@Slf4j
public class JacksonUtil {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Pattern p = Pattern.compile("^\\{[\\s*\"\\w+\"=\"\\w+\",*\\s*]+\\}$");
    private static ConcurrentHashMap<String, String> DeserializerMap = new ConcurrentHashMap<String, String>();
    private static final String JSON = "JSON";

    static {

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);// 设置序列化配置，为null的属性不加入到json中
        //Json字符串中有未知属性时不报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String writeToStr(Object o) {
        String jsonStr = "";
        try {
            if (o != null) {
                jsonStr = objectMapper.writeValueAsString(o);
            }
        } catch (JsonProcessingException e) {
            log.error("JsonUtil writeToStr exception", e);
        }
        return jsonStr;
    }

    /**
     * 使用时，结果不可直接抛到前端，jackson只解析一级子节点
     *
     * @param json
     * @return
     */
    public static Object fromObject(String json) {
        try {
            if (isNotNull(json)) {
                return objectMapper.readTree(json);
            }
        } catch (Exception e) {
            log.error("JsonUtil fromObject exception[" + json + "]", e);
        }
        return null;
    }

    public static Map<String, Object> toMap(String json) {
        try {
            if (isNotNull(json)) {
                if (isMapString(json)) {
                    json = json.replace("=", ":");
                    return (Map<String, Object>) JSONObject.fromObject(json);
                }
                return objectMapper.readValue(json, Map.class);
            }
        } catch (Exception e) {
            log.error("JsonUtil toMap exception", e);
        }
        return null;
    }

    public static Map<String, Object> toMap(Object node) {
        try {
            if (isNode(node)) {
                return objectMapper.treeToValue((JsonNode) node, Map.class);
            }
        } catch (Exception e) {
            log.error("JsonUtil toMap exception", e);
        }
        return null;
    }

    public static Map<String, String> toMapOfString(String json) {
        try {
            if (isNotNull(json)) {
                return objectMapper.readValue(json, Map.class);
            }
        } catch (Exception e) {
            log.error("JsonUtil toMapOfString exception", e);
        }
        return null;
    }

    public static Map<String, String> toMapOfString(Object node) {
        try {
            if (isNode(node)) {
                return objectMapper.treeToValue((JsonNode) node, Map.class);
            }
        } catch (Exception e) {
            log.error("JsonUtil toMapOfString exception", e);
        }
        return null;
    }

    public static <T> T toBean(String json, Class<T> valueType) {
        try {
            if (isNotNull(json) && valueType != null) {
                return objectMapper.readValue(json, valueType);
            }
        } catch (Exception e) {
            log.error("JsonUtil toBean exception", e);
        }
        return null;
    }

    public static <T> T toBean(Object node, Class<T> valueType) {
        try {
            if (isNode(node) && valueType != null) {
                return objectMapper.treeToValue((JsonNode) node, valueType);
            }
        } catch (Exception e) {
            log.error("JsonUtil toBean exception", e);
        }
        return null;
    }

    public static <T> T toBean(String json, Class<T> valueType, Object jsonDeserializer) {
        try {
            if (isNotNull(json) && valueType != null
                    && jsonDeserializer != null && jsonDeserializer instanceof JsonDeserializer) {
                JacksonUtil.registerDeserializerModule(valueType, (JsonDeserializer) jsonDeserializer);
                return objectMapper.readValue(json, valueType);
            }
        } catch (Exception e) {
            log.error("JsonUtil toBean exception", e);
        }
        return null;
    }

    public static <T> T toBean(Object node, Class<T> valueType, Object jsonDeserializer) {
        try {
            if (isNode(node) && valueType != null
                    && jsonDeserializer != null && jsonDeserializer instanceof JsonDeserializer) {
                JacksonUtil.registerDeserializerModule(valueType, (JsonDeserializer) jsonDeserializer);
                return objectMapper.treeToValue((JsonNode) node, valueType);
            }
        } catch (Exception e) {
            log.error("JsonUtil toBean exception", e);
        }
        return null;
    }

    public static <T> List<T> toList(String json, Class<T> elementClasses) {
        try {
            if (isNotNull(json) && elementClasses != null) {
                JavaType javaTypeObj = objectMapper.getTypeFactory().constructParametricType(List.class, elementClasses);
                return objectMapper.readValue(json, javaTypeObj);
            }
        } catch (Exception e) {
            log.error("JsonUtil toList exception", e);
        }
        return null;
    }

    public static <T> List<T> toList(Object node, Class<T> elementClasses) {
        try {
            if (isArray(node) && elementClasses != null) {
                JavaType javaTypeObj = objectMapper.getTypeFactory().constructParametricType(List.class, elementClasses);
                return objectMapper.convertValue(node, javaTypeObj);
            }
        } catch (Exception e) {
            log.error("JsonUtil toList exception", e);
        }
        return null;
    }

    public static <T> List<T> toList(String json, Class<T> elementClasses, Object jsonDeserializer) {
        try {
            if (isNotNull(json) && elementClasses != null
                    && jsonDeserializer != null && jsonDeserializer instanceof JsonDeserializer) {
                JacksonUtil.registerDeserializerModule(elementClasses, (JsonDeserializer) jsonDeserializer);
                JavaType javaTypeObj = objectMapper.getTypeFactory().constructParametricType(List.class, elementClasses);
                return objectMapper.readValue(json, javaTypeObj);
            }
        } catch (Exception e) {
            log.error("JsonUtil toList exception", e);
        }
        return null;
    }

    public static <T> List<T> toList(String json, Class<T> elementClasses, Object jsonDeserializer, List<T> defaultValue) {
        try {
            if (isNotNull(json) && elementClasses != null
                    && jsonDeserializer != null && jsonDeserializer instanceof JsonDeserializer) {
                JacksonUtil.registerDeserializerModule(elementClasses, (JsonDeserializer) jsonDeserializer);
                JavaType javaTypeObj = objectMapper.getTypeFactory().constructParametricType(List.class, elementClasses);
                return objectMapper.readValue(json, javaTypeObj);
            }
        } catch (Exception e) {
            log.error("JsonUtil toList exception", e);
        }
        return defaultValue;
    }

    public static <T> List<T> toList(String json, Class<T> elementClasses, List<T> defaultValue) {
        try {
            if (isNotNull(json) && elementClasses != null) {
                JavaType javaTypeObj = objectMapper.getTypeFactory().constructParametricType(List.class, elementClasses);
                return objectMapper.readValue(json, javaTypeObj);
            }
        } catch (Exception e) {
            log.error("JsonUtil toList exception", e);
        }
        return defaultValue;
    }

    public static <T> List<T> toList(Object node, Class<T> elementClasses, List<T> defaultValue) {
        try {
            if (isArray(node) && elementClasses != null) {
                JavaType javaTypeObj = objectMapper.getTypeFactory().constructParametricType(List.class, elementClasses);
                return objectMapper.convertValue(node, javaTypeObj);
            }
        } catch (Exception e) {
            log.error("JsonUtil toList exception", e);
        }
        return defaultValue;
    }

    public static List<Map<String, Object>> toListOfMapObject(String json) {
        try {
            if (isNotNull(json)) {
//                List<Map<String,Object>> resultList = new ArrayList<Map<String, Object>>();
//                List<Object> objList = objectMapper.readValue(json, List.class);
//                Iterator it = objList.iterator();
//                while(it.hasNext()){
//                    Object item = it.next();
//                    if (item instanceof Map) {
//                        resultList.add((Map)item);
//                    }
//                }
//                return resultList;
                return objectMapper.readValue(json, List.class);
            }
        } catch (Exception e) {
            log.error("JsonUtil toListOfMapObject exception", e);
        }
        return new ArrayList<Map<String, Object>>();
    }

    public static List<Map<String, Object>> toListOfMapObject(Object node) {
        try {
            if (isNode(node)) {
//                List<Map<String,Object>> resultList = new ArrayList<Map<String, Object>>();
//                List<Object> objList = objectMapper.convertValue(node, List.class);
//                Iterator it = objList.iterator();
//                while(it.hasNext()){
//                    Object item = it.next();
//                    if (item instanceof Map) {
//                        resultList.add((Map)item);
//                    }
//                }
//                return resultList;
                return objectMapper.convertValue(node, List.class);
            }
        } catch (Exception e) {
            log.error("JsonUtil toListOfMapObject exception", e);
        }
        return new ArrayList<Map<String, Object>>();
    }

    public static List<Map<String, String>> toListOfMapString(String json) {
        try {
            if (isNotNull(json)) {
//                List<Map<String,String>> resultList = new ArrayList<Map<String, String>>();
//                List<Object> objList = objectMapper.readValue(json, List.class);
//                Iterator it = objList.iterator();
//                while(it.hasNext()){
//                    Object item = it.next();
//                    if (item instanceof Map) {
//                        resultList.add((Map)item);
//                    }
//                }
//                return resultList;
                return objectMapper.readValue(json, List.class);
            }
        } catch (Exception e) {
            log.error("JsonUtil toListOfMapString exception", e);
        }
        return new ArrayList<Map<String, String>>();
    }

    public static List<Map<String, String>> toListOfMapString(Object node) {
        try {
            if (isNode(node)) {
//                List<Map<String,String>> resultList = new ArrayList<Map<String, String>>();
//                List<Object> objList = objectMapper.convertValue(node, List.class);
//                Iterator it = objList.iterator();
//                while(it.hasNext()){
//                    Object item = it.next();
//                    if (item instanceof Map) {
//                        resultList.add((Map)item);
//                    }
//                }
//                return resultList;
                return objectMapper.convertValue(node, List.class);
            }
        } catch (Exception e) {
            log.error("JsonUtil toListOfMapString exception", e);
        }
        return new ArrayList<Map<String, String>>();
    }

    public static Object toNode(Object object) {
        try {
            if (object != null) {
                return objectMapper.valueToTree(object);
            }
        } catch (Exception e) {
            log.error("JsonUtil  toJson exception", e);
        }
        return null;
    }

    /**
     * 直接抛到前端展示的对象，不可为JsonNode节点，需转为Object或List、Map等
     * Jackson的节点JsonNode，只会解析下一层子节点，无法取到二级及以下节点，故不可直接返回到前端使用
     * 例：
     * result.addDefaultModel("feeDetail",feeDetailNode);
     * success:function(data){
     * var payAmount = data.feeDetail.payAmount;
     * }
     * 若直接返回JsonNode对象，将无法取到二级及以下节点payAmount
     * 这里采用JSONObject.class(Object.class也可以)
     *
     * @param json
     * @return
     */
    public static Object toJsonObject(String json) {
        try {
            if (isNotNull(json)) {
                JsonNode root = objectMapper.readTree(json);
                if (root != null) {
                    if (root.isValueNode()) {
                        return root.asText();
                    }

                    if (root.isArray()) {
                        return objectMapper.convertValue(root, JSONArray.class);
                    }

                    return objectMapper.convertValue(root, Object.class);
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  toJson exception", e);
        }
        return null;
    }

    public static Object toJsonObject(Object obj) {
        try {
            if (obj != null) {
                if (isArray(obj)) {
                    return objectMapper.convertValue(obj, JSONArray.class);
                }
                return objectMapper.convertValue(obj, JSONObject.class);
            }
        } catch (Exception e) {
            log.error("JsonUtil toJsonObject exception", e);
        }
        return null;
    }

    public static Object toJsonArray(Object obj) {
        try {
            if (obj != null) {
                return objectMapper.convertValue(obj, JSONArray.class);
            }
        } catch (Exception e) {
            log.error("JsonUtil toJsonArray exception", e);
        }
        return null;
    }

    public static Object toJsonObject(Object node, String fieldName) {
        try {
            if (isNode(node) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = (JsonNode) node;
                JsonNode subNode = root.get(fieldName);
                if (subNode != null) {
                    if (subNode.isValueNode()) {
                        return subNode.asText();
                    }

                    if (subNode.isArray()) {
                        return objectMapper.convertValue(subNode, JSONArray.class);
                    }

                    return objectMapper.convertValue(subNode, JSONObject.class);
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  toJson exception", e);
        }
        return null;
    }

    public static Object getNode(Object node, String fieldName) {
        try {
            if (isNode(node) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = (JsonNode) node;
                JsonNode subNode = root.get(fieldName);
                if (subNode != null) {
                    return subNode;
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  get exception", e);
        }
        return null;
    }

    public static Object getSubNode(Object node, String fieldName, String subFieldName) {
        try {
            if (isNode(node)
                    && StringUtils.isNotBlank(fieldName)
                    && StringUtils.isNotBlank(subFieldName)) {
                JsonNode root = (JsonNode) node;
                JsonNode subNode = root.get(fieldName);
                if (subNode != null) {
                    if (subNode.get(subFieldName) != null) {
                        return subNode.get(subFieldName);
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  get exception", e);
        }
        return null;
    }

    public static Iterator<String> keys(Object node) {
        try {
            if (isNode(node)) {
                JsonNode root = (JsonNode) node;
                return root.fieldNames();
            }
        } catch (Exception e) {
            log.error("JsonUtil  get exception", e);
        }
        return null;
    }

    public static List getNodeArray(Object node, String fieldName) {
        List resultList = new ArrayList();
        try {
            if (isNode(node) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = (JsonNode) node;
                JsonNode subNode = root.get(fieldName);
                if (subNode != null && isArray(subNode)) {
                    for (JsonNode objNode : subNode) {
                        resultList.add(objNode);
                    }
                    return resultList;
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  get exception", e);
        }
        return resultList;
    }

    public static List getNodeArray(Object node) {
        List resultList = new ArrayList();
        try {
            if (isNode(node) && isArray(node)) {
                JsonNode root = (JsonNode) node;
                for (JsonNode objNode : root) {
                    resultList.add(objNode);
                }
                return resultList;
            }
        } catch (Exception e) {
            log.error("JsonUtil  get exception", e);
        }
        return resultList;
    }

    public static String getValueAsJson(String json, String fieldName) {
        try {
            if (isNotNull(json) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = objectMapper.readTree(json);
                if (root != null) {
                    JsonNode node = root.get(fieldName);
                    if (node != null) {
                        return node.toString();
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getValueAsJson exception", e);
        }
        return null;
    }

    public static String getValueAsJson(Object node, String fieldName) {
        try {
            if (isNode(node) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = (JsonNode) node;
                JsonNode subNode = root.get(fieldName);
                if (subNode != null) {
                    return subNode.toString();
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getValueAsJson exception", e);
        }
        return null;
    }

    public static String getValueAsString(String json, String fieldName) {
        try {
            if (isNotNull(json) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = objectMapper.readTree(json);
                if (root != null) {
                    JsonNode node = root.get(fieldName);
                    if (node != null) {
                        if (node.isValueNode()) {
                            return node.asText();
                        } else {
                            return node.toString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getValueAsString exception", e);
        }
        return null;
    }

    public static String getValueAsString(Object node, String fieldName) {
        try {
            if (isNode(node) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = (JsonNode) node;
                JsonNode subNode = root.get(fieldName);
                if (subNode != null) {
                    if (subNode.isValueNode()) {
                        return subNode.asText();
                    } else {
                        return subNode.toString();
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getValueAsString exception", e);
        }
        return null;
    }

    public static Boolean getValueAsBoolean(String json, String fieldName) {
        try {
            if (isNotNull(json) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = objectMapper.readTree(json);
                if (root != null) {
                    JsonNode node = root.get(fieldName);
                    if (node != null) {
                        return node.asBoolean();
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getValueAsBoolean exception", e);
        }
        return Boolean.FALSE;
    }

    public static Boolean getValueAsBoolean(Object node, String fieldName) {
        try {
            if (isNode(node) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = (JsonNode) node;
                JsonNode subNode = root.get(fieldName);
                if (subNode != null && subNode.isValueNode()) {
                    return subNode.asBoolean();
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getValueAsBoolean exception", e);
        }
        return Boolean.FALSE;
    }

    public static Integer getValueAsInt(String json, String fieldName) {
        try {
            if (isNotNull(json) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = objectMapper.readTree(json);
                if (root != null) {
                    JsonNode node = root.get(fieldName);
                    if (node != null && node.isValueNode()) {
                        return node.asInt();
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getValueAsInt exception", e);
        }
        return 0;
    }

    public static Integer getValueAsInt(Object node, String fieldName) {
        try {
            if (isNode(node) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = (JsonNode) node;
                JsonNode subNode = root.get(fieldName);
                if (subNode != null && subNode.isValueNode()) {
                    return subNode.asInt();
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getValueAsInt exception", e);
        }
        return 0;
    }

    public static Long getValueAsLong(String json, String fieldName) {
        try {
            if (isNotNull(json) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = objectMapper.readTree(json);
                if (root != null) {
                    JsonNode node = root.get(fieldName);
                    if (node != null && node.isValueNode()) {
                        return node.asLong();
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getValueAsLong exception", e);
        }
        return Long.valueOf(0);
    }

    public static Long getValueAsLong(Object node, String fieldName) {
        try {
            if (isNode(node) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = (JsonNode) node;
                JsonNode subNode = root.get(fieldName);
                if (subNode != null && subNode.isValueNode()) {
                    return subNode.asLong();
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getValueAsLong exception", e);
        }
        return Long.valueOf(0);
    }

    public static Double getValueAsDouble(String json, String fieldName) {
        try {
            if (isNotNull(json) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = objectMapper.readTree(json);
                if (root != null) {
                    JsonNode node = root.get(fieldName);
                    if (node != null && node.isValueNode()) {
                        return node.asDouble();
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getValueAsDouble exception", e);
        }
        return Double.valueOf(0);
    }

    public static Double getValueAsDouble(Object node, String fieldName) {
        try {
            if (isNode(node) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = (JsonNode) node;
                JsonNode subNode = root.get(fieldName);
                if (subNode != null && subNode.isValueNode()) {
                    return subNode.asDouble();
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getValueAsDouble exception", e);
        }
        return Double.valueOf(0);
    }

    public static String getSubAsJson(String json, String fieldName, String subFieldName) {
        try {
            if (isNotNull(json) && StringUtils.isNotBlank(fieldName)
                    && StringUtils.isNotBlank(subFieldName)) {
                JsonNode root = objectMapper.readTree(json);
                if (root != null) {
                    JsonNode node = root.get(fieldName);
                    if (node != null && node.get(subFieldName) != null) {
                        return node.get(subFieldName).toString();
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getSubAsJson exception", e);
        }
        return null;
    }

    public static Object toSubJsonObject(String json, String fieldName, String subFieldName) {
        try {
            if (isNotNull(json) && StringUtils.isNotBlank(fieldName)
                    && StringUtils.isNotBlank(subFieldName)) {
                JsonNode root = objectMapper.readTree(json);
                if (root != null) {
                    JsonNode node = root.get(fieldName);
                    if (node != null && node.get(subFieldName) != null) {
                        JsonNode subNode = node.get(subFieldName);

                        if (subNode.isValueNode()) {
                            return subNode.asText();
                        }

                        if (subNode.isArray()) {
                            return objectMapper.convertValue(subNode, JSONArray.class);
                        }

                        return objectMapper.convertValue(subNode, JSONObject.class);
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getSubValue exception", e);
        }
        return null;
    }

    public static String getSubValueAsString(Object node, String fieldName, String subFieldName) {
        try {
            JsonNode subSubNode = getSubSubNode(node, fieldName, subFieldName);
            if (subSubNode != null && subSubNode.isValueNode()) {
                return subSubNode.asText();
            } else if (subSubNode != null) {
                return subSubNode.toString();
            }
        } catch (Exception e) {
            log.error("JsonUtil  getSubValueAsString exception", e);
        }
        return null;
    }

    public static Boolean getSubValueAsBoolean(Object node, String fieldName, String subFieldName) {
        try {
            JsonNode subSubNode = getSubSubNode(node, fieldName, subFieldName);
            if (subSubNode != null && subSubNode.isValueNode()) {
                return subSubNode.asBoolean();
            }
        } catch (Exception e) {
            log.error("JsonUtil  getSubValueAsBoolean exception", e);
        }
        return Boolean.FALSE;
    }

    public static Integer getSubValueAsInt(Object node, String fieldName, String subFieldName) {
        try {
            JsonNode subSubNode = getSubSubNode(node, fieldName, subFieldName);
            if (subSubNode != null && subSubNode.isValueNode()) {
                return subSubNode.asInt();
            }
        } catch (Exception e) {
            log.error("JsonUtil  getSubValueAsInt exception", e);
        }
        return 0;
    }

    public static Long getSubValueAsLong(Object node, String fieldName, String subFieldName) {
        try {
            JsonNode subSubNode = getSubSubNode(node, fieldName, subFieldName);
            if (subSubNode != null && subSubNode.isValueNode()) {
                return subSubNode.asLong();
            }
        } catch (Exception e) {
            log.error("JsonUtil  getSubValueAsLong exception", e);
        }
        return Long.valueOf(0);
    }

    private static JsonNode getSubSubNode(Object node, String fieldName, String subFieldName) {
        if (isNode(node) && StringUtils.isNotBlank(fieldName)
                && StringUtils.isNotBlank(subFieldName)) {
            JsonNode root = (JsonNode) node;
            if (root != null) {
                JsonNode subNode = root.get(fieldName);
                if (subNode != null && subNode.get(subFieldName) != null) {
                    return subNode.get(subFieldName);
                }
            }
        }
        return null;
    }

    /**
     * 是否包含属性
     *
     * @param json
     * @param fieldName
     * @return
     */
    public static boolean has(String json, String fieldName) {
        try {
            if (isNotNull(json) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = objectMapper.readTree(json);
                if (root != null) {
                    JsonNode node = root.get(fieldName);
                    if (node != null) {
                        return Boolean.TRUE;
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  has exception", e);
        }
        return Boolean.FALSE;
    }

    public static boolean has(Object node, String fieldName) {
        try {
            if (isNode(node) && StringUtils.isNotBlank(fieldName)) {
                JsonNode root = (JsonNode) node;
                if (root != null) {
                    JsonNode fieldNode = root.get(fieldName);
                    if (fieldNode != null) {
                        return Boolean.TRUE;
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  has exception", e);
        }
        return Boolean.FALSE;
    }

    public static boolean has(Object node, String fieldName, String subFieldName) {
        try {
            if (isNode(node) && StringUtils.isNotBlank(fieldName)
                    && StringUtils.isNotBlank(subFieldName)) {
                JsonNode root = (JsonNode) node;
                if (root != null) {
                    JsonNode fieldNode = root.get(fieldName);
                    if (fieldNode != null && fieldNode.get(subFieldName) != null) {
                        return Boolean.TRUE;
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  has exception", e);
        }
        return Boolean.FALSE;
    }

    public static boolean has(String json, String fieldName, String subFieldName) {
        try {
            if (isNotNull(json) && StringUtils.isNotBlank(fieldName)
                    && StringUtils.isNotBlank(subFieldName)) {
                JsonNode root = objectMapper.readTree(json);
                if (root != null) {
                    JsonNode node = root.get(fieldName);
                    if (node != null && node.get(subFieldName) != null) {
                        return Boolean.TRUE;
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  has exception", e);
        }
        return Boolean.FALSE;
    }

    /**
     * 节点是否不为空
     *
     * @param json
     * @return
     */
    public static boolean isNotNull(String json) {
        try {
            if (StringUtils.isNotBlank(json)
                    && !("''".equals(json))
                    && !("\"\"".equals(json))
                    && !StringUtils.equalsIgnoreCase("null", json)) {

                return Boolean.TRUE;
            }
        } catch (Exception e) {
            log.error("JsonUtil  isNotNull exception", e);
        }
        return false;
    }

    public static boolean isNotNull(Object node) {
        try {
            if (node != null && node instanceof JsonNode
                    && !((JsonNode) node).isNull()) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            log.error("JsonUtil  isNotNull exception", e);
        }
        return false;
    }

    public static boolean isNode(Object node) {
        try {
            if (node != null && node instanceof JsonNode) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            log.error("JsonUtil  isNotNull exception", e);
        }
        return false;
    }

    /**
     * 是否数组
     *
     * @param json
     * @return
     */
    public static boolean isArray(String json) {
        try {
            if (StringUtils.isNotBlank(json)) {
                JsonNode root = objectMapper.readTree(json);
                if (root != null && root.isArray()) {

                    return Boolean.TRUE;
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  isArray exception", e);
        }
        return false;
    }

    public static boolean isArray(Object node) {
        try {
            if (isNode(node)) {
                JsonNode root = (JsonNode) node;
                if (root != null && root.isArray()) {

                    return Boolean.TRUE;
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  isArray exception", e);
        }
        return false;
    }

    public static Integer getArraySize(Object node) {
        try {
            if (isArray(node)) {
                JsonNode root = (JsonNode) node;
                if (root != null && root.isArray()) {

                    return root.size();
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  getArraySize exception", e);
        }
        return 0;
    }

    public static void remove(Object node, String fieldName) {
        try {
            if (isNode(node) && StringUtils.isNotBlank(fieldName)) {
                ObjectNode root = (ObjectNode) node;
                if (root != null) {
                    root.remove(fieldName);
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  remove exception", e);
        }
    }

    public static void put(Object node, String fieldName, Object value) {
        try {
            if (isNode(node) && StringUtils.isNotBlank(fieldName)
                    && value != null) {
                ObjectNode root = (ObjectNode) node;
                if (root != null) {
                    if (value instanceof String) {
                        root.put(fieldName, value.toString());
                    } else if (value instanceof JsonNode) {
                        root.put(fieldName, (JsonNode) value);
                    } else if (value instanceof Double) {
                        root.put(fieldName, (Double) value);
                    } else if (value instanceof Integer) {
                        root.put(fieldName, (Integer) value);
                    } else if (value instanceof Boolean) {
                        root.put(fieldName, (Boolean) value);
                    } else {
                        Object valueNode = toNode(value);
                        if (valueNode != null && valueNode instanceof JsonNode) {
                            root.put(fieldName, (JsonNode) valueNode);
                        } else {
                            root.put(fieldName, value.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  put exception", e);
        }
    }

    /**
     * 只针对数组
     *
     * @param node
     * @param value
     */
    public static void set(Object node, int index, Object value) {
        try {
            if (isArray(node)
                    && value != null) {
                ArrayNode root = (ArrayNode) node;
                if (root != null && root.isArray()
                        && root.size() > index) {
                    root.set(index, (JsonNode) value);
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  set exception", e);
        }
    }

    /**
     * 只针对数组
     *
     * @param node
     * @param value
     */
    public static void add(Object node, Object value) {
        try {
            if (isArray(node) && value != null) {
                ArrayNode root = (ArrayNode) node;
                if (root != null && root.isArray()) {
                    if (value instanceof String) {
                        root.add(value.toString());
                    } else if (value instanceof JsonNode) {
                        root.add((JsonNode) value);
                    } else if (value instanceof Double) {
                        root.add((Double) value);
                    } else if (value instanceof Integer) {
                        root.add((Integer) value);
                    } else if (value instanceof Boolean) {
                        root.add((Boolean) value);
                    } else {
                        Object valueNode = toNode(value);
                        if (valueNode != null && valueNode instanceof JsonNode) {
                            root.add((JsonNode) valueNode);
                        } else {
                            root.add(value.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  add exception", e);
        }
    }

    public static <T> void registerDeserializerModule(Class<T> type, JsonDeserializer<? extends T> deser) {
        try {
            if (null != type && deser != null) {
                if (!DeserializerMap.containsKey(type.toString())) {
                    SimpleModule module = new SimpleModule();
                    DeserializerMap.put(type.toString(), deser.toString());
                    module.addDeserializer(type, deser);
                    objectMapper.registerModule(module);
                }
            }
        } catch (Exception e) {
            log.error("JsonUtil  registerDeserializerModule exception", e);
        }
    }

    /**
     * 检查是否为mapString格式数据，{key=value,key2=value2,key3=value3}
     * <p>
     * Jackson默认会将各个节点，转换为LinkMap，
     * 前端vm使用input hidden，struts2，ognl表达式，会将map转换为字符串,得到类似{key=value,key2=value2,key3=value3}
     * 的数据，传入到后台，将无法再转换为Map
     * 例
     * <input type="hidden" id="big_promiseSendPay" value='$!bigItemJZD.promiseSendPay'/>
     * promiseSendPay为map对象，放到input，value将变为map.toString,类似{33=8}；
     * 后端调用toMap方法时，将无法解析
     * 故前端在使用时，最好不要直接将对象放到value属性上
     *
     * @param json
     * @return
     */
    private static boolean isMapString(String json) {
        Matcher m = p.matcher(json);
        if (m.find()) {
            return true;
        }
        return false;
    }
}