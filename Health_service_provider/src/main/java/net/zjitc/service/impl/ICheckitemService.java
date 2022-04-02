package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.entity.CheckGoupAndItem;
import net.zjitc.entity.CheckItem;
import net.zjitc.mapper.CheckGroupAndItemMapper;
import net.zjitc.mapper.CheckItemMapper;
import net.zjitc.service.CheckItemService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service(interfaceClass = CheckItemService.class)
public class ICheckitemService extends ServiceImpl<CheckItemMapper,CheckItem> implements CheckItemService {
    @Autowired
    private CheckItemMapper checkItemMapper;

    @Autowired
    private CheckGroupAndItemMapper checkGroupAndItemMapper;
    /**
     * 添加检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemMapper.insert(checkItem);
    }

    /**
     * 分页查询
     */
    @Override
    public Page<CheckItem> findPage(Integer pagenum, Integer pagesize, String itemName) {
        Page<CheckItem> page = new Page<>(pagenum, pagesize);
        if (itemName != null && !itemName.equals("")){
            QueryWrapper<CheckItem> wrapper = new QueryWrapper<>();
            wrapper.like("code",itemName).or().like("name",itemName);
            Page<CheckItem> itemPage = checkItemMapper.selectPage(page, wrapper);
            return itemPage;
        }else {
            Page<CheckItem> itemPage = checkItemMapper.selectPage(page, null);
            return itemPage;
        }
    }

    /**
     * 编辑检查组
     * @param checkItem
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemMapper.updateById(checkItem);
    }

    /**
     * 获取所有检查项
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        List<CheckItem> checkItems = checkItemMapper.selectList(null);
        return checkItems;
    }

    /**
     * 根据code查询检查项
     * @param code
     * @return
     */
    @Override
    public CheckItem findByCode(String code) {
        QueryWrapper<CheckItem> wrapper = new QueryWrapper<>();
        wrapper.eq("code",code);
        CheckItem checkItem = checkItemMapper.selectOne(wrapper);
        return checkItem;
    }

    /**
     * 根据name查询检查项
     * @param name
     * @return
     */
    @Override
    public CheckItem findByName(String name) {
        QueryWrapper<CheckItem> wrapper = new QueryWrapper<>();
        wrapper.eq("name",name);
        CheckItem checkItem = checkItemMapper.selectOne(wrapper);
        return checkItem;
    }

    @Override
    public void deleteById(Integer id) {
//        删除checkitem表中的数据
        checkItemMapper.deleteById(id);
//        删除checkitem与group关联表的数据
        QueryWrapper<CheckGoupAndItem> wrapper = new QueryWrapper<>();
        wrapper.eq("checkitem_id",id);
        checkGroupAndItemMapper.delete(wrapper);
    }


}
