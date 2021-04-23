package com.framelibrary.util.gsonconverter;

import com.framelibrary.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: wangweixu
 * @Date: 2021/03/23 10:33:03
 * @Description: Gson工具类
 * @Version: v1.0
 */
public class GsonUtil {
    //不用创建对象,直接使用Gson.就可以调用方法
    private static Gson gson = null;

    //判断gson对象是否存在了,不存在则创建对象
    static {
        if (gson == null) {

            // 当使用GsonBuilder方式时属性为空的时候输出来的json字符串是有键值key的,显示形式是"key":null，而直接new出来的就没有"key":null的
            // 可设置serializeNulls()序列化后的内容包含"key":null
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .serializeNulls()
                    .registerTypeAdapter(String.class, new StringAdapter())
                    .registerTypeAdapterFactory(TypeAdapters.newFactory(int.class, Integer.class, new IntegerAdapter()))
                    // .registerTypeAdapterFactory(CustomGsonObjectTypeAdapter.FACTORY)
                    //  .registerTypeHierarchyAdapter(Object.class, new CustomGsonObjectTypeAdapter())
//                      .registerTypeAdapterFactory(CustomGsonObjectTypeAdapter.FACTORY)

                    .setDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                Class builder = gsonBuilder.getClass();
                Field f = builder.getDeclaredField("instanceCreators");
                f.setAccessible(true);
                Map<Type, InstanceCreator<?>> val = (Map<Type, InstanceCreator<?>>) f.get(gsonBuilder);
                gsonBuilder.registerTypeAdapterFactory(new CustomCollectionTypeAdapterFactory(new ConstructorConstructor(val)));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                gsonBuilder.registerTypeAdapterFactory(new CustomCollectionTypeAdapterFactory(new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>())));
            }

            gson = gsonBuilder.create();

        }
    }

    //无参的私有构造方法
    private GsonUtil() {
    }

    /**
     * 将对象转成json格式
     *
     * @param object
     * @return String
     */
    public static String string(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    public static List<Object> joValue(String gsonString) {
        return joValue(gsonString, "data");
    }

    /**
     * 根据传递的JO key 去json数据中查找对应的value
     *
     * @param gsonString
     * @param keyParam
     * @return 当前gson 或者json数据是空 直接返回null,如果根据key获取到value则返回值是value
     */
    public static List<Object> joValue(String gsonString, String keyParam) {
        List<Object> joList = null;
        if (gson == null)
            return null;

        JsonObject jsonObject = gson.fromJson(gsonString, JsonObject.class);
        if (jsonObject == null)
            return null;

        joList = new ArrayList<>();
        joList.add(jsonObject);

        if (!jsonObject.has(keyParam)) {
            return joList;
        }

        JsonElement jsonElement = jsonObject.get(keyParam);
        if (jsonElement.isJsonNull()) {
            joList.add("");
            return joList;
        }
        if (jsonElement.isJsonObject()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            joList.add(GsonUtil.string(asJsonObject));
            return joList;
        }
        if (jsonElement.isJsonArray()) {
            JsonArray asJsonArr = jsonElement.getAsJsonArray();
            joList.add(GsonUtil.string(asJsonArr));
            return joList;
        }
        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive asJsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (asJsonPrimitive.isNumber()) {
                String numToStr = getNumToLong(String.valueOf(asJsonPrimitive.getAsNumber())).toString();
                jsonObject.addProperty(keyParam, numToStr);
                joList.add(numToStr);
            } else if (asJsonPrimitive.isBoolean()) {
                joList.add(Boolean.toString(asJsonPrimitive.getAsBoolean()));
            } else if (asJsonPrimitive.isString()) {
                joList.add(asJsonPrimitive.getAsString());
            } /*else {
                throw new AssertionError();
            }*/

            /*

            String primitiveAsString = asJsonPrimitive.toString();
            if (StringUtils.isBlank(primitiveAsString))
                return joList;

            primitiveAsString = primitiveAsString.substring(
                    !primitiveAsString.startsWith("\"") ? 0 : 1,
                    !primitiveAsString.endsWith("\"") ?
                            primitiveAsString.length() :
                            primitiveAsString.length() - 1
            );
            if (StringUtils.isNumeric(primitiveAsString))
                jsonObject.addProperty(keyParam, getNumToLong(primitiveAsString).toString());
            else
                jsonObject.addProperty(keyParam, primitiveAsString);

            joList.add(primitiveAsString);
            +*/
        }
        return joList;
    }


    /**
     * 将json转成特定的cls的对象
     *
     * @return
     */
    public static <T> T toBean(String gsonString, Class<T> cls) {
        List<Object> joList = joValue(gsonString);
        if (joList == null || joList.isEmpty())
            return null;

        JsonObject jsonObject = (JsonObject) joList.get(0);

        return toBean(jsonObject, cls);
    }

    /**
     * 将json转成特定的cls的对象
     *
     * @param jsonObject
     * @param cls
     * @return
     */
    public static <T> T toBean(JsonElement jsonObject, Class<T> cls) {
        T t = null;
        if (gson == null || jsonObject == null)
            return null;

        //传入json对象和对象类型,将json转成对象
        t = gson.fromJson(jsonObject, cls);

        return t;
    }

    /**
     * 将json转成特定的cls的对象
     *
     * @param jsonObject
     * @return
     */
    public static <T> T toBean(JsonElement jsonObject, TypeToken<T> typeToken) {
        T t = null;
        if (gson == null || jsonObject == null || typeToken == null)
            return null;

        Type type = typeToken.getType();
        //传入json对象和对象类型,将json转成对象
        t = gson.fromJson(jsonObject, type);
        return t;
    }

    /**
     * 根据字符串的整形转换Long类型
     *
     * @param str
     * @return
     */
    public static Object getNumToLong(String str) {
        if (!StringUtils.isNumeric(str))
            return 0;

        // 改写数字的处理逻辑，将数字值分为整型与浮点型。
        long lngNum = Long.parseLong(str);
        double dbNum = Double.valueOf(String.valueOf(lngNum));
        // 数字超过long的最大值，返回浮点类型
        if (dbNum > Long.MAX_VALUE) {
            return dbNum;
        }
        // 判断数字是否为整数值
        if (dbNum == lngNum) {
            return lngNum;
        } else {
            return dbNum;
        }
    }

    /**
     * json字符串转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> toListMaps(String gsonString) {
        Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<List<Map<String, T>>>() {
        }.getType(), new MapTypeAdapter()).create();

        return gson.fromJson(gsonString,
                new TypeToken<List<Map<String, T>>>() {
                }.getType());


    }

    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) throws Exception {
        List<T> lst = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            lst.add(toBean(elem, clazz));
        }
        return lst;
    }

    /**
     * json字符串转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> toMaps(String gsonString) {
        Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, T>>() {
        }.getType(), new MapTypeAdapter()).create();

        return gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());


    }

    public static Gson getGson() {
        return gson;
    }

    public static class MapTypeAdapter extends TypeAdapter<Object> {

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

                case STRING:
                    return in.nextString();

                case NUMBER:
                    /**
                     * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                     */
                    double dbNum = in.nextDouble();

                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        return dbNum;
                    }

                    // 判断数字是否为整数值
                    long lngNum = (long) dbNum;
                    if (dbNum == lngNum) {
                        return lngNum;
                    } else {
                        return dbNum;
                    }

                case BOOLEAN:
                    return in.nextBoolean();

                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            // 序列化无需实现
        }

    }
}
