package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.entity.Member;
import net.zjitc.entity.Order;
import net.zjitc.mapper.MemberMapper;
import net.zjitc.mapper.MenuMapper;
import net.zjitc.mapper.OrderMapper;
import net.zjitc.service.MemberService;
import net.zjitc.utils.DateUtils;
import net.zjitc.utils.MD5Utils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MemberService.class)
@Transactional
public class IMemberService extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 根据电话查询是否有该会员的信息
     * @param phone
     * @return
     */
    @Override
    public Member findByPhone(String phone) {
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("phoneNumber",phone);
        Member member = memberMapper.selectOne(wrapper);
        return member;
    }

    /**
     * 添加用户为会员
     * @param member
     */
    @Override
    public Member addMember(Member member) {
//        如果密码不为空则进行加密处理
        if(member.getPassword() != null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberMapper.insert(member);
        return member;
    }

    /**
     * 根据日期同级会员数量
     * @param month
     * @return
     */
    @Override
    public List<Integer> findCountMemberByMonth(List<String> month) {
        ArrayList<Integer> list = new ArrayList<>();
        for (String m:month) {
            String s = m +"-28";
            String d = m +"-1";
            QueryWrapper<Member> wrapper = new QueryWrapper<>();
            wrapper.lt("regTime",s).ge("regTime",d);
            List<Member> members = memberMapper.selectList(wrapper);
            int size = members.size();
            list.add(size);
        }
        return list;
    }

    /**
     * 获取运营数据
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
//        获取当前日期
        String today = DateUtils.parseDate2String(DateUtils.getToday());
//        获取本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
//        获取本月第一天的日期
        String firstDayThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
//        获取今日新增会员数量
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("regTime",today);
        List<Member> members = memberMapper.selectList(wrapper);
        int todayNewMember = members.size();
//        获取总会员数
        List<Member> memberList = memberMapper.selectList(null);
        int totalMember = memberList.size();
//        获取本周新增会员数
        QueryWrapper<Member> wrapper1 = new QueryWrapper<>();
        wrapper1.lt("regTime",today).ge("regTime",thisWeekMonday);
        List<Member> members1 = memberMapper.selectList(wrapper1);
        int thisWeekNewMember = members1.size();
//        本月新增会员数
        QueryWrapper<Member> wrapper2 = new QueryWrapper<>();
        wrapper2.lt("regTime",today).ge("regTime",thisWeekMonday);
        List<Member> members2 = memberMapper.selectList(wrapper2);
        int thisMonthNewMember = members2.size();
//        获取今日预约数
        QueryWrapper<Order> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("orderDate",today);
        List<Order> orders = orderMapper.selectList(wrapper3);
        int todayOrderNumber = orders.size();
//        获取本周预约数
        QueryWrapper<Order> wrapper4 = new QueryWrapper<>();
        wrapper4.lt("orderDate",today).ge("orderDate",thisWeekMonday);
        List<Order> orders1 = orderMapper.selectList(wrapper4);
        int thisWeekOrderNumber = orders1.size();
//        本月预约数
        QueryWrapper<Order> wrapper5 = new QueryWrapper<>();
        wrapper5.lt("orderDate",today).ge("orderDate",firstDayThisMonth);
        List<Order> orders2 = orderMapper.selectList(wrapper5);
        Integer thisMonthOrderNumber = orders2.size();
//        今日到诊数
        QueryWrapper<Order> wrapper6 = new QueryWrapper<>();
        wrapper6.eq("orderDate",today).eq("orderStatus","已到诊");
        List<Order> orders3 = orderMapper.selectList(wrapper6);
        Integer todayVisitsNumber = orders3.size();
//        本周到诊数
        QueryWrapper<Order> wrapper7 = new QueryWrapper<>();
        wrapper7.lt("orderDate",today).ge("orderDate",thisWeekMonday).eq("orderStatus","已到诊");
        List<Order> orders4 = orderMapper.selectList(wrapper7);
        Integer thisWeekVisitsNumber = orders4.size();
//        本月到诊数
        QueryWrapper<Order> wrapper8 = new QueryWrapper<>();
        wrapper8.lt("orderDate",today).ge("orderDate",firstDayThisMonth).eq("orderStatus","已到诊");
        List<Order> orders5 = orderMapper.selectList(wrapper8);
        Integer thisMonthVisitsNumber = orders5.size();
//        热门套餐（取前4）
        List<Map> hotSetmeal = null;
        HashMap<String, Object> result = new HashMap<>();
        result.put("reportDate",today);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotSetmeal",hotSetmeal);
        return result;
    }
}
