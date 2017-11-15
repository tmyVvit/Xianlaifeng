package com.xianlaifeng.utils;

import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtils {



    public static String covertToUrlList(String files,String url){
        String[] file =  files.split(",");
        List<String> list = new ArrayList<String>();
        for(String f:file){
            list.add(url+f);
        }
        return list.toString().replace("[","").replace("]","");
    }


    public static String add(Object object) {

        String className = object.getClass().getSimpleName();
        StringBuilder sql = new StringBuilder("insert into ");
        sql.append(className.toLowerCase()+" ");
        sql.append("set ");
        String str= getClassValueObj(object).toString().replace("{","").replace("}","");
        sql.append(str);
        return sql.toString();


    }


    public static String delete(Object object) {

        String className = object.getClass().getSimpleName();
        StringBuilder sql = new StringBuilder("delete from ");
        sql.append(className+" ");
        sql.append("where id=");
        String str= (String)getClassValueObj(object).get("id");
        sql.append(str);
        return sql.toString();


    }



    public static Map<String, Object> getClassValueObj(Object object) {


        Field[] fields = object.getClass().getDeclaredFields();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        for (int i = 0; i < fields.length; i++) {
            try {
                //获得目标类的接入权限
                boolean isAccess = fields[i].isAccessible();
                if (!isAccess) fields[i].setAccessible(true);
                //System.out.println(fields[i].get(object).getClass());
                //如果属性类型是字符串 加上' '
                if(fields[i].getGenericType().toString().equals("class java.lang.String")){
                    paraMap.put(fields[i].getName(), fields[i].get(object)==null?null:"'"+fields[i].get(object)+"'");

                }
                else if(fields[i].getGenericType().toString().equals(
                        "class java.util.Date")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                    paraMap.put(fields[i].getName(), "'"+sdf.format(new Date().getTime())+"'");
                }else if(fields[i].getName().equals("id")){

                }
                else {
                    paraMap.put(fields[i].getName(), fields[i].get(object));
                }


                if (!isAccess) fields[i].setAccessible(false);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //System.out.println(paraMap);
        return paraMap;
    }


    public static Map<String, Object> objectToMap(Object obj){
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> clazz = obj.getClass();
        try {
            System.out.println(clazz);
            for(Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                map.put(fieldName, value);
            }

        }catch(IllegalAccessException e){
            return null;
        }
        return map;
     }
}