package com.hainu.common.dto;

import cn.hutool.system.oshi.CpuInfo;
import lombok.Data;
import oshi.software.common.AbstractOSFileStore;

import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.common.dto
 * @Date：2021/8/27 17:25
 * @Author：yy188
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: yy188
 */

@Data
public class SysInfoDto {
    private double[] loadAverage;
    private CpuInfo cpuInfo;
    private String[] globalMemoryInfo;
    private List<AbstractOSFileStore> hardDiskInfo;
}
