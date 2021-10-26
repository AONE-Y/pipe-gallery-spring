package com.hainu.system.entity;

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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRes implements Serializable {
    /**
     * 主键
     */

    @ApiModelProperty(value = "主键")
    private Integer id;

    /**
     * 仓端地址
     */

    @ApiModelProperty(value = "仓端地址")
    private String wsName;

    /**
     * 节点名
     */

    @ApiModelProperty(value = "节点名")
    private String node;

    /**
     * 数据类型
     */

    @ApiModelProperty(value = "数据类型")
    private String codeType;

    /**
     * 数据代码
     */

    @ApiModelProperty(value = "数据代码")
    private String code;

    /**
     * 数据值
     */

    @ApiModelProperty(value = "数据值")
    private Double codeValue;


}