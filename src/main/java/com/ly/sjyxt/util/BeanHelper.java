package com.ly.sjyxt.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 使bean中为null的属性转换成空字符串
 */
public class BeanHelper {

  public static <T> void nullToEmpty(T bean) {
    Field[] field = bean.getClass().getDeclaredFields();
    for (int j = 0; j < field.length; j++) {
      String name = field[j].getName();
      //将属性的首字符大写，方便构造get，set方法
      name = name.substring(0, 1).toUpperCase() + name.substring(1);
      //获取属性的类型
      String type = field[j].getGenericType().toString();
      //如果type是类类型，则前面包含"class "，后面跟类名
      if (type.equals("class java.lang.String")) {
        try {
          Method mGet = bean.getClass().getMethod("get" + name);
          //调用getter方法获取属性值
          String value = (String) mGet.invoke(bean);
          if (value == null || "".equals(value)) {
            Method mSet = bean.getClass().getMethod("set" + name, new Class[]{String.class});
            mSet.invoke(bean, new Object[]{new String("")});
          }
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
  }


}
