package com.hainu.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.entity
 * @Date：2021/9/11 15:07
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@ApiModel(value = "com-hainu-system-entity-DeviceList")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "testshardingjdbc.device_list")
public class DeviceList implements Serializable {

    /**
     * 话题辨识id
     */
    @TableId(value = "topic_id", type = IdType.AUTO)
    @ApiModelProperty(value = "话题辨识id")
    private Long topicId;

    /**
     * 设备订阅话题
     */
    @TableField(value = "ws_topic")
    @ApiModelProperty(value = "设备订阅话题")
    private String wsTopic;

    /**
     * 设备名称
     */
    @TableField(value = "ws_name")
    @ApiModelProperty(value = "设备名称")
    private String wsName;

    private static final long serialVersionUID = 1L;

    public static final String COL_TOPIC_ID = "topic_id";

    public static final String COL_WS_TOPIC = "ws_topic";

    public static final String COL_WS_NAME = "ws_name";
}