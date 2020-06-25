package com.ly.sjyxt.common;

import com.alibaba.fastjson.JSONObject;

/**
 * 相应状态
 */
public class BaseApiService {

  // 返回成功，可以传data 值；
  public  ResponseBase setResultSuccess(Object data){
    return  setResult(true,200,"处理成功",data);
  }

  //返回成功，没有data值。
  public ResponseBase setResultSuccess(){
    return setResult( true,200,"处理成功","");
  }

  // 返回成功，自定义返回信息，没有data值。
  public ResponseBase setResultSuccess(String msg){
    return  setResult(true,200,msg,"");
  }


  //返回错误。可以传，msg
  public ResponseBase setResultError(String msg){
    return setResult(false,500,msg,"");
  }

  //返回code和消息自定义,没有返回数据
  public ResponseBase setResultError(Boolean tf,Integer code,String msg){
    return setResult( tf,code,msg,"");
  }


  /**
   *   调用封装
   * @param success
   * @param code
   * @param msg
   * @param data
   * @return
   */
  public  ResponseBase setResult(Boolean success ,Integer code,String msg,Object data){
    return  new ResponseBase(success,code,msg,data);
  }


}
