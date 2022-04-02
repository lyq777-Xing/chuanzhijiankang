package net.zjitc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.zjitc.entity.Member;

import java.util.List;
import java.util.Map;

public interface MemberService extends IService<Member> {
    Member findByPhone(String phone);
    Member addMember(Member member);
    List<Integer> findCountMemberByMonth(List<String> month);
    Map<String,Object> getBusinessReportData() throws Exception;

}
