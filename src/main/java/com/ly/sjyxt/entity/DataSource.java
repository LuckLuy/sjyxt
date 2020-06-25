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
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 数据源信息表
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SYS_DATA_SOURCE")
@ApiModel("数据源信息表_SYS_DATA_SOURCE")
public class DataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据源ID")
    private String ds_id;

    @ApiModelProperty(value = "数据源名称")
    private String ds_name;

    @ApiModelProperty(value = "数据源类型：SQL/存储过程")
    private String ds_type;

    @ApiModelProperty(value = "数据源：SQL语句")
    private String ds;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "数据库链接编号,SYS_DATA_CONNECT:DB_LINK_NO")
    private String db_link_no;

    @ApiModelProperty(value = "数据库链接名称",hidden = true)
    private String db_link_name;

    @ApiModelProperty(value = "备用字段" ,hidden = true)
    private String other;

    @ApiModelProperty(value = "添加时间",hidden = true)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date create_date;

}
