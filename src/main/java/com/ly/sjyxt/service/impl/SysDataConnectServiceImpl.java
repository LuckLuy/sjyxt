package com.ly.sjyxt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.sjyxt.common.BaseApiService;
import com.ly.sjyxt.common.DataSourceFactory;
import com.ly.sjyxt.common.JDBCUtil;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.SysDataConnect;
import com.ly.sjyxt.mapper.SysDataColumnMapper;
import com.ly.sjyxt.mapper.SysDataConnectMapper;
import com.ly.sjyxt.mapper.SysDataParmMapper;
import com.ly.sjyxt.mapper.SysDataSourceMapper;
import com.ly.sjyxt.service.SysDataConnectService;
import com.ly.sjyxt.util.BeanHelper;
import com.ly.sjyxt.util.StringUtil;
import com.petrochina.sso.passwordencodeclient.PasswordEncodeDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据库信息表 服务实现类
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Service
public class SysDataConnectServiceImpl extends BaseApiService implements SysDataConnectService {

  @Resource
  private SysDataConnectMapper dataConnectMapper;

  @Resource
  private SysDataSourceMapper dataSourceMapper;

  @Resource
  private SysDataParmMapper dataParmMapper;

  @Resource
  private SysDataColumnMapper dataColumnMapper;

   @Autowired(required = false)
    private com.ly.sjyxt.util.KeyGenerator key;

  @Resource
  private DataSourceFactory dataSourceFactory;

  /**
   *  @Value 这个注解的作用是 获取配置文件 application-dev.yml中的 db_secretKey 对应的值。
   */
 /* @Value(value = "${db_secretKey}")
  private String db_secretKey;*/

  /**
   * 数据库链路测试
   *
   * @param dataConnect 数据库链路对象
   * @return
   */
  @Override
  public Boolean testConnection(SysDataConnect dataConnect) {

    String db_link_no = dataConnect.getDb_link_no();
    if (!StringUtil.isNull(db_link_no)) {
      //说明是编辑状态进行测试
      //查询原始数据
      SysDataConnect dataConnectOld = dataConnectMapper.queryDataConnectById(db_link_no);
      //如果页面和原始数据密码一致,说明密码没有进行修改,直接解密
      if (dataConnectOld.getDb_pw().equals(dataConnect.getDb_pw())) {
        // 密码解码
      /*  PasswordEncodeDecode p = new PasswordEncodeDecode();
        String db_pw_decode = null;
        try {
          db_pw_decode = p.passwordDecode(db_secretKey, dataConnect.getDb_pw());
        } catch (Exception e) {
          e.printStackTrace();
        }*/
        dataConnect.setDb_pw(dataConnect.getDb_pw());
      }

    }
    if (JDBCUtil.getConnection(dataConnect) != null) {
      return true;
    }
    return false;
  }

  /**
   * @throws
   * @方法名称: querySysDataConnect
   * @功能描述: 分页查询数据库链路信息
   * @参数: @param parameter 数据库连接名称/数据库IP
   * @参数: @param pageNumber 页码数
   * @参数: @param pageSize 行数
   * @参数: @return
   * @返回类型: JSONObject
   */
  @Override
  public JSONObject querySysDataConnect(String parameter, Integer pageNum, Integer pageSize) {
    // 页面显示的数据 多少行 多少页
    PageHelper.startPage(pageNum, pageSize);
    List<SysDataConnect> list = dataConnectMapper.querySysDataConnect(parameter.toUpperCase());
    // PageInfo 实体类下的 当前页 、每页的数量、当前页的数量
    PageInfo<SysDataConnect> pageInfoUserList = new PageInfo<SysDataConnect>(list);

    // 获取总记录数
    Long total = pageInfoUserList.getTotal();
    JSONObject obj = new JSONObject();
    obj.put("total", pageInfoUserList.getTotal());
    List<SysDataConnect> listPage = new ArrayList<SysDataConnect>();
    for (SysDataConnect dataConnect : pageInfoUserList.getList()) {
      BeanHelper.nullToEmpty(dataConnect);
      listPage.add(dataConnect);
    }
    obj.put("rows", JSONArray.parseArray(JSONArray.toJSONString(listPage)));
    return obj;
  }

  /**
   * 新增；链路
   * 新增数据库链路
   *
   * @return
   * @参数: @param DataConnect 数据库链路对象
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean addData_state(SysDataConnect dataConnect) {
    // 1，设置默认的主键
    dataConnect.setDb_link_no(key.getKey());

    //密码进行加密
    PasswordEncodeDecode p = new PasswordEncodeDecode();

   /* String db_pw_encode = null;
    try {
      db_pw_encode = p.passwordEncode(db_secretKey, dataConnect.getDb_pw());
    } catch (Exception e) {
      e.printStackTrace();
    }*/

    dataConnect.setDb_pw(dataConnect.getDb_pw());
    //将对象中的null 转空字符串
    BeanHelper.nullToEmpty(dataConnect);
    int num = dataConnectMapper.addData_user(dataConnect);

    boolean flag = false;
    if (num == 1) {
      flag = true;
      // 动态添加数据源
      dataSourceFactory.getDataSource(dataConnect);
    } else {
      // 手动进行回滚
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    return flag;
  }

  /**
   *   修改链路数据信息
   * @param dataConnect
   * @return
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseBase edit(SysDataConnect dataConnect) {

    SysDataConnect data_old = dataConnectMapper.queryDataConnectById(dataConnect.getDb_link_no());
    if(!data_old.getDb_pw().equals(dataConnect.getDb_pw())){
      //说明密码修改过,将最新的密码加密
      //密码进行加密
      PasswordEncodeDecode p= new PasswordEncodeDecode();
      String db_pw_encode =null;
    /*  try {
        db_pw_encode =p.passwordEncode(db_secretKey,dataConnect.getDb_pw());
      } catch (Exception e) {
        e.printStackTrace();
      }*/
      dataConnect.setDb_pw(dataConnect.getDb_pw());
    }

    //将对象中的null 转空字符串
    BeanHelper.nullToEmpty(dataConnect);
    boolean flag =testConnection(dataConnect);
    if(!flag){
      return setResultError("链路不通！！！");
    }
    // 调用修改的方法
    int rows = dataConnectMapper.edit(dataConnect);
    if(rows ==1){
      //删除原有的数据源
      dataSourceFactory.removeById(dataConnect.getDb_link_no());

      //添加编辑后的数据源
      dataSourceFactory.addDataSource(dataConnect);
      return  setResultSuccess("修改成功！！！");
    }else{

      //手动进行回滚
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return  setResultSuccess("修改失败！！！");
    }



  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseBase deletes(String[] db_link_nos) {
    int connectNum =0;
    int dataSourceNum =0;
    int parmNum =0;
    int columnNum =0;
    // 查询 出数据库链路下所有的数据源信息
    String[] ds_ids = dataSourceMapper.getDsIdsByDblinkNo(db_link_nos);
    if(ds_ids == null || ds_ids.length ==0){
        connectNum =dataConnectMapper.deletes(ds_ids);
        return setResultSuccess("删除成功,删除数据链路:" + connectNum + "行,数据源:" + dataSourceNum + "行," +
            "字段信息:" + columnNum + "行,参数信息:" + parmNum + "行");
    }

    //删除数据链路下所有数据源对应的参数
    parmNum = dataParmMapper.deleteByDsIds(ds_ids);
    //删除数据链路下所有数据源对应的字段
    columnNum =dataColumnMapper.deleteByDsIds(ds_ids);
    //删除数据链路对应的数据源
    dataSourceNum = dataSourceMapper.deleteByDsIds(ds_ids);
    //删除链路
    connectNum =dataConnectMapper.deletes(db_link_nos);
    for(String id:db_link_nos){
      //动态删除数据源
      dataSourceFactory.removeById(id);
    }
    return setResultSuccess("删除成功,删除数据链路:" + connectNum + "行,数据源:" + dataSourceNum + "行," +
        "字段信息:" + columnNum + "行,参数信息:" + parmNum + "行");
  }

/**
 * @throws
 * @方法名称: listAllDbState
 * @功能描述: 查询所有启用的数据链路
 * @参数: @return
 * @返回类型: List<SysDataConnect>
 */
  @Override
  public List<SysDataConnect> listAllDbState() {
    return dataConnectMapper.listAllDbState();
  }
}
