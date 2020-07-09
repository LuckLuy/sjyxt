package com.ly.sjyxt.api;

import com.ly.sjyxt.common.BaseApiService;
import com.ly.sjyxt.common.ResponseBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *   对外暴露的Api
 */

@Slf4j
@Api(tags = "API、PAI服务接口")
@RestController
@RequestMapping("/api")
public class DataSourceApi extends BaseApiService {

  @Autowired
  private com.ly.sjyxt.service.DataSourceServiceApi dataSourceServiceApi;

  /**
   * CrossOrigin  允许跨域的注解
   * @param map
   * @return
   */
  @ApiOperation(value = "数据查询接口", notes = "数据查询接口")
  @RequestMapping("/select")
  @CrossOrigin
  public ResponseBase select(@RequestBody Map map){
    if (map == null) {
      return setResultError("参数信息不能为空");
    }
    return  dataSourceServiceApi.select(map);
  }

  /**
   * @throws
   * @方法名称: insert
   * @功能描述: 数据新增接口
   * @参数: @param map 数据新增相关的参数
   * @参数: @return
   * @返回类型: ResponseBase
   */
  @ApiOperation(value = "数据新增接口", notes = "数据新增接口")
  @RequestMapping("/insert")
  @CrossOrigin(origins="*",maxAge=3600)
  public ResponseBase insert(@RequestBody Map map){
    if (map == null) {
      return setResultError("参数信息不能为空");
    }
    return  dataSourceServiceApi.insert(map);
  }

  @ApiOperation(value = "数据更新接口", notes = "数据更新接口")
  @RequestMapping("/update")
  @CrossOrigin(origins="*",maxAge=3600)
  public ResponseBase update(@RequestBody Map map){
    if (map == null) {
      return setResultError("参数信息不能为空");
    }
    return  dataSourceServiceApi.update(map);
  }




  @ApiOperation(value = "数据删除接口", notes = "数据删除接口")
  @PostMapping(value = "/delete")
  public ResponseBase delete(@RequestBody Map map) {
    if (map == null) {
      return setResultError("参数信息不能为空");
    }
    return dataSourceServiceApi.delete(map);
  }


}
