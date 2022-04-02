package net.zjitc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.zjitc.common.MessageConstant;
import net.zjitc.common.Result;
import net.zjitc.constant.RedisConstant;
import net.zjitc.entity.Setmeal;
import net.zjitc.service.SetmealService;
import net.zjitc.utils.QiniuUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;


    /**
     * 查询所有
     * @return
     */
    @GetMapping("/findall")
    public Result findAll(){
        try{
            List<Setmeal> all = setmealService.findAll();
            if(all.size()>0 && all!=null){
                return new Result(true,"查询成功",all);
            }else {
                return new Result(false,"查询失败");
            }
        }catch (Exception e){
            return new Result(false,"查询失败");
        }
    }

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @GetMapping("findbyid")
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setmealService.findById(id);
            if(setmeal!=null){
                return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
            }else {
                return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL,"该id对应套餐不存在");
            }
        }catch (Exception e){
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }

}
