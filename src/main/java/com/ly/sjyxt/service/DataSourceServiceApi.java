package com.ly.sjyxt.service;

import com.ly.sjyxt.common.ResponseBase;

import java.util.Map;

/**
 *  数据服务api 接口
 */
public interface DataSourceServiceApi {

  ResponseBase select(Map map);

  ResponseBase insert (Map map);

  ResponseBase update(Map map);

  ResponseBase delete(Map map);
}
