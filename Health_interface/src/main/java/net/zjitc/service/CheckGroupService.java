package net.zjitc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.zjitc.entity.CheckGoupAndItem;
import net.zjitc.entity.CheckGroup;
import net.zjitc.entity.CheckItem;

import java.util.List;

public interface CheckGroupService extends IService<CheckGroup> {
    public void add(CheckGroup checkGroup,Integer[] checkitemIds);
    public Page<CheckGroup> findPage(Integer pagenum,Integer pagesize,String query);
    public Page<CheckGroup> findPage(Integer pagenum,Integer pagesize);
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer groupId);
    public void edit(CheckGroup checkGroup,Integer[] checkitemIds);
    CheckGroup findByCode(String code);
    CheckGroup findByName(String name);
    List<CheckGroup> findAll();
}
