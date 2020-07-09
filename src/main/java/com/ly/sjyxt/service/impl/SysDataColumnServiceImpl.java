package com.ly.sjyxt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ly.sjyxt.common.BaseApiService;
import com.ly.sjyxt.common.JDBCUtil;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.SysDataColumn;
import com.ly.sjyxt.entity.SysDataConnect;
import com.ly.sjyxt.entity.SysDataParm;
import com.ly.sjyxt.entity.SysDataSource;
import com.ly.sjyxt.mapper.SysDataColumnMapper;
import com.ly.sjyxt.mapper.SysDataConnectMapper;
import com.ly.sjyxt.mapper.SysDataParmMapper;
import com.ly.sjyxt.mapper.SysDataSourceMapper;
import com.ly.sjyxt.service.SysDataColumnService;
import com.ly.sjyxt.util.BeanHelper;
import com.ly.sjyxt.util.KeyGenerator;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 数据源字段表 服务实现类
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Service
public class SysDataColumnServiceImpl extends BaseApiService implements SysDataColumnService {

  @Autowired
  private SysDataColumnMapper dataColumnMapper;

  @Autowired
  private SysDataSourceMapper dataSourceMapper;

  @Autowired
  private SysDataConnectMapper dataConnectMapper;

  @Autowired
  private SysDataParmMapper dataParmMapper;

  @Override
  public JSONObject list(String parameter, String ds_id) {
    // 根据 id 或paramter 查询出列表数据
    List<SysDataColumn> dataColumnList = dataColumnMapper.querySysDataColumn(parameter.toUpperCase(),
        ds_id != null || "".equals(ds_id) ? null :ds_id);

    JSONObject obj = new JSONObject();
    //  将查询出来的数据遍历添加到新的 集合里
    List<SysDataColumn> listParameter = new ArrayList<SysDataColumn>();
    for (SysDataColumn dataColumn : dataColumnList){
      BeanHelper.nullToEmpty(dataColumn);
      listParameter.add(dataColumn);
    }
    List<SysDataColumn> listAll = dataColumnMapper.querySysDataColumn("",ds_id);
    // 转换jSON 数据
    JSONArray array = new JSONArray();
    for(SysDataColumn column : listAll){
      array.add(column.getColumn_num());
    }
    obj.put("rows",JSONArray.parseArray(JSON.toJSONString(listParameter)));
    obj.put("column_num_all",array);
    return obj;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseBase add(SysDataColumn dataColumn) {
    BeanHelper.nullToEmpty(dataColumn);
    dataColumn.setColumn_id(KeyGenerator.getKey());
    int rows = dataColumnMapper.add(dataColumn);
    if(rows ==1){
      return  setResultSuccess("新增字段成功。");
    }else{
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return  setResultError("新增失败！");
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseBase edit(SysDataColumn dataColumn) {
    BeanHelper.nullToEmpty(dataColumn);
    int row= dataColumnMapper.edit(dataColumn);
    if(row ==1){
      return  setResultSuccess("修改成功。");
    }else{
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return  setResultError("修改失败！");
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseBase delete(String[] column_ids) {
    int rows= dataColumnMapper.delete(column_ids);
    if (rows >=1){
      return  setResultSuccess("删除成功。");
    }else{
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return setResultError("删除失败！！！");
    }

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseBase importForDb(String ds_id) {
    SysDataSource dataSource =dataSourceMapper.queryById(ds_id);
    if(dataSource == null){
      return  setResultError("数据源ID 不存在！");
    }
    // 先删除 再插入
    dataColumnMapper.deleteById(ds_id);

    //根据数据库链接ID,查询出对应的数据库链路
    SysDataConnect dataConnect =dataConnectMapper.queryDataConnectById(dataSource.getDb_link_no());

    //根据数据源ID,查询出数据源对应的参数信息
    List<SysDataParm> dataParmList = dataParmMapper.querySysDataParm("",ds_id);
    ResponseBase responseBase= getSysDataColumnForDb(dataSource,dataConnect,dataParmList);

    if(!responseBase.isSuccess()){
      return responseBase;
    }
    // 根据数据源查询出，所有的字段信息
    List<SysDataColumn> sysDataColumnList = (List<SysDataColumn>) responseBase.getReturnData();
    if (sysDataColumnList != null) {
      for (SysDataColumn sysDataColumn : sysDataColumnList) {
        dataColumnMapper.add(sysDataColumn);
      }
    }
    return setResultSuccess("导入成功");
  }
  /**
   * @throws
   * @方法名称: getSysDataColumnForDb
   * @功能描述: 根据查询sql语句从数据库中获取对应的返回列信息
   * @参数: @param ds_id 数据源ID
   * @参数: @return
   * @返回类型: ResponseBase
   */
  public ResponseBase getSysDataColumnForDb(SysDataSource dataSourcs,
                                            SysDataConnect dataConnect,
                                            List<SysDataParm> dataParmList) {

      dataConnect.setDb_pw(dataConnect.getDb_pw());
    // 数据库连接池
    Connection conn = JDBCUtil.getConnection(dataConnect);
    // sql 类型
    String dsType =dataSourcs.getDs_type();
    // 数据源
    String sqlStr = dataSourcs.getDs();
    // 需要返回的字段集合
    List<SysDataColumn> dataColumnList = new ArrayList<SysDataColumn>();

    if("SQL".equals(dsType)){
      // 测试此字符串是否以指定的前缀开头。startsWith("SELECT")
      if(sqlStr == null || "".equals(sqlStr) || !sqlStr.trim().toUpperCase().startsWith("SELECT")){
        return setResultError("该数据源不支持从数据库导入,请检查SQL");
      }
      //预编译类
      PreparedStatement ps =null;
      ResultSet rs = null; //表示数据库结果集的数据表

      try {
        ps= conn.prepareStatement(sqlStr);
        if(dataParmList != null ){
          for(int i=0;i<dataParmList.size();i++){
            SysDataParm dataParm = dataParmList.get(i);
            ps.setObject(i+1,null);
          }
        }
        //在此<code> PreparedStatement </ code>对象*中执行SQL查询，并返回查询生成的<code> ResultSet </ code>对象。
        rs =ps.executeQuery();
        //可用于获取有关<code> ResultSet </ code>对象中列的类型*和属性的信息的对象。
        ResultSetMetaData rsmd = rs.getMetaData();
        //将JDBC返回的结果信息组装为集合对象
        dataColumnList = assembleSysDataColumn(rsmd, dataSourcs.getDs_id());
      } catch (SQLException e) {
        e.printStackTrace();
      }finally {
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


    }else if("存储过程".equals(dsType)){
      //  查询是否可以导入字段
      ResponseBase responseBase = getProcedureSql(sqlStr, dataParmList);
      if(!responseBase.isSuccess()){
        return responseBase;
      }
      //用于执行SQL存储过程的接口。
      CallableStatement cs =null;
      ResultSet rs =null;

      try {
        cs =conn.prepareCall(responseBase.getReturnData().toString());
        int num =0;
        for(int i=0;i< dataParmList.size() ;i++){
          if("IN".equals(dataParmList.get(i).getParm_type())){
            cs.setObject(i+1,null);
          } else if("OUT".equals(dataParmList.get(i).getParm_type())){
            String parm_data_type = dataParmList.get(i).getParm_data_type();
            if ("String".equals(parm_data_type) || "Data".equals(parm_data_type) || "DataTime".equals(parm_data_type)) {
              cs.registerOutParameter(i+1, OracleTypes.VARCHAR);
            }else if("Integer".equals(parm_data_type) || "Long".equals(parm_data_type)){
              cs.registerOutParameter(i+1,OracleTypes.NUMBER);
            }else if("Double".equals(parm_data_type) ){
              cs.registerOutParameter(i+1,OracleTypes.DOUBLE);
            }else if("Float".equals(parm_data_type)){
              cs.registerOutParameter(i+1,OracleTypes.FLOAT);
            }else if("结果集(OUT)".equals(parm_data_type)){
              cs.registerOutParameter(i+1,OracleTypes.CURSOR);
              num=i+1;
            }
          }
        }
        cs.execute(); //在此<code> PreparedStatement </ code>对象中执行SQL语句，
        rs = (ResultSet) cs.getObject(num);
        dataColumnList = assembleSysDataColumn(rs.getMetaData(),dataSourcs.getDs_id());
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return setResultSuccess(dataColumnList);
  }



  /**
   * @throws
   * @方法名称: assembleSysDataColumn
   * @功能描述: 将JDBC返回的结果信息组装为集合对象
   * @参数: @param ds_id 数据源ID
   * @参数: @param rsmd JDBC返回的字段信息结果集
   * @参数: @return
   * @返回类型: ResponseBase
   */
  public List<SysDataColumn> assembleSysDataColumn(ResultSetMetaData rsmd, String ds_id) {
    int count = 0;
    List<SysDataColumn> sysDataColumnList = new ArrayList<SysDataColumn>();//需要返回的字段集合
    try {
      count = rsmd.getColumnCount();
      for (int i = 1; i <= count; i++) {
        SysDataColumn column = new SysDataColumn();
        column.setColumn_id(KeyGenerator.getKey());
        column.setColumn_num(i);
        column.setColumn_isnull(rsmd.isNullable(i) != 0 ? 0 : 1);
        int precision = rsmd.getPrecision(i);
        column.setColumn_length(precision == 0 ? rsmd.getColumnDisplaySize(i) : precision);
        column.setColumn_name(rsmd.getColumnLabel(i));
        column.setColumn_primary_key(0);//此字段暂时不维护,因为根据SQL查询语句,查询不出字段是否为主键信息,暂时先统一给为非主键
        column.setColumn_title(rsmd.getColumnLabel(i));
        column.setColumn_type(rsmd.getColumnTypeName(i));
        column.setColumn_unique(0);//此字段暂时不维护,因为根据SQL查询语句,查询不出字段是否唯一,暂时先统一给为不唯一
        column.setDs_id(ds_id);
        sysDataColumnList.add(column);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return sysDataColumnList;
  }


  /**
   *    查询数据是否可以导入字段
   * @param sqlStr
   * @param dataParmList
   * @return
   */
  public ResponseBase getProcedureSql(String sqlStr, List<SysDataParm> dataParmList) {
    if (sqlStr == null || "".equals(sqlStr) || dataParmList == null) {
      return setResultError("该数据源不支持从数据库导入,请检查存储过程");
    }
    boolean flag = false;
    StringBuffer sql_procedure = new StringBuffer(" {call " + sqlStr + " (");
    for (int i = 0; i < dataParmList.size(); i++) {
      SysDataParm sysDataParm = dataParmList.get(i);
      if ("OUT".equals(sysDataParm.getParm_type()) && "结果集(OUT)".equals(sysDataParm.getParm_data_type())) {
        flag = true;
      }
      if (i == dataParmList.size() - 1) {
        sql_procedure.append("?)}");
      } else {
        sql_procedure.append("?,");
      }
    }

    if (!flag) {
      return setResultError("该数据源不支持从数据库导入,请检查存储过程");
    } else {
      return setResult(true, 200, "处理成功", sql_procedure.toString());
    }
  }














}
