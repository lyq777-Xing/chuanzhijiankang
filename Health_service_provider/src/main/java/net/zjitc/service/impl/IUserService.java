package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.zjitc.entity.*;
import net.zjitc.mapper.*;
import net.zjitc.service.UserService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = UserService.class)
@Transactional
public class IUserService implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleAndUserMapper roleAndUserMapper;

    @Autowired
    private RoleAndPermissionMapper roleAndPermissionMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 通过用户名查询对应的用户信息以及角色信息
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
//        先通过用户名查询用户id
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);
        Integer userId = user.getId();
//        通过用户id查询其对应的角色id
        QueryWrapper<RoleAndUser> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id",userId);
        List<RoleAndUser> roleAndUsers = roleAndUserMapper.selectList(wrapper1);
        ArrayList<Role> roles = new ArrayList<>();
        for (RoleAndUser ur:roleAndUsers) {
            Integer roleId = ur.getRole_id();
//            通过role表查询其对应的角色
            Role role = roleMapper.selectById(roleId);
            roles.add(role);
        }
        user.setRoles(roles);
        return user;
    }

    /**
     * 通过userid查询其对应的权限信息
     * @param userid
     * @return
     */
    @Override
    public String getAuthorityInfo(int userid) {
        String authority = "";
//        获取用户的name
        User user = userMapper.selectById(userid);
        String username = user.getUsername();
//        获取角色
        QueryWrapper<RoleAndUser> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id",userid);
        List<RoleAndUser> roleAndUsers = roleAndUserMapper.selectList(wrapper1);
        for (RoleAndUser ur:roleAndUsers) {
            Integer roleId = ur.getRole_id();
//            通过role表查询其对应的角色
            Role role = roleMapper.selectById(roleId);
            if(authority.equals("")){
                authority = authority + role.getKeyword();
            }else {
                authority = authority + "," +role.getKeyword();
            }
//          获取菜单操作权限id
            QueryWrapper<RoleAndPermission> wrapper = new QueryWrapper<>();
            wrapper.eq("role_id",roleId);
            List<RoleAndPermission> roleAndPermissions = roleAndPermissionMapper.selectList(wrapper);
            if(roleAndPermissions != null && roleAndPermissions.size() > 0){
                for (RoleAndPermission rp:roleAndPermissions) {
                    Integer permissionId = rp.getPermission_id();
                    Permission permission = permissionMapper.selectById(permissionId);
                    if(permission != null){
                        if(permission.getKeyword()!=null){
                            if(authority.equals("")){
                                authority = authority + permission.getKeyword();
                            }else {
                                authority = authority + "," + permission.getKeyword();
                            }
                        }
                    }
                }
            }
        }
        return authority;
    }


}
