package com.hainu.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.dto
 * @Date：2021/11/30 14:30
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DeviceBaseDto {
    private String wsName;
    private String node;
    private String code;
    private int type;
    private String onOrOff;
}
