package com.framelibrary.util.gsonconverter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @Author: wangweixu
 * @Date: 2021/04/23 10:38:29
 * @Description: Gson转换工厂
 * @Version: v1.0
 */
public class CustomGsonConverterFactory extends Converter.Factory {

    private final Gson gson;

    private CustomGsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    public static CustomGsonConverterFactory create() {
//        Gson gson = new Gson();
//        gson.factories.add(ObjectTypeAdapter1.FACTORY);

        /*Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeHierarchyAdapter(Object.class, new CustomGsonObjectTypeAdapter()).create();*/

        /*GsonBuilder gsonBulder = new GsonBuilder();
//        gsonBulder.registerTypeAdapter(Object.class, new CustomGsonObjectTypeAdapter());
        gsonBulder.registerTypeAdapterFactory(CustomGsonObjectTypeAdapter.FACTORY);
        Gson gson = gsonBulder.create();*/

        /*//通过反射获取instanceCreators属性
        try {
            Class builder = (Class) gsonBulder.getClass();
            Field f = builder.getDeclaredField("instanceCreators");
            f.setAccessible(true);
            Map<Type, InstanceCreator<?>> val = (Map<Type, InstanceCreator<?>>) f.get(gsonBulder);//得到此属性的值
            //注册数组的处理器
            gsonBulder.registerTypeAdapterFactory(new CustomCollectionTypeAdapterFactory(new ConstructorConstructor(val)));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/


        return create(GsonUtil.getGson());
    }

    public static CustomGsonConverterFactory create(Gson gson) {
        return new CustomGsonConverterFactory(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));

        return new CustomGsonResponseBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomGsonRequestBodyConverter<>(gson, adapter);
    }
}
