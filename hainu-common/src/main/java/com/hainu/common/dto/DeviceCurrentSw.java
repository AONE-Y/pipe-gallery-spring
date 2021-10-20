package com.hainu.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
     * 仓端名
     */
    @ApiModelProperty(value="仓端名")
    private String wsName;

    /**
     * 节点名
     */
    @ApiModelProperty(value="节点名")
    private String node;


    private String changeSw;

    private int changeValue;
}
