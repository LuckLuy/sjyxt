package com.ly.sjyxt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ly.sjyxt.common.BaseApiService;
import com.ly.sjyxt.common.ResponseBase;
import com.ly.sjyxt.entity.DataParm;
import com.ly.sjyxt.mapper.DataParmMapper;
import com.ly.sjyxt.service.IDataParmService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ly.sjyxt.util.BeanHelper;
import com.ly.sjyxt.util.KeyGenerator;
import jdk.internal.org.objectweb.asm.util.TraceAnnotationVisitor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据源参数表 服务实现类
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Service
public class DataParmServiceImpl extends BaseApiService implements IDataParmService {

  @Resource
  private  DataParmMapper dataParmMapper;

  @Override
  public JSONObject list(String parameter, String ds_id) {
    List<DataParm>  list = dataParmMapper.querySysDataParm(parameter.toUpperCase(),
        ds_id == null || "".equals(ds_id) ? null :ds_id);
    JSONObject obj = new JSONObject();

    List<DataParm> listParameter = new ArrayList<DataParm>();
    for(DataParm dataParm :list){
      BeanHelper.nullToEmpty(dataParm);
      listParameter.add(dataParm);
    }
    List<DataParm> listAll = dataParmMapper.querySysDataParm("",ds_id);
    JSONArray array = new JSONArray();
    for(DataParm parm :listAll){
      array.add(parm);
    }
    obj.put("rows",JSONArray.parseArray(JSON.toJSONString(listParameter)));
    obj.put("parm_num_all",array);
    return obj;

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseBase add(DataParm dataParm) {
    // 将数据中的null 转换成字符串“”
    BeanHelper.nullToEmpty(dataParm);
    // 设置主键
    dataParm.setParm_id(KeyGenerator.getKey());
    // 插入数据
    Integer rows = dataParmMapper.add(dataParm);
    if(rows == 1){
       return  setResultSuccess("插入成功！");
    }else{
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return setResultError("新增失败！！！");

    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseBase delete(String[] parm_ids) {
    // 此处 记住要int 类型，不要写integer 识别不了
    int rows = dataParmMapper.delete(parm_ids);
    if(rows >=1 ){
      return  setResultSuccess("删除成功；");
    }else{
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return  setResultError("删除失败！！！");
    }
  }
}
