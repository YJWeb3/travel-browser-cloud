package com.zheng.travel.browser.article.run;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class RunnableTest {

    @Test
    public void test() {
        try {
            /*new Thread(() -> {
                if (true) {
                    throw new RuntimeException("1111");
                }
            }).start();*/
            ExecutorService service = Executors.newSingleThreadExecutor();
            Future<?> future = service.submit(new Runnable() {
                @Override
                public void run() {

                }
            });

            System.out.println("future.isDone() = " + future.isDone());
            if (future.isDone()) {
                Object o = future.get();
                System.out.println("-----" + o);
            }
            System.out.println("future.isDone() = " + future.isDone());

            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            System.out.println("-------------------------------");
        }
    }

    @Test
    public void testJoin() {
        Thread thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println("子线程执行......");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        try {
            // 当在主线程中调用 join 方法时, 主线程会等待子线程执行完毕后, 再继续执行
            thread.join();
            System.out.println("子线程执行结束了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
