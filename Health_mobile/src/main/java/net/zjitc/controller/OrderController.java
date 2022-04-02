package net.zjitc.controller;

import net.zjitc.common.MessageConstant;
import net.zjitc.common.Result;
import net.zjitc.constant.RedisMessageConstant;
import net.zjitc.entity.*;
import net.zjitc.service.MemberService;
import net.zjitc.service.OrderService;
import net.zjitc.service.OrderSettingService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderSettingService orderSettingService;

    @Reference
    private MemberService memberService;

    @Reference
    private OrderService orderService;

    /**
     * 提交验证表单信息
     * @param orderInfo
     * @return
     */
    @PostMapping("/submit")
    public Result Submit(@RequestBody OrderInfo orderInfo){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Order order = new Order();
        Member member = new Member();
        try{
            Date parse = dateFormat.parse(orderInfo.getDate());
//            判断验证码是否正确
            String telephone = orderInfo.getTelephone();
            String jedisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            String code = orderInfo.getValidateCode();
            if(code.equals(jedisCode) && code != null && jedisCode != null){
//                验证码校验成功
//                根据日期查询可预约人数
                OrderSetting orderSetting = orderSettingService.findByDate(parse);
                if(orderSetting == null){
                    return new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
                }
//                获取可预约人数
                int number = orderSetting.getNumber();
//                获取已预约人数
                int reservations = orderSetting.getReservations();
                if(reservations >= number){
//                    表明预约已经满了 不可以预约
                    return new Result(false,MessageConstant.ORDER_FULL);
                }
//                根据电话判断当前用户是否为会员用户
                member =  memberService.findByPhone(orderInfo.getTelephone());
                if (member != null){
//                    是会员，判断是否已经预约过该套餐
                    Integer id = member.getId();
                    List<Order> orderList = orderService.findByMemberId(id);
                    if(orderList != null && orderList.size()>0){
                        for (Order o:orderList) {
                            Integer setmealId = o.getSetmealId();
                            if(setmealId.equals(orderInfo.getSetmealId())){
//                                表明已经预约过了
                                return new Result(false,MessageConstant.HAS_ORDERED);
                            }
                        }
                    }
                }

//                表示可以预约
                reservations = reservations + 1;
                orderSetting.setReservations(reservations);
//                更新预约表的数据
                orderSettingService.editNumberByDate(orderSetting);
                Member member1 = new Member();
                if(member == null){
                    member1 = new Member();
//                    表示还不是会员，将其注册为会员
                    member1.setIdCard(orderInfo.getIdCard());
                    member1.setName(orderInfo.getName());
                    member1.setPhoneNumber(orderInfo.getTelephone());
                    member1.setSex(orderInfo.getSex());
                    member1.setRegTime(new Date());
                    memberService.addMember(member1);
                }
//                插入order表
//                通过phone获取memberid
                Member byPhone = memberService.findByPhone(member1.getPhoneNumber());
                order.setOrderDate(parse);
                order.setOrderType(Order.ORDERTYPE_WEIXIN);
                order.setMemberId(byPhone.getId());
                order.setSetmealId(orderInfo.getSetmealId());
                order.setOrderStatus(Order.ORDERSTATUS_NO);
                order = orderService.addOrder(order);
            }else {
//                验证码校验不成功
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception e){
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    /**
     * 根据orderId查询预约信息
     * @param id
     * @return
     */
    @GetMapping("/findbyid")
    public Result findById(Integer id){
        try{
            OrderSuccess orderSuccess = orderService.findById(id);
            if(orderSuccess != null){
                return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,orderSuccess);
            }else {
                return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
            }
        }catch (Exception e){
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }
}
