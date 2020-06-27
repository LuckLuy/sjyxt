package com.ly.sjyxt.controller;


import com.ly.sjyxt.common.BaseApiService;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.DataParm;
import com.ly.sjyxt.service.IDataParmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * <p>
 * 数据源参数表 前端控制器
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Slf4j
@ApiIgnore
@Api(tags = "3、数据源参数信息管理")
@RestController
@RequestMapping("/dataSource/sysDataParm")
public class DataParmController extends BaseApiService {

  @Resource
  private com.ly.sjyxt.service.IDataParmService iDataParmService;


  @ApiOperation(value = "数据源参数信息管理-查询数据源下所有参数信息", notes = "数据源参数信息管理-查询数据源下所有参数信息")
  @PostMapping(value = "/list")
  public ResponseBase list (@RequestParam("parameter") String parameter,
                            @RequestParam("ds_id") String ds_id){
    return  setResultSuccess(iDataParmService.list(parameter,ds_id));
  }

  @ApiOperation(value = "数据源参数信息管理-参数添加", notes = "数据源参数信息管理-参数添加")
  @PostMapping(value = "/add")
  public ResponseBase add(@RequestBody DataParm dataParm){
    return  setResultSuccess(iDataParmService.add(dataParm));
  }


  @ApiOperation(value = "数据源参数信息管理-参数删除", notes = "数据源参数信息管理-参数删除")
  @PostMapping(value = "/delete")
  public ResponseBase delete(@RequestParam(value="parm_ids[]") String[] parm_ids){
    return  setResultSuccess(iDataParmService.delete(parm_ids));
  }
}
