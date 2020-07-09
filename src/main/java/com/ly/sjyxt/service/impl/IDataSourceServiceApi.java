package com.ly.sjyxt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ly.sjyxt.common.BaseApiService;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.SysDataColumn;
import com.ly.sjyxt.entity.SysDataConnect;
import com.ly.sjyxt.entity.SysDataParm;
import com.ly.sjyxt.entity.SysDataSource;
import com.ly.sjyxt.mapper.SysDataColumnMapper;
import com.ly.sjyxt.mapper.SysDataConnectMapper;
import com.ly.sjyxt.mapper.SysDataParmMapper;
import com.ly.sjyxt.mapper.SysDataSourceMapper;
import com.ly.sjyxt.service.DataSourceServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class IDataSourceServiceApi extends BaseApiService implements DataSourceServiceApi {

  @Autowired(required = false)
  private SysDataSourceMapper dataSourceMapper;

  @Autowired
  private SysDataConnectMapper dataConnectMapper;

  @Autowired
  private SysDataColumnMapper dataColumnMapper;

  @Autowired
  private SysDataParmMapper dataParmMapper;

  @Autowired
  private  ApiUtil apiUtil;



  @Override
  public ResponseBase select(Map map) {
    // 1,首先获取数据源
    String ds_id = (String) map.get("ds_id");
    if(ds_id == null || "".equals(ds_id)){
      return  setResultError("数据源ID为空！");
    }
    /*根据数据源ID 查询链路 字段 参数信息 数据源信息*/
    ResponseBase responseBase = getObjectByDsId(ds_id);
    if(!responseBase.isSuccess()){
      return responseBase ;
    };
    //获取数据链路
    SysDataConnect dataConnects = (SysDataConnect) ((Map)responseBase.getReturnData()).get("connect");

    SysDataSource dataSources= (SysDataSource) ((Map) responseBase.getReturnData()).get("dataSource");

    List<SysDataColumn> dataColumns = (List<SysDataColumn>) ((Map) responseBase.getReturnData()).get("columns");

    List<SysDataParm> dataParms = (List<SysDataParm>) ((Map) responseBase.getReturnData()).get("parms");

    return apiUtil.selectUtil(dataSources,dataConnects,dataParms,dataColumns,map);
  }
/**
 * @throws
 * @方法名称: getObjectByDsId
 * @功能描述: 根据数据源ID 查询链路 字段 参数信息 数据源信息
 */
  private ResponseBase getObjectByDsId(String ds_id) {
      Map<String,Object> map =new HashMap<String, Object>();
      // 查询数据源
    SysDataSource dataSource = dataSourceMapper.queryById(ds_id);
    if(dataSource == null || "".equals(dataSource)){
      return  setResultError("数据源不存在");
    }

    // 获取链路对象
    SysDataConnect dataConnect =dataConnectMapper.queryDataConnectById(dataSource.getDb_link_no());
    if("停用".equals(dataConnect.getDb_state())){
      return  setResultError("数据链路已经停用");
    }
    //获取字段信息
    List<SysDataColumn> columnList =dataColumnMapper.querySysDataColumn("",ds_id);

    //获取参数信息
    List<SysDataParm> dataParmList =dataParmMapper.querySysDataParm("",ds_id);

    map.put("connect",dataConnect);
    map.put("columns",columnList);
    map.put("parms",dataParmList);
    map.put("dataSource",dataSource);

    return setResultSuccess(map);
  }

  /**
   * @throws
   * @方法名称: insert
   * @功能描述: 新增数据
   * @参数: @param map 查询相关的参数
   * @参数: @return
   * @返回类型: ResponseBase
   */
  @Override
  public ResponseBase insert(Map map) {

    // 获取数据源id
    String ds_id = (String) map.get("ds_id");
    if(ds_id == null || "".equals(ds_id)){
      return setResultError("数据源ID为空");
    }
    ResponseBase responseBase = getObjectByDsId(ds_id);
    if(!responseBase.isSuccess()){
      return responseBase;
    }
    // 获取链路
    SysDataConnect sysDataConnect = (SysDataConnect) ((Map)responseBase.getReturnData()).get("connect");
    //获取数据源
    SysDataSource sysDataSoure = (SysDataSource) ((Map) responseBase.getReturnData()).get("datasoure");
    //获取参数对象
    List<SysDataParm> parmList = (List<SysDataParm>) ((Map) responseBase.getReturnData()).get("parms");

    //获取字段信息
    List<SysDataColumn> columnList=(List<SysDataColumn>) ((Map) responseBase.getReturnData()).get("columns");

    sysDataConnect.setDb_pw(sysDataConnect.getDb_pw());
    return apiUtil.insertOrUpdateOrDeleteUtil(sysDataSoure, sysDataConnect, parmList, columnList, map);
  }
  /**
   * @throws
   * @方法名称: update
   * @功能描述: 更新数据
   * @参数: @param map 查询相关的参数
   * @参数: @return
   * @返回类型: ResponseBase
   */
  @Override
  public ResponseBase update(Map map) {
    // 获取数据源id
    String ds_id= (String) map.get("ds_id");
    if(ds_id == null || "".equals(ds_id)){
      return setResultError("数据源ID为空");
    }

    ResponseBase responseBase = getObjectByDsId(ds_id);
    if(!responseBase.isSuccess()){
      return responseBase;
    }
    // 获取数据链路
    SysDataConnect dataConnect= (SysDataConnect) ((Map) responseBase.getReturnData()).get("connect");
    // 获取数据源
    SysDataSource sysDataSource = (SysDataSource) ((Map) responseBase.getReturnData()).get("dataSource");
    // 获取字段
    List<SysDataColumn> columnList = (List<SysDataColumn>) ((Map)responseBase.getReturnData()).get("columns");
    //获取参数对象
    List<SysDataParm> parmList = (List<SysDataParm>) ((Map)responseBase.getReturnData()).get("parms");

    dataConnect.setDb_pw(dataConnect.getDb_pw());
    return  apiUtil.insertOrUpdateOrDeleteUtil(sysDataSource,dataConnect,parmList,columnList,map);

  }
  /**
   * @throws
   * @方法名称: delete
   * @功能描述: 删除数据
   * @参数: @param map 查询相关的参数
   * @参数: @return
   * @返回类型: ResponseBase
   */
  @Override
  public ResponseBase delete(Map map) {
    //获取数据源ID
    String ds_id = (String) map.get("ds_id");
    if (ds_id == null || "".equals(ds_id)) {return setResultError("数据源ID为空");};
    ResponseBase responseBase = getObjectByDsId(ds_id);
    if (!responseBase.isSuccess()) {
      return responseBase;
    };
    // 获取数据链路
    SysDataConnect dataConnect= (SysDataConnect) ((Map) responseBase.getReturnData()).get("connect");
    // 获取数据源
    SysDataSource sysDataSource = (SysDataSource) ((Map) responseBase.getReturnData()).get("dataSource");
    // 获取字段
    List<SysDataColumn> columnList = (List<SysDataColumn>) ((Map)responseBase.getReturnData()).get("columns");
    //获取参数对象
    List<SysDataParm> parmList = (List<SysDataParm>) ((Map)responseBase.getReturnData()).get("parms");

    return  apiUtil.insertOrUpdateOrDeleteUtil(sysDataSource,dataConnect,parmList,columnList,map);

  }


}
