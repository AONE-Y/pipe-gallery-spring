package com.hainu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.common.dto
 * @Date：2021/10/16 16:48
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorNodeRes {
    private String node;
    private List<String> sensors;
}
