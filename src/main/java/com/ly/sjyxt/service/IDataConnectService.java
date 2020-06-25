package com.ly.sjyxt.service;

import com.alibaba.fastjson.JSONObject;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.DataConnect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据库信息表 服务类
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
public interface IDataConnectService{

    /**
     * 测试 链路是否畅通
     * @param dataConnect
     * @return
     */
    Boolean testConnection(DataConnect dataConnect);


  /**
   *    数据列表信息
   * @param parameter
   * @param pageNum
   * @param pageSize
   * @return
   */
   JSONObject querySysDataConnect ( String parameter ,Integer pageNum, Integer pageSize);

    /**
     * 添加联络信息
     * @param dataConnect
     * @return
     */
    boolean addData_state(DataConnect dataConnect);

  /**
   *  修改链路信息
   * @param dataConnect
   * @return
   */
    ResponseBase  edit(DataConnect dataConnect);


  /**
   *  删除数据
   * @param db_link_nos
   * @return
   */
  ResponseBase deletes(String[] db_link_nos);

  /**
   *  查询启用状态下 显示顺序
   * @return
   */
  List<DataConnect> listAllDbState();

}
