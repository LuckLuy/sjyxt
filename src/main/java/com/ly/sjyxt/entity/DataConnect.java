package com.ly.sjyxt.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;



import com.baomidou.mybatisplus.annotations.Version;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 数据库信息表
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Data
@ToString
@TableName("SYS_DATA_CONNECT")
@ApiModel("数据库信息表_SYS_DATA_CONNECT")
public  class DataConnect implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据库链接编号")
    private String db_link_no;

    @ApiModelProperty(value = "数据库链接名称")
    private  String db_link_name;

    @ApiModelProperty(value = "数据库链接描述")
    private  String db_link_desc;

    @ApiModelProperty(value = "数据库链接类型")
    private  String db_link_type;

    @ApiModelProperty(value = "数据库ip")
    private  String db_ip;

    @ApiModelProperty(value = "数据库名称")
    private  String db_name;

    @ApiModelProperty(value = "数据库用户")
    private  String db_user;

    @ApiModelProperty(value = "数据库口令",hidden = true)
    private  String db_pw;

    @ApiModelProperty(value = "显示顺序",hidden = true)
    private  Integer sequ_num;

    @ApiModelProperty(value = "数据库状态 显示停用或启用")
    private  String db_state;

    @ApiModelProperty(value = "添加人姓名",hidden = true)
    private  String create_user_id;

    @ApiModelProperty(value = "添加时间",hidden = true)
    //@JSONField 因pom引用了fastjson 会有冲突,不生效,改为JSONField 即可
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date create_date;

    @ApiModelProperty(value = "端口")
    private  String server_port;

    @ApiModelProperty(value = "数据库版本")
    private  String db_ver;

    @ApiModelProperty(value = "是否是SID，针对oracle数据库，Y：代表SID，N：代表SERVICE_NAME",hidden = true)
    private  String issid;

    @ApiModelProperty(value = "备用字段",hidden = true)
    private  String other;


}

