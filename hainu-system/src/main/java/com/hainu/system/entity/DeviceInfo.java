package com.hainu.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.entity
 * @Date：2021/11/20 20:47
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description: 
 * @Modified By: ANONE
 */
@ApiModel(value="testshardingjdbc.device_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "testshardingjdbc.device_info")
public class DeviceInfo implements Serializable {
    /**
     * 节点信息id
     */
    @TableId(value = "node_id", type = IdType.AUTO)
    @ApiModelProperty(value="节点信息id")
    private Long nodeId;

    /**
     * 节点名
     */
    @TableField(value = "node")
    @ApiModelProperty(value="节点名")
    private String node;

    /**
     * 设备名
     */
    @TableField(value = "code_str")
    @ApiModelProperty(value="设备名")
    private String codeStr;

    /**
     * 设备代码
     */
    @TableField(value = "code")
    @ApiModelProperty(value="设备代码")
    private String code;

    /**
     * 计算方法，被除数
     */
    @TableField(value = "calculate")
    @ApiModelProperty(value="计算方法，被除数")
    private Integer calculate;

    /**
     * 显示权重，越大显示越高
     */
    @TableField(value = "weight")
    @ApiModelProperty(value="显示权重，越大显示越高")
    private Integer weight;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_NODE_ID = "node_id";

    public static final String COL_NODE = "node";

    public static final String COL_CODE_STR = "code_str";

    public static final String COL_CODE = "code";

    public static final String COL_CALCULATE = "calculate";

    public static final String COL_WEIGHT = "weight";

    public static final String COL_CREATE_TIME = "create_time";
}