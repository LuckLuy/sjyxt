package com.ly.sjyxt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.sjyxt.common.BaseApiService;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.SysDataSource;
import com.ly.sjyxt.mapper.SysDataColumnMapper;
import com.ly.sjyxt.mapper.SysDataParmMapper;
import com.ly.sjyxt.mapper.SysDataSourceMapper;
import com.ly.sjyxt.service.SysDataSourceService;
import com.ly.sjyxt.util.BeanHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据源信息表 服务实现类
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Service
public class SysDataSourceServiceImpl extends BaseApiService implements SysDataSourceService {

  @Resource
  private SysDataSourceMapper dataSourceMapper;

  @Resource
  private SysDataParmMapper dataParmMapper;

  @Resource
  private SysDataColumnMapper dataColumnMapper;

  /**
   * @throws
   * @方法名称: querySysDataSoure
   * @功能描述: 分页查询数据源信息
   * @参数: @param parameter 数据源ID/数据源名称/数据源
   * @参数: @param db_link_no 数据库链路ID
   * @参数: @param pageNumber 页码数
   * @参数: @param pageSize 行数
   * @参数: @return
   * @return
   */
  @Override
  public JSONObject querySysDataSoure(String parameter, String db_link_no, Integer pageNum, Integer pageSize) {

    PageHelper.startPage(pageNum,pageSize);
    List<SysDataSource>  list= dataSourceMapper.querySysDataSoure(parameter.toUpperCase(),
        db_link_no == null || "".equals(db_link_no) ? null :db_link_no);

    // 分页数据
    PageInfo<SysDataSource> pageInfoList = new PageInfo<SysDataSource>(list);

    Long total = pageInfoList.getTotal();
    JSONObject obj = new JSONObject();
    obj.put("total",pageInfoList.getTotal());
    List<SysDataSource> listPage = new ArrayList<SysDataSource>();
    for(SysDataSource dataSource : pageInfoList.getList()){
      BeanHelper.nullToEmpty(dataSource);
      listPage.add(dataSource);
    }
    obj.put("rows", JSONArray.parseArray(JSON.toJSONString(listPage)));
    return obj;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean add( SysDataSource dataSource) {
    BeanHelper.nullToEmpty(dataSource);
    int rows = dataSourceMapper.add(dataSource);
    boolean flag= false;
    if(rows == 1){
      flag =true;
    }else{
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    return flag;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean edit(SysDataSource dataSource) {
    BeanHelper.nullToEmpty(dataSource);
    int row= dataSourceMapper.edit(dataSource);
    boolean b = false;
    if(row == 1){
      b=true;
    }else {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    return b;
  }

  @Override
  public ResponseBase delete(String[] db_ids) {

    int dataSourceNum =0;
    int parmNum =0;
    int columnNum =0;
    //删除数据源对应的参数
    parmNum = dataParmMapper.deleteByDsIds(db_ids);

    //删除数据源对应的字段
    columnNum = dataColumnMapper.deleteByDsIds(db_ids);

    //删除数据源
    dataSourceNum =  dataSourceMapper.deleteByDsIds(db_ids);

    return setResultSuccess("删除成功，删除数据源："+dataSourceNum +"行，"+
        "字段信息："+columnNum +"行，参数信息："+parmNum+"行。");
  }
}
