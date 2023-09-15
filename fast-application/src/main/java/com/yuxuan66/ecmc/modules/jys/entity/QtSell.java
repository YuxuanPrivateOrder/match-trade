package com.yuxuan66.ecmc.modules.jys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 委托买入表(DmQtSell)实体类
 *
 * @author makejava
 * @since 2023-09-15 14:10:41
 */
@Data
@TableName("dm_qt_sell")
public class QtSell extends Model<QtSell> implements Serializable {

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
     * 委托买入数量
     */
    private Integer total;
/**
     * 成交数量
     */
    private Integer done;
/**
     * 状态 0：买委托 1：已撤单 2：买成功 3：部分成交 4:已撤单，部分成交
     */
    private Integer status;
/**
     * 委托时间
     */
    private Date createTime;
/**
     * 委托单号
     */
    private String osn;
/**
     * 委托价格
     */
    private Double reqPrice;
/**
     * 委托手续费
     */
    private Double sxfPrice;
/**
     * 订单取消时间
     */
    private Date cancelTime;
/**
     * 更新时间
     */
    private Date updateTime;
/**
     * 剩余数量
     */
    private Integer surplus;
/**
     * 类型 1：限价 2：市价
     */
    private Integer type;
/**
     * 数据类型：2:卖单（无需设置）
     */
    private Integer mode;
/**
     * 印花税
     */
    private Double taxes;
/**
     * 印花税总额
     */
    private Double taxesTotal;
/**
     * 当前印花税
     */
    private Double taxesNow;
/**
     * 当前手续费
     */
    private Double sxfNow;
/**
     * 手续费总额
     */
    private Double sxfTotal;


}
