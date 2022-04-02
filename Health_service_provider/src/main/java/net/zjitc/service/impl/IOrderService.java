package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.entity.Member;
import net.zjitc.entity.Order;
import net.zjitc.entity.OrderSuccess;
import net.zjitc.entity.Setmeal;
import net.zjitc.mapper.MemberMapper;
import net.zjitc.mapper.OrderMapper;
import net.zjitc.mapper.SetmealMapper;
import net.zjitc.service.OrderService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = OrderService.class)
@Transactional
public class IOrderService extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 根据会员id查询该会员预约的检查套餐
     * @param memberId
     * @return
     */
    @Override
    public List<Order> findByMemberId(Integer memberId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id",memberId);
        List<Order> orders = orderMapper.selectList(wrapper);
        return orders;
    }

    /**
     * 添加order表
     * @param order
     */
    @Override
    public Order addOrder(Order order) {
        orderMapper.insert(order);
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id",order.getMemberId())
                .eq("orderDate",order.getOrderDate())
                .eq("orderType",order.getOrderType())
                .eq("orderStatus",order.getOrderStatus())
                .eq("setmeal_id",order.getSetmealId());
        Order one = orderMapper.selectOne(wrapper);
        return one;
    }

    /**
     * 根据OrderId查询
     * @param orderId
     * @return
     */
    @Override
    public OrderSuccess findById(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        Integer memberId = order.getMemberId();
        Member member = memberMapper.selectById(memberId);
        Integer setmealId = order.getSetmealId();
        Setmeal setmeal = setmealMapper.selectById(setmealId);
        OrderSuccess orderSuccess = new OrderSuccess();
        orderSuccess.setOrderDate(order.getOrderDate());
        orderSuccess.setOrderType(order.getOrderType());
        orderSuccess.setName(member.getName());
        orderSuccess.setSetmeal(setmeal.getName());
        return orderSuccess;
    }
}
