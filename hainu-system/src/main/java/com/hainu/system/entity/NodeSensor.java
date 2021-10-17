package com.hainu.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.entity
 * @Date：2021/10/16 16:45
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@ApiModel(value = "testshardingjdbc.node_sensor")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "testshardingjdbc.node_sensor")
public class NodeSensor implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Integer id;

    @TableField(value = "node")
    @ApiModelProperty(value = "")
    private String node;

    @TableField(value = "sensor")
    @ApiModelProperty(value = "")
    private String sensor;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_NODE = "node";

    public static final String COL_SENSOR = "sensor";
}