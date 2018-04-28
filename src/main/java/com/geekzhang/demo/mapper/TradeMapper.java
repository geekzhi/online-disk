package com.geekzhang.demo.mapper;

import com.geekzhang.demo.orm.Trade;

import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/4/24 下午2:50
 * @version: V1.0
 */
public interface TradeMapper  {

    Trade selectByTradeNo(String tradeNo);

    int insert(Trade trade);

    int updateSuccess(Trade trade);
}
