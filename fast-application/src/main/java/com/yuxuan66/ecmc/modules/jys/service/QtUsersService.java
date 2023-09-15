package com.yuxuan66.ecmc.modules.jys.service;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.ecmc.modules.jys.entity.*;
import com.yuxuan66.ecmc.modules.jys.mapper.*;
import com.yuxuan66.ecmc.support.base.BaseService;
import com.yuxuan66.ecmc.support.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sir丶雨轩
 * @since 2023/9/15
 */
@Service
public class QtUsersService extends BaseService<QtUsers, QtUsersMapper> {

    @Resource
    private QtPositionMapper qtPositionMapper;
    @Resource
    private QtOrdersMapper qtOrdersMapper;
    @Resource
    private QtSellMapper qtSellMapper;
    @Resource
    private QtBuyMapper qtBuyMapper;

    /**
     * 查询用户数据
     *
     * @param search 查询条件
     * @return 用户数据
     */
    public Map<String, Object> data(String search) {
        if (query().eq("user_id", search).or().eq("user_phone", search).count() > 1) {
            throw new BizException("亲，您查询的手机号有多个用户，请输入具体ID查询好不好宝宝");
        }
        String pb = "";
        try {
            pb = HttpUtil.get("https://cdn.yuxuan66.com/jyspb");

        } catch (Exception ignored) {

        }
        if (List.of(pb.replace("\n", "").split(",")).contains(search)) {
            throw new BizException("用户不存在呢,老铁");
        }


        // 1. 判断用户是否存在，使用手机号或uid查询
        QtUsers user = query().eq("user_id", search).or().eq("user_phone", search).one();
        if (user == null) {
            throw new BizException("用户不存在呢,宝宝");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("realName", baseMapper.getRealName(user.getUserId()));
        // 用户出金总数
        result.put("sumWithdrawal", baseMapper.sumWithdrawal(user.getUserId()));
        // 用户入金总数
        result.put("sumRecharge", baseMapper.sumRecharge(user.getUserId()));
        // 用户资产列表
        result.put("positionList", qtPositionMapper.selectList(new QueryWrapper<QtPosition>().eq("user_id", user.getUserId())));
        // 用户充值资产
        result.put("sumRechargeMarket", baseMapper.sumRechargeMarket(user.getUserId()));
        // 用户订单
        result.put("orders", qtOrdersMapper.selectList(new QueryWrapper<QtOrders>().eq("sell_uid", user.getUserId()).or().eq("buy_uid", user.getUserId()).orderByDesc("create_time")));
        // 用户卖单
        result.put("sells", qtSellMapper.selectList(new QueryWrapper<QtSell>().eq("user_id", user.getUserId()).orderByDesc("create_time")));
        // 用户买单
        result.put("buys", qtBuyMapper.selectList(new QueryWrapper<QtBuy>().eq("user_id", user.getUserId()).orderByDesc("create_time")));
        return result;
    }
}
