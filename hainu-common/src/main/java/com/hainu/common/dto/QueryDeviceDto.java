package com.hainu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.common.dto
 * @Date：2021/9/13 20:03
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryDeviceDto {
    private String wsName;

    private String node;

    private String sw;

    private String measure;

    private Integer start;

    @Value("${123}")
    private Integer end;
}
