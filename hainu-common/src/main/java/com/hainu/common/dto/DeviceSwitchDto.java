package com.hainu.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.common.dto
 * @Date：2021/9/12 15:30
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Builder
@Data
public class DeviceSwitchDto {

    /**
     * 客户端名
     */
    private String clientId;
    /**
     * 节点名
     */
    @ApiModelProperty(value = "节点名")
    private String node;
    /**
     * 设备烟感控制
     */
    @ApiModelProperty(value = "设备烟感控制")
    private Integer smoke;

    /**
     * 设备照明控制
     */

    @ApiModelProperty(value = "设备照明控制")
    private Integer lighting;

    /**
     * 设备水泵控制
     */

    @ApiModelProperty(value = "设备水泵控制")
    private Integer waterpump;

    /**
     * 设备风机控制
     */

    @ApiModelProperty(value = "设备风机控制")
    private Integer fan;

    /**
     * 设备红外人体控制
     */

    @ApiModelProperty(value = "设备红外人体控制")
    private Integer infra;

    /**
     * 设备门禁控制
     */

    @ApiModelProperty(value = "设备门禁控制")
    private Integer guard;
}
