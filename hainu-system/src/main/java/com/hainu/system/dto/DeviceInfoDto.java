package com.hainu.system.dto;

import com.hainu.system.entity.DeviceCmd;
import com.hainu.system.entity.DeviceQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.dto
 * @Date：2021/11/20 21:35
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceInfoDto {
    private String wsName;
    private List<DeviceQuery> queries;
    private List<DeviceCmd> cmds;
}
