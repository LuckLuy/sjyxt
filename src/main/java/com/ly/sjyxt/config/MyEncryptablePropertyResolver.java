package com.ly.sjyxt.config;

import com.petrochina.sso.passwordencodeclient.PasswordEncodeDecode;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import org.springframework.beans.factory.annotation.Value;

public class MyEncryptablePropertyResolver implements EncryptablePropertyResolver {

    @Value(value = "${db_secretKey}")
    private String db_secretKey;

    //自定义解密方法
    @Override
    public String resolvePropertyValue(String s) {
        PasswordEncodeDecode p=new PasswordEncodeDecode();
        if (null != s && s.startsWith(MyEncryptablePropertyDetector.ENCODED_PASSWORD_HINT)) {
            //此处进行解密处理
            try {
                return p.passwordDecode(db_secretKey, s.substring(MyEncryptablePropertyDetector.ENCODED_PASSWORD_HINT.length()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    public static void main(String[] args) {
        PasswordEncodeDecode p=new PasswordEncodeDecode();
        try {

            System.out.println("QX");
            System.out.println("'@dbpsw@"+p.passwordEncode("83abe08f8b1040b2a75b6faf29384cfd", "datasource")+"'");



            //System.out.println(p.passwordDecode("83abe08f8b1040b2a75b6faf29384cfd", "0ec3901f1a18973e940b45a01125ef17"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

