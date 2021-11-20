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
 * @Date：2021/11/20 20:47
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description: 
 * @Modified By: ANONE
 */
@ApiModel(value="testshardingjdbc.device_query")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "testshardingjdbc.device_query")
public class DeviceQuery implements Serializable {
    /**
     * 查询id
     */
    @TableId(value = "query_id", type = IdType.AUTO)
    @ApiModelProperty(value="查询id")
    private Long queryId;

    /**
     * 仓端名
     */
    @TableField(value = "ws_name")
    @ApiModelProperty(value="仓端名")
    private String wsName;

    /**
     * 节点名
     */
    @TableField(value = "node")
    @ApiModelProperty(value="节点名")
    private String node;

    /**
     * 查询代码
     */
    @TableField(value = "code")
    @ApiModelProperty(value="查询代码")
    private String code;

    /**
     * 查询数值
     */
    @TableField(value = "code_value")
    @ApiModelProperty(value="查询数值")
    private Double codeValue;
    /**
     * 传感器名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value="传感器名称")
    private String codeStr;
    /**
     * 连接时间
     */
    @TableField(value = "connect_time")
    @ApiModelProperty(value="连接时间")
    private LocalDateTime connectTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_QUERY_ID = "query_id";

    public static final String COL_WS_NAME = "ws_name";

    public static final String COL_NODE = "node";

    public static final String COL_CODE = "code";

    public static final String COL_CODE_VALUE = "code_value";

    public static final String COL_CONNECT_TIME = "connect_time";
}