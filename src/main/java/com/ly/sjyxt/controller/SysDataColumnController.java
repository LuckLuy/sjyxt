package com.ly.sjyxt.controller;


import com.ly.sjyxt.common.BaseApiService;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.SysDataColumn;
import com.ly.sjyxt.service.SysDataColumnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * <p>
 * 数据源字段表 前端控制器
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Slf4j
@ApiIgnore
@Api(tags = "4、数据源字段信息管理")
@RestController
@RequestMapping("/dataSource/sysDataColumn")
public class SysDataColumnController extends BaseApiService {

@Resource
  private SysDataColumnService iDataColumnService;

  @ApiOperation(value = "数据源字段信息管理-查询数据源下所有字段信息", notes = "数据源字段信息管理-查询数据源下所有字段信息")
  @PostMapping(value = "/list")
  public ResponseBase list (@RequestParam("parameter") String parameter,@RequestParam("ds_id") String ds_id){
    return  setResultSuccess(iDataColumnService.list(parameter,ds_id));
  }



  @ApiOperation(value = "数据源字段信息管理-添加字段", notes = "数据源字段信息管理-添加字段")
  @PostMapping(value = "/add")
  public ResponseBase add (@RequestBody SysDataColumn dataColumn){
    return  iDataColumnService.add(dataColumn);
  }



  @ApiOperation(value = "数据源字段信息管理-修改字段", notes = "数据源字段信息管理-修改字段")
  @PostMapping(value = "/edit")
  public ResponseBase edit ( @RequestBody SysDataColumn dataColumn){
    return  iDataColumnService.edit(dataColumn);
  }



  @ApiOperation(value = "数据源字段信息管理-删除字段", notes = "数据源字段信息管理-删除字段")
  @PostMapping(value = "/delete")
  public ResponseBase delete (@RequestParam(value = "column_ids[]") String[] column_ids){
    return  iDataColumnService.delete(column_ids);
  }

  @ApiOperation(value = "数据源字段信息管理-从数据库导入字段信息", notes = "数据源字段信息管理-从数据库导入字段信息")
  @PostMapping(value = "/importForDb")
  public ResponseBase importForDb (@RequestParam(name="ds_id") @ApiParam(value="数据源ID") String ds_id){

    return iDataColumnService.importForDb(ds_id);
  }


}
