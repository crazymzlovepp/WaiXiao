package com.waiXiao.common.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @param :
 * @author :     mym
 * @version :    V1.0
 * @date :       2019/11/29 10:19
 * @describe :   对象转map
 * @return :
 */
public class BeanToMap implements Serializable {
    public static Map<String,Object> beanToMap(Object obj) throws Exception{
        Map<String,Object> returnMap = new HashMap<>();
        Class<?> parentClazz = obj.getClass().getSuperclass();
        Class<?> clazz = obj.getClass();
        List<Field> fileList = new ArrayList<>();
        fileList.addAll(Arrays.asList(parentClazz.getDeclaredFields()));
        fileList.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field:fileList) {
            field.setAccessible(true);
            String fileName = field.getName();
            Object value = field.get(obj);
            if(value == null){
                value = "";
            }
            returnMap.put(fileName,value);
        }
        return returnMap;
    }
}
