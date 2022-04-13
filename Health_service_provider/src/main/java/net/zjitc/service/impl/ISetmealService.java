package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.constant.RedisConstant;
import net.zjitc.entity.*;
import net.zjitc.mapper.*;
import net.zjitc.service.SetmealService;
import net.zjitc.utils.DateUtils;
import net.zjitc.utils.RedisUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@Service(interfaceClass = SetmealService.class)
public class ISetmealService extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private SetmealAndCheckgroupMapper setmealAndCheckgroupMapper;

    @Autowired
    private CheckGroupMapper checkGroupMapper;

    @Autowired
    private CheckItemMapper checkItemMapper;

    @Autowired
    private CheckGroupAndItemMapper checkGroupAndItemMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 新增套餐 同时需要关联检查组
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealMapper.insert(setmeal);
        Integer setmealId = setmeal.getId();
        if(checkgroupIds != null && checkgroupIds.length > 0){
            for (int i = 0; i < checkgroupIds.length; i++) {
                SetmealAndCheckgroup setmealAndCheckgroup = new SetmealAndCheckgroup();
                setmealAndCheckgroup.setSetmeal_id(setmealId);
                setmealAndCheckgroup.setCheckgroup_id(checkgroupIds[i]);
                setmealAndCheckgroupMapper.insert(setmealAndCheckgroup);
            }
        }
//        将图片保存到redis集合中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
    }

    /**
     * 查询分页
     * @param pagenum
     * @param pagesize
     * @param query
     * @return
     */
    @Override
    public Page<Setmeal> findPage(Integer pagenum, Integer pagesize, String query) {
        Page<Setmeal> page = new Page<>(pagenum, pagesize);
        Page<Setmeal> setmealPage = null;
        if(query == null || query.equals("")){
            setmealPage = setmealMapper.selectPage(page, null);
            return setmealPage;
        }else {
            QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
            wrapper.like("code",query).or().like("name",query).or().like("helpCode",query);
            setmealPage = setmealMapper.selectPage(page,wrapper);
            return setmealPage;
        }
    }

    /**
     * 根据code查询
     * @param code
     * @return
     */
    @Override
    public Setmeal findByCode(String code) {
        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        wrapper.eq("code",code);
        Setmeal setmeal = setmealMapper.selectOne(wrapper);
        return setmeal;
    }

    /**
     * 根据name查询
     * @param name
     * @return
     */
    @Override
    public Setmeal findByName(String name) {
        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        wrapper.eq("name",name);
        Setmeal setmeal = setmealMapper.selectOne(wrapper);
        return setmeal;
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> setmeals = setmealMapper.selectList(null);
        return setmeals;
    }

    /**
     * 根据id查询套餐以及group和item
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        QueryWrapper<SetmealAndCheckgroup> wrapper = new QueryWrapper<>();
        wrapper.eq("setmeal_id",id);
        List<SetmealAndCheckgroup> setmealAndCheckgroups = setmealAndCheckgroupMapper.selectList(wrapper);
        if(setmealAndCheckgroups.size() == 0){

        }else {
            ArrayList<CheckGroup> checkGroups = new ArrayList<>();
            ArrayList<Integer> list = new ArrayList<>();
            for (SetmealAndCheckgroup sc:setmealAndCheckgroups) {
                Integer checkgroup_id = sc.getCheckgroup_id();
                list.add(checkgroup_id);
                CheckGroup checkGroup = checkGroupMapper.selectById(checkgroup_id);
//            通过groupid获取itemid
                QueryWrapper<CheckGoupAndItem> wrapper1 = new QueryWrapper<>();
                wrapper1.eq("checkgroup_id",checkgroup_id);
                List<CheckGoupAndItem> checkGoupAndItems = checkGroupAndItemMapper.selectList(wrapper1);
                ArrayList<CheckItem> checkItems  = new ArrayList<>();
                if(checkGoupAndItems.size()==0){

                }else {
                    for (CheckGoupAndItem ci:checkGoupAndItems) {
                        Integer checkitem_id = ci.getCheckitem_id();
                        CheckItem checkItem = checkItemMapper.selectById(checkitem_id);
                        checkItems.add(checkItem);
                    }
                }
                checkGroup.setCheckItems(checkItems);
                checkGroups.add(checkGroup);
            }
            setmeal.setCheckgroupids(list);
            setmeal.setCheckGroups(checkGroups);
        }
        return setmeal;
    }

    /**
     * 根据套餐获取对应套餐数量
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return orderMapper.findSetmealCount();
    }

    /**
     * 根据套餐信息以及对应检查组关联表
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @Override
    public Setmeal updateSetmeal(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealMapper.updateById(setmeal);
        QueryWrapper<SetmealAndCheckgroup> wrapper = new QueryWrapper<>();
        wrapper.eq("setmeal_id",setmeal.getId());
        ArrayList<CheckGroup> list = new ArrayList<>();
        setmealAndCheckgroupMapper.delete(wrapper);
        for (int i = 0; i < checkgroupIds.length; i++) {
            CheckGroup checkGroup = checkGroupMapper.selectById(checkgroupIds[i]);
            if (checkGroup != null) {
                SetmealAndCheckgroup setmealAndCheckgroup = new SetmealAndCheckgroup();
                setmealAndCheckgroup.setSetmealId(setmeal.getId());
                setmealAndCheckgroup.setCheckgroupId(checkgroupIds[i]);
                setmealAndCheckgroupMapper.insert(setmealAndCheckgroup);
                list.add(checkGroup);
            }
        }
        setmeal.setCheckGroups(list);
        return setmeal;
    }

    /**
     * 根据id删除套餐以及其相关信息
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        QueryWrapper<SetmealAndCheckgroup> wrapper = new QueryWrapper<>();
        wrapper.eq("setmeal_id",id);
        setmealAndCheckgroupMapper.delete(wrapper);
        setmealMapper.deleteById(id);
    }


}
