//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jzkj.gyxg.util;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class JsonUtils {
    public JsonUtils() {
    }

    public static Map deserializeJson(String json) {
        JSONDeserializer deserializer = new JSONDeserializer();
        return json != null && json.startsWith("{")?(Map)deserializer.deserialize(json):Collections.EMPTY_MAP;
    }

    public static List deserializeJson2List(String json) {
        JSONDeserializer deserializer = new JSONDeserializer();
        return json != null && json.startsWith("[")?(List)deserializer.deserialize(json):Collections.emptyList();
    }

    public static Object deserializeJson2Obj(String json, Class classez) {
        JSONDeserializer deserializer = new JSONDeserializer();
        deserializer.use((String)null, classez);
        return deserializer.deserialize(json);
    }

    public static Object deserializeJson2ListObj(String json, Class classez) {
        JSONDeserializer deserializer = new JSONDeserializer();
        deserializer.use("values", classez);
        return deserializer.deserialize(json);
    }

    public static String serialize(Object obj) {
        return serialize(obj, (String)null, (String)null);
    }

    public static String serialize(Object obj, String includes, String excludes) {
        JSONSerializer serializer = new JSONSerializer();
        if(obj == null) {
            return "";
        } else {
            StringTokenizer tokenizer = null;
            String item;
            if(includes != null) {
                tokenizer = new StringTokenizer(includes, ",");

                while(tokenizer.hasMoreTokens()) {
                    item = tokenizer.nextToken();
                    if(item != null && item.length() > 0) {
                        serializer.include(new String[]{item});
                    }
                }
            }

            if(excludes != null) {
                tokenizer = new StringTokenizer(excludes, ",");

                while(tokenizer.hasMoreTokens()) {
                    item = tokenizer.nextToken();
                    if(item != null && item.length() > 0) {
                        serializer.exclude(new String[]{item});
                    }
                }
            }

            return serializer.deepSerialize(obj);
        }
    }

    public static String shallowserialize(Object obj, String includes, String excludes) {
        JSONSerializer serializer = new JSONSerializer();
        if(obj == null) {
            return "";
        } else {
            StringTokenizer tokenizer = null;
            String item;
            if(includes != null) {
                tokenizer = new StringTokenizer(includes, ",");

                while(tokenizer.hasMoreTokens()) {
                    item = tokenizer.nextToken();
                    if(item != null && item.length() > 0) {
                        serializer.include(new String[]{item});
                    }
                }
            }

            if(excludes != null) {
                tokenizer = new StringTokenizer(excludes, ",");

                while(tokenizer.hasMoreTokens()) {
                    item = tokenizer.nextToken();
                    if(item != null && item.length() > 0) {
                        serializer.exclude(new String[]{item});
                    }
                }
            }

            return serializer.serialize(obj);
        }
    }

    public static void main(String[] args) {
        String[] a = new String[]{"a", "b", "c"};
        String s = serialize(a);
        System.out.println(s);
        System.out.println(deserializeJson2List(s));
    }
}
