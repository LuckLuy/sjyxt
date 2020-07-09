package com.ly.sjyxt.service;

import com.alibaba.fastjson.JSONObject;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.SysDataColumn;

/**
 * <p>
 * 数据源字段表 服务类
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
public interface SysDataColumnService {

  JSONObject list(String parameter ,String  ds_id);

  ResponseBase add(SysDataColumn dataColumn);
  ResponseBase edit(SysDataColumn dataColumn);

  ResponseBase delete(String[]  columnIds);

  ResponseBase importForDb(String ds_id);
}
