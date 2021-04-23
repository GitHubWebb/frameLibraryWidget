package com.framelibrary.util.gsonconverter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 处理解析String 格式 null转换为""
 * 序列化结果：
 * {"code":null,"data":null,"msg":""}
 * <p>
 * 反序列化结果：
 * Result{code=200, msg='success', data=City{id=1, name=''}}
 *
 * @author wangweixu
 * @Date 2021年03月19日10:40:49
 */
public class StringAdapter extends TypeAdapter<String> {

    @Override
    public void write(JsonWriter out, String value) throws IOException {
        if (value == null) {
            out.value("");
            return;
        }
        out.value(value);
    }

    public String read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return "";
        }
        return reader.nextString();
    }
}
