package com.ly.sjyxt.util;


import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @类名: KeyGenerator
 * @功能描述: 主键生成器, 生成规则为：格式化时间串yyyyMMddHHmmss+四位随机数
 * @作者: Administrator
 * @日期:2019年2月27日 下午2:43:21
 */
public final class KeyGenerator {
    /*随机器*/
    private final static Random RANDOM = new Random();
    /*随机数最大值*/
    private final static int RAND_MAX = 999999;
    /*日期格式化*/
    private final static DecimalFormat format = new DecimalFormat("000000");
    /*日期格式化*/
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 功能：生成唯一的键值，时间值yyyyMMddHHmmssS+一位随机字符
     * 参数：
     * 返回：String - 键值
     * 异常：
     */
    public static String getKey(){
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.substring(0, 8) + uuid.substring(9, 13)
                + uuid.substring(14, 18) + uuid.substring(19, 23)
                + uuid.substring(24);
        return uuid;
    }

    private static StringBuffer m_strbKeyChars = new StringBuffer(
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");

    private static int count = Integer.MAX_VALUE;

    public static String getKey(int nLength) {
        if (nLength <= 0) {
            throw new IllegalArgumentException(
                    "Key Length is invalid. generateKey(nLength): " + nLength);
        }

        try {
            InetAddress inet = InetAddress.getLocalHost();
            int ip = toInt(inet.getAddress());
            if (ip < 0)
                ip *= -1;
            long seed = ip * count + System.currentTimeMillis();
            count--;
            if (count == 0)
                count = Integer.MAX_VALUE;

            StringBuffer strbKey = new StringBuffer();
            Random m_generate = new Random();
            Date dateNow = new Date(seed);
            m_generate.setSeed(dateNow.getTime());

            for (int i = 0; i < nLength; i++) {
                int nValue = m_generate.nextInt();

                if (nValue < 0) {
                    nValue *= -1;
                }

                nValue %= 61;

                strbKey.append(m_strbKeyChars.charAt(nValue));
            }

            return strbKey.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int toInt(byte[] bytes) {
        int result = 0;

        for (int i = 0; i < 4; i++) {
            int intval = (int) bytes[i];
            result = (result << 8) + (intval < 0 ? intval + 256 : intval);
        }

        return result;
    }


}

