package com.framelibrary.util.gsonconverter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gson 自定义类型适配器
 * 自定义adapter，解决由于数据类型实际传过来的值为NUMBER，解析后变成Double,导致解析出错的问题
 *
 * @author wangweixu
 * @Date 2021年03月16日10:31:55
 */
public class CustomGsonObjectTypeAdapter extends TypeAdapter<Object> {

    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() == Object.class) {
                return (TypeAdapter<T>) new CustomGsonObjectTypeAdapter(gson);
            }
            return null;
        }
    };

    private final Gson gson;

    public CustomGsonObjectTypeAdapter() {
        this.gson = null;
    }

    public CustomGsonObjectTypeAdapter(Gson gson) {
        this.gson = gson;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void write(JsonWriter out, Object value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        if (gson == null)
            return;

        TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>) gson.getAdapter(value.getClass());
        if (typeAdapter instanceof CustomGsonObjectTypeAdapter) {
            out.beginObject();
            out.endObject();
            return;
        }

        typeAdapter.write(out, value);
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        JsonToken token = in.peek();

        switch (token) {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList();
                in.beginArray();

                while (in.hasNext()) {
                    list.add(this.read(in));
                }
                in.endArray();
                return list;
            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), this.read(in));
                }
                in.endObject();
                return map;
            case STRING:
                return in.nextString();
            case NUMBER:
                // 改写数字的处理逻辑，将数字值分为整型与浮点型。
                double dbNum = in.nextDouble();
                // 数字超过long的最大值，返回浮点类型
                if (dbNum > Long.MAX_VALUE) {
                    return dbNum;
                }
                // 判断数字是否为整数值
                long lngNum = Double.valueOf(dbNum).longValue();
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

    private void out(Object object) {
        System.out.println(object);
    }
}