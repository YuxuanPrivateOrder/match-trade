package com.yuxuan66.ecmc.modules.jys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.yuxuan66.ecmc.support.base.BaseMapper;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 委托交易成交表(DmQtOrders)实体类
 *
 * @author makejava
 * @since 2023-09-15 14:10:12
 */
@Data
@TableName("dm_qt_orders")
public class QtOrders extends Model<QtOrders> implements Serializable {

    private Integer id;
    /**
     * 出售ID
     */
    private Integer sellId;
    /**
     * 购买ID
     */
    private Integer buyId;
    /**
     * 出售用户ID
     */
    private Integer sellUid;
    /**
     * 购买用户ID
     */
    private Integer buyUid;
    /**
     * 购票ID
     */
    private Integer tid;
    /**
     * 交易数量
     */
    private Integer num;
    /**
     * 成交价
     */
    private Double reqPrice;
    /**
     * 购买实际价（含手续费）
     */
    private Double buyOriginPrice;
    /**
     * 出售实际价（含手续费）
     */
    private Double sellOriginPrice;
    /**
     * 成交金额
     */
    private Double amount;
    /**
     * 购买手续费
     */
    private Double buySxf;
    /**
     * 出售手续费
     */
    private Double sellSxf;
    /**
     * 成交时间
     */
    private Date createTime;
    /**
     * 成交单号
     */
    private String osn;
    /**
     * 成本价（卖出）
     */
    private Double cbjPrice;
    /**
     * 买家印花税
     */
    private Double buyTaxe;
    /**
     * 卖家印花税
     */
    private Double sellTaxe;


}

