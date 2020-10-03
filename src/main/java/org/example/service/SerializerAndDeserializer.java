package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

public abstract class SerializerAndDeserializer<T> {
    public byte[] serialize(Object object) {
        String json = objectToString(object);
        try {
            return new ObjectMapper().writeValueAsString(object).getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("serialize does not work");
    }

    private String objectToString(Object object) {
        return object.getClass().getSimpleName() +
                "(" +
                getFieldWithValue(object) +
                ")";
    }

    private String getFieldWithValue(Object object) {
        StringBuilder stringBuilder = new StringBuilder();
        if (Collection.class.isAssignableFrom(object.getClass()) || object.getClass().isArray()) {
            stringBuilder.append("[");
            ((Collection) object).forEach(element -> stringBuilder.append(getFieldWithValue(element)));
            stringBuilder.append("]");
        } else if (Map.class.isAssignableFrom(object.getClass())) {
            stringBuilder.append("{");
            ((Map) object).entrySet().forEach(key -> stringBuilder.append(
                    getFieldWithValue(key) + ":" + getFieldWithValue(((Map) object).get(key))
            ));
            stringBuilder.append("}");
        } else if (String.class.isAssignableFrom(object.getClass())) {
            stringBuilder.append("\"").append(object).append("\"");
        } else if (
                Byte.class.isAssignableFrom(object.getClass()) ||
                        Short.class.isAssignableFrom(object.getClass()) ||
                        Integer.class.isAssignableFrom(object.getClass()) ||
                        Long.class.isAssignableFrom(object.getClass()) ||
                        Double.class.isAssignableFrom(object.getClass()) ||
                        Float.class.isAssignableFrom(object.getClass())
        ) {
            stringBuilder.append(object);
        } else {
            Field[] fields = object.getClass().getDeclaredFields();
            String prefix = "";
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(object);
                    stringBuilder.append(prefix);
                    prefix = ",";
                    stringBuilder.append(field.getName()).append(":").append(getFieldWithValue(value));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }


//    public byte[] deserialize(byte[] arr) {
//        // used Class Loader
//    }

}
