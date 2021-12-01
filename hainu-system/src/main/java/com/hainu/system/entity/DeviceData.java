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
import java.time.LocalDateTime;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.entity
 * @Date：2021/11/21 11:50
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@ApiModel(value = "testshardingjdbc.device_data")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "testshardingjdbc.device_data")
public class DeviceData implements Serializable {
    public static final String COL_TYPE = "type";
    /**
     * 查询id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "查询id")
    private Long id;

    /**
     * 仓端名
     */
    @TableField(value = "ws_name")
    @ApiModelProperty(value = "仓端名")
    private String wsName;

    /**
     * 节点名
     */
    @TableField(value = "node")
    @ApiModelProperty(value = "节点名")
    private String node;

    /**
     * 设备状态
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value = "设备状态")
    private Integer status;

    /**
     * 查询代码
     */
    @TableField(value = "code")
    @ApiModelProperty(value = "查询代码")
    private String code;

    /**
     * 单位
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "单位")
    private String unit;
    /**
     * 设备名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "设备名称")
    private String codeStr;

    /**
     * 设备类型
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "设备类型")
    private int type;

    /**
     * 查询数值
     */
    @TableField(value = "code_value")
    @ApiModelProperty(value = "查询数值")
    private Double codeValue;

    /**
     * 连接时间
     */
    @TableField(value = "connect_time")
    @ApiModelProperty(value = "连接时间")
    private LocalDateTime connectTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_WS_NAME = "ws_name";

    public static final String COL_NODE = "node";

    public static final String COL_STATUS = "status";

    public static final String COL_CODE = "code";

    public static final String COL_CODE_VALUE = "code_value";

    public static final String COL_CONNECT_TIME = "connect_time";


}