package com.ly.sjyxt.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.DataSource;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据源信息表 服务类
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
public interface IDataSourceService  {

  /**
   *   数据源查询接口
   * @param parameter
   * @param db_link_no
   * @return
   */
  public JSONObject querySysDataSoure(String parameter, String db_link_no,Integer pageNum,Integer pageSize);

  /**
   * 添加数据源
   * @param dataSource
   * @return
   */
  boolean  add (DataSource dataSource);

  /**
   *   修改数据源
   * @param dataSource
   * @return
   */
  boolean edit (DataSource dataSource);

  ResponseBase delete(String[] db_ids);
}
