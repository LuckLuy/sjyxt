package com.ly.sjyxt.controller;


import com.ly.sjyxt.common.BaseApiService;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.DataConnect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


/**
 * <p>
 * 数据库信息表 数据库链路信息控制层
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */

@Slf4j
@ApiIgnore
@Api
@ApiModel("1,数据链路信息管理")
@RestController
@RequestMapping("/dataSource/sysDataConnect")
public class DataConnectController extends BaseApiService {


  @Autowired
  private com.ly.sjyxt.service.IDataConnectService iDataConnectService;

  /**
   * 测试数据库链路是否通畅
   * @param dataConnect  数据库链路对象
   * @return
   *
   * @RequestBody : 请求body 中的数据
   */
  @ApiOperation(value = "数据链路管理-数据库链路测试", notes = "数据链路管理-数据库链路测试")
  @PostMapping(value = "/testConnection")
  public ResponseBase testConnection(@RequestBody DataConnect dataConnect){
    if(iDataConnectService.testConnection(dataConnect)){
      return setResultSuccess("链路畅通");
    }else{
      return setResultError("链路不通！！！");
    }
  }


  /**
   *  查询列表信息
   * @param parameter
   * @param pageNumber
   * @param pageSize
   * @return
   */
  @ApiOperation(value = "数据链路管理-分页列表查询", notes = "数据链路管理-分页列表查询")
  @PostMapping(value = "/list")
  public ResponseBase list(
      @RequestParam(name = "parameter") @ApiParam(value = "数据库连接名称/数据库IP") String parameter,
      @RequestParam(name = "pageNumber") @ApiParam(value = "页码数", required = true) Integer pageNumber,
      @RequestParam(name = "pageSize") @ApiParam(value = "行数", required = true) Integer pageSize) {
    return setResultSuccess( iDataConnectService.querySysDataConnect(parameter,pageNumber,pageSize));
  }


  /**
   *  @RequestBody : 此注解是 请求路径为body 内地数据。
   * @param dataConnect
   * @return
   */
  @ApiOperation(value = "数据链路管理-新增数据库链路信息", notes = "数据链路管理-新增数据库链路信息")
  @PostMapping(value = "/add")
  public ResponseBase add_database( @RequestBody DataConnect dataConnect) {
    // 先进行链路测试
    if (!iDataConnectService.testConnection(dataConnect)) {
      return setResultError("链路不通！！！");
    }

    if (iDataConnectService.addData_state(dataConnect)) {
      return setResultError("新增成功！！！");
    } else {
      return setResultError("新增失败！！！");
    }

  }
  @ApiOperation(value = "修改链路数据信息")
  @PostMapping("/edit")
  public ResponseBase edit(@RequestBody DataConnect dataConnect) {
    return  iDataConnectService.edit(dataConnect);
  }

  // @RequestParam：将请求参数绑定到你控制器的方法参数上（是springmvc中接收普通参数的注解）

  @ApiOperation(value = "删除数据信息")
  @PostMapping("/delete")
  public ResponseBase deletes(@RequestParam(value="db_link_nos[]") String[] db_link_nos) {
    return  iDataConnectService.deletes(db_link_nos );
  }




  /**
   * @throws
   * @方法名称: listAllDbState
   * @功能描述: 查询所有启用的数据库链路
   * @参数: @return
   * @返回类型: ResponseBase
   */
  @ApiOperation(value = "数据链路管理-查询所有启用的", notes = "数据链路管理-查询所有启用的")
  @PostMapping(value = "/listAllDbState")
  public ResponseBase listAllDbState() {
    return setResultSuccess(iDataConnectService.listAllDbState());
  }





}
