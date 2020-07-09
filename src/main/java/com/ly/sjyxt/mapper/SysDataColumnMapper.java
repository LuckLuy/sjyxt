package com.ly.sjyxt.mapper;

import com.ly.sjyxt.entity.SysDataColumn;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 数据源字段表 Mapper 接口
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Component(value ="DataColumnMapper")
public interface SysDataColumnMapper {


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
  int  deleteByDsIds(String[] ds_ids);

  @Select("<script> " +
      " select s.*,c.ds_name as ds_name " +
      "   from sys_data_source c, sys_data_column s " +
      "  where s.ds_id = c.ds_id " +
      " and (" +
      "      upper(trim(s.column_title)) LIKE CONCAT(CONCAT('%', #{parameter}), '%') or" +
      "      upper(trim(s.column_name)) LIKE CONCAT(CONCAT('%', #{parameter}), '%') " +
      ")" +
      "  <when  test='ds_id!=null'> and  s.ds_id=#{ds_id} </when>   " +
      "  ORDER BY s.column_num "+
      "</script>")
  List<SysDataColumn>  querySysDataColumn(@Param("parameter") String parameter, @Param("ds_id") String ds_id);


  @Insert({
      "<script>",
      "insert into sys_data_column (COLUMN_ID, COLUMN_TITLE, COLUMN_NAME, DS_ID, COLUMN_NUM, COLUMN_LENGTH, COLUMN_PRIMARY_KEY, COLUMN_UNIQUE, COLUMN_TYPE, COLUMN_ISNULL) values",
      "( #{column_id},#{column_title},#{column_name},#{ds_id},#{column_num},#{column_length},#{column_primary_key},#{column_unique},#{column_type},#{column_isnull})",
      "</script>"
  })
  int add(SysDataColumn dataColumn);


  @Update({"update sys_data_column t " +
      " set  column_num= #{column_num}," +
      "column_title= #{column_title}," +
      "column_name= #{column_name}," +
      "column_type= #{column_type}," +
      "column_isnull= #{column_isnull}," +
      "column_length= #{column_length}" +
      "where t.column_id =#{column_id}"})
  int edit(SysDataColumn dataColumn);

  @Delete("<script> " +
      "  delete from sys_data_column where COLUMN_ID  in " +
      "  <foreach collection=\"array\" item=\"columnIds\" index=\"index\"  open=\"(\" close=\")\" separator=\",\"> " +
      " #{columnIds} " +
      " </foreach> "+
      "</script>")
  public  int delete(String[]  columnIds);


  @Delete({" delete from sys_data_column t where t.ds_id =#{ds_id}"})
  int deleteById(@Param( "ds_id") String ds_id);

}
