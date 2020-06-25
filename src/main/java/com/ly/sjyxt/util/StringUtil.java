package com.ly.sjyxt.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类
 * @author wf
 */
public class StringUtil {
    /**
     * 判断字符串是否为null、“ ”、“null”
     * @param obj
     * @return
     */
    public static boolean isNull(String obj) {
        if (obj == null){
            return true;
        }else if (obj.toString().trim().equals("")){
            return true;
        }else if(obj.toString().trim().toLowerCase().equals("null")){
            return true;
        }

        return false;
    }


    /**
     * 正则验证是否是数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[+-]?[0-9]+[0-9]*(\\.[0-9]+)?");
        Matcher match = pattern.matcher(str);

        return match.matches();
    }
    /**
     * 将一个长整数转换位字节数组(8个字节)，b[0]存储高位字符，大端
     *
     * @param l
     *            长整数
     * @return 代表长整数的字节数组
     */
    public static byte[] longToBytes(long l) {
        byte[] b = new byte[8];
        b[0] = (byte) (l >>> 56);
        b[1] = (byte) (l >>> 48);
        b[2] = (byte) (l >>> 40);
        b[3] = (byte) (l >>> 32);
        b[4] = (byte) (l >>> 24);
        b[5] = (byte) (l >>> 16);
        b[6] = (byte) (l >>> 8);
        b[7] = (byte) (l);
        return b;
    }
    /**
     * @param <T>
     *
     * @方法名称: listToJsonArrayString
     * @功能描述: 将list集合转为JsonArray字符串
     * @参数: @param list
     * @参数: @return
     * @返回类型: String
     * @作者: Administrator
     * @时间: 2016年5月29日
     * @throws
     */

    public  static <T>JSONArray listTOJSONArrayString(List<T> list){
        JSONArray array = new JSONArray();
        if(array != null && list.size() >0){
            for(T t:list){
                array.add(t.toString());
            }
        }
        return array;
    }


    /**
     *
     * @方法名称: jsonToBean
     * @功能描述: json字符串转JavaBean
     * @参数: @param jsonString
     * @参数: @param beanClass
     * @参数: @return
     * @返回类型: T
     * @作者: Administrator
     * @时间: 2016年5月30日
     * @throws
     */
    public static  <T> T jsonToBean(String jsonString,Class<T> beanClass){
        if(jsonString != null && !"".equals(jsonString)){
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            T bean =(T) JSONObject.parseObject(jsonString,beanClass);
            return  bean;
        }else{
            return  null;
        }

    }


    /**
     * 去除字符串中的空格、回车、换行符、制表符等
     * @param str
     * @return
     */
    public static  String replaceSpecialStr(String str){
        String repl ="";
        if(str != null){
            //正则 对象
            Pattern p= Pattern.compile("\t|\r|\n");
            // 匹配的内容
            Matcher m=p.matcher(str);
            // 把匹配的内容正则  全部替换成 ”“
            repl =m.replaceAll("");
        }
        return repl;
    }



















}
