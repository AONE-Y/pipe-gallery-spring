package com.hainu.controller.device;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hainu.common.constant.DeviceConst;
import com.hainu.common.lang.Result;
import com.hainu.system.config.netty2.handler.ResponseHandler;
import com.hainu.system.dto.DeviceInfoDto;
import com.hainu.system.entity.DeviceData;
import com.hainu.system.entity.DeviceInfo;
import com.hainu.system.service.DeviceDataService;
import com.hainu.system.service.DeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private DeviceInfoService deviceInfoService;

    @Autowired
    private DeviceDataService deviceDataService;

    @GetMapping("/get")
    public Result<?> getDeviceData(@RequestParam(value = "wsName",required = false) String wsName) {
        if (wsName == null){
            return new Result<>().success().put(null);
        }
        if (wsName== DeviceConst.defaultWsName) {
            wsName= ResponseHandler.firstIp;
        }

        List<DeviceData> deviceData = deviceDataService.getDeviceData();
        String finalWsName = wsName;
        Map<Integer, List<DeviceData>> groupData = deviceData.stream().filter((e)->
                        finalWsName.equals(e.getWsName())|| ObjectUtil.isNull(e.getWsName()))
                .collect(Collectors.groupingBy(DeviceData::getType));
        DeviceInfoDto deviceInfoDto = DeviceInfoDto.builder()
                .wsName(wsName).queries(groupData.get(0))
                .cmds(groupData.get(1)).build();
        return new Result<>().success().put(deviceInfoDto);
    }

    @GetMapping("/getSensor")
    public Result<?>  getSensor(){
        QueryWrapper<DeviceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DeviceInfo.COL_WEIGHT);
        List<DeviceInfo> deviceInfos = deviceInfoService.list(queryWrapper);
        return new Result<>().success().put(deviceInfos);
    }

}
