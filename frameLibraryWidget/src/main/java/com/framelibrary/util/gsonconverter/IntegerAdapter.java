package com.framelibrary.util.gsonconverter;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 序列化结果：
 * {"code":0,"data":null,"msg":""}
 * <p>
 * 反序列化结果：
 * Result{code=0, msg='success', data=City{id=1, name='Beijing'}}
 *
 * @author wangweixu
 * @Date 2021年03月19日10:42:55
 */
public class IntegerAdapter extends TypeAdapter<Number> {

    @Override
    public void write(JsonWriter out, Number value) throws IOException {
        if (value == null) {
            out.value(0);
            return;
        }
        out.value(value);
    }

    public Number read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.skipValue();
            return 0;
        } else if (reader.peek() == JsonToken.STRING) {
            try {
                return Integer.valueOf(reader.nextString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        }

        try {
            return reader.nextInt();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }
}
