package config;


import jobs.ClearImg;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class TaskConfig {
    //配置定时任务1
    @Bean
    public JobDetailFactoryBean job1() {
        JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();
        //配置任务的具体实现
        jobDetail.setJobClass(ClearImg.class);
        //是否持久化
        jobDetail.setDurability(true);
        //出现异常是否重新执行
        jobDetail.setRequestsRecovery(true);
        //配置定时任务信息
        jobDetail.setName("job1111------");
        jobDetail.setGroup("quartzTest--------");
        jobDetail.setDescription("这是job1111");
        return jobDetail;
    }

    //配置任务定时规则1
    @Bean
    public CronTriggerFactoryBean trigger1() {
        CronTriggerFactoryBean cronTrigger = new CronTriggerFactoryBean();
        //定时规则的分组
        cronTrigger.setGroup("TriggerTest11111");
        cronTrigger.setName("trigger1");
        //配置执行的任务jobdetail
        cronTrigger.setJobDetail(job1().getObject());
        //配置执行规则 每5秒执行一次
        cronTrigger.setCronExpression("0/5 * * * * ?");
        return cronTrigger;
    }
}
