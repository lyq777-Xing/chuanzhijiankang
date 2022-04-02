package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.entity.Menu;
import net.zjitc.mapper.MenuMapper;
import net.zjitc.service.MenuService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(interfaceClass = MenuService.class)
public class IMenuService extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private MenuMapper mapper;

    /**
     * 获取左侧菜单栏
     * @return
     */
    @Override
    public List<Menu> findAll() {
//        寻找第一层
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("level",1);
        List<Menu> menus1 = mapper.selectList(wrapper);
//        查询第二层
        QueryWrapper<Menu> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("level",2);
        List<Menu> menus2 = mapper.selectList(wrapper1);
//        遍历第一层
        for (Menu parent:menus1) {
            ArrayList<Menu> menus = new ArrayList<>();
            for (Menu children:menus2) {
                if(children.getParentMenuId().intValue() == parent.getId().intValue()){
                    menus.add(children);
                }
            }
            parent.setChildren(menus);
        }
        return menus1;
    }
}
