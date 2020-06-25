package com.ly.sjyxt.config;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;

/**
 *
 * @ClassName: MyEncryptablePropertyDetector
 * @Description:  自定义过滤配置文件所有属性信息
 * @author: zlm
 * @date:2019年7月10日 下午2:43:21
 *
 */
public class MyEncryptablePropertyDetector implements EncryptablePropertyDetector {

    //数据库密码前缀,用于区分是否加密
    public static final String ENCODED_PASSWORD_HINT = "@dbpsw@";

    // 如果属性的字符开头为"@dbpsw@"，返回true，表明该属性是加密过的
    @Override
    public boolean isEncrypted(String s) {
        if (null != s) {
            return s.startsWith(ENCODED_PASSWORD_HINT);
        }
        return false;
    }
    // 该方法告诉工具，如何将自定义前缀去除
    @Override
    public String unwrapEncryptedValue(String s) {
        return s.substring(ENCODED_PASSWORD_HINT.length());
    }

}
