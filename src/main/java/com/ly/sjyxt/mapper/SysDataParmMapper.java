package com.ly.sjyxt.mapper;

import com.ly.sjyxt.entity.SysDataParm;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>  数据源参数数据交互层
 * 数据源参数表 Mapper 接口
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Component(value ="DataParmMapper")
public interface SysDataParmMapper {


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

  /**
   *   查询当前数据源的参数列表
   * @param parameter
   * @param ds_id
   * @return
   */
  @Select("<script> " +
      " select s.*,c.ds_name as ds_name " +
      "   from sys_data_source c, sys_data_parm s " +
      "  where s.ds_id = c.ds_id " +
      " and (" +
      "      upper(trim(s.parm_code)) LIKE CONCAT(CONCAT('%', #{parameter}), '%') or" +
      "      upper(trim(s.parm_name)) LIKE CONCAT(CONCAT('%', #{parameter}), '%') " +
      ")" +
      "  <when  test='ds_id!=null'> and  s.ds_id=#{ds_id} </when>   " +
      "  ORDER BY s.parm_num " +
      "</script>")
  List<SysDataParm> querySysDataParm(
      @Param("parameter") String parameter,
      @Param("ds_id") String ds_id);

  @Insert({" insert into sys_data_parm (PARM_ID, DS_ID, PARM_CODE, PARM_NAME, PARM_TYPE, PARM_NUM, DEF_VALUE, PARM_DESC, PARM_DATA_TYPE, OTHER,PARM_QUERY_TYPE) " +
      "  values ( #{parm_id},#{ds_id},#{parm_code},#{parm_name},#{parm_type},#{parm_num},#{def_value},#{parm_desc},#{parm_data_type},#{other},#{parm_query_type} )"})
  int add(SysDataParm dataParm);

  @Delete("<script>" +
      "delete from sys_data_parm " +
      "where parm_id  in " +
      "<foreach collection=\"array\"  item=\"parm_ids\" index=\"index\" " +
      " open=\"(\"  close=\") \"  separator=\",\" >" +
      " #{parm_ids}" +
      "</foreach>  " +
      "</script>")
  int delete( String[] parm_ids);


  @Update({" update SYS_DATA_PARM " +
      "    set PARM_CODE = #{parm_code}, " +
      "        PARM_NAME = #{parm_name}, " +
      "        PARM_TYPE = #{parm_type}, " +
      "        PARM_NUM = #{parm_num}, " +
      "        DEF_VALUE  = #{def_value}, " +
      "        PARM_DESC = #{parm_desc}, " +
      "        PARM_DATA_TYPE = #{parm_data_type}, " +
      "        OTHER = #{other}, " +
      "        PARM_QUERY_TYPE = #{parm_query_type} " +
      "  where PARM_ID = #{parm_id} "})
  int edit(SysDataParm dataParm);

  @Update({" update SYS_DATA_PARM " +
      "    set PARM_NUM = #{parm_num} " +
      "  where PARM_ID = #{parm_id} "})
  public int editParmNumByParmId(@Param("parm_id") String parm_id, @Param("parm_num") int parm_num);
}
