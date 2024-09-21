package com.zheng.travel.browser.article.timer;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskTest {

    @Test
    public void test() {
        // 对象锁
        final Object lock = new Object();
        // 需求: 3s 后打印时间
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("定时任务: " + LocalDateTime.now());

                synchronized (lock) {
                    lock.notify();
                }
            }
        }, 3000);

        try {
            System.out.println("锁之前的时间: " + LocalDateTime.now());
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
