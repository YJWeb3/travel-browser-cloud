package com.zheng.travel.browser.article.threads;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 生产者, 消费者
 * 面试题: 创建两个线程, 一个线程打印 A, 一个线程打印 B, 两个线程需要交替打印
 * 拓展: 对以上问题进行升级, 要求 A 必须在 B 之前打印
 */
public class WaitNotifyTest {

    class TestRun implements Runnable {

        private Integer stock;
        private String flag;

        public TestRun(Integer stock, String flag) {
            this.stock = stock;
            this.flag = flag;
        }

        @Override
        public void run() {
            synchronized (stock) {
                while (true) {
                    System.out.println(flag);
                    try {
                        Thread.sleep(100);

                        // 先唤醒其他线程
                        stock.notify();
                        // 自己进入等待, 此处会释放 sync 锁
                        stock.wait();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Test
    public void test() {
        // 创建锁对象
        Integer stock = new Integer(1);

        CompletableFuture.runAsync(new TestRun(stock, "A"));

        CompletableFuture.runAsync(new TestRun(stock, "B"));

        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
