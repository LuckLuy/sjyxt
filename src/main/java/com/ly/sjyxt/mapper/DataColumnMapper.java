package com.ly.sjyxt.mapper;

import com.ly.sjyxt.entity.DataColumn;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

/**
 * <p>
 * 数据源字段表 Mapper 接口
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
public interface DataColumnMapper{


  /**
   * 删除列表字段信息
   * @param ds_ids
   * @return
   */
  @Delete("<script> " +
      "  delete from sys_data_column where ds_id  in " +
      "  <foreach collection=\"array\" item=\"ds_ids\" index=\"index\"  open=\"(\" close=\")\" separator=\",\"> " +
      " #{ds_ids} " +
      " </foreach> "+
      "</script>")
  int deleteByDsIds(String[] ds_ids);
}
