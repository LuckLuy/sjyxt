package com.ly.sjyxt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ly.sjyxt.common.BaseApiService;
import com.ly.sjyxt.common.DataSourceFactory;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.SysDataColumn;
import com.ly.sjyxt.entity.SysDataConnect;
import com.ly.sjyxt.entity.SysDataParm;
import com.ly.sjyxt.entity.SysDataSource;
import com.ly.sjyxt.util.DateUtil;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.oracore.OracleType;
import oracle.sql.CLOB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataUnit;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * api ：接口公共类
 */
@Component
public class ApiUtil extends BaseApiService {

  @Autowired
  private DataSourceFactory dataSourceFactory;

  /**
   * @throws
   * @方法名称: selectUtil
   * @功能描述: 查询公共方法
   * @参数: @param sysDataSoure 数据源对象
   * @参数: @param sysDataConnect 数据链路对象
   * @参数: @param parmList 参数集合
   * @参数: @param columnList 字段集合
   * @参数: @param mapParameter 客户端传递过来的参数信息
   * @返回类型: ResponseBase
   */
  public ResponseBase selectUtil(SysDataSource dataSource, SysDataConnect dataConnect,
                                 List<SysDataParm> dataParmList, List<SysDataColumn> dataColumnList, Map mapParamter) {
    ResponseBase responseBase = null;
    String ds_type = dataSource.getDs_type();//数据源类型
    String sql = dataSource.getDs();//SQL语句

    if ("SQL".equals(ds_type)) {
      responseBase = selectSql(dataSource, dataConnect, dataParmList, dataColumnList, mapParamter);

    } else if ("储存过程".equals(ds_type)) {
      responseBase = selectProcedure(dataSource, dataConnect, dataParmList, dataColumnList, mapParamter);
    }
    return responseBase;
  }

  /**
   * @throws
   * @方法名称: selectSql
   * @功能描述: 执行sql查询
   * @参数: @param sysDataSoure 数据源对象
   * @参数: @param sysDataConnect 数据链路对象
   * @参数: @param parmList 参数集合
   * @参数: @param columnList 字段集合
   * @参数: @param mapParameter 客户端传递过来的参数信息
   * @返回类型: ResponseBase
   */
  private ResponseBase selectSql(SysDataSource dataSource, SysDataConnect dataConnect, List<SysDataParm> dataParmList, List<SysDataColumn> dataColumnList, Map mapParamter) {
    Connection conn = null;  // 链接信息
    PreparedStatement ps = null; //sql 信息
    ResultSet rs = null;// 数据库结果集的数据
    JSONArray array = new JSONArray();
    try {
      DataSource dataSource1 = dataSourceFactory.getById(dataConnect.getDb_link_no());
      conn = dataSource1.getConnection(); // 获得链接
      ps = conn.prepareStatement(dataSource.getDs());
      // 如果字段数据大于0 进行遍历
      if (dataParmList != null && dataParmList.size() > 0) {
        for (SysDataParm parm : dataParmList) {
          String def_value = parm.getDef_value();//默认值
          Integer parm_num = parm.getParm_num();//序号
          String parm_query_type = parm.getParm_query_type(); //查询方式
          String parm_code = parm.getParm_code(); //参数代码
          Object parameterValue = mapParamter.get(parm_code); //获取值

          if (parameterValue == null) {
            if (def_value != null) {
              parameterValue = def_value;
            }
          }
          switch (parm_query_type) {
            case "1":
              parameterValue = "%" + parameterValue + "%";
              break;
            case "2":
              parameterValue = parameterValue + "%";
              break;
            case "3":
              parameterValue = "%" + parameterValue;
              break;
          }

          ps.setObject(parm_num, parameterValue);
        }
      }
      // 执行sql 并返回一个结果集
      rs = ps.executeQuery();
      //返回此<code> ResultSet </ code>对象中的列数。
      ResultSetMetaData rsmd = rs.getMetaData();//得到所有列
      // 获取列队数量
      int count = rsmd.getColumnCount();

      while (rs.next()) {
        JSONObject obj = new JSONObject(true);
        if (dataColumnList != null && dataColumnList.size() > 0) {
          for (SysDataColumn column : dataColumnList) {
            String columnType = column.getColumn_type(); //字段类型
            String columnTitle = column.getColumn_title(); //字段标题
            switch (columnType) {
              case "CLOB":
                obj.put(columnTitle, clobToString((oracle.sql.CLOB) rs.getClob(columnTitle)));
                break;
              case "BLOB":
                obj.put(columnTitle, blobToString((oracle.sql.BLOB) rs.getBlob(columnTitle)));
                break;
              case "DATE":
                obj.put(columnTitle, DateUtil.parseDateToStr(rs.getDate(columnTitle), "yyy-MM-dd HH:mm:ss"));
                break;
              case "TIMESTAMP":
                obj.put(columnTitle, DateUtil.parseTimestampToStr(rs.getTimestamp(columnTitle), "yyy-MM-dd HH:mm:ss"));
                break;
              default:
                obj.put(columnTitle, rs.getObject(columnTitle));
            }
          }
        } else {
          for (int i = 0; i < count; i++) {
            String columnType = rsmd.getColumnTypeName(i); //检索指定列的数据库特定类型名称。
            String columnTitle = rsmd.getColumnLabel(i);//获取指定列的建议标题，以用于打印输出和*显示

            switch (columnType) {
              case "CLOB":
                obj.put(columnTitle, clobToString((oracle.sql.CLOB) rs.getClob(columnTitle)));
                break;
              case "BLOB":
                obj.put(columnTitle, blobToString((oracle.sql.BLOB) rs.getBlob(columnTitle)));
                break;
              case "DATE":
                obj.put(columnTitle, rs.getDate(columnTitle) == null ? "" : DateUtil.parseDateToStr(rs.getDate(columnTitle), "yyyy-MM-dd HH:mm:ss"));
                break;
              case "TIMESTAMP":
                obj.put(columnTitle, rs.getTimestamp(columnTitle) == null ? "" : DateUtil.parseTimestampToStr(rs.getTimestamp(columnTitle), "yyyy-MM-dd HH:mm:ss"));
                break;
              default:
                obj.put(columnTitle, rs.getObject(columnTitle));
            }
          }
        }
        array.add(obj);
      }
    } catch (SQLException | IOException e) {
      return setResultError("查询失败,异常信息:" + e.getMessage());
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (ps != null) {
          ps.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return setResultSuccess(array);
  }

  /**
   * @throws
   * @方法名称: blobToString
   * @功能描述: 将oracle数据库中BLOB字段转为字符串, 不支持图片
   * @参数: @param blob BLOB数据类型字段
   * @返回类型: String
   */
  private Object blobToString(Blob blob) throws SQLException, IOException {
    String reString = "";
    if (blob == null) {
      return reString;
    }
    InputStream inputStream = blob.getBinaryStream();// 建立输出流
    byte[] bytes = new byte[0];
    /*返回可以从此输入流读取（或*跳过）而不会被该输入流的下一次*方法调用阻塞的字节数的估计值。下一个调用*可能是同一线程或另一个线程。
    仅读取或跳过此*个字节不会阻塞，但可能会读取或跳过较少的字节。*/
    bytes = new byte[inputStream.available()];

    inputStream.read(bytes);
    String str = new String(bytes, "gb2312");
    return str;
  }

  /**
   * clob 类型转换String
   * 将oracle数据库中CLOB字段转为字符串
   *
   * @param clob
   * @return
   */
  private Object clobToString(CLOB clob) throws SQLException, IOException {
    String reString = "";
    if (clob == null) {
      return reString;
    }
    Reader rd = clob.getCharacterStream();// 得到流
    //创建使用默认大小*输入缓冲区的缓冲字符输入流
    BufferedReader br = new BufferedReader(rd);
    String s = br.readLine(); //读取一行文本
    //构造一个字符串缓冲区，其中没有字符，初始容量为16个字符。
    StringBuffer sf = new StringBuffer();
    // 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
    while (s != null) {
      sf.append(s);
      s = br.readLine();
    }
    reString = sf.toString();
    return reString;
  }


  /**
   * @throws
   * @方法名称: selectProcedure
   * @功能描述: 执行存储过程查询
   * @参数: @param sysDataSoure 数据源对象
   * @参数: @param sysDataConnect 数据链路对象
   * @参数: @param parmList 参数集合
   * @参数: @param columnList 字段集合
   * @参数: @param mapParameter 客户端传递过来的参数信息
   * @返回类型: ResponseBase
   */
  private ResponseBase selectProcedure(SysDataSource dataSource, SysDataConnect dataConnect, List<SysDataParm> dataParmList, List<SysDataColumn> dataColumnList, Map mapParamter) {

    Connection conn = null;  // 链接信息
    CallableStatement cs = null; // 用于执行SQL存储过程的接口。
    ResultSet rs = null;

    String ds = dataSource.getDs();// 获取存储过程。
    StringBuffer sqlBuffer = new StringBuffer("{call " + ds + "(");
    String sql = "";

    JSONObject obj = new JSONObject(true);

    if (dataParmList != null && dataParmList.size() > 0) {
      for (SysDataParm parm : dataParmList) {
        sqlBuffer.append("?,");
      }
      sql = sqlBuffer.substring(0, sqlBuffer.length() - 1) + ")}";
    } else {
      sql = sqlBuffer.substring(0, sqlBuffer.length() - 1) + ")}";
    }

    DataSource new_dataSource = dataSourceFactory.getById(dataConnect.getDb_link_no());
    try {
      // 根据数据源信息 获取连接信息
      conn = new_dataSource.getConnection();
      // 调用储存过程
      cs = conn.prepareCall(sql);
      if (dataParmList != null && dataParmList.size() > 0) {
        for (SysDataParm parm : dataParmList) {
          String parm_type = parm.getParm_type(); //参数类型 IN/OUT
          //参数值类型,String,Integer,Long,Double,Float,Data,DataTime,结果集(OUT)
          String parm_data_type = parm.getParm_data_type();
          String parm_code = parm.getParm_code();//参数代码
          Integer parm_num = parm.getParm_num(); //序号

          if ("IN".equals(parm_type)) {
            cs.setObject(parm_num, mapParamter.get(parm_code));
          } else if ("OUT".equals(parm_type)) {
            switch (parm_data_type) {
              case "结果集(OUT)":
                cs.registerOutParameter(parm_num, OracleTypes.CURSOR);
                break;
              case "String":
                cs.registerOutParameter(parm_num, OracleTypes.VARCHAR);
                break;
              case "Integer":
                cs.registerOutParameter(parm_num, OracleTypes.INTEGER);
                break;
              case "Long":
                cs.registerOutParameter(parm_num, OracleTypes.NUMBER);
                break;
              case "Double":
                cs.registerOutParameter(parm_num, OracleTypes.DOUBLE);
                break;
              case "Float":
                cs.registerOutParameter(parm_num, OracleTypes.FLOAT);
                break;
              case "Data":
                cs.registerOutParameter(parm_num, OracleTypes.DATE);
                break;
              case "DataTime":
                cs.registerOutParameter(parm_num, OracleTypes.TIMESTAMP);
                break;
            }
          }
        }
      }
      // 执行储存
      cs.execute();
      if (dataParmList != null && dataParmList.size() > 0) {
        for (SysDataParm parm : dataParmList) {
          String parm_type = parm.getParm_type(); //参数类型 IN/OUT
          String parm_data_type = parm.getParm_data_type();//参数值类型,String,Integer,Long,Double,Float,Data,DataTime,结果集(OUT)
          String parm_code = parm.getParm_code(); //参数代码
          Integer parm_num = parm.getParm_num(); //序号
          if ("OUT".equals(parm_type)) {
            switch (parm_data_type) {
              case "结果集(OUT)":
                JSONArray array = new JSONArray();
                rs = (ResultSet) cs.getObject(parm_code);
                // ResultSetMetaData  可用于获取有关<code> ResultSet </ code>对象中列的类型*和属性的信息的对象。
                // rs.getMetaData(): 检索此<code> ResultSet </ code> 对象的列的数量，类型和属性。
                ResultSetMetaData rsmd = rs.getMetaData();

                int count = rsmd.getColumnCount();
                while (rs.next()) {
                  JSONObject objColumn = new JSONObject(true);
                  if (dataColumnList != null && dataColumnList.size() > 0) {
                    for (SysDataColumn column : dataColumnList) {
                      String columnType = column.getColumn_type();
                      ;//字段类型
                      String columnTile = column.getColumn_title();//字段标题
                      switch (columnType) {
                        case "CLOB":
                          objColumn.put(columnTile, clobToString((oracle.sql.CLOB) rs.getClob(columnTile)));
                          break;
                        case "BLOB":
                          objColumn.put(columnTile, blobToString((oracle.sql.BLOB) rs.getBlob(columnTile)));
                          break;
                        case "DATE":
                          objColumn.put(columnTile, DateUtil.parseDateToStr(rs.getDate(columnTile), "yyyy-MM-dd HH:mm:ss"));
                          break;
                        case "TIMESTAMP":
                          objColumn.put(columnTile, DateUtil.parseTimestampToStr(rs.getTimestamp(columnTile), "yyyy-MM-dd HH:mm:ss"));
                          break;

                        default:
                          objColumn.put(columnTile, rs.getObject(columnTile));
                      }
                    }
                  } else {

                    for (int i = 1; i <= count; i++) {
                      //检索指定列的数据库特定类型名称。
                      String columnType = rsmd.getColumnTypeName(i);
                      //获取指定列的建议标题
                      String columnTitle = rsmd.getColumnLabel(i);

                      switch (columnType) {
                        case "CLOB":
                          objColumn.put(columnTitle, clobToString((oracle.sql.CLOB) rs.getClob(columnTitle)));
                          break;
                        case "BLOB":
                          objColumn.put(columnTitle, blobToString((oracle.sql.BLOB) rs.getBlob(columnTitle)));
                          break;
                        case "DATE":
                          objColumn.put(columnTitle, DateUtil.parseDateToStr(rs.getDate(columnTitle), "yyyy-MM-dd HH:mm:ss"));
                          break;
                        case "TIMESTAMP":
                          objColumn.put(columnTitle, DateUtil.parseTimestampToStr(rs.getTimestamp(columnTitle), "yyyy-MM-dd HH:mm:ss"));
                          break;
                        default:
                          objColumn.put(columnTitle, rs.getObject(columnTitle));
                      }
                    }
                  }
                  array.add(objColumn);
                }
                obj.put(parm_code, array);
                break;
              case "String":
                obj.put(parm_code, cs.getString(parm_code) == null ? "" : cs.getString(parm_code));
                break;
              case "Integer":
                obj.put(parm_code, cs.getInt(parm_code));
                break;
              case "Long":
                obj.put(parm_code, cs.getInt(parm_code));
                break;
              case "Double":
                obj.put(parm_code, cs.getDouble(parm_code));
                break;
              case "Float":
                obj.put(parm_code, cs.getFloat(parm_code));
                break;
              case "Data":
                obj.put(parm_code, DateUtil.parseDateToStr(cs.getDate(parm_code), "yyyy-MM-dd HH:mm:ss"));
                break;
              case "DataTime":
                obj.put(parm_code, DateUtil.parseTimestampToStr(cs.getTimestamp(parm_code), "yyyy-MM-dd HH:mm:ss"));
                break;
            }
          }
        }
      }
    } catch (SQLException | IOException e) {
      e.printStackTrace();
      return setResultError("查询失败,异常信息:" + e.getMessage());
    } finally {
      try {
        if (rs != null) rs.close();
        if (cs != null) cs.close();
        if (conn != null) conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return setResultSuccess(obj);
  }


  /**
   * @throws
   * @方法名称: insertOrUpdateOrDeleteUtil
   * @功能描述: 新增或者修改或者删除数据 可批量操作
   * @参数: @param sysDataSoure 数据源对象
   * @参数: @param sysDataConnect 数据链路对象
   * @参数: @param parmList 参数集合
   * @参数: @param columnList 字段集合
   * @参数: @param mapParameter 客户端传递过来的参数信息
   * @返回类型: ResponseBase
   */
  public ResponseBase insertOrUpdateOrDeleteUtil(SysDataSource sysDataSoure, SysDataConnect sysDataConnect, List<SysDataParm> parmList, List<SysDataColumn> columnList, Map mapParameter) {

    ResponseBase responseBase = null;
    String ds_type = sysDataSoure.getDs_type();//数据源类型：SQL/存储过程
    String ds = sysDataSoure.getDs();//sql语句
    if ("SQL".equals(ds_type)) {
      responseBase = insertOrUpdateOrDeleteSql(sysDataSoure, sysDataConnect, parmList, columnList, mapParameter);
    } else if ("存储过程".equals(ds_type)) {
      //如果 数据源类型是存储过程,客户端在调用时,直接调用的新增或者修改接口,此处直接调用selectProcedure
      responseBase = selectProcedure(sysDataSoure, sysDataConnect, parmList, columnList, mapParameter);
    }

    return responseBase;
  }

  private ResponseBase insertOrUpdateOrDeleteSql(SysDataSource sysDataSoure, SysDataConnect sysDataConnect, List<SysDataParm> parmList, List<SysDataColumn> columnList, Map mapParameter) {
    String sql = sysDataSoure.getDs().trim().toUpperCase();
    List listParmeter = (List) mapParameter.get("parameter");//获取客户端传递参数信息
    boolean flag = false; //记录执行成功或失败
    int length = 0; //记录执行成功行数
    // startsWith: 测试此字符串是否以指定的前缀开头
    if (sql.startsWith("INSERT") || sql.startsWith("UPDATE") || sql.startsWith("DELETE")) {
      Connection conn = null;
      PreparedStatement ps = null;
      DataSource dataSource = dataSourceFactory.getById(sysDataConnect.getDb_link_no());
      try {
        // getConnection  尝试与此{@code DataSource}对象表示的数据源建立连接。
        conn = dataSource.getConnection();
        //将此连接的自动提交模式设置为给定状态。
        conn.setAutoCommit(false);
        //创建一个 PreparedStatement对象，以将参数化的SQL语句发送到数据库。
        ps = conn.prepareStatement(sysDataSoure.getDs());
        if (listParmeter != null && listParmeter.size() > 0) {
          for (int j = 0; j < listParmeter.size(); j++) {
            Map map = (Map) listParmeter.get(j); //返回此列表中指定位置的元素。
            for (int i = 0; i < parmList.size(); i++) {
              SysDataParm parm = parmList.get(i);
              Integer parm_num = parm.getParm_num(); //序号
              String def_value = parm.getDef_value(); //默认值
              String parm_code = parm.getParm_code(); //参数代码
              ps.setObject(parm_num, map.get(parm_code) == null || "".equals(map.get(parm_code)) ? def_value : map.get(parm_code));
            }
            //向此<code> PreparedStatement </ code> *对象的命令批次中添加一组参数。
            ps.addBatch();
            if (j % 100 == 0) {
              //executeBatch  将一批命令提交给数据库以执行
              length = length + ps.executeBatch().length;
            }
          }
        }

        length = length + ps.executeBatch().length;
        conn.commit();
        flag = true;

      } catch (SQLException e) {
        e.printStackTrace();
        try {
          conn.rollback();
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      } finally {
        try {
          conn.setAutoCommit(true);
          if (ps != null) ps.close();
          if (conn != null) conn.close();

        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    } else {
      return setResultError("请检查数据源ID为:" + sysDataSoure.getDs_id() + ",的数据源,不支持新增或者修改");
    }
    if (flag) {
      return setResultSuccess("执行成功,成功执行" + length + "行数据");
    } else {
      return setResultError("执行失败,事物已经回滚");
    }
  }


}

