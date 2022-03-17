package net.zjitc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.zjitc.entity.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<Users> {
}
