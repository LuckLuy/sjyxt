package com.ly.sjyxt.controller;


import com.alibaba.fastjson.JSONArray;
import com.ly.sjyxt.common.BaseApiService;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.SysDataParm;
import com.ly.sjyxt.service.SysDataParmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
public class SysDataParmController extends BaseApiService {

  @Resource
  private SysDataParmService iDataParmService;


  @ApiOperation(value = "数据源参数信息管理-查询数据源下所有参数信息", notes = "数据源参数信息管理-查询数据源下所有参数信息")
  @PostMapping(value = "/list")
  public ResponseBase list (@RequestParam("parameter") String parameter,
                            @RequestParam("ds_id") String ds_id){
    return  setResultSuccess(iDataParmService.list(parameter,ds_id));
  }

  @ApiOperation(value = "数据源参数信息管理-参数添加", notes = "数据源参数信息管理-参数添加")
  @PostMapping(value = "/add")
  public ResponseBase add(@RequestBody SysDataParm dataParm){
    return  setResultSuccess(iDataParmService.add(dataParm));
  }


  @ApiOperation(value = "数据源参数信息管理-参数删除", notes = "数据源参数信息管理-参数删除")
  @PostMapping(value = "/delete")
  public ResponseBase delete(@RequestParam(value="parm_ids[]") String[] parm_ids){
    return  setResultSuccess(iDataParmService.delete(parm_ids));
  }


  @ApiOperation(value = "数据源参数信息管理-参数修改", notes = "数据源参数信息管理-参数修改")
  @PostMapping(value = "/edit")
  public ResponseBase edit(@RequestBody SysDataParm dataParm) {
    return setResultSuccess(iDataParmService.edit(dataParm));
  }

  @ApiOperation(value = "数据源参数信息管理-序号排序", notes = "数据源参数信息管理-序号排序")
  @PostMapping(value = "/parmNumSort")
  public ResponseBase editParmNumByParmId(String parmNums) {
    return iDataParmService.editParmNumByParmId(parmNums != null && ! "".equals(parmNums) ? JSONArray.parseArray(parmNums) :null);
  }
}
