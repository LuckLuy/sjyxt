package com.ly.sjyxt.entity;

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

/**
 * <p>
 * 数据源字段表
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SYS_DATA_COLUMN")
@ApiModel("数据源字段表_SYS_DATA_COLUMN")
public class DataColumn implements Serializable{

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "字段ID")
    private String column_id;

    @ApiModelProperty(value = "字段标题")
    private String column_title;

    @ApiModelProperty(value = "字段名")
    private String column_name;

    @ApiModelProperty(value = "数据源ID")
    private String ds_id;

    @ApiModelProperty(value = "数据源名称")
    private String ds_name;

    @ApiModelProperty(value = "序号")
    private Integer column_num;

    @ApiModelProperty(value = "字段长度")
    private Integer column_length;

    @ApiModelProperty(value = "是否主键  0:非主键 1:主键")
    private Integer column_primary_key;

    @ApiModelProperty(value = "可唯一 0:不唯一 1:唯一")
    private Integer column_unique;

    @ApiModelProperty(value = "字段类型")
    private String column_type;

    @ApiModelProperty(value = "可非空 0:可为空 1:空")
    private Integer column_isnull;
}
