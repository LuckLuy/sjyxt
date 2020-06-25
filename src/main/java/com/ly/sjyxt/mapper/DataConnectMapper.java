package com.ly.sjyxt.mapper;

import com.ly.sjyxt.entity.DataConnect;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 数据库信息表 Mapper 接口
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
public interface DataConnectMapper {

  /**
   * 根据 数据库链接名称  db_link_name 或者 数据库ip 查询数据
   *
   * @param parameter
   * @return
   */
  @Select({" select * " +
      "   from sys_data_connect t " +
      "  where (upper(trim(t.db_link_name)) LIKE CONCAT(CONCAT('%', #{parameter}), '%') or" +
      "   upper(trim(t.db_ip)) LIKE CONCAT(CONCAT('%', #{parameter}), '%')) order by t.sequ_num"})
  List<DataConnect> querySysDataConnect(@Param("parameter") String parameter);


  /**
   * 添加数据库连接
   *
   * @param
   * @return
   */
  @Insert(" insert into sys_data_connect " +
      "   (DB_LINK_NO, " +
      "    DB_LINK_NAME, " +
      "    DB_LINK_DESC, " +
      "    DB_LINK_TYPE, " +
      "    DB_IP, " +
      "    DB_NAME, " +
      "    DB_USER, " +
      "    DB_PW, " +
      "    SEQU_NUM, " +
      "    DB_STATE, " +
      "    CREATE_USER_ID, " +
      "    CREATE_DATE, " +
      "    SERVER_PORT, " +
      "    DB_VER, " +
      "    ISSID, " +
      "    OTHER) " +
      " values " +
      "   (#{db_link_no}, " +
      "    #{db_link_name}, " +
      "    #{db_link_desc}, " +
      "    #{db_link_type}, " +
      "    #{db_ip}, " +
      "    #{db_name}, " +
      "    #{db_user}, " +
      "    #{db_pw}, " +
      //"    (select Max(t.sequ_num)+1 from sys_data_connect t), " +
      "    (select case when  Max(sequ_num)+1 is null then 1 else Max(sequ_num)+1 end xh from sys_data_connect), " +
      "    #{db_state}, " +
      "    #{create_user_id}, " +
      "   sysdate, " +
      "    #{server_port}, " +
      "    #{db_ver}, " +
      "    #{issid}, " +
      "    #{other}) ")
  public int addData_user(DataConnect dataConnect);


  /**
   * 根据 数据库链接名称  db_link_name 或者 数据库ip 查询数据
   *
   * @param parameter
   * @return
   */
  @Select({" select * " +
      "   from sys_data_connect t " +
      "  where (upper(trim(t.db_link_name)) LIKE CONCAT(CONCAT('%', #{parameter}), '%') or" +
      "   upper(trim(t.db_ip)) LIKE CONCAT(CONCAT('%', #{parameter}), '%')) order by t.sequ_num"})
  public List<DataConnect> getDatabase(@Param("parameter") String parameter);

  /**
   * 根据数据库 数据库链接编号 查询数据
   *
   * @param db_link_no
   * @return
   */
  @Select({"select * from sys_data_connect t where t.db_link_no = #{db_link_no}"})
  DataConnect queryDataConnectById(@Param("db_link_no") String db_link_no);

  /**
   * 修改链路信息
   * @param dataConnect
   * @return
   */
  @Update({" update sys_data_connect " +
      "    set DB_LINK_NAME = #{db_link_name}, " +
      "        DB_LINK_DESC = #{db_link_desc}, " +
      "        DB_LINK_TYPE = #{db_link_type}, " +
      "        DB_IP        = #{db_ip}, " +
      "        DB_NAME      = #{db_name}, " +
      "        DB_USER      = #{db_user}, " +
      "        DB_PW        = #{db_pw}, " +
      "        DB_STATE     = #{db_state}, " +
      "        SERVER_PORT  = #{server_port}, " +
      "        DB_VER       = #{db_ver}, " +
      "        ISSID        = #{issid} " +
      "  where DB_LINK_NO = #{db_link_no} "})
  int  edit(DataConnect dataConnect);


  /**
   * 删除一条
   *
   * @param db_link_no
   * @return
   */
  @Delete({"delete from SYS_DATA_CONNECT t where t.db_link_no =#{db_link_no}"})
  Integer delete(@Param("db_link_no") String db_link_no);

    /**
        删除多 条数据库链接编号，
     *   以下添加《script 是为了 使用foreach 循环的语法。
     * @param db_link_nos
     * @return
     */
  @Delete({"<script>" +
             "delete from sys_data_connect where db_link_no in"+
            "<foreach collection=\"array\" item=\"db_link_nos\"  index=\"index\" open=\"(\" close=\")\" separator=\",\" >" +
                "#{db_link_nos}"+
            "</foreach>" +
          "</script>"})
  int deletes(String[] db_link_nos);

  /**
   * 查询 启用状态下 显示顺序
   * @return
   */
  @Select({" select * " +
      "   from sys_data_connect t " +
      "  where t.db_state='启用' order by t.sequ_num "})
  List<DataConnect> listAllDbState();

}
