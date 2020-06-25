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
 * 数据源参数表
 * </p>
 *
 * @author ly
 * @since 2020-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SYS_DATA_PARM")
@ApiModel("数据源参数表_SYS_DATA_PARM")
public class DataParm implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "参数ID",hidden = true)
    private String parm_id;

    @ApiModelProperty(value = "数据源ID")
    private String ds_id;

    @ApiModelProperty(value = "数据源名称",hidden = true)
    private String ds_name;

    @ApiModelProperty(value = "参数代码")
    private String parm_code;

    @ApiModelProperty(value = "参数名称")
    private String parm_name;

    @ApiModelProperty(value = "参数类型：IN/OUT")
    private String parm_type;

    @ApiModelProperty(value = "序号",hidden = true)
    private Integer parm_num;

    @ApiModelProperty(value = "默认值")
    private String def_value;

    @ApiModelProperty(value = "描述")
    private String parm_desc;

    @ApiModelProperty(value = "参数值类型,String,Integer,Long,Double,Float,Data,DataTime,结果集(OUT)")
    private String parm_data_type;

    @ApiModelProperty(value = "查询方式,0:非模糊查询,1:模糊查询(包含),2:模糊查询(开头),3:模糊查询(结尾)")
    private String parm_query_type;

    @ApiModelProperty(value = "备用字段",hidden = true)
    private String other;

}
