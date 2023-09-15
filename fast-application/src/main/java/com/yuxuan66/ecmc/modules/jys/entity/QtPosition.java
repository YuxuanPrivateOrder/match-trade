package com.yuxuan66.ecmc.modules.jys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 用户持仓(DmQtPosition)实体类
 *
 * @author makejava
 * @since 2023-09-15 12:51:39
 */
@Data
@TableName("dm_qt_position")
public class QtPosition extends Model<QtPosition> implements Serializable {

    private Integer id;
/**
     * 用户ID
     */
    private Integer userId;
/**
     * 股票ID
     */
    private Integer tid;
/**
     * 持仓总数
     */
    private Integer total;
/**
     * 卖出冻结
     */
    private Integer frozen;
/**
     * 创建时间
     */
    private Date createTime;
/**
     * 更新时间
     */
    private Date updateTime;
/**
     * 成本价
     */
    private Double reqPrice;



}

