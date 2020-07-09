package com.ly.sjyxt.common;

import com.alibaba.druid.pool.DruidDataSource;
import com.ly.sjyxt.entity.SysDataConnect;
import com.ly.sjyxt.entity.SysDataSource;
import com.ly.sjyxt.mapper.SysDataConnectMapper;
import com.petrochina.sso.passwordencodeclient.PasswordEncodeDecode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * JDBC 动态加载数据源
 * @component （把普通pojo实例化到spring容器中，相当于配置文件中的
 *
 */
@Component
public class DataSourceFactory {

    //volatile 是一个类型修饰符。volatile 的作用是作为指令关键字，确保本条指令不会因编译器的优化而省略
    //
    //volatile 的特性
    //
    //保证了不同线程对这个变量进行操作时的可见性，即一个线程修改了某个变量的值，这新值对其他线程来说是立即可见的。（实现可见性）
    //禁止进行指令重排序。（实现有序性）
    //volatile 只能保证对单次读/写的原子性。i++ 这种操作不能保证原子性。
    private  volatile Map<String, DataSource> dataSourceMap=new HashMap<String, DataSource>();

    @Resource
    private SysDataConnectMapper dataConnectMapper;

  /*  @Value(value = "${db_secretKey}")
    private String db_secretKey;*/


    /**
     * 初始化jdbc 链接api
     * 被@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次
     */
    @PostConstruct
    public void init (){

        List<SysDataConnect> connectList = dataConnectMapper.getDatabase("");
        if(connectList != null && connectList.size()>0 ){
            for(SysDataConnect dataConnect : connectList){
                DataSource dataSource = getDataSource(dataConnect);

                dataSourceMap.put(dataConnect.getDb_link_no(),dataSource);
            }
        }
    }

    /**
     * 数据源api链接
     *JDBC2.0 提供了javax.sql.DataSource接口，它负责建立与数据库的连接，当在应用程序中访问数据库时
     * 不必编写连接数据库的代码，直接引用DataSource获取数据库的连接对象即可。用于获取操作数据Connection对象。
     * @param
     * @return
     */
    public DruidDataSource getDataSource (SysDataConnect DataConnect){
        String driver = "";
        String url = "";
        if (DataConnect.getDb_link_type().equalsIgnoreCase("ORACLE")) {
            driver = JDBCUtil.ORACLE_DRIVER_CLASS_NAME;
            String isSid = DataConnect.getIssid();
            if (null != isSid && "Y".equals(isSid)) {// 使用SID作为连接参数
                url = "jdbc:oracle:thin:@" + DataConnect.getDb_ip() + ":"
                        + DataConnect.getServer_port() + ":" + DataConnect.getDb_name();
            } else {
                url = "jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = "
                        + DataConnect.getDb_ip()
                        + ")(PORT = "
                        + DataConnect.getServer_port()
                        + ")))(CONNECT_DATA =(SERVICE_NAME = "
                        + DataConnect.getDb_name() + ")))";
            }
        } else if (DataConnect.getDb_link_type().toUpperCase().equals("MYSQL")) {
            driver = JDBCUtil.MYSQL_DRIVER_CLASS_NAME;
            url = "jdbc:mysql://" + DataConnect.getDb_ip() + ":"
                    + DataConnect.getServer_port() + "/" + DataConnect.getDb_name();
        } else if (DataConnect.getDb_link_type().toUpperCase().equals("SQLSERVER")) {
            driver = JDBCUtil.SQLSERVER_DRIVER_CLASS_NAME;
            url = "jdbc:microsoft:sqlserver://" + DataConnect.getDb_ip()
                    + ":" + DataConnect.getServer_port() + ";DatabaseName=" + DataConnect.getDb_name();
        }
        PasswordEncodeDecode p = new PasswordEncodeDecode();
        String db_pw_decode = null;
       /* try {
            db_pw_decode = p.passwordDecode(db_secretKey, DataConnect.getDb_pw());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(DataConnect.getDb_user());
        datasource.setPassword(DataConnect.getDb_pw());
        datasource.setDriverClassName(driver);
        try {
            datasource.setFilters("stat,wall,slf4j");
            datasource.setInitialSize(5);
            datasource.setMaxActive(5);
            datasource.setMaxWait(60000);
            datasource.setTimeBetweenEvictionRunsMillis(60000);
            datasource.setMinEvictableIdleTimeMillis(300000);
            datasource.setValidationQuery("SELECT 1 FROM DUAL");
            datasource.setTestWhileIdle(true);
            datasource.setTestWhileIdle(true);
            datasource.setTestOnBorrow(false);
            datasource.setPoolPreparedStatements(true);
            datasource.setMaxPoolPreparedStatementPerConnectionSize(20);
            datasource.setFilters("stat,wall,slf4j");
            datasource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");
            datasource.setConnectionErrorRetryAttempts(0);
            datasource.setBreakAfterAcquireFailure(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource ;
    }

    /**
     *   获取JDBC 链接
     * @param id
     * @return
     */
    public DataSource getById(String id){
        return  dataSourceMap.get(id);
    }

    /**
     * 添加数据源管理
     * @param dataConnect
     */
    public void addDataSource (SysDataConnect dataConnect){
        DruidDataSource dataSource = getDataSource(dataConnect);
        dataSourceMap.put(dataConnect.getDb_link_no(),dataSource);
    }


    public  void removeById(String id){
        dataSourceMap.remove(id);
    }
}
