package com.zheng.travel.browser.data.job;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyTestJob {

    // 需求: 每周的工作日早上9:30查询邮件
    // @Scheduled(cron = "0 30 9 * * MON-FRI")
    // 需求: 每天凌晨 2 点执行一次数据同步操作
    // @Scheduled(cron = "0 0 2 * * *")
    // 需求: 每天 14,16,18 点整去 JD 抢购
    // @Scheduled(cron = "0 0 14,16,18 * * *")
    // 需求: 在每小时的第 0/15/30/45 执行一次
    // @Scheduled(cron = "0 0/15 * * * *")
    // 需求: 每十分钟执行一次定时任务查询操作
    // @Scheduled(cron = "0 */10 * * * *")
    public void cronTest() {
        System.out.println("定时查询数据");
    }

    // @Scheduled(fixedDelay = 2000)
    public void printTime() {
        System.out.println("当前时间: " + LocalDateTime.now());
    }
}
