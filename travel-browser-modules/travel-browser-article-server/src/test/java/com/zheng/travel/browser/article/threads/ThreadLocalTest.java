package com.zheng.travel.browser.article.threads;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadLocalTest {

    class TestController {

        /**
         * ThreadLocal 的应用场景: 共享资源的传递, 并发访问共享变量问题(特定场景)
         */
        private ThreadLocal<String> nameHolder = new ThreadLocal<>();

        public void saveName(String name) {
            this.nameHolder.set(name);
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.printName();
        }

        public void printName() {
            System.out.println("name = " + nameHolder.get());
        }
    }

    private TestController testController = new TestController();

    @Test
    public void test() {
        // 创建线程池, 模拟 10 个用户
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        // 利用线程池实现用户并发方法
        for (int i = 0; i < 10; i++) {
            // 模拟并发发送请求
            int finalI = i;
            executorService.execute(() -> testController.saveName("test-" + finalI));
        }

        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
