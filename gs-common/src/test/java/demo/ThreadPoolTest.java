package demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Administator
 * @date 2021-10-19 16:04
 * @Description
 */
public class ThreadPoolTest {
    public static ExecutorService execs = Executors.newCachedThreadPool();

    /**
     * 线程复用测试
     *
     * @param args
     */
    public static void main(String[] args) {
        // 模拟10个任务
        for (int i = 0; i < 10; i++) {
            // 线程内任务索引
            int taskIndex = i;
            try {
                // 每隔一秒，向线程池添加一个任务，一秒足以让下面输出的任务执行完，
                // 任务执行完线程就会空闲，即可实现线程的复用；
                // 任务没有执行完，将会再次创建线程
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            execs.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "---->正在执行的任务-->" + taskIndex);
                }
            });

        }
    }
}
