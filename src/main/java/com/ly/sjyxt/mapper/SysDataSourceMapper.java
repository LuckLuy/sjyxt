package com.ly.sjyxt.mapper;

import com.ly.sjyxt.entity.SysDataSource;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 数据源信息表 Mapper 接口
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Component(value ="DataSourceMapper")
public interface SysDataSourceMapper {


  /**
   *  查询数据源数据
   * @param parameter
   * @param db_link_no
   * @return
   */
  @Select("<script> " +
      " select s.*,c.db_link_name as db_link_name " +
      "   from sys_data_connect c, sys_data_source s " +
      "  where s.db_link_no = c.db_link_no " +
      " and (upper(trim(s.ds_id)) LIKE CONCAT(CONCAT('%', #{parameter}), '%') or" +
      "      upper(trim(s.ds_name)) LIKE CONCAT(CONCAT('%', #{parameter}), '%') or" +
      "      upper(trim(s.ds)) LIKE CONCAT(CONCAT('%', #{parameter}), '%') )" +
      "  <when  test='db_link_no != null'> and  s.db_link_no = #{db_link_no} </when>   " +
      "  ORDER BY NLSSORT(s.ds_name,'NLS_SORT = SCHINESE_PINYIN_M') "+
      "</script>")
  List<SysDataSource> querySysDataSoure(@Param("parameter") String parameter,
                                        @Param("db_link_no") String db_link_no);


  /**
   *  根据数据id 查询数据源 有多少数据
   * @param db_link_nos
   * @return
   */
  @Select("<script> " +
      "  select ds_id from sys_data_source where db_link_no in " +
      "  <foreach collection=\"array\" item=\"db_link_nos\" index=\"index\"  open=\"(\" close=\")\" separator=\",\"> " +
      " #{db_link_nos} " +
      " </foreach> "+
      "</script>")
  String[] getDsIdsByDblinkNo(String[] db_link_nos);

  /**
   *  根据id 或多个id  删除数据
   * @param ds_ids
   * @return
   */
  @Delete("<script> " +
      "  delete from sys_data_source where ds_id  in " +
      "  <foreach collection=\"array\" item=\"ds_ids\" index=\"index\"  open=\"(\" close=\")\" separator=\",\"> " +
      " #{ds_ids} " +
      " </foreach> "+
      "</script>")
  int deleteByDsIds(String[] ds_ids);

  @Insert({" insert into sys_data_source (DS_ID, DS_NAME, DS_TYPE, DS, REMARKS, DB_LINK_NO, OTHER, CREATE_DATE) " +
      "  values ( #{ds_id},#{ds_name},#{ds_type},#{ds},#{remarks},#{db_link_no},#{other},sysdate )"})
  int  add (SysDataSource dataSource);

  @Update({" update sys_data_source " +
      "    set DS_NAME = #{ds_name}, " +
      "        DS_TYPE = #{ds_type}, " +
      "        DS = #{ds}, " +
      "        REMARKS = #{remarks}, " +
      "        DB_LINK_NO  = #{db_link_no}, " +
      "        OTHER = #{other} " +
      "  where DS_ID = #{ds_id} "})
  int edit(SysDataSource dataSource);


  @Select({" select * from sys_data_source s where s.ds_id =#{ds_id}"})
  SysDataSource queryById(@Param("ds_id") String ds_id);
}
