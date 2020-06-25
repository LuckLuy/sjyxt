package com.ly.sjyxt.mapper;

import com.ly.sjyxt.entity.DataParm;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

/**
 * <p>  数据源参数数据交互层
 * 数据源参数表 Mapper 接口
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
public interface DataParmMapper {


  /**
   *   根据id 删除数据源表的数据
   * @param ds_ids
   * @return
   */
  @Delete("<script> " +
      "  delete from sys_data_parm where ds_id  in " +
      "  <foreach collection=\"array\" item=\"ds_ids\" index=\"index\"  open=\"(\" close=\")\" separator=\",\"> " +
      " #{ds_ids} " +
      " </foreach> "+
      "</script>")
  int deleteByDsIds(String[] ds_ids);
}
