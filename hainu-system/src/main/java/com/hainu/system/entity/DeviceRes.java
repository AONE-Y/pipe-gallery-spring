package com.hainu.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.entity
 * @Date：2021/10/7 15:27
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@ApiModel(value = "testshardingjdbc.device_res")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "testshardingjdbc.device_res")
public class DeviceRes implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;

    /**
     * 仓端地址
     */
    @TableField(value = "ws_name")
    @ApiModelProperty(value = "仓端地址")
    private String wsName;

    /**
     * 节点名
     */
    @TableField(value = "node")
    @ApiModelProperty(value = "节点名")
    private String node;

    /**
     * 数据类型
     */
    @TableField(value = "code_type")
    @ApiModelProperty(value = "数据类型")
    private String codeType;

    /**
     * 数据代码
     */
    @TableField(value = "code")
    @ApiModelProperty(value = "数据代码")
    private String code;

    /**
     * 数据值
     */
    @TableField(value = "code_value")
    @ApiModelProperty(value = "数据值")
    private Double codeValue;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_WS_NAME = "ws_name";

    public static final String COL_NODE = "node";

    public static final String COL_CODE_TYPE = "code_type";

    public static final String COL_CODE = "code";

    public static final String COL_CODE_VALUE = "code_value";
}