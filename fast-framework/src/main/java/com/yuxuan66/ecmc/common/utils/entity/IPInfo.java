package com.yuxuan66.ecmc.common.utils.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IP信息实体类
 *
 * @author Sir丶雨轩
 * @since 2023/9/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IPInfo {

    private String ip;

    /**
     * 与客户端IP地址相关的国家。
     */
    private String country;

    /**
     * 与客户端IP地址相关的地区或区域。
     */
    private String region;

    /**
     * 与客户端IP地址相关的省份。
     */
    private String province;

    /**
     * 与客户端IP地址相关的城市。
     */
    private String city;

    /**
     * 与客户端IP地址相关的互联网服务提供商（ISP）。
     */
    private String isp;

    /**
     * 获取与客户端IP地址相关的国家。如果为0或null，则返回"未知"。
     */
    public String getCountry() {
        return country != null && !country.equals("0") ? country : "未知";
    }

    /**
     * 获取与客户端IP地址相关的地区或区域。如果为0或null，则返回"未知"。
     */
    public String getRegion() {
        return region != null && !region.equals("0") ? region : "未知";
    }

    /**
     * 获取与客户端IP地址相关的省份。如果为0或null，则返回"未知"。
     */
    public String getProvince() {
        return province != null && !province.equals("0") ? province : "未知";
    }

    /**
     * 获取与客户端IP地址相关的城市。如果为0或null，则返回"未知"。
     */
    public String getCity() {
        return city != null && !city.equals("0") ? city : "未知";
    }

    /**
     * 获取与客户端IP地址相关的互联网服务提供商（ISP）。如果为0或null，则返回"未知"。
     */
    public String getIsp() {
        return isp != null && !isp.equals("0") ? isp : "未知";
    }
}
