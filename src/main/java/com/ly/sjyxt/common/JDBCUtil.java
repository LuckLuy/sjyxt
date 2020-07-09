package com.ly.sjyxt.common;

import com.ly.sjyxt.entity.SysDataConnect;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接管理
 * 加载驱动等
 */
@Slf4j
public class JDBCUtil {

    /**
     * oracle 驱动
     */
    public  static final  String  ORACLE_DRIVER_CLASS_NAME ="oracle.jdbc.OracleDriver";

    /**
     * mysql 驱动
     */
    public static  final  String MYSQL_DRIVER_CLASS_NAME ="com.mysql.jdbc.Driver";

    /**
     * sqlserver 驱动
     */
    public static  final  String SQLSERVER_DRIVER_CLASS_NAME ="com.microsoft.sqlserver.jdbc.SQLServerDriver";


    /**
     * Connection 对象的数据库能够提供描述其表、所支持的 SQL 语法、存储过程、此连接功能等等的信息。此信息是使用 getMetaData 方法获得的。
     */
    public static Connection getConnection(SysDataConnect dbLink) {
        String driver = "";
        String url = "";
        Connection conn = null;
        try {
            if (dbLink.getDb_link_type().equalsIgnoreCase("ORACLE")) {
                driver = ORACLE_DRIVER_CLASS_NAME;
                String isSid = dbLink.getIssid();
                if (null != isSid && "Y".equals(isSid)) {// 使用SID作为连接参数
                    url = "jdbc:oracle:thin:@" + dbLink.getDb_ip() + ":"
                            + dbLink.getServer_port() + ":" + dbLink.getDb_name();
                } else {
                    url = "jdbc:oracle:thin:@(" +
                            "DESCRIPTION =(ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = "
                            + dbLink.getDb_ip()
                            + ")(PORT = "
                            + dbLink.getServer_port()
                            + ")))(CONNECT_DATA =(SERVICE_NAME = "
                            + dbLink.getDb_name() + ")))";
                }
            } else if (dbLink.getDb_link_type().toUpperCase().equals("MYSQL")) {
                driver = MYSQL_DRIVER_CLASS_NAME;
                url = "jdbc:mysql://" + dbLink.getDb_ip() + ":"
                        + dbLink.getServer_port() + "/" + dbLink.getDb_name();
            } else if (dbLink.getDb_link_type().toUpperCase().equals("SQLSERVER")) {
                driver = SQLSERVER_DRIVER_CLASS_NAME;
                url = "jdbc:microsoft:sqlserver://" + dbLink.getDb_ip()
                        + ":" + dbLink.getServer_port() + ";DatabaseName=" + dbLink.getDb_name();
            }
            Class.forName(driver).newInstance();
            String user = dbLink.getDb_user();
            String password = dbLink.getDb_pw();
            DriverManager.setLoginTimeout(3);//获取数据库连接超时设置
            conn = DriverManager.getConnection(url, user, password);
        } catch (InstantiationException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return conn;
    }














}
