package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.entity.CheckGoupAndItem;
import net.zjitc.entity.CheckGroup;
import net.zjitc.entity.CheckItem;
import net.zjitc.mapper.CheckGroupAndItemMapper;
import net.zjitc.mapper.CheckGroupMapper;
import net.zjitc.mapper.CheckItemMapper;
import net.zjitc.service.CheckGroupService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(interfaceClass = CheckGroupService.class)
public class ICheckGroupService extends ServiceImpl<CheckGroupMapper,CheckGroup> implements CheckGroupService {
    @Autowired
    private CheckGroupMapper checkGroupMapper;

    @Autowired
    private CheckGroupAndItemMapper checkGroupAndItemMapper;

    @Autowired
    private CheckItemMapper checkItemMapper;
    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupMapper.insert(checkGroup);
//        获取插入的检查组id
        Integer checkGroupId = checkGroup.getId();
        if(checkitemIds != null && checkitemIds.length >0){
            for (Integer checkItemId:checkitemIds) {
                CheckGoupAndItem item = new CheckGoupAndItem();
                item.setCheckgroupId(checkGroupId);
                item.setCheckitem_id(checkItemId);
                checkGroupAndItemMapper.insert(item);
            }
        }
    }

    /**
     * 查询检查组分页
     * @param pagenum
     * @param pagesize
     * @param query
     * @return
     */
    @Override
    public Page<CheckGroup> findPage(Integer pagenum, Integer pagesize, String query) {
        Page<CheckGroup> page = new Page<>(pagenum, pagesize);
        QueryWrapper<CheckGroup> wrapper = new QueryWrapper<>();
        wrapper.like("code",query).or().like("name",query).or().like("helpCode",query);
        page = checkGroupMapper.selectPage(page, wrapper);
        return page;
    }

    /**
     * 查询检查组分页
     * @param pagenum
     * @param pagesize
     * @return
     */
    @Override
    public Page<CheckGroup> findPage(Integer pagenum, Integer pagesize) {
        Page<CheckGroup> page = new Page<>(pagenum, pagesize);
        page = checkGroupMapper.selectPage(page, null);
        return page;
    }

    /**
     * 根据检查组id查询对应所有的检查项id
     * @param groupId
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer groupId) {
        CheckGroup checkGroup = checkGroupMapper.selectById(groupId);
        QueryWrapper<CheckGoupAndItem> wrapper = new QueryWrapper<>();
        wrapper.eq("checkgroup_id",groupId);
        List<CheckGoupAndItem> checkGoupAndItems = checkGroupAndItemMapper.selectList(wrapper);
        ArrayList<Integer> list = new ArrayList<>();
        for (CheckGoupAndItem c:checkGoupAndItems) {
            list.add(c.getCheckitem_id());
        }
        return list;
    }

    /**
     * 编辑检查组，并且完成检查组与检查项之间关系的更新
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupMapper.updateById(checkGroup);
        Integer groupId = checkGroup.getId();
        QueryWrapper<CheckGoupAndItem> wrapper = new QueryWrapper<>();
        wrapper.eq("checkgroup_id",groupId);
        checkGroupAndItemMapper.delete(wrapper);
        for (int i = 0; i < checkitemIds.length; i++) {
            CheckGoupAndItem item = new CheckGoupAndItem();
            item.setCheckitem_id(checkitemIds[i]);
            item.setCheckgroupId(groupId);
            checkGroupAndItemMapper.insert(item);
        }
    }

    /**
     * 根据编码查询检查组
     * @param code
     * @return
     */
    @Override
    public CheckGroup findByCode(String code) {
        QueryWrapper<CheckGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("code",code);
        CheckGroup checkGroup = checkGroupMapper.selectOne(wrapper);
        return checkGroup;
    }

    /**
     * 根据检查组名称查询
     * @param name
     * @return
     */
    @Override
    public CheckGroup findByName(String name) {
        QueryWrapper<CheckGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("name",name);
        CheckGroup checkGroup = checkGroupMapper.selectOne(wrapper);
        return checkGroup;
    }

    /**
     * 查询所有检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        List<CheckGroup> checkGroups = checkGroupMapper.selectList(null);
        return checkGroups;
    }

    /**
     * 根据检查组id删除检查组
     * @param id
     */
    @Override
    public void deleteByGroupId(Integer id) {
//        根据groupid删除关联表有关检查项id
        QueryWrapper<CheckGoupAndItem> wrapper = new QueryWrapper<>();
        wrapper.eq("checkgroup_id",id);
        checkGroupAndItemMapper.delete(wrapper);
//        删除检查组
        checkGroupMapper.deleteById(id);
    }
}
