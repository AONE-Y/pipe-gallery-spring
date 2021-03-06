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
 * @Date：2021/9/11 16:03
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@ApiModel(value = "com-hainu-system-entity-DeviceLog")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "testshardingjdbc.device_log")
public class DeviceLog implements Serializable {
    /**
     * 仓端id
     */
    @TableId(value = "ws_id", type = IdType.AUTO)
    @ApiModelProperty(value = "仓端id")
    private Long wsId;

    /**
     * 仓段名称
     */
    @TableField(value = "ws_name")
    @ApiModelProperty(value = "仓段名称")
    private String wsName;

    /**
     * 节点名
     */
    @TableField(value = "node")
    @ApiModelProperty(value = "节点名")
    private String node;

    /**
     * 设备温度 0.1℃
     */
    @TableField(value = "device_temp")
    @ApiModelProperty(value = "设备温度 0.1℃")
    private Double deviceTemp;

    /**
     * 设备湿度 0.1%RH
     */
    @TableField(value = "device_humi")
    @ApiModelProperty(value = "设备湿度 0.1%RH")
    private Double deviceHumi;

    /**
     * 设备液位值 0.01 米
     */
    @TableField(value = "device_llv")
    @ApiModelProperty(value = "设备液位值 0.01 米")
    private Double deviceLlv;

    /**
     * 设备可燃气浓度 0.1ppm
     */
    @TableField(value = "device_gas")
    @ApiModelProperty(value = "设备可燃气浓度 0.1ppm")
    private Double deviceGas;

    /**
     * 设备氧气浓度 0.1%
     */
    @TableField(value = "device_O2")
    @ApiModelProperty(value = "设备氧气浓度 0.1%")
    private Double deviceO2;

    /**
     * 设备烟感控制
     */
    @TableField(value = "device_smoke")
    @ApiModelProperty(value = "设备烟感控制")
    private Integer deviceSmoke;

    /**
     * 设备照明控制
     */
    @TableField(value = "device_lighting")
    @ApiModelProperty(value = "设备照明控制")
    private Integer deviceLighting;

    /**
     * 设备水泵控制
     */
    @TableField(value = "device_waterpump")
    @ApiModelProperty(value = "设备水泵控制")
    private Integer deviceWaterpump;

    /**
     * 设备风机控制
     */
    @TableField(value = "device_fan")
    @ApiModelProperty(value = "设备风机控制")
    private Integer deviceFan;

    /**
     * 设备红外人体控制
     */
    @TableField(value = "device_infra")
    @ApiModelProperty(value = "设备红外人体控制")
    private Integer deviceInfra;

    /**
     * 设备门禁控制
     */
    @TableField(value = "device_guard")
    @ApiModelProperty(value = "设备门禁控制")
    private Integer deviceGuard;

    /**
     * 设备信息创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "设备信息创建时间")
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_WS_ID = "ws_id";

    public static final String COL_WS_NAME = "ws_name";

    public static final String COL_NODE = "node";

    public static final String COL_DEVICE_TEMP = "device_temp";

    public static final String COL_DEVICE_HUMI = "device_humi";

    public static final String COL_DEVICE_LLV = "device_llv";

    public static final String COL_DEVICE_GAS = "device_gas";

    public static final String COL_DEVICE_O2 = "device_O2";

    public static final String COL_DEVICE_SMOKE = "device_smoke";

    public static final String COL_DEVICE_LIGHTING = "device_lighting";

    public static final String COL_DEVICE_WATERPUMP = "device_waterpump";

    public static final String COL_DEVICE_FAN = "device_fan";

    public static final String COL_DEVICE_INFRA = "device_infra";

    public static final String COL_DEVICE_GUARD = "device_guard";

    public static final String COL_CREATE_TIME = "create_time";
}