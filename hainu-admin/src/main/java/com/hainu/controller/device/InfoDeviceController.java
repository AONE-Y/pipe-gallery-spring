package com.hainu.controller.device;

import com.hainu.common.lang.Result;
import com.hainu.system.dto.DeviceInfoDto;
import com.hainu.system.entity.DeviceCmd;
import com.hainu.system.entity.DeviceQuery;
import com.hainu.system.service.DeviceCmdService;
import com.hainu.system.service.DeviceInfoService;
import com.hainu.system.service.DeviceQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.controller.device
 * @Date：2021/11/20 20:48
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@RestController
@RequestMapping("/device/info")
public class InfoDeviceController {
    @Autowired
    private DeviceQueryService deviceQueryService;
    @Autowired
    private DeviceCmdService deviceCmdService;
    @Autowired
    private DeviceInfoService deviceInfoService;

    @GetMapping("/get")
    public Result<?> getDeviceInfo(String wsName) {
        List<DeviceQuery> deviceQueryInfoes = deviceQueryService.getDeviceQueryInfo(wsName);
        List<DeviceCmd> deviceCmdInfoes = deviceCmdService.getDeviceCmdInfo(wsName);
        DeviceInfoDto deviceInfoDto = DeviceInfoDto.builder().wsName(wsName).queries(deviceQueryInfoes).cmds(deviceCmdInfoes).build();
        return new Result<>().success().put(deviceInfoDto);
    }
}
