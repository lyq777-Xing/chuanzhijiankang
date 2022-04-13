package net.zjitc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.zjitc.entity.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingService extends IService<OrderSetting> {
    public void add(List<OrderSetting> list);

    public List<Map> getOrderSettingByMonth(String date);
    //参数格式为：2019-03

    public void editNumberByDate(OrderSetting orderSetting);

    public OrderSetting findByDate(Date date);

    public List<OrderSetting> findAll();
}
