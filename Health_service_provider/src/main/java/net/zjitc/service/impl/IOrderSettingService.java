package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import net.zjitc.entity.OrderSetting;
import net.zjitc.mapper.OrderSettingMapper;
import net.zjitc.service.OrderSettingService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class IOrderSettingService extends ServiceImpl<OrderSettingMapper, OrderSetting> implements OrderSettingService {
    @Autowired
    private OrderSettingMapper orderSettingMapper;

    /**
     * 批量添加到数据库
     * @param list
     */
    @Override
    public void add(List<OrderSetting> list) {
        if(list != null && list.size() > 0){
            for (OrderSetting orderSetting :list) {
//                检查此数据（日期）是否存在
                QueryWrapper<OrderSetting> wrapper = new QueryWrapper<>();
                wrapper.eq("orderDate",orderSetting.getOrderDate());
                OrderSetting orderSettings = orderSettingMapper.selectOne(wrapper);
                if(orderSettings!=null){
                    orderSettingMapper.updateById(orderSetting);
                }else {
                    orderSettingMapper.insert(orderSetting);
                }
            }
        }
    }

    /**
     * 根据日期查询预约设置数据
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String dateBegin = date +"-1";
        String dateEnd = date +"-31";
/*        HashMap<Object, Object> map = new HashMap<>();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);*/
        QueryWrapper<OrderSetting> wrapper = new QueryWrapper<>();
        wrapper.between("orderDate",dateBegin,dateEnd);
        List<OrderSetting> orderSettings = orderSettingMapper.selectList(wrapper);
        ArrayList<Map> maps = new ArrayList<>();
        for (OrderSetting ordersetting:orderSettings) {
            HashMap<Object, Object> map = new HashMap<>();
//            获得日期
            map.put("data",ordersetting.getOrderDate().getDate());
//            可预约人数
            map.put("number",ordersetting.getNumber());
//            已预约人数
            map.put("reservations",ordersetting.getReservations());
            maps.add(map);
        }
        return maps;
    }

    /**
     * 根据日期修改可预约人数
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        QueryWrapper<OrderSetting> wrapper = new QueryWrapper<>();
        wrapper.eq("orderDate",orderSetting.getOrderDate());
        OrderSetting selectOne = orderSettingMapper.selectOne(wrapper);
        if(selectOne != null){
//            表明当前日期已经进行了预约设置，进行修改操作
            orderSettingMapper.updateById(orderSetting);
        }else {
//            表明当前日期未进行预约设置，进行添加操作
            orderSettingMapper.insert(orderSetting);
        }
    }

    /**
     * 根据日期查询是否课预约
     * @param date
     * @return
     */
    @Override
    public OrderSetting findByDate(Date date) {
        QueryWrapper<OrderSetting> wrapper = new QueryWrapper<>();
        wrapper.eq("orderDate",date);
        OrderSetting orderSetting = orderSettingMapper.selectOne(wrapper);
        return orderSetting;
    }
}
