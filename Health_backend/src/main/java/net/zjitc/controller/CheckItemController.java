package net.zjitc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.zjitc.common.MessageConstant;
import net.zjitc.common.Result;
import net.zjitc.entity.CheckItem;
import net.zjitc.service.CheckItemService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 检查项管理
 */
@CrossOrigin
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;
    /**
     * 新增检查项
     */
    @PostMapping("/add")
    public Result add (@RequestBody CheckItem checkItem){
//        通过try，catch判断是否添加成功
        try {
//            判断项目编码是否已经存在
            CheckItem code = checkItemService.findByCode(checkItem.getCode());
            if(code != null){
//                说明该code已经被占用
                return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL,"该code已经被占用，请重新输入");
            }else {
//                判断项目名称是否已经存在
                CheckItem name = checkItemService.findByName(checkItem.getName());
                if(name != null){
//                    说明name已经被占用
                    return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL,"该name已经被占用，请重新输入");
                }else {
//                    表明方法调用成功
                    checkItemService.add(checkItem);
                }
            }
        }catch (Exception e){
//            表明服务调用失败
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS,checkItem);
    }

    /**
     * 分页查询方法
     */
    @GetMapping("/findpage")
    public Result findpage(@RequestParam(required = true) Integer pagenum,
                           @RequestParam(required = true) Integer pagesize,
                           @RequestParam(required = false) String query){
        Page<CheckItem> page = null;
        try{
            if(pagenum.equals("") || pagesize.equals("")){
                return new Result(false,"请输入分页数据");
            }
            page = checkItemService.findPage(pagenum, pagesize, query);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"获取分页失败");
        }
        return new Result(true,"获取分页数据成功",page);
    }

    /**
     * 删除检查项
     */
    @DeleteMapping("/delete")
    public Result delete(Integer id){
        try{
            checkItemService.deleteById(id);
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }catch (Exception e){
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    /**
     * 编辑检查项
     * @param checkItem
     * @return
     */
    @PutMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try{
//            根据id查询原来数据
            CheckItem serviceById = checkItemService.getById(checkItem);
            if(serviceById.getCode().equals(checkItem.getCode())){
//                表明code未进行修改 无需判断是否已经被占用
                if(serviceById.getName().equals(checkItem.getName())){
//                表明name未进行修改 无需判断是否已经被占用
                    checkItemService.edit(checkItem);
                }
            }else {
//              表明code已经进行了修改 需要判断项目编码是否已经存在
                CheckItem code = checkItemService.findByCode(checkItem.getCode());
                if(code != null){
//                说明该code已经被占用
                    return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL,"该code已经被占用，请重新输入");
                }else {
                    if(serviceById.getName().equals(checkItem.getName())){
//                      表明name未进行修改 无需判断是否已经被占用
                        checkItemService.edit(checkItem);
                    }else {
//                      判断项目名称是否已经存在
                        CheckItem name = checkItemService.findByName(checkItem.getName());
                        if(name != null){
//                          说明name已经被占用
                            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL,"该name已经被占用，请重新输入");
                        }else {
//                          表明方法调用成功
                            checkItemService.edit(checkItem);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 查询检查项
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(Integer id){
        try{
            CheckItem checkItem = checkItemService.getById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 获取所有检查项
     * @return
     */
    @GetMapping("findAll")
    public Result findAll(){
        try{
            List<CheckItem> all = checkItemService.findAll();
            if(all.size() != 0){
                return  new Result(true,"获取所有检查成功",all);
            }else {
                return new Result(false,"获取所有信息失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"获取所有信息失败");
        }

    }

}
