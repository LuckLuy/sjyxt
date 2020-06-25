package com.ly.sjyxt.controller;


import com.ly.sjyxt.common.BaseApiService;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.DataSource;
import com.ly.sjyxt.mapper.DataSourceMapper;
import com.ly.sjyxt.service.IDataSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * <p>
 *  数据源信息表 前端控制器
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Slf4j
@ApiIgnore
@Api("数据源 界面管理")
@RestController
@RequestMapping("/dataSource/sysDataSoure")
public class DataSourceController extends BaseApiService {

  @Resource
  private com.ly.sjyxt.service.IDataSourceService  iDataSourceService;

  @ApiOperation(value = "数据源信息管理-分页列表查询", notes = "数据源信息管理-分页列表查询")
  @PostMapping(value = "/list")
  public ResponseBase list(@RequestParam(name = "parameter") @ApiParam(value = "数据源ID/数据源名称/数据源") String parameter,
                           @RequestParam(name = "db_link_no") @ApiParam(value = "数据链路ID") String db_link_no,
                           @RequestParam(name = "pageNumber") @ApiParam(value = "页码数", required = true) Integer pageNumber,
                           @RequestParam(name = "pageSize") @ApiParam(value = "行数", required = true) Integer pageSize) {

    return setResultSuccess(iDataSourceService.querySysDataSoure(parameter,db_link_no,pageNumber,pageSize));
  }





  @ApiOperation(value = "数据源-新增", notes = "数据源新增数据")
  @PostMapping(value = "/add")
  public  ResponseBase add( @RequestBody DataSource dataSource) {
    if (iDataSourceService.add(dataSource)) {
      return setResultSuccess("新增成功！！！");
    } else {
      return setResultError("新增失败！！！");
    }
  }


  @ApiOperation(value = "数据源-新增", notes = "数据源新增数据")
  @PostMapping(value = "/edit")
  public  ResponseBase edit( @RequestBody DataSource dataSource){
    if(iDataSourceService.edit(dataSource)){
      return  setResultSuccess("修改成功。");
    }else{
      return  setResultError("修改失败");
    }
  }


  @ApiOperation(value = "数据源-删除", notes = "数据源删除数据")
  @PostMapping(value = "/delete")
  public  ResponseBase delete( @RequestParam(value = "ds_ids[]") String[] db_ids){
      return  setResultSuccess(iDataSourceService.delete(db_ids));
  }
}