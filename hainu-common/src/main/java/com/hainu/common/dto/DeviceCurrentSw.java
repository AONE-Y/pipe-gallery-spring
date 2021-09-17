package com.hainu.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.common.dto
 * @Date：2021/9/17 14:37
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Data
public class DeviceCurrentSw {
    /**
     * 仓端地址
     */
    @ApiModelProperty(value="仓端地址")
    private Integer wsId;

    /**
     * 仓端名
     */
    @ApiModelProperty(value="仓端名")
    private String wsName;

    /**
     * 节点名
     */
    @ApiModelProperty(value="节点名")
    private String node;

    /**
     * 节点状态
     */
    @ApiModelProperty(value="节点状态")
    private Integer status;

    /**
     * 设备温度 0.1℃
     */
    @ApiModelProperty(value="设备温度 0.1℃")
    private Double deviceTemp;

    /**
     * 设备湿度 0.1%RH
     */
    @ApiModelProperty(value="设备湿度 0.1%RH")
    private Double deviceHumi;

    /**
     * 设备液位值 0.01 米
     */
    @ApiModelProperty(value="设备液位值 0.01 米")
    private Double deviceLlv;

    /**
     * 设备可燃气浓度 0.1ppm
     */
    @ApiModelProperty(value="设备可燃气浓度 0.1ppm")
    private Double deviceGas;

    /**
     * 设备氧气浓度 0.1%
     */
    @ApiModelProperty(value="设备氧气浓度 0.1%")
    private Double deviceO2;

    /**
     * 设备烟感控制
     */
    @ApiModelProperty(value="设备烟感控制")
    private Integer deviceSmoke;

    /**
     * 设备照明控制
     */
    @ApiModelProperty(value="设备照明控制")
    private Integer deviceLighting;

    /**
     * 设备水泵控制
     */
    @ApiModelProperty(value="设备水泵控制")
    private Integer deviceWaterpump;

    /**
     * 设备风机控制
     */
    @ApiModelProperty(value="设备风机控制")
    private Integer deviceFan;

    /**
     * 设备红外人体控制
     */
    @ApiModelProperty(value="设备红外人体控制")
    private Integer deviceInfra;

    /**
     * 设备门禁控制
     */

    @ApiModelProperty(value="设备门禁控制")
    private Integer deviceGuard;

    /**
     * 设备信息更新时间
     */
    @ApiModelProperty(value="设备信息更新时间")
    private LocalDateTime updateTime;


    private String changeSw;

    private int changeValue;
}
