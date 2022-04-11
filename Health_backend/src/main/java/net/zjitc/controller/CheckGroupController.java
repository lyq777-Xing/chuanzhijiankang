package net.zjitc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.zjitc.common.MessageConstant;
import net.zjitc.common.Result;
import net.zjitc.entity.CheckGroup;
import net.zjitc.entity.CheckItem;
import net.zjitc.service.CheckGroupService;
import org.apache.dubbo.config.annotation.Reference;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查组管理
 */
@RestController
@CrossOrigin
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")
    @PostMapping("/add/{checkitemIds}")
    public Result add(@RequestBody CheckGroup checkGroup,
                      @PathVariable("checkitemIds") Integer[] checkitemIds){
        try{
//            判断项目编码是否已经存在
            CheckGroup code = checkGroupService.findByCode(checkGroup.getCode());
            if(code != null){
//                说明该code已经被占用
                return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL,"该code已经被占用，请重新输入");
            }else {
//                判断项目名称是否已经存在
                CheckGroup name = checkGroupService.findByName(checkGroup.getName());
                if(name != null){
//                    说明name已经被占用
                    return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL,"该name已经被占用，请重新输入");
                }else {
//                    表明方法调用成功
                    checkGroupService.add(checkGroup, checkitemIds);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }


    /**
     * 分页查询检查组
     * @param pagenum
     * @param pagesize
     * @param query
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @GetMapping("/findpage")
    public Result findPage(@RequestParam(required = true) Integer pagenum,
                           @RequestParam(required = true) Integer pagesize,
                           @RequestParam(required = false) String query){
        try {
            Page<CheckGroup> page = null;
            if(pagenum.equals("")||pagesize.equals("")){
                return new Result(false,"请输入正确的分页数据");
            }
            if(query!=null && !query.equals("")){
                page = checkGroupService.findPage(pagenum, pagesize, query);
            }else {
                page = checkGroupService.findPage(pagenum, pagesize);
            }
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,page);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }


    /**
     * 根据id查询检查组
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @GetMapping("/findbyid")
    public Result findById(Integer id){
        try{
            CheckGroup checkGroup = checkGroupService.getById(id);
            if(checkGroup != null){
                return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
            }else {
                return new Result(false,"该id所对应的检查组不存在");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据groupid查询检查项id
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @GetMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id){
        try{
            List<Integer> list = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            if(list != null && list.size() != 0){
                return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
            }else {
                return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 编辑检查组并且更新检查组与检查项的关系表
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")
    @PutMapping("/edit/{checkitemIds}")
    public Result edit(@RequestBody CheckGroup checkGroup,@PathVariable("checkitemIds") Integer[] checkitemIds){
        try{
            checkGroupService.edit(checkGroup, checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 查询所有检查组
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @GetMapping("/findAll")
    public Result findAll(){
        try{
            List<CheckGroup> all = checkGroupService.findAll();
            if(all != null && all.size()>0){
                return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,all);
            }else {
                return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 删除检查组
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    @DeleteMapping("/delete")
    public Result deleteById(Integer id){
        try{
            checkGroupService.deleteByGroupId(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除检查组失败");
        }
        return new Result(true,"删除检查组成功");
    }
}
