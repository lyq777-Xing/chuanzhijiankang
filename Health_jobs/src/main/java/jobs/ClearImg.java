package jobs;

import net.zjitc.constant.RedisConstant;
import net.zjitc.utils.QiniuUtils;
import net.zjitc.utils.RedisUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Set;

/**
 * 自定义job 实现定期清理垃圾图片
 */
@Component
public class ClearImg extends QuartzJobBean {
    @Autowired
    private JedisPool jedisPool;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("job doing......" + new Date());
//        根据redis中保存的两个set集合进行差值计算，获得垃圾图片名称集合
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set != null){
            for (String picName : set) {
//              删除七牛云服务器上的图片
                QiniuUtils.deleteFileFromQiniu(picName);
//              从Redis集合中删除图片名称
                jedisPool.getResource().
                        srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
            }
        }
    }
}
